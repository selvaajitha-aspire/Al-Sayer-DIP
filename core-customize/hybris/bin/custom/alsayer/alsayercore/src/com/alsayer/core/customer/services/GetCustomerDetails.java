package com.alsayer.core.customer.services;

import com.alsayer.core.constants.AlsayerCoreConstants;
import com.alsayer.occ.dto.ECCCustomerWsDTO;
import de.hybris.platform.processengine.action.AbstractSimpleDecisionAction;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.user.UserService;
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

import java.util.HashMap;
import java.util.Map;

public class GetCustomerDetails  extends AbstractSimpleDecisionAction {

    private static final Logger LOG = Logger.getLogger(GetCustomerDetails.class);
    protected static final String HEADER_AUTH_KEY = "Authorization";
    protected static final String HEDER_AUTH_VALUE = "Basic UzAwMjE4NDAzMzM6T2N0LjIwMTc=";
    public final String url = AlsayerCoreConstants.SCPI_URL;


    private ConfigurationService configurationService;
    private UserService userService;




    @Override
    public Transition executeAction(BusinessProcessModel businessProcessModel) throws RetryLaterException, Exception {


        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        RestTemplate restTemplate  = new RestTemplate(requestFactory);
        ECCCustomerWsDTO eccCustomerWsDTO = new ECCCustomerWsDTO();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HEADER_AUTH_KEY,HEDER_AUTH_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> map = new HashMap<>();
        //map.put("civilId",  currentUser.getCivilId());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("civilid","290010195209");
        jsonObject.put("Kunnr","1000051987");

        JSONArray jsonArray = new JSONArray();
        jsonArray.add(jsonObject);

        JSONObject finalObj = new JSONObject();
        finalObj.put("HeaderData",jsonArray);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(finalObj, headers);
        System.out.println("enity : " + entity.getBody());

        ResponseEntity<String> response = restTemplate.postForEntity(getConfigurationService().getConfiguration().getString(url), entity, String.class);
        LOG.info("it came in the process and fetched details" + response.getBody());

        return Transition.OK;

    }



    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }



    public ConfigurationService getConfigurationService() {
        return configurationService;
    }

    public void setConfigurationService(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }





}
