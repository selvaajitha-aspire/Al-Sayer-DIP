package com.alsayer.core.actions.service;

import com.alsayer.core.constants.AlsayerCoreConstants;
import com.alsayer.core.model.InsuranceModel;
import com.alsayer.core.model.VehicleModel;
import com.alsayer.core.response.InsuranceList;
import com.alsayer.core.response.InsuranceListSetResponse;
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

public class GetVehicleInsurancesAction extends AbstractSimpleDecisionAction<StoreFrontCustomerProcessModel> {

    private static final Logger LOG = Logger.getLogger(GetServiceHistoryAction.class);
    protected static final String HEADER_AUTH_KEY = "Authorization";
    protected static final String HEDER_AUTH_VALUE = "Basic UzAwMjE4NDAzMzM6T2N0LjIwMTc=";
    protected static final String EINSURANCESET = "InsuranceList";
    protected static final String CIVILID = "civilId";
    public final String url = AlsayerCoreConstants.SCPT_INSURANCE_URL;

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
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(CIVILID, customer.getCivilId());
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(jsonObject);

        JSONObject finalObj = new JSONObject();
        finalObj.put(EINSURANCESET, jsonArray);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(finalObj, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(getConfigurationService().getConfiguration().getString(url), entity, String.class);
        if (!("\"\"").equals(response.getBody())) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            try {
                InsuranceListSetResponse responseBody = objectMapper.readValue(response.getBody(), InsuranceListSetResponse.class);
                final List<InsuranceModel> insuranceModelList = new LinkedList<>();
                responseBody.getInsuranceList().forEach(x -> {
                        InsuranceModel insuranceModel = new InsuranceModel();
                        insuranceModel.setUid(UUID.randomUUID().toString());
                        Date expiryDate = DateUtil.convertStringToDate(x.getDateOfExpiry());
                        insuranceModel.setDateOfExpiry(expiryDate);
                        Date issueDate = DateUtil.convertStringToDate(x.getDateOfIssue());
                        insuranceModel.setDateOfIssue(issueDate);
                        insuranceModel.setCoverageInfo(x.getCoverageInfo());
                        insuranceModel.setPolicyNumber(x.getPolicyNumber());
                        insuranceModel.setChassisNumber(x.getChassisNumber().isEmpty()?"":x.getChassisNumber());
                        insuranceModel.setPlateNumber(x.getPlateNumber().isEmpty()?"":x.getPlateNumber());
                        insuranceModelList.add(insuranceModel);
                    });
                    customer.setInsurances(insuranceModelList);
                    getModelService().save(customer);
                    LOG.debug(AlsayerCoreConstants.INSURANCE_ACTION_MSG);
            } catch (JsonProcessingException ex) {
                LOG.error(ex + AlsayerCoreConstants.INSURANCE_ERR_MSG);
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
