package com.alsayer.core.actions.service;

import com.alsayer.core.constants.AlsayerCoreConstants;
import com.alsayer.core.enums.EccIssueType;
import com.alsayer.core.enums.ReqType;
import com.alsayer.core.enums.ServiceStatus;
import com.alsayer.core.model.RsaRequestModel;
import com.alsayer.core.model.RsaRequestProcessModel;
import com.alsayer.core.response.E_orderSet;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.hybris.platform.core.model.user.AddressModel;
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
    private static final String RETURN="RETURN";
    private static final String LATITUDE="LATITUDE";
    private static final String LONGITUDE="LONGITUDE";
    private static final String ZIPCODE="ZIPCODE";
    private static final String STREET="STREET";
    private static final String GOVERNANCE="GOVERNANCE";
    private static final String COUNTRY="COUNTRY";
    private static final String PAID_SERVICE="PAID_SERVICE";

    public final String url = AlsayerCoreConstants.SCPT_RSA_POST_URL;
    protected static final String HEADER_AUTH_KEY = "Authorization";
    protected static final String HEDER_AUTH_VALUE = "Basic UzAwMjE4NDAzMzM6T2N0LjIwMTc=";
    protected static final String EORDER = "E_order";



    private ConfigurationService configurationService;



    @Override
    public Transition executeAction(RsaRequestProcessModel process) {
        final RsaRequestModel serviceRequest = process.getRsaRequest();
        final String issue=serviceRequest.getIssue().getCode();
        final AddressModel address=serviceRequest.getAddress();
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
                    jsonObject.put(RETURN,null);
                    jsonObject.put(LATITUDE,serviceRequest.getLatitude().toString());
                    jsonObject.put(LONGITUDE,serviceRequest.getLongitude().toString());
                    jsonObject.put(ZIPCODE,address.getPostalcode());
                    jsonObject.put(STREET,address.getLine1()+" "+address.getLine2());
                    jsonObject.put(GOVERNANCE,address.getRegion().getName());
                    jsonObject.put(COUNTRY,address.getCountry().getIsocode());


                    JSONArray jsonArray = new JSONArray();

                   if(serviceRequest.getType().equalsIgnoreCase("RSA")){
                       jsonObject.put(REQTYPE, ReqType.T.getCode());
                       jsonObject.put(ISSUETYPE, EccIssueType.MR.getCode());
                       jsonObject.put(PAID_SERVICE,"X");
                   }else {
                       jsonObject.put(REQTYPE, ReqType.M);
                       if(issue.equalsIgnoreCase("OUT_OF_FUEL"))
                            jsonObject.put(ISSUETYPE,EccIssueType.M1.getCode());

                       else if(issue.equalsIgnoreCase("DEAD_BATTERY"))
                           jsonObject.put(ISSUETYPE,EccIssueType.M2.getCode());

                       else if(issue.equalsIgnoreCase("FLAT_TYRE"))
                           jsonObject.put(ISSUETYPE, EccIssueType.M3.getCode());

                       else if(issue.equalsIgnoreCase("OTHERS_MUSAADA"))
                           jsonObject.put(ISSUETYPE,EccIssueType.M4.getCode());

                       jsonObject.put(PAID_SERVICE,"-");
                   }
                    jsonArray.add(jsonObject);
                    JSONObject finalObj = new JSONObject();
                    finalObj.put(EORDER, jsonArray);

                    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(finalObj, headers);
                    ResponseEntity<String> response = restTemplate.postForEntity(getConfigurationService().getConfiguration().getString(url), entity, String.class);
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    E_orderSet responseBody = objectMapper.readValue(response.getBody(), E_orderSet.class);
                    if(!responseBody.getE_order().getVBELN().isEmpty()) {
                        serviceRequest.setEccTicketId(responseBody.getE_order().getVBELN());
                        getModelService().save(serviceRequest);
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
