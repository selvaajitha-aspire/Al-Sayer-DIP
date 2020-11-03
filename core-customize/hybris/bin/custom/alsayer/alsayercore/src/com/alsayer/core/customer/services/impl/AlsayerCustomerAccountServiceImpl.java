package com.alsayer.core.customer.services.impl;

import com.alsayer.core.constants.AlsayerCoreConstants;
import com.alsayer.core.customer.daos.impl.AlsayerCustomerServicesDaoImpl;
import com.alsayer.core.customer.services.AlsayerCustomerAccountService;
import com.alsayer.core.model.CustomerAuthenticationModel;
import com.alsayer.core.response.EccCustomerDetailsResponse;
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

public class AlsayerCustomerAccountServiceImpl extends DefaultCustomerAccountService  implements AlsayerCustomerAccountService {

    private static final Logger LOG = Logger.getLogger(AlsayerCustomerAccountServiceImpl.class);
    protected static final String HEADER_AUTH_KEY = "Authorization";
    protected static final String HEDER_AUTH_VALUE = "Basic UzAwMjE4NDAzMzM6T2N0LjIwMTc=";

    public final String url = AlsayerCoreConstants.SCPI_URL;
    public final String OTP_VALIDATION = AlsayerCoreConstants.OTP_TIME;


    private SessionService sessionService;
    private ModelService modelService;
    private TimeService timeService;



    private AlsayerCustomerServicesDaoImpl alsayerCustomerServicesDao;





    public void activateUser(@RequestParam(name = "token") String token) throws TokenInvalidatedException {
        final SecureToken data = getSecureTokenService().decryptData(token);
        if (getTokenValiditySeconds() > 0L)
        {
            final long delta = new Date().getTime() - data.getTimeStamp();
            if (delta / 1000 > getTokenValiditySeconds())
            {
                throw new IllegalArgumentException("token expired");
            }
        }

        final CustomerModel customer = getUserService().getUserForUID(data.getData(), CustomerModel.class);
        if (customer == null)
        {
            throw new IllegalArgumentException("user for token not found");
        }
        if (!token.equals(customer.getToken()))
        {
            throw new TokenInvalidatedException();
        }
        customer.setToken(null);
        customer.setLoginDisabled(false);
        getModelService().save(customer);
    }

    public ECCCustomerWsDTO getCustomerECCDetails(String code) {

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        RestTemplate restTemplate  = new RestTemplate(requestFactory);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HEADER_AUTH_KEY,HEDER_AUTH_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        /*map.put("civilid", "290010195209");
        map.put("Kunnr", "1000051987");*/

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("civilid","290010195209");
        jsonObject.put("Kunnr","1000051987");

         JSONArray jsonArray = new JSONArray();
         jsonArray.add(jsonObject);

        JSONObject finalObj = new JSONObject();
        finalObj.put("HeaderData",jsonArray);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(finalObj, headers);
        LOG.info("CustomerDetails Service request : " + entity.getBody());
        ResponseEntity<String> response = restTemplate.postForEntity(getConfigurationService().getConfiguration().getString(url), entity, String.class);
        LOG.info("CustomerDetails Service response Name : " +response.getBody());
        ObjectMapper objectMapper=new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        ECCCustomerWsDTO eccCustomerWsDTO=new ECCCustomerWsDTO();
        try {

            EccCustomerDetailsResponse eccCustomerDetailsResponse =objectMapper.readValue(response.getBody(), EccCustomerDetailsResponse.class);
            LOG.info("ECC Response : " + eccCustomerDetailsResponse.toString());

            if(eccCustomerDetailsResponse.getNavCust()!=null) {
                eccCustomerWsDTO.setName(eccCustomerDetailsResponse.getNavCust().getResults().getName());
                eccCustomerWsDTO.setArabicName(eccCustomerDetailsResponse.getNavCust().getResults().getNamearabic());
                eccCustomerWsDTO.setMobile(eccCustomerDetailsResponse.getNavCust().getResults().getMobile());
            }
        }catch (JsonProcessingException ex){
            ex.printStackTrace();
        }
        //Response and request need to be add as per the service.

        return eccCustomerWsDTO;
    }

    public void sendOTP() {

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        RestTemplate restTemplate  = new RestTemplate(requestFactory);
        ECCCustomerWsDTO eccCustomerWsDTO = new ECCCustomerWsDTO();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HEADER_AUTH_KEY,HEDER_AUTH_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        String otp = getOtp();
        CustomerAuthenticationModel customerAuthentication = getModelService().create(CustomerAuthenticationModel.class);
        customerAuthentication.setCivilId("290010195209");
        customerAuthentication.setOneTimePassword(otp);
        customerAuthentication.setJsessionId(getSessionService().getCurrentSession().getSessionId());
        getModelService().save(customerAuthentication);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("civilid","290010195209");
        jsonObject.put("Kunnr","1000051987");

        JSONArray jsonArray = new JSONArray();
        jsonArray.add(jsonObject);

        JSONObject finalObj = new JSONObject();
        finalObj.put("HeaderData",jsonArray);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(finalObj, headers);
       LOG.info("OTP Service request : " + entity.getBody());
        ResponseEntity<String> response = restTemplate.postForEntity(getConfigurationService().getConfiguration().getString(url), entity, String.class);
        LOG.info("OTP Service response : " +response.getBody());

        //Response and request need to be add as per the service.


    }

    private String getOtp() {
        return new DecimalFormat("000000").format(new Random().nextInt(999999));
    }


    public void eccRecordSynchronization(RegisterData registerData) {

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        RestTemplate restTemplate  = new RestTemplate(requestFactory);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HEADER_AUTH_KEY,HEDER_AUTH_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        String otp = getOtp();
        Map<String, Object> map = new HashMap<>();
            map.put("name", registerData.getName());
        map.put("arabicName", registerData.getArabicName());
        map.put("mobile", registerData.getMobile());
        map.put("emailId", registerData.getUid());
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);
        LOG.info("enity : " + entity.getBody());
        LOG.info("URL is : " + getConfigurationService().getConfiguration().getString(url));


        ResponseEntity<String> response = restTemplate.postForEntity(getConfigurationService().getConfiguration().getString(url), entity, String.class);
        System.out.println("Response came is" +response.getBody());

        //Response and request need to be add as per the service.

    }


    public boolean getOTPForValidation(RegisterData registerData) throws ParseException {

        CustomerAuthenticationModel otp = getAlsayerCustomerServicesDao().getSavedOtp(registerData.getCivilId());
        SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);

        Date createdTime = format.parse(String.valueOf(otp.getCreationtime()));
        Date currentTime = format.parse(String.valueOf(getTimeService().getCurrentTime()));
        long difference_In_Time = currentTime.getTime() - createdTime.getTime();
        long difference_In_Hours = TimeUnit.MILLISECONDS.toHours(difference_In_Time) % 60;
        long difference_In_Minutes = TimeUnit.MILLISECONDS.toMinutes(difference_In_Time) % 60;
        long difference_In_Days = TimeUnit.MILLISECONDS.toDays(difference_In_Time) % 365;
        if(difference_In_Days < 1 && difference_In_Hours <1 && difference_In_Minutes < getConfigurationService().getConfiguration().getInt(OTP_VALIDATION)) {
            return  true;
        }

        return false;
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
