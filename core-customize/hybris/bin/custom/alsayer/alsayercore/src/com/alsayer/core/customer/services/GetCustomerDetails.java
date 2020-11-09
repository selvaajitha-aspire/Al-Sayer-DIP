package com.alsayer.core.customer.services;

import com.alsayer.core.constants.AlsayerCoreConstants;
import com.alsayer.core.model.VehicleModel;
import com.alsayer.core.response.*;
import com.alsayer.occ.dto.ECCCustomerWsDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.processengine.action.AbstractSimpleDecisionAction;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GetCustomerDetails  extends AbstractSimpleDecisionAction {

    private static final Logger LOG = Logger.getLogger(GetCustomerDetails.class);
    protected static final String HEADER_AUTH_KEY = "Authorization";
    protected static final String HEDER_AUTH_VALUE = "Basic UzAwMjE4NDAzMzM6T2N0LjIwMTc=";
    public final String vehicleInfoUrl = AlsayerCoreConstants.SCPI_VEHICLE_INFO_URL;
    public final String wtyInfoUrl=AlsayerCoreConstants.SCPI_WTY_INFO_URL;


    private ConfigurationService configurationService;
    private UserService userService;
    private ModelService modelService;




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
        jsonObject.put("civilid","290100903547");
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(jsonObject);

        JSONObject finalObj = new JSONObject();
        finalObj.put("E_Vehicle_Info",jsonArray);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(finalObj, headers);
        System.out.println("enity : " + entity.getBody());

        ResponseEntity<String> response = restTemplate.postForEntity(getConfigurationService().getConfiguration().getString(vehicleInfoUrl), entity, String.class);
        LOG.info("GetCustomerDetails: it came in the process and fetched details" + response.getBody());
        ObjectMapper objectMapper=new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        CustomerModel customer= (CustomerModel) getUserService().getCurrentUser();
        //getVehicleWarranty();
        try {

            EccVehicleDetailsResponse responseBody =objectMapper.readValue(response.getBody(), EccVehicleDetailsResponse.class);
            LOG.info("ECC Response : " + responseBody.toString());
            List<E_Vehicle_Info> e_vehicle_infos=responseBody.getE_vehicle_infos();
            List<VehicleModel> vehicleList=new LinkedList<>();
            if(e_vehicle_infos.get(0)!=null) {
                for(E_Vehicle_Info vehicleRes:e_vehicle_infos){
                    VehicleModel vehicle=new VehicleModel();
                    vehicle.setChassisNumber(vehicleRes.getVHVIN());
                    vehicle.setStatus(vehicleRes.getStatus());
                    vehicle.setPlateNumber(vehicleRes.getDBM_LICEXT());
                    vehicle.setModline(vehicleRes.getMODLINE());
                    vehicle.setModyear(vehicleRes.getMODYEAR());
                    vehicleList.add(vehicle);
                }
               if(!vehicleList.isEmpty())
               {
                   customer.setVehicle(vehicleList);
                   modelService.save(customer);

               }
            }
        }catch (JsonProcessingException ex){
            ex.printStackTrace();
        }

        return Transition.OK;

    }

    private void getVehicleWarranty() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        RestTemplate restTemplate  = new RestTemplate(requestFactory);
        ECCCustomerWsDTO eccCustomerWsDTO = new ECCCustomerWsDTO();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HEADER_AUTH_KEY,HEDER_AUTH_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> map = new HashMap<>();
        //map.put("civilId",  currentUser.getCivilId());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("vhvin","UMNSSTESTVIN1");
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(jsonObject);

        JSONObject finalObj = new JSONObject();
        finalObj.put("E_wty_info",jsonArray);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(finalObj, headers);
        System.out.println("enity : " + entity.getBody());

        ResponseEntity<String> response = restTemplate.postForEntity(getConfigurationService().getConfiguration().getString(wtyInfoUrl), entity, String.class);
        LOG.info("it came in the process and fetched details" + response.getBody());
        ObjectMapper objectMapper=new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {

            EccWarrantyResponse responseBody =objectMapper.readValue(response.getBody(), EccWarrantyResponse.class);
            LOG.info("ECC Response 1 : " + responseBody.geteWtyInfo().getWarrantyResults().get(0).toString());
            List<WarrantyResult> warrantyResults=responseBody.geteWtyInfo().getWarrantyResults();


        }catch (JsonProcessingException ex){
            ex.printStackTrace();
        }
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

    @Override
    public ModelService getModelService() {
        return modelService;
    }

    @Override
    public void setModelService(ModelService modelService) {
        this.modelService = modelService;
    }
}
