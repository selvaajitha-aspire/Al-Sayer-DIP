package com.alsayer.core.actions.service;

import com.alsayer.core.constants.AlsayerCoreConstants;
import com.alsayer.core.enums.EccIssueType;
import com.alsayer.core.enums.ReqType;
import com.alsayer.core.enums.ServiceStatus;
import com.alsayer.core.model.RsaRequestModel;
import com.alsayer.core.model.RsaRequestProcessModel;
import de.hybris.platform.processengine.action.AbstractSimpleDecisionAction;
import de.hybris.platform.servicelayer.config.ConfigurationService;
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
    private static final String VHVIN="VHVIN";
    private static final String KUNNR="KUNNR";
    private static final String MILEAGE="MILEAGE";
    private static final String WERKS="WERKS";
    private static final String REMARKS1="REMARKS1";
    private static final String VBELN="VBELN";
    private static final String RETURN="RETURN";
    public final String url = AlsayerCoreConstants.SCPT_RSA_POST_URL;
    protected static final String HEADER_AUTH_KEY = "Authorization";
    protected static final String HEDER_AUTH_VALUE = "Basic UzAwMjE4NDAzMzM6T2N0LjIwMTc=";
    protected static final String EORDER = "E_order";



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
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(VHVIN,chassisNumber);
                    jsonObject.put(KUNNR,"");
                    jsonObject.put(MILEAGE,"");
                    jsonObject.put(WERKS,null);
                    jsonObject.put(REMARKS1,"");
                    jsonObject.put(VBELN,null);
                    jsonObject.put(RETURN,null);
                    JSONArray jsonArray = new JSONArray();

                   if(process.getRsaRequest().getType().equalsIgnoreCase("RSA")){
                       jsonObject.put(REQTYPE, ReqType.T.getCode());
                       jsonObject.put(ISSUETYPE, EccIssueType.MR.getCode());
                   }else {
                       jsonObject.put(REQTYPE, ReqType.M);
                       if(issue.equalsIgnoreCase("OUT_OF_FUEL"))
                            jsonObject.put(ISSUETYPE,EccIssueType.M1.getCode());
                       if(issue.equalsIgnoreCase("DEAD_BATTERY"))
                           jsonObject.put(ISSUETYPE,EccIssueType.M2.getCode());
                       if(issue.equalsIgnoreCase("FLAT_TYRE"))
                           jsonObject.put(ISSUETYPE, EccIssueType.M3.getCode());
                       if(issue.equalsIgnoreCase("OTHERS_MUSAADA"))
                           jsonObject.put(ISSUETYPE,EccIssueType.M4.getCode());
                   }
                    jsonArray.add(jsonObject);
                    JSONObject finalObj = new JSONObject();
                    finalObj.put(EORDER, jsonArray);

                    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(finalObj, headers);
                    ResponseEntity<String> response = restTemplate.postForEntity(getConfigurationService().getConfiguration().getString(url), entity, String.class);
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
