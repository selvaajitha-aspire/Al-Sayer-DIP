package com.alsayer.core.actions.service;

import com.alsayer.core.constants.AlsayerCoreConstants;
import com.alsayer.core.model.VehicleModel;
import com.alsayer.core.model.WarrantyModel;
import com.alsayer.core.response.E_Vehicle_Info;
import com.alsayer.core.response.E_wty_info;
import com.alsayer.core.response.EccVehicleDetailsResponse;
import com.alsayer.core.response.EccWarrantyResponse;
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
import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.*;

public class GetCustomerDetailsAction extends AbstractSimpleDecisionAction<StoreFrontCustomerProcessModel> {

    private static final Logger LOG = Logger.getLogger(GetCustomerDetailsAction.class);
    protected static final String HEADER_AUTH_KEY = "Authorization";
    protected static final String HEDER_AUTH_VALUE = "Basic UzAwMjE4NDAzMzM6T2N0LjIwMTc=";
    protected static final String EVEHICLESET = "E_Vehicle_Info";
    protected static final String EWTYSET = "E_wty_info";
    protected static final String CIVILID = "civilid";
    protected static final String CHASSIS_NO = "vhvin";

    public final String vehicleInfoUrl = AlsayerCoreConstants.SCPI_VEHICLE_INFO_URL;
    public final String wtyInfoUrl = AlsayerCoreConstants.SCPI_WTY_INFO_URL;


    private ConfigurationService configurationService;

    private ModelService modelService;


    @Override
    public Transition executeAction(StoreFrontCustomerProcessModel businessProcessModel) throws RetryLaterException, Exception {

        CustomerModel customer = businessProcessModel.getCustomer();
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HEADER_AUTH_KEY, HEDER_AUTH_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(CIVILID, customer.getCivilId());
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(jsonObject);

        JSONObject finalObj = new JSONObject();
        finalObj.put(EVEHICLESET, jsonArray);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(finalObj, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(getConfigurationService().getConfiguration().getString(vehicleInfoUrl), entity, String.class);
        LOG.debug(AlsayerCoreConstants.CUST_REG_PROCESS);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            EccVehicleDetailsResponse responseBody = objectMapper.readValue(response.getBody(), EccVehicleDetailsResponse.class);
            LOG.debug(AlsayerCoreConstants.VEHICLE_RESPONSE+ responseBody.toString());
            List<E_Vehicle_Info> e_vehicle_infos = responseBody.getE_vehicle_infos();
            List<VehicleModel> vehicleList = new LinkedList<>();
            if (e_vehicle_infos.get(0) != null) {
                for (E_Vehicle_Info vehicleRes : e_vehicle_infos) {
                    VehicleModel vehicle = new VehicleModel();
                    vehicle.setChassisNumber(vehicleRes.getVHVIN());
                    vehicle.setStatus(vehicleRes.getStatus());
                    vehicle.setPlateNumber(vehicleRes.getDBM_LICEXT());
                    vehicle.setModline(vehicleRes.getMODLINE());
                    vehicle.setModyear(vehicleRes.getMODYEAR());
                    vehicleList.add(vehicle);
                }
                if (!vehicleList.isEmpty()) {

                    customer.setVehicles(vehicleList);
                    modelService.save(customer);
                    getVehicleWarranty(customer);
                }
            }
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }

        return Transition.OK;

    }

    private void getVehicleWarranty(CustomerModel customerModel) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HEADER_AUTH_KEY, HEDER_AUTH_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject jsonObject = new JSONObject();
        for (VehicleModel vehicle : customerModel.getVehicles()) {
            jsonObject.put(CHASSIS_NO, vehicle.getChassisNumber());
            JSONArray jsonArray = new JSONArray();
            jsonArray.add(jsonObject);

            JSONObject finalObj = new JSONObject();
            finalObj.put(EWTYSET, jsonArray);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(finalObj, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(getConfigurationService().getConfiguration().getString(wtyInfoUrl), entity, String.class);
            if (!("\"\"").equals(response.getBody())) {
                LOG.debug(AlsayerCoreConstants.WTY_DETAILS_RESPONSE+ response.getBody());
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                try {
                    EccWarrantyResponse responseBody = objectMapper.readValue(response.getBody(), EccWarrantyResponse.class);
                    List<E_wty_info> warrantyResults = responseBody.geteWtyInfo();
                    List<WarrantyModel> warrantyModelList = new ArrayList<>();

                    for (E_wty_info warranty : warrantyResults) {
                        WarrantyModel warrantyModel = new WarrantyModel();
                        warrantyModel.setWarrantyType(warranty.getDescription());
                        Date date = DateUtil.convertStringToDate(warranty.getWty_e_date());
                        warrantyModel.setWarrantyExpiryDate(date);
                        warrantyModelList.add(warrantyModel);
                    }
                    vehicle.setWarranties(warrantyModelList);
                    getModelService().save(vehicle);

                } catch (JsonProcessingException ex) {
                    ex.printStackTrace();
                }
            }
        }
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
