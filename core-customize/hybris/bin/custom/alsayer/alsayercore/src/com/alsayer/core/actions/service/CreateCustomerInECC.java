package com.alsayer.core.actions.service;

import com.alsayer.core.constants.AlsayerCoreConstants;
import com.alsayer.core.response.ProspectCustomerSetResponse;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.hybris.platform.commerceservices.enums.CustomerType;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.processengine.action.AbstractSimpleDecisionAction;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.task.RetryLaterException;
import net.minidev.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class CreateCustomerInECC extends AbstractSimpleDecisionAction<StoreFrontCustomerProcessModel> {

    private ConfigurationService configurationService;

    private ModelService modelService;

    private static final Logger LOG = Logger.getLogger(CreateCustomerInECC.class);
    private static final String EN_NAME="EnName";
    private static final String MOBILE="mobile";
    private static final String AR_NAME="ArName";
    private static final String CIVILID="civilId";
    private static final String CUSTID="bpNumber";
    private static final String EMAIL="email";

    public final String url = AlsayerCoreConstants.SCPI_PROSPECT_CUSTOMER_POST_URL;
    protected static final String HEADER_AUTH_KEY = "Authorization";
    protected static final String HEDER_AUTH_VALUE = "Basic UzAwMjE4NDAzMzM6T2N0LjIwMTc=";
    protected static final String ECUSTOMER = "ProspectCustomer";

    @Override
    public Transition executeAction(StoreFrontCustomerProcessModel storeFrontCustomerProcessModel) throws RetryLaterException, Exception {
        CustomerModel customer = storeFrontCustomerProcessModel.getCustomer();
        System.out.println("Action getting Started" + CustomerType.REGISTERED.equals(customer.getType()));
        if (CustomerType.REGISTERED.equals(customer.getType())) {
            LOG.debug(AlsayerCoreConstants.PROSPECT_CUST_MSG);
            return Transition.NOK;
        }else {
            try {
                LOG.debug(AlsayerCoreConstants.NEW_CUST_MSG);
                SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
                RestTemplate restTemplate = new RestTemplate(requestFactory);
                HttpHeaders headers = new HttpHeaders();
                headers.add(HEADER_AUTH_KEY, HEDER_AUTH_VALUE);
                headers.setContentType(MediaType.APPLICATION_JSON);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(EN_NAME, customer.getName());
                jsonObject.put(MOBILE, customer.getMobile());
                jsonObject.put(AR_NAME, customer.getArabicName());
                jsonObject.put(CIVILID, customer.getCivilId());
                jsonObject.put(CUSTID, "");
                jsonObject.put(EMAIL, customer.getEmail().isEmpty()?"":customer.getEmail());

                JSONObject finalObj = new JSONObject();
                finalObj.put(ECUSTOMER, jsonObject);

                HttpEntity<Map<String, Object>> entity = new HttpEntity<>(finalObj, headers);
                ResponseEntity<String> response = restTemplate.postForEntity(getConfigurationService().getConfiguration().getString(url), entity, String.class);
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                ProspectCustomerSetResponse responseBody = objectMapper.readValue(response.getBody(), ProspectCustomerSetResponse.class);
                customer.setEccCustId(responseBody.getProspectCustomerSet().getProspectCustomer().getBpNumber());
                getModelService().save(customer);
                return Transition.OK;

            } catch (final Exception e) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug(e);
                }
                return Transition.NOK;
            }

        }
    }

        public ConfigurationService getConfigurationService () {
            return configurationService;
        }

        public void setConfigurationService (ConfigurationService configurationService){
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
