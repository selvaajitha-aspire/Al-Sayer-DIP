package com.alsayer.core.customer;

import com.alsayer.core.constants.AlsayerCoreConstants;
import com.alsayer.occ.dto.ECCCustomerWsDTO;
import de.hybris.platform.commercefacades.user.data.RegisterData;
import de.hybris.platform.commerceservices.customer.TokenInvalidatedException;
import de.hybris.platform.commerceservices.customer.impl.DefaultCustomerAccountService;
import de.hybris.platform.commerceservices.security.SecureToken;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class AlsayerCustomerAccountService extends DefaultCustomerAccountService {

    private static final Logger LOG = Logger.getLogger(AlsayerCustomerAccountService.class);
    protected static final String HEADER_AUTH_KEY = "Authorization";
    protected static final String HEDER_AUTH_VALUE = "Basic UzAwMjE4NDAzMzM6T2N0LjIwMTc=";

    public final String url = AlsayerCoreConstants.SCPI_URL;



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
        ECCCustomerWsDTO eccCustomerWsDTO = new ECCCustomerWsDTO();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HEADER_AUTH_KEY,HEDER_AUTH_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> map = new HashMap<>();
        map.put("name", code);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);
        System.out.println("enity : " + entity.getBody());
        ResponseEntity<String> response = restTemplate.postForEntity(getConfigurationService().getConfiguration().getString(url), entity, String.class);
        System.out.println("Response came is" +response.getBody());


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
        Map<String, Object> map = new HashMap<>();
        map.put("otp", otp);
        map.put("salary", "111");
        map.put("age", "1233");
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);
        System.out.println("enity : " + entity.getBody());


        ResponseEntity<String> response = restTemplate.postForEntity(getConfigurationService().getConfiguration().getString(url), entity, String.class);
        System.out.println("Response came is" +response.getBody());

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
        map.put("mobileNumber", registerData.getMobileNumber());
        map.put("emailId", registerData.getEmailId());
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);
        LOG.info("enity : " + entity.getBody());
        LOG.info("URL is : " + getConfigurationService().getConfiguration().getString(url));


        ResponseEntity<String> response = restTemplate.postForEntity(getConfigurationService().getConfiguration().getString(url), entity, String.class);
        System.out.println("Response came is" +response.getBody());

        //Response and request need to be add as per the service.

    }



}
