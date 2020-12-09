package com.alsayer.core.actions.service;

import com.alsayer.core.constants.AlsayerCoreConstants;
import com.alsayer.core.model.SMSGatewayProcessModel;
import de.hybris.platform.processengine.action.AbstractSimpleDecisionAction;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.task.RetryLaterException;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class SendOTPAction extends AbstractSimpleDecisionAction<SMSGatewayProcessModel> {
    private static final Logger LOG = Logger.getLogger(CreateServiceRequestAction.class);
    public final String smsUrl = AlsayerCoreConstants.SCPI_SMS_URL;
    protected static final String HEADER_AUTH_KEY = "Authorization";
    protected static final String HEDER_AUTH_VALUE = "Basic UzAwMjE4NDAzMzM6T2N0LjIwMTc=";

    protected static final String ESMSSET = "ZINSURANCE_SMS";

    protected static final String MOBILE="LV_NUMBER";
    protected static final String MESSAGE= "LV_MESSAGE";


    private ConfigurationService configurationService;

    @Override
    public Transition executeAction(SMSGatewayProcessModel process) throws RetryLaterException, Exception {
        final String mobile  = process.getMobile();
        final String message  = process.getMessage();
        if (mobile != null && message!=null)
        {
            SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
            RestTemplate restTemplate = new RestTemplate(requestFactory);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HEADER_AUTH_KEY, HEDER_AUTH_VALUE);
            headers.setContentType(MediaType.APPLICATION_JSON);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(MOBILE, process.getMobile());
            jsonObject.put(MESSAGE, process.getMessage());
            JSONArray jsonArray = new JSONArray();
            jsonArray.add(jsonObject);

            JSONObject finalObj = new JSONObject();
            finalObj.put(ESMSSET, jsonArray);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(finalObj, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(getConfigurationService().getConfiguration().getString(smsUrl), entity, String.class);
               return Transition.OK;
        }
        return Transition.NOK;

    }

    public ConfigurationService getConfigurationService() {
        return configurationService;
    }

    public void setConfigurationService(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }
}
