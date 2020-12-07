package com.alsayer.occ.controllers;

import com.alsayer.core.servicerequest.service.RsaRequestService;
import com.alsayer.facades.data.RsaRequestData;
import com.alsayer.occ.dto.DriverDetailsWsDTO;
import com.alsayer.occ.dto.RsaRequestListWsDTO;
import com.alsayer.occ.dto.RsaRequestWsDTO;
import com.alsayer.occ.dto.VehicleWsDTO;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;

@Controller
@RequestMapping(value = "{baseSiteId}/rsa-request")
@Api(tags = "")
public class RsaRequestController
{

    final static Logger LOG = LoggerFactory.getLogger(RsaRequestController.class);


    private static final String BASIC_FIELD_SET = "BASIC";

    @Resource
    private RsaRequestService rsaRequestService;

    @Resource
    private DataMapper dataMapper;

    @Secured(
            { "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERGROUP" })
    @RequestMapping(value = "/getAllRsaRequests", method = RequestMethod.GET,
            consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    @ApiOperation(value = "Get all Service Requests", notes = "Only ADMIN users can get all service requests")
    public List<RsaRequestData> getAllRsaRequests(@ApiParam(value = "Response configuration. This is the list of fields that should be returned in the response body.", allowableValues = "BASIC, DEFAULT, FULL")
                                                              @RequestParam(defaultValue = BASIC_FIELD_SET) final String fields)
    {
    	return rsaRequestService.getAllRsaRequests();
    }

    @Secured(
            { "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERGROUP" })
    @RequestMapping(value = "/getRsaRequestsByStatus", method = RequestMethod.GET,
            consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    @ApiOperation(value = "Get Service Requests by service request status", notes = "")
    public List<RsaRequestData> getRsaRequestsByStatus(@ApiParam(value = "Response configuration. This is the list of fields that should be returned in the response body.", allowableValues = "BASIC, DEFAULT, FULL")
                                                          @RequestParam(defaultValue = BASIC_FIELD_SET) final String fields,@RequestParam("status") String status )
    {
        return rsaRequestService.getRsaRequestsByStatus(status);
    }

    @Secured(
            { "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERGROUP" })
    @RequestMapping(value = "/getRsaRequestById", method = RequestMethod.GET,
            consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    @ApiOperation(value = "Get Service Request by pk", notes = "")
    public RsaRequestData getRsaRequestById(@ApiParam(value = "Response configuration. This is the list of fields that should be returned in the response body.", allowableValues = "BASIC, DEFAULT, FULL")
                                                          @RequestParam(defaultValue = BASIC_FIELD_SET) final String fields,@RequestParam("serviceId") String serviceId )
    {
        return rsaRequestService.getRsaRequestByUID(serviceId);
    }

    @Secured(
            { "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERGROUP" })
    @RequestMapping(value = "/getRsaRequestsByCustomer", method = RequestMethod.GET,
            consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    @ApiOperation(value = "Get Service Requests by customer PK", notes = "Can be used by customer himself or alsayer to get all services requests for a specific customer")
    public RsaRequestListWsDTO getRsaRequestsByCustomer(@ApiParam(value = "Response configuration. This is the list of fields that should be returned in the response body.", allowableValues = "BASIC, DEFAULT, FULL")
                                                          @RequestParam(defaultValue = BASIC_FIELD_SET) final String fields)
    {
        List<RsaRequestData> dataList = rsaRequestService.getRsaRequestsByCustomerId();
        RsaRequestListWsDTO rsaRequestListWsDTO = new RsaRequestListWsDTO();
        if(CollectionUtils.isNotEmpty(dataList))
        {

            rsaRequestListWsDTO.setRsaRequestsList(getRSAWsDtoList(dataList));
        }
        return rsaRequestListWsDTO;
    }

    @Secured(
            { "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERGROUP" })
    @RequestMapping(value = "/getRsaRequestsByChassisNo/{chassisNo}", method = RequestMethod.GET,
            consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    @ApiOperation(value = "Get Service Requests by customer PK", notes = "Can be used by customer himself or alsayer to get all services requests for a specific customer")
    public RsaRequestListWsDTO getRsaRequestsByChassisNo(@ApiParam(value = "Response configuration. This is the list of fields that should be returned in the response body.", allowableValues = "BASIC, DEFAULT, FULL")
                                                        @RequestParam(defaultValue = BASIC_FIELD_SET) final String fields,
                                                         @PathVariable("chassisNo") final String chassisNo)
    {
        List<RsaRequestData> dataList = rsaRequestService.getRsaRequestsByChassisAndCustomer(chassisNo);
        RsaRequestListWsDTO rsaRequestListWsDTO = new RsaRequestListWsDTO();
        if(CollectionUtils.isNotEmpty(dataList))
        {

            rsaRequestListWsDTO.setRsaRequestsList(getRSAWsDtoList(dataList));
        }
        return rsaRequestListWsDTO;
    }

    private  List<RsaRequestWsDTO> getRSAWsDtoList(List<RsaRequestData> dataList) {
        List<RsaRequestWsDTO> rsaRequestWsDTOList=new LinkedList<>();
        RsaRequestWsDTO rsaRequestWsDTO=new RsaRequestWsDTO();
        for(RsaRequestData data:dataList) {
           rsaRequestWsDTO.setVehicle( dataMapper.map(data.getVehicle(), VehicleWsDTO.class));
           rsaRequestWsDTO.setDriverDetails(dataMapper.map(data.getDriverDetails(), DriverDetailsWsDTO.class));
           rsaRequestWsDTO.setType(data.getType());
           rsaRequestWsDTO.setStatus(data.getStatus());
           rsaRequestWsDTO.setIssue(data.getIssue());
           rsaRequestWsDTO.setLatitude(data.getLatitude());
           rsaRequestWsDTO.setLongitude(data.getLongitude());
           rsaRequestWsDTO.setNotes(data.getNotes());
           rsaRequestWsDTO.setAttachments(data.getAttachments());
           rsaRequestWsDTOList.add(rsaRequestWsDTO);
        }
        return rsaRequestWsDTOList;
    }

    @Secured(
            { "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERGROUP" })
    @RequestMapping(value = "/getRsaRequestsByCustomerStatus", method = RequestMethod.GET,
            consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    @ApiOperation(value = "Get Service Requests by Customer PK and status", notes = "Can be used by customer himself or alsayer to get all services requests for a specific customer with specific status")
    public List<RsaRequestData> getRsaRequestsByCustomerStatus(@ApiParam(value = "Response configuration. This is the list of fields that should be returned in the response body.", allowableValues = "BASIC, DEFAULT, FULL")
                                                          @RequestParam(defaultValue = BASIC_FIELD_SET) final String fields,@RequestParam("status") List<String> statuses)
    {
        return rsaRequestService.getRsaRequestsByCustomerIdAndStatus(statuses);
    }

    @Secured(
            { "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERGROUP" })
    @RequestMapping(value = "/getRsaRequestsByVehicle", method = RequestMethod.GET,
            consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    @ApiOperation(value = "Get Service Requests by Vehicle PK", notes = "Can be used by customer himself or alsayer to get all services requests for a specific Vehicle")
    public List<RsaRequestData> getRsaRequestsByVehicle(@ApiParam(value = "Response configuration. This is the list of fields that should be returned in the response body.", allowableValues = "BASIC, DEFAULT, FULL")
                                                                @RequestParam(defaultValue = BASIC_FIELD_SET) final String fields,@RequestParam("vehicleId") String vehicleId)
    {
        return rsaRequestService.getRsaRequestsByVehicleId(vehicleId);
    }

    @Secured(
            { "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERGROUP" })
    @RequestMapping(value = "/getRsaRequestsByVehicleStatus", method = RequestMethod.GET,
            consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    @ApiOperation(value = "Get Service Requests by Vehicle PK and status", notes = "Can be used by customer himself or alsayer to get all services requests for a specific Vehicle and a specific status")
    public List<RsaRequestData> getRsaRequestsByVehicleStatus(@ApiParam(value = "Response configuration. This is the list of fields that should be returned in the response body.", allowableValues = "BASIC, DEFAULT, FULL")
                                                         @RequestParam(defaultValue = BASIC_FIELD_SET) final String fields,@RequestParam("vehicleId") String vehicleId,@RequestParam("status") String status)
    {
        return rsaRequestService.getRsaRequestsByVehicleIdAndStatus(vehicleId,status);
    }
}
