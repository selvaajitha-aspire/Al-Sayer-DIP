package com.alsayer.core.actions.service;

import com.alsayer.core.constants.AlsayerCoreConstants;
import com.alsayer.core.enums.ServiceStatus;
import com.alsayer.core.model.DriverDetailsModel;
import com.alsayer.core.model.RsaRequestModel;
import com.alsayer.core.model.RsaRequestProcessModel;
import com.alsayer.core.model.VehicleModel;
import com.alsayer.core.response.E_Vehicle_Info;
import com.alsayer.core.response.EccVehicleDetailsResponse;
import com.alsayer.core.response.fsm.Data;
import com.alsayer.core.response.fsm.FSMTechnicianResponse;
import com.alsayer.core.response.fsm.location;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.processengine.action.AbstractSimpleDecisionAction;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.task.RetryLaterException;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GetTechnicianLocationAction extends AbstractSimpleDecisionAction<RsaRequestProcessModel> {
    private static final Logger LOG = Logger.getLogger(GetTechnicianLocationAction.class);

    public final String url = AlsayerCoreConstants.ECOM_FSM_URL;
    protected static final String HEADER_AUTH_KEY = "Authorization";
    protected static final String HEDER_AUTH_VALUE = "Basic UzAwMjE4NDAzMzM6T2N0LjIwMTc=";
    private static final String EXT_ID_KEY="external_id";
    private static final String DATA="data";

    private static final String CLOSED="close";
    private static final String WORK="work";
    private ConfigurationService configurationService;
    private ModelService modelService;


    @Override
    public Transition executeAction(RsaRequestProcessModel process) throws RetryLaterException, Exception {
        final RsaRequestModel serviceRequest = process.getRsaRequest();
        final CustomerModel customer=serviceRequest.getCustomer();
        if (serviceRequest != null)
        {
            try {
                if (ServiceStatus.CANCELLED.equals(serviceRequest.getStatus()) || ServiceStatus.FAILED.equals(serviceRequest.getStatus())) {
                    return Transition.NOK;
                } else {
                    SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
                    RestTemplate restTemplate = new RestTemplate(requestFactory);
                    HttpHeaders headers = new HttpHeaders();
                    headers.add(HEADER_AUTH_KEY, HEDER_AUTH_VALUE);
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(EXT_ID_KEY, serviceRequest.getUid());
                    JSONArray jsonArray = new JSONArray();
                    jsonArray.add(jsonObject);
                    JSONObject finalObj = new JSONObject();
                    finalObj.put(DATA, jsonArray);

                    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(finalObj, headers);
                    ResponseEntity<String> response = restTemplate.postForEntity(getConfigurationService().getConfiguration().getString(url), entity, String.class);
                    LOG.debug(AlsayerCoreConstants.RSA_FSM_PROCESS);
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    try {
                        FSMTechnicianResponse responseBody = objectMapper.readValue(response.getBody(), FSMTechnicianResponse.class);
                        List<Data> data = responseBody.getData();
                        if(CollectionUtils.isNotEmpty(data)){
                            DriverDetailsModel driverDetailsModel = getModelService().create(DriverDetailsModel.class);
                            driverDetailsModel.setLatitude(data.get(0).getP().getLocation().getLatitude());
                            driverDetailsModel.setLongitude(data.get(0).getP().getLocation().getLongitude());
                            serviceRequest.setDriverDetails(driverDetailsModel);
                           if( data.get(0).getSas().getName().equalsIgnoreCase(CLOSED)) {
                               serviceRequest.setStatus(ServiceStatus.COMPLETED);
                            }else{
                                serviceRequest.setStatus(ServiceStatus.IN_PROGRESS);
                            }
                            getModelService().save(serviceRequest);
                        }

                } catch(JsonProcessingException ex){
                    ex.printStackTrace();
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

    @Override
    public ModelService getModelService() {
        return modelService;
    }

    @Override
    public void setModelService(ModelService modelService) {
        this.modelService = modelService;
    }

    public ConfigurationService getConfigurationService() {
        return configurationService;
    }

    public void setConfigurationService(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }
}
