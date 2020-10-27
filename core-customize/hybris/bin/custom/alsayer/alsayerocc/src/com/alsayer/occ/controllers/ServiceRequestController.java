package com.alsayer.occ.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import com.alsayer.core.servicerequest.service.ServiceRequestService;
import com.alsayer.facades.data.ServiceRequestData;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "{baseSiteId}/service-request")
@Api(tags = "")
public class ServiceRequestController {

    final static Logger LOG = LoggerFactory.getLogger(ServiceRequestController.class);


    private static final String BASIC_FIELD_SET = "BASIC";

    @Resource
    private ServiceRequestService serviceRequestService;

    @Secured(
            { "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERGROUP" })
    @RequestMapping(value = "/get-all-services", method = RequestMethod.GET,
            consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    @ApiOperation(value = "Get all Service Requests", notes = "Only ADMIN users can get all service requests")
    public List<ServiceRequestData> getAllServiceRequests(@ApiParam(value = "Response configuration. This is the list of fields that should be returned in the response body.", allowableValues = "BASIC, DEFAULT, FULL")
                                                              @RequestParam(defaultValue = BASIC_FIELD_SET) final String fields)
    {
    	return serviceRequestService.getAllServiceRequests();
    }

    @Secured(
            { "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERGROUP" })
    @RequestMapping(value = "/get-services-by-status", method = RequestMethod.GET,
            consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    @ApiOperation(value = "Get all Service Requests", notes = "Only ADMIN users can get all service requests")
    public List<ServiceRequestData> getServiceRequestsByStatus(@ApiParam(value = "Response configuration. This is the list of fields that should be returned in the response body.", allowableValues = "BASIC, DEFAULT, FULL")
                                                          @RequestParam(defaultValue = BASIC_FIELD_SET) final String fields,@RequestParam("status") String status )
    {
        return serviceRequestService.getServiceRequestsByStatus(status);
    }

    @Secured(
            { "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERGROUP" })
    @RequestMapping(value = "/get-services-by-id", method = RequestMethod.GET,
            consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    @ApiOperation(value = "Get all Service Requests", notes = "Only ADMIN users can get all service requests")
    public ServiceRequestData getServiceRequestsById(@ApiParam(value = "Response configuration. This is the list of fields that should be returned in the response body.", allowableValues = "BASIC, DEFAULT, FULL")
                                                          @RequestParam(defaultValue = BASIC_FIELD_SET) final String fields,@RequestParam("serviceId") String serviceId )
    {
        return serviceRequestService.getServiceRequestByUID(serviceId);
    }

    @Secured(
            { "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERGROUP" })
    @RequestMapping(value = "/get-services-by-customer", method = RequestMethod.GET,
            consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    @ApiOperation(value = "Get all Service Requests", notes = "Only ADMIN users can get all service requests")
    public List<ServiceRequestData> getServicesByCustomer(@ApiParam(value = "Response configuration. This is the list of fields that should be returned in the response body.", allowableValues = "BASIC, DEFAULT, FULL")
                                                          @RequestParam(defaultValue = BASIC_FIELD_SET) final String fields,@RequestParam("customerId") String customerId)
    {
        return serviceRequestService.getServiceRequestsByCustomerId(customerId);
    }

    @Secured(
            { "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERGROUP" })
    @RequestMapping(value = "/get-services-by-customer-status", method = RequestMethod.GET,
            consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    @ApiOperation(value = "Get all Service Requests", notes = "Only ADMIN users can get all service requests")
    public List<ServiceRequestData> getServicesByCustomerStatus(@ApiParam(value = "Response configuration. This is the list of fields that should be returned in the response body.", allowableValues = "BASIC, DEFAULT, FULL")
                                                          @RequestParam(defaultValue = BASIC_FIELD_SET) final String fields,@RequestParam("customerId") String customerId,@RequestParam("status") String status)
    {
        return serviceRequestService.getServiceRequestsByCustomerIdAndStatus(customerId,status);
    }

    @Secured(
            { "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERGROUP" })
    @RequestMapping(value = "/get-services-by-vehicle", method = RequestMethod.GET,
            consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    @ApiOperation(value = "Get all Service Requests", notes = "Only ADMIN users can get all service requests")
    public List<ServiceRequestData> getServicesByVehicle(@ApiParam(value = "Response configuration. This is the list of fields that should be returned in the response body.", allowableValues = "BASIC, DEFAULT, FULL")
                                                                @RequestParam(defaultValue = BASIC_FIELD_SET) final String fields,@RequestParam("vehicleId") String vehicleId)
    {
        return serviceRequestService.getServiceRequestsByVehicleId(vehicleId);
    }

    @Secured(
            { "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERGROUP" })
    @RequestMapping(value = "/get-services-by-vehicle-status", method = RequestMethod.GET,
            consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    @ApiOperation(value = "Get all Service Requests", notes = "Only ADMIN users can get all service requests")
    public List<ServiceRequestData> getServicesByVehicleStatus(@ApiParam(value = "Response configuration. This is the list of fields that should be returned in the response body.", allowableValues = "BASIC, DEFAULT, FULL")
                                                         @RequestParam(defaultValue = BASIC_FIELD_SET) final String fields,@RequestParam("vehicleId") String vehicleId,@RequestParam("status") String status)
    {
        return serviceRequestService.getServiceRequestsByVehicleIdAndStatus(vehicleId,status);
    }
}
