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
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.personalizationservices.model.CxSegmentModel;
import de.hybris.platform.personalizationservices.model.CxUserToSegmentModel;
import de.hybris.platform.personalizationservices.segment.CxSegmentService;
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

import java.math.BigDecimal;
import java.util.*;

public class GetCustomerDetailsAction extends AbstractSimpleDecisionAction<StoreFrontCustomerProcessModel> {

    private static final Logger LOG = Logger.getLogger(GetCustomerDetailsAction.class);
    protected static final String HEADER_AUTH_KEY = "Authorization";
    protected static final String HEDER_AUTH_VALUE = "Basic UzAwMjE4NDAzMzM6T2N0LjIwMTc=";
    protected static final String EVEHICLESET = "E_Vehicle_Info";
    protected static final String EWTYSET = "E_wty_info";
    protected static final String CIVILID = "civilid";
    protected static final String CHASSIS_NO = "vhvin";
    protected static final String LXPROVIDER="lexus";
    protected static final String TYPROVIDER="toyota";
    protected static final String LXCATEGORY="CATEGORY LEXUS";
    protected static final String TYCATEGORY="CATEGORY TOYOTA";

    public final String vehicleInfoUrl = AlsayerCoreConstants.SCPI_VEHICLE_INFO_URL;
    public final String wtyInfoUrl = AlsayerCoreConstants.SCPI_WTY_INFO_URL;
    private CxSegmentService cxSegmentService;
    private ConfigurationService configurationService;
    private CMSSiteService cmsSiteService;
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
        try {
            if (!("\"\"").equals(response.getBody())) {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

                EccVehicleDetailsResponse responseBody = objectMapper.readValue(response.getBody(), EccVehicleDetailsResponse.class);
                LOG.debug(AlsayerCoreConstants.VEHICLE_RESPONSE + responseBody.toString());
                List<VehicleModel> vehicleList=getVehicleList(responseBody.getE_vehicle_infos());
                    if (!vehicleList.isEmpty()) {
                        /* CxUserToSegmentModel cxUserToSegmentModel=new CxUserToSegmentModel();
                        for (VehicleModel vehicleModel : vehicleList){

                            if (vehicleModel.getBrand().equalsIgnoreCase("LX")) {
                                cxUserToSegmentModel= getSegmentByCode(LXCATEGORY,LXPROVIDER,customer);
                                break;
                            } else {
                                 cxUserToSegmentModel=getSegmentByCode(TYCATEGORY,TYPROVIDER,customer);
                            }
                    }
                        List<CxUserToSegmentModel> userToSegmentModelList= new LinkedList<>();
                        if(CollectionUtils.isNotEmpty(customer.getUserToSegments())) {
                             userToSegmentModelList = new LinkedList<>((Collection<? extends CxUserToSegmentModel>) Arrays.asList(customer.getUserToSegments()));
                        }
                        userToSegmentModelList.add(cxUserToSegmentModel);
                        customer.setUserToSegments(userToSegmentModelList); */
                        customer.setVehicles(vehicleList);
                        modelService.save(customer);
                        getVehicleWarranty(customer);
                    }
                }

        } catch (JsonProcessingException ex) {
            LOG.debug(ex.getMessage());
        }

        return Transition.OK;

    }

    private CxUserToSegmentModel getSegmentByCode(String segment,String provider,CustomerModel customerModel){
        CxSegmentModel segmentModel=getCxSegmentService().getSegment(segment).get();
        CxUserToSegmentModel userToSegmentModel=new CxUserToSegmentModel();
        userToSegmentModel.setSegment(segmentModel);
        userToSegmentModel.setProvider(provider);
        userToSegmentModel.setAffinity(BigDecimal.ONE);
        userToSegmentModel.setBaseSite(getCmsSiteService().getCurrentSite());
        userToSegmentModel.setUser(customerModel);
        return userToSegmentModel;
    }
  private List<VehicleModel> getVehicleList(List<E_Vehicle_Info> e_vehicle_infos) {
      List<VehicleModel> vehicleList = new LinkedList<>();
      if (e_vehicle_infos.get(0) != null) {
          for (E_Vehicle_Info vehicleRes : e_vehicle_infos) {
              VehicleModel vehicle = new VehicleModel();
              vehicle.setChassisNumber(vehicleRes.getVHVIN());
              vehicle.setStatus(vehicleRes.getStatus());
              vehicle.setPlateNumber(vehicleRes.getDBM_LICEXT());
              vehicle.setModline(vehicleRes.getMODLINE());
              vehicle.setModyear(vehicleRes.getMODYEAR());
              vehicle.setBrand(vehicleRes.getBRAND());
              vehicleList.add(vehicle);
          }
      }
      return vehicleList;
  }
      private void getVehicleWarranty (CustomerModel customerModel){
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
                  LOG.debug(AlsayerCoreConstants.WTY_DETAILS_RESPONSE + response.getBody());
                  ObjectMapper objectMapper = new ObjectMapper();
                  objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                  try {
                      EccWarrantyResponse responseBody = objectMapper.readValue(response.getBody(), EccWarrantyResponse.class);
                      List<WarrantyModel> warrantyModelList = getWarrantyList(responseBody.geteWtyInfo());
                      vehicle.setWarranties(warrantyModelList);
                      getModelService().save(vehicle);
                  } catch (JsonProcessingException ex) {
                      LOG.debug(ex.getMessage());
                  }
              }
          }
      }

     private List<WarrantyModel> getWarrantyList(List<E_wty_info> warrantyResults){
         List<WarrantyModel> warrantyModelList = new LinkedList<>();
         for (E_wty_info warranty : warrantyResults) {
             WarrantyModel warrantyModel = new WarrantyModel();
             warrantyModel.setWarrantyType(warranty.getDescription());
             Date date = DateUtil.convertStringToDate(warranty.getWty_e_date());
             warrantyModel.setWarrantyExpiryDate(date);
             warrantyModel.setDescription(warranty.getDescription());
             warrantyModelList.add(warrantyModel);
         }
         return warrantyModelList;
     }


      public ConfigurationService getConfigurationService () {
          return configurationService;
      }

      public void setConfigurationService (ConfigurationService configurationService){
          this.configurationService = configurationService;
      }

      @Override
      public ModelService getModelService () {
          return modelService;
      }

      @Override
      public void setModelService (ModelService modelService){
          this.modelService = modelService;
      }

    public CxSegmentService getCxSegmentService() {
        return cxSegmentService;
    }

    public void setCxSegmentService(CxSegmentService cxSegmentService) {
        this.cxSegmentService = cxSegmentService;
    }

    public CMSSiteService getCmsSiteService() {
        return cmsSiteService;
    }

    public void setCmsSiteService(CMSSiteService cmsSiteService) {
        this.cmsSiteService = cmsSiteService;
    }
}

