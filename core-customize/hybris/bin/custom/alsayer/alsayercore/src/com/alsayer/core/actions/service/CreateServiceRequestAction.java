package com.alsayer.core.actions.service;

import com.alsayer.core.constants.AlsayerCoreConstants;
import com.alsayer.core.enums.EccIssueType;
import com.alsayer.core.enums.ReqType;
import com.alsayer.core.enums.ServiceStatus;
import com.alsayer.core.model.RsaRequestModel;
import com.alsayer.core.model.RsaRequestProcessModel;
import com.alsayer.core.model.VehicleModel;
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

public class CreateServiceRequestAction extends AbstractSimpleDecisionAction<RsaRequestProcessModel> {

    private static final Logger LOG = Logger.getLogger(CreateServiceRequestAction.class);
    private static final String ISSUETYPE="ISSUETYPE";
    private static final String REQTYPE="REQTYPE";
    public final String url = AlsayerCoreConstants.SCPI_URL;
    protected static final String HEADER_AUTH_KEY = "Authorization";
    protected static final String HEDER_AUTH_VALUE = "Basic UzAwMjE4NDAzMzM6T2N0LjIwMTc=";
    protected static final String ESMSSET = "ZINSURANCE_SMS";



    private ConfigurationService configurationService;



    @Override
    public Transition executeAction(RsaRequestProcessModel process) {
        final RsaRequestModel serviceRequest = process.getRsaRequest();
        final String issue=serviceRequest.getIssue().getCode();
        final String chassisNumber=serviceRequest.getVehicle().getChassisNumber();
        if (serviceRequest != null)
        {
            try
            {
                // Check if the Service Request is Cancelled
                if (ServiceStatus.CANCELLED.equals(serviceRequest.getStatus())|| ServiceStatus.FAILED.equals(serviceRequest.getStatus()))
                {
                    return Transition.NOK;
                }
                else
                {
                    SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
                    RestTemplate restTemplate = new RestTemplate(requestFactory);
                    HttpHeaders headers = new HttpHeaders();
                    headers.add(HEADER_AUTH_KEY, HEDER_AUTH_VALUE);
                    headers.setContentType(MediaType.APPLICATION_JSON);

                   if(process.getRsaRequest().getType().equalsIgnoreCase("RSA")){
                       JSONObject jsonObject = new JSONObject();
                       jsonObject.put(REQTYPE, ReqType.T);
                       jsonObject.put(ISSUETYPE, EccIssueType.MR);
                       JSONArray jsonArray = new JSONArray();
                       jsonArray.add(jsonObject);
                       JSONObject finalObj = new JSONObject();
                       finalObj.put(ESMSSET, jsonArray);

                       HttpEntity<Map<String, Object>> entity = new HttpEntity<>(finalObj, headers);
                       ResponseEntity<String> response = restTemplate.postForEntity(getConfigurationService().getConfiguration().getString(url), entity, String.class);

                   }else {
                       JSONObject jsonObject = new JSONObject();
                       jsonObject.put(REQTYPE, ReqType.M);
                       if(issue.equalsIgnoreCase("OUT_OF_FUEL"))
                            jsonObject.put(ISSUETYPE,EccIssueType.M1);
                       if(issue.equalsIgnoreCase("DEAD_BATTERY"))
                           jsonObject.put(ISSUETYPE,EccIssueType.M2);
                       if(issue.equalsIgnoreCase("FLAT_TYRE"))
                           jsonObject.put(ISSUETYPE, EccIssueType.M3);
                       if(issue.equalsIgnoreCase("OTHERS_MUSAADA"))
                           jsonObject.put(ISSUETYPE,EccIssueType.M4);
                       JSONArray jsonArray = new JSONArray();
                       jsonArray.add(jsonObject);

                       JSONObject finalObj = new JSONObject();
                       finalObj.put(ESMSSET, jsonArray);

                       HttpEntity<Map<String, Object>> entity = new HttpEntity<>(finalObj, headers);
                       ResponseEntity<String> response = restTemplate.postForEntity(getConfigurationService().getConfiguration().getString(url), entity, String.class);
                   }
                    return Transition.OK;
                }
            }
            catch (final Exception e)
            {
                if (LOG.isDebugEnabled())
                {
                    LOG.debug(e);
                }
                return Transition.NOK;
            }
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
