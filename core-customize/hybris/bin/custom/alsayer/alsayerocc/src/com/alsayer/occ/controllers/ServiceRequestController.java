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
@RequestMapping(value = "{baseSiteId}/service_request")
@Api(tags = "")
public class ServiceRequestController {

    final static Logger LOG = LoggerFactory.getLogger(ServiceRequestController.class);


    private static final String BASIC_FIELD_SET = "BASIC";

    @Resource
    private ServiceRequestService serviceRequestService;

    @Secured(
            { "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERGROUP" })
    @RequestMapping(value = "/get_all_service_requests", method = RequestMethod.GET,
            consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    @ApiOperation(value = "Get All Service Requests", notes = "For ALSAYER ADMIN")
    public List<ServiceRequestData> getAllServiceRequests(@ApiParam(value = "Response configuration. This is the list of fields that should be returned in the response body.",
            allowableValues = "BASIC, DEFAULT, FULL")
                                                              @RequestParam(defaultValue = BASIC_FIELD_SET) final String fields)
    {
    	serviceRequestService.getAllServiceRequests();

        serviceRequestService.getServiceRequestsByStatus("STARTED");

        serviceRequestService.getServiceRequestByUID("8796093066979");

        serviceRequestService.getServiceRequestsByCustomerId("8796125954052");

        serviceRequestService.getServiceRequestsByCustomerIdAndStatus("8796125954052","STARTED");

        serviceRequestService.getServiceRequestsByVehicleId("8796093066978");

        serviceRequestService.getServiceRequestsByVehicleIdAndStatus("8796093066978","STARTED");

        return null;
    }

}
