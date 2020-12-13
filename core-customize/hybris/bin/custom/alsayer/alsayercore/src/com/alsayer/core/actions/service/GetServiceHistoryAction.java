package com.alsayer.core.actions.service;

import com.alsayer.core.constants.AlsayerCoreConstants;
import com.alsayer.core.model.ServiceHistoryModel;
import com.alsayer.core.model.VehicleModel;
import com.alsayer.core.model.WarrantyModel;
import com.alsayer.core.response.E_Vehicle_Info;
import com.alsayer.core.response.E_ser_info;
import com.alsayer.core.response.EccServiceHistoryResponse;
import com.alsayer.core.response.Results;
import com.alsayer.core.utils.DateUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
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

import java.util.*;

public class GetServiceHistoryAction  extends AbstractSimpleDecisionAction<StoreFrontCustomerProcessModel> {

    private static final Logger LOG = Logger.getLogger(GetServiceHistoryAction.class);
    protected static final String HEADER_AUTH_KEY = "Authorization";
    protected static final String HEDER_AUTH_VALUE = "Basic UzAwMjE4NDAzMzM6T2N0LjIwMTc=";
    protected static final String ESERSET = "E_ser_info";
    protected static final String CHASSIS_NO = "vhvin";
    public final String serInfoUrl = AlsayerCoreConstants.SCPI_SER_INFO_URL;

    private ConfigurationService configurationService;

    private ModelService modelService;

    @Override
    public Transition executeAction(StoreFrontCustomerProcessModel storeFrontCustomerProcessModel) throws RetryLaterException, Exception {

        CustomerModel customer = storeFrontCustomerProcessModel.getCustomer();
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HEADER_AUTH_KEY, HEDER_AUTH_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);

        for (VehicleModel vehicle : customer.getVehicles()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(CHASSIS_NO, vehicle.getChassisNumber());
            JSONArray jsonArray = new JSONArray();
            jsonArray.add(jsonObject);

            JSONObject finalObj = new JSONObject();
            finalObj.put(ESERSET, jsonArray);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(finalObj, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(getConfigurationService().getConfiguration().getString(serInfoUrl), entity, String.class);
            if (!("\"\"").equals(response.getBody())) {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                try {

                    EccServiceHistoryResponse responseBody = objectMapper.readValue(response.getBody(), EccServiceHistoryResponse.class);
                    List<E_ser_info> e_ser_infos = responseBody.getE_ser_infos();
                    List<ServiceHistoryModel> serviceHistoryModels = new ArrayList<>();
                    for (E_ser_info ser_info : e_ser_infos) {
                        ServiceHistoryModel serviceHistoryModel = new ServiceHistoryModel();
                        serviceHistoryModel.setUid(UUID.randomUUID().toString());
                        serviceHistoryModel.setMileage(Double.parseDouble(ser_info.getMileage()));
                        serviceHistoryModel.setLocation(ser_info.getWerks_n());
                        Date date = DateUtil.convertStringToDate(ser_info.getAudat());
                        serviceHistoryModel.setServiceDate(date);
                        serviceHistoryModel.setInvAmt(Double.parseDouble(ser_info.getInv_Amt()));
                        serviceHistoryModel.setServiceType(ser_info.getService_Type());
                        serviceHistoryModel.setServiceDesc(ser_info.getServiceDesc());
                        serviceHistoryModel.setLocationCode(ser_info.getLocation());
                        serviceHistoryModels.add(serviceHistoryModel);
                    }
                    vehicle.setServiceHistory(serviceHistoryModels);
                    getModelService().save(vehicle);

                } catch (JsonProcessingException ex) {
                    LOG.error(ex+AlsayerCoreConstants.SER_ERR_MSG);
                }
            }
        }
        return Transition.OK;
    }

    public ConfigurationService getConfigurationService() {
        return configurationService;
    }

    public void setConfigurationService(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    @Override
    public ModelService getModelService() {
        return modelService;
    }

    @Override
    public void setModelService(ModelService modelService) {
        this.modelService = modelService;
    }
}
