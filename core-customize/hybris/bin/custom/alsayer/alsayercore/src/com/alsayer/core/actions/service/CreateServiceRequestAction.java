package com.alsayer.core.actions.service;

import com.alsayer.core.constants.AlsayerCoreConstants;
import com.alsayer.core.enums.EccIssueType;
import com.alsayer.core.enums.ReqType;
import com.alsayer.core.enums.RequestType;
import com.alsayer.core.enums.ServiceStatus;
import com.alsayer.core.model.RsaRequestModel;
import com.alsayer.core.model.RsaRequestProcessModel;
import com.alsayer.core.response.E_orderSet;
import com.alsayer.core.storelocator.service.PosService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.processengine.action.AbstractSimpleDecisionAction;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.storelocator.GPS;
import de.hybris.platform.storelocator.data.RouteData;
import de.hybris.platform.storelocator.exception.GeoServiceWrapperException;
import de.hybris.platform.storelocator.impl.DefaultGPS;
import de.hybris.platform.storelocator.impl.GoogleMapTools;
import de.hybris.platform.storelocator.location.Location;
import de.hybris.platform.storelocator.location.impl.DistanceUnawareLocation;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import de.hybris.platform.storelocator.route.DistanceAndRoute;
import de.hybris.platform.storelocator.route.Route;
import de.hybris.platform.storelocator.route.impl.DefaultDistanceAndRoute;
import de.hybris.platform.storelocator.route.impl.DefaultRoute;
import de.hybris.platform.util.Config;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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

    private final String url = AlsayerCoreConstants.SCPT_RSA_POST_URL;
    protected static final String HEADER_AUTH_KEY = "Authorization";
    protected static final String HEDER_AUTH_VALUE = "Basic UzAwMjE4NDAzMzM6T2N0LjIwMTc=";
    protected static final String EORDER = "E_order";
    private static final String TYLOCATIONID="3101";
    private static final String LXLOCATIONID="3147";

    public static final String GOOGLE_MAPS_URL = "google.maps.url";
    public static final String GOOGLE_GEOCODING_URL = "google.geocoding.url";
    private GoogleMapTools googleMapTools;

    private ConfigurationService configurationService;

    private PosService posService;



    @Override
    public Transition executeAction(RsaRequestProcessModel process) {
        final RsaRequestModel serviceRequest = process.getRsaRequest();

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
                    JSONObject finalObj=createRSARequestJSON(serviceRequest);
                    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(finalObj, headers);
                    ResponseEntity<String> response = restTemplate.postForEntity(getConfigurationService().getConfiguration().getString(url), entity, String.class);
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    E_orderSet responseBody = objectMapper.readValue(response.getBody(), E_orderSet.class);
                    if(HttpStatus.CREATED.equals(response.getStatusCode())&& !responseBody.getE_order().getVBELN().isEmpty()) {
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
                LOG.error(e);
                return Transition.NOK;
            }
        }
        return Transition.NOK;

    }

    private JSONObject createRSARequestJSON(final RsaRequestModel serviceRequest) {
        final String issue=serviceRequest.getIssue().getCode();
        final AddressModel address=serviceRequest.getAddress();
        final String chassisNumber=serviceRequest.getVehicle().getChassisNumber();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put(VHVIN,chassisNumber);
        jsonObject.put(KUNNR,"");
        jsonObject.put(MILEAGE,"");
        chooseLocationCode(serviceRequest,jsonObject);

        jsonObject.put(REMARKS1,"");
        jsonObject.put(RETURN,null);
        jsonObject.put(LATITUDE,serviceRequest.getLatitude().toString());
        jsonObject.put(LONGITUDE,serviceRequest.getLongitude().toString());
        jsonObject.put(ZIPCODE,address.getPostalcode());
        jsonObject.put(STREET,address.getLine1()+" "+address.getLine2());
        jsonObject.put(GOVERNANCE,address.getRegion().getName());
        jsonObject.put(COUNTRY,address.getCountry().getIsocode());


        JSONArray jsonArray = new JSONArray();

        if(serviceRequest.getType().equals(RequestType.RSA)){
            jsonObject.put(REQTYPE, ReqType.T.getCode());
            jsonObject.put(ISSUETYPE, EccIssueType.MR.getCode());

        }else {
            jsonObject.put(REQTYPE, ReqType.M.getCode());
            if(issue.equalsIgnoreCase("OUT_OF_FUEL"))
                jsonObject.put(ISSUETYPE,EccIssueType.M1.getCode());

            else if(issue.equalsIgnoreCase("DEAD_BATTERY"))
                jsonObject.put(ISSUETYPE,EccIssueType.M2.getCode());

            else if(issue.equalsIgnoreCase("FLAT_TYRE"))
                jsonObject.put(ISSUETYPE, EccIssueType.M3.getCode());

            else if(issue.equalsIgnoreCase("OTHERS_MUSAADA"))
                jsonObject.put(ISSUETYPE,EccIssueType.M4.getCode());


        }
        if(serviceRequest.getPaidService().equals(Boolean.TRUE)){
            jsonObject.put(PAID_SERVICE,"X");
        }else {
            jsonObject.put(PAID_SERVICE,"-");
        }
        jsonArray.add(jsonObject);
        JSONObject finalObj = new JSONObject();
        finalObj.put(EORDER, jsonArray);
        return finalObj;
    }

    private void chooseLocationCode(RsaRequestModel rasRequest, JSONObject jsonObject) {
        if(rasRequest.getPaidService()==Boolean.FALSE && rasRequest.getVehicle().getBrand().equalsIgnoreCase("TY")){
            jsonObject.put(WERKS,TYLOCATIONID);
            rasRequest.setLocationCode(TYLOCATIONID);
        }else if(rasRequest.getPaidService()==Boolean.FALSE && rasRequest.getVehicle().getBrand().equalsIgnoreCase("LX")){
            jsonObject.put(WERKS,LXLOCATIONID);
            rasRequest.setLocationCode(LXLOCATIONID);
        }
        else{
            String locationCode=chooseNearestGarageForNonMusaada(rasRequest,jsonObject);
            jsonObject.put(WERKS,locationCode);
            rasRequest.setLocationCode(locationCode);

        }
    }

    private String chooseNearestGarageForNonMusaada(RsaRequestModel rsaRequest, JSONObject jsonObject) {
        GPS currentGps=(new DefaultGPS()).create(rsaRequest.getLatitude(), rsaRequest.getLongitude());
        Map<Double,String>distanceMap=new HashMap<>();
        List<PointOfServiceModel> serviceCenters=getPosService().getServiceCenterPOS();
        serviceCenters.forEach(x->{
            GPS destGPS=(new DefaultGPS()).create(x.getLatitude(), x.getLongitude());
            DistanceAndRoute result = getDistanceAndRoute(currentGps, new DistanceUnawareLocation(x),destGPS);
            double roadDistance = result.getRoadDistance();
            distanceMap.put(roadDistance, x.getLocationCode());
        });
        TreeMap<Double,String> sorted = new TreeMap<>();
        sorted.putAll(distanceMap);
        Map.Entry<Double,String> entry = distanceMap.entrySet().iterator().next();
        return entry.getValue();
    }

    private DistanceAndRoute getDistanceAndRoute(GPS start, Location destination,GPS destGPS) throws GeoServiceWrapperException {
        GoogleMapTools geocodingModule = this.getMapTools(Config.getString("google.maps.url", (String)null));
        RouteData routeData = geocodingModule.getDistanceAndRoute(start, destGPS);
        Route route = new DefaultRoute(start, destination, routeData.getCoordinates());
        return new DefaultDistanceAndRoute(routeData.getDistance(), routeData.getEagleFliesDistance(), route);
    }
    public ConfigurationService getConfigurationService() {
        return configurationService;
    }

    public void setConfigurationService(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    public PosService getPosService() {
        return posService;
    }

    public void setPosService(PosService posService) {
        this.posService = posService;
    }

    public static Logger getLOG() {
        return LOG;
    }

    protected GoogleMapTools getMapTools(String url) {
        this.googleMapTools.setBaseUrl(url);
        return this.googleMapTools;
    }

    public void setGoogleMapTools(GoogleMapTools googleMapTools) {
        this.googleMapTools = googleMapTools;
    }
}
