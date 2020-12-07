package com.alsayer.core.customer.services.impl;

import com.alsayer.core.constants.AlsayerCoreConstants;
import com.alsayer.core.customer.daos.impl.AlsayerCustomerServicesDaoImpl;
import com.alsayer.core.customer.services.AlsayerCustomerAccountService;
import com.alsayer.core.event.sms.gateway.SMSGatewayEvent;
import com.alsayer.core.model.CustomerAuthenticationModel;
import com.alsayer.core.model.SMSGatewayProcessModel;
import com.alsayer.core.response.Results;
import com.alsayer.occ.dto.ECCCustomerWsDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.hybris.platform.commercefacades.user.data.RegisterData;
import de.hybris.platform.commerceservices.customer.TokenInvalidatedException;
import de.hybris.platform.commerceservices.customer.impl.DefaultCustomerAccountService;
import de.hybris.platform.commerceservices.security.SecureToken;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.time.TimeService;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class AlsayerCustomerAccountServiceImpl extends DefaultCustomerAccountService implements AlsayerCustomerAccountService {

    private static final Logger LOG = Logger.getLogger(AlsayerCustomerAccountServiceImpl.class);
    protected static final String HEADER_AUTH_KEY = "Authorization";
    protected static final String HEDER_AUTH_VALUE = "Basic UzAwMjE4NDAzMzM6T2N0LjIwMTc=";
    protected static final String ECUSTDETAILSSET = "E_CUSTDETAILS";
    protected static final String CIVILID = "civilid";
    protected static final String NAME = "name";
    protected static final String ARABICNAME = "namearabic";
    protected static final String MOBILE = "mobie";
    protected static final String EMAIL = "email";
    protected static final String OTP_PATTERN="000000";
    protected static final Integer OTP_BOUND=999999;
    protected static final String DATE_FORMAT="EEE MMM dd HH:mm:ss zzz yyyy";

    public final String url = AlsayerCoreConstants.SCPI_URL;
    public final String custDetailsUrl = AlsayerCoreConstants.SCPI_CUSTDETAILS_URL;
    public final String OTP_VALIDATION = AlsayerCoreConstants.OTP_TIME;


    private SessionService sessionService;
    private ModelService modelService;
    private TimeService timeService;


    private AlsayerCustomerServicesDaoImpl alsayerCustomerServicesDao;


    public void activateUser(@RequestParam(name = "token") String token) throws TokenInvalidatedException {
        final SecureToken data = getSecureTokenService().decryptData(token);
        if (getTokenValiditySeconds() > 0L) {
            final long delta = new Date().getTime() - data.getTimeStamp();
            if (delta / 1000 > getTokenValiditySeconds()) {
                throw new IllegalArgumentException(AlsayerCoreConstants.EXPRED_TOKEN_ERR_MSG);
            }
        }

        final CustomerModel customer = getUserService().getUserForUID(data.getData(), CustomerModel.class);
        if (customer == null) {
            throw new IllegalArgumentException(AlsayerCoreConstants.TOKEN_ERR_MSG);
        }
        if (!token.equals(customer.getToken())) {
            throw new TokenInvalidatedException();
        }
        customer.setToken(null);
        customer.setLoginDisabled(false);
        getModelService().save(customer);
    }

    public ECCCustomerWsDTO getCustomerECCDetails(String code) {

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HEADER_AUTH_KEY, HEDER_AUTH_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(CIVILID, code);
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(jsonObject);

        JSONObject finalObj = new JSONObject();
        finalObj.put(ECUSTDETAILSSET, jsonArray);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(finalObj, headers);
        ResponseEntity<String> response;
        try {
            response = restTemplate.postForEntity(getConfigurationService().getConfiguration().getString(custDetailsUrl), entity, String.class);
        }catch (Exception ex){
            return null;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        ECCCustomerWsDTO eccCustomerWsDTO = new ECCCustomerWsDTO();
        try {

            Results responseBody = objectMapper.readValue(response.getBody(), Results.class);
            LOG.debug(AlsayerCoreConstants.CUST_DETAILS_RESPONSE + responseBody.toString());

            if (responseBody != null) {
                eccCustomerWsDTO.setCivilId(responseBody.getCivilid());
                eccCustomerWsDTO.setEccCustId(responseBody.getCustid());
                eccCustomerWsDTO.setName(responseBody.getName());
                eccCustomerWsDTO.setArabicName(responseBody.getNamearabic());
                eccCustomerWsDTO.setMobile(responseBody.getMobile());
                eccCustomerWsDTO.setEmail(responseBody.getEmail() != null ? responseBody.getEmail() : null);
            }
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }
        return eccCustomerWsDTO;
    }

    @Override
    public void sendOTP(String code,String mobile) {
        String otp = getOtp();
        CustomerAuthenticationModel customerAuthentication = getModelService().create(CustomerAuthenticationModel.class);
        customerAuthentication.setCivilId(code);
        customerAuthentication.setOneTimePassword(otp);
        customerAuthentication.setJsessionId(getSessionService().getCurrentSession().getSessionId());
        getModelService().save(customerAuthentication);
        SMSGatewayProcessModel process=new SMSGatewayProcessModel();
        process.setMobile(mobile);
        process.setMessage("Your One Time Password :"+otp);
        getEventService().publishEvent(new SMSGatewayEvent(process));
    }

    private String getOtp() {
        return new DecimalFormat(OTP_PATTERN).format(new Random().nextInt(OTP_BOUND));
    }


    public void eccRecordSynchronization(RegisterData registerData) {

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HEADER_AUTH_KEY, HEDER_AUTH_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        String otp = getOtp();
        Map<String, Object> map = new HashMap<>();
        map.put(NAME, registerData.getName());
        map.put(ARABICNAME, registerData.getArabicName());
        map.put(MOBILE, registerData.getMobile());
        map.put(EMAIL, registerData.getUid());
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(getConfigurationService().getConfiguration().getString(url), entity, String.class);


    }


    public boolean getOTPForValidation(RegisterData registerData) throws ParseException {

        CustomerAuthenticationModel otp = getAlsayerCustomerServicesDao().getSavedOtp(registerData.getCivilId());
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);

        Date createdTime = format.parse(String.valueOf(otp.getCreationtime()));
        Date currentTime = format.parse(String.valueOf(getTimeService().getCurrentTime()));
        long difference_In_Time = currentTime.getTime() - createdTime.getTime();
        long difference_In_Hours = TimeUnit.MILLISECONDS.toHours(difference_In_Time) % 60;
        long difference_In_Minutes = TimeUnit.MILLISECONDS.toMinutes(difference_In_Time) % 60;
        long difference_In_Days = TimeUnit.MILLISECONDS.toDays(difference_In_Time) % 365;
        if (difference_In_Days < 1 && difference_In_Hours < 1 && difference_In_Minutes < getConfigurationService().getConfiguration().getInt(OTP_VALIDATION)) {
            return true;
        }

        return false;
    }

    public boolean validateOtp(String civilId, String otp) throws ParseException {
        if(null == civilId || "".equals(civilId) || null == otp || "".equals(otp)){
            return false;
        }

        CustomerAuthenticationModel custAuth = getAlsayerCustomerServicesDao().getSavedOtp(civilId);

        if(null == custAuth){
            return false;
        }
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);

        Date createdTime = format.parse(String.valueOf(custAuth.getCreationtime()));
        Date currentTime = format.parse(String.valueOf(getTimeService().getCurrentTime()));
        long difference_In_Time = currentTime.getTime() - createdTime.getTime();
        long difference_In_Hours = TimeUnit.MILLISECONDS.toHours(difference_In_Time) % 60;
        long difference_In_Minutes = TimeUnit.MILLISECONDS.toMinutes(difference_In_Time) % 60;
        long difference_In_Days = TimeUnit.MILLISECONDS.toDays(difference_In_Time) % 365;

        if(difference_In_Days < 1 && difference_In_Hours < 1 && difference_In_Minutes < getConfigurationService().getConfiguration().getInt(OTP_VALIDATION)){
            return otp.equals(custAuth.getOneTimePassword());
        }else{
            return false;
        }
    }


    public SessionService getSessionService() {
        return sessionService;
    }

    public void setSessionService(SessionService sessionService) {
        this.sessionService = sessionService;
    }


    @Override
    public ModelService getModelService() {
        return modelService;
    }

    @Override
    public void setModelService(ModelService modelService) {
        this.modelService = modelService;
    }


    public AlsayerCustomerServicesDaoImpl getAlsayerCustomerServicesDao() {
        return alsayerCustomerServicesDao;
    }

    public void setAlsayerCustomerServicesDao(AlsayerCustomerServicesDaoImpl alsayerCustomerServicesDao) {
        this.alsayerCustomerServicesDao = alsayerCustomerServicesDao;
    }


    @Override
    public TimeService getTimeService() {
        return timeService;
    }

    @Override
    public void setTimeService(TimeService timeService) {
        this.timeService = timeService;
    }


}
