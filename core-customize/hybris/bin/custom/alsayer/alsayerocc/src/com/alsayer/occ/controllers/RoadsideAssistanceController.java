
/*
 * Author: Archana Prasad
 * Roadside Assistance Controller
 */

package com.alsayer.occ.controllers;

import com.alsayer.facades.data.ServiceRequestData;
import com.alsayer.facades.data.VehicleData;
import com.alsayer.facades.roadsideassistance.impl.DefaultRoadSideAssistanceFacade;
import com.alsayer.occ.dto.ResponseWsDTO;
import com.alsayer.occ.dto.ServiceWsDTO;
import com.alsayer.occ.dto.VehicleListWsDTO;
import com.alsayer.occ.dto.VehicleWsDTO;
import de.hybris.platform.webservicescommons.dto.error.ErrorWsDTO;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@Controller
@RequestMapping(value = "{baseSiteId}/rsa")
@Api(tags = "")
public class RoadsideAssistanceController {

    final static Logger LOG = LoggerFactory.getLogger(AlsayeroccController.class);

    private static final String ERROR_MSG = "error";
    private static final String SUCCESS_MSG = "success";
    private static final String ERROR_STATUS = "";
    private static final String SUCCESS_STATUS = "SUCCESS_STATUS";

    @Resource
    private DefaultRoadSideAssistanceFacade roadsideAssistanceFacade;


    @Resource
    private DataMapper dataMapper;

    private static final String BASIC_FIELD_SET = "BASIC";

    @Secured(
            { "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERGROUP" })
    @RequestMapping(value = "/get-vehicles", method = RequestMethod.GET,
            consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    @ApiOperation(value = "Get Vehicles of Customer", notes = "Only registered users can get vehicles")
    public VehicleListWsDTO getVehicles(@ApiParam(value = "Response configuration. This is the list of fields that should be returned in the response body.", allowableValues = "BASIC, DEFAULT, FULL")
                                                   @RequestParam(defaultValue = BASIC_FIELD_SET) final String fields)
    {
        List<VehicleData> dataList = roadsideAssistanceFacade.getVehicles();
        VehicleListWsDTO vehicleList = new VehicleListWsDTO();
        if(CollectionUtils.isNotEmpty(dataList))
        {
            vehicleList.setVehicleList(dataMapper.mapAsList(dataList, VehicleWsDTO.class, fields));
        }
        return vehicleList;
    }


    @Secured(
            {"ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERGROUP"})
    @RequestMapping(value = "/save-details", method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    @ApiOperation(value = "", notes = "Services for logged in user")
    public ResponseWsDTO displayServiceDetails(@RequestBody ServiceWsDTO data) {
        ResponseWsDTO response = new ResponseWsDTO();
        try {
            LOG.debug(data.toString() + "" + SUCCESS_STATUS);
            response.setData("vehicleName:" + data.getVehicle() + " /problem:" + data.getIssue() + " /latitude:" + data.getLatitude().toString() + " /longitude:" + data.getLongitude().toString()+ " /notes:" + data.getNotes() + " /attachments:" + data.getAttachments());
            response.setStatus(SUCCESS_STATUS);
            response.setMessage(SUCCESS_MSG);
            roadsideAssistanceFacade.storeServiceRequest(storeServiceRequest(data));
        } catch (IllegalArgumentException ex) {
            LOG.error("failed to save details: ", ex.getMessage());
            ErrorWsDTO error = new ErrorWsDTO();
            response.setStatus(ERROR_STATUS);
            response.setMessage(ERROR_MSG);
            response.setErrors(ERROR_MSG);
        }
        return response;
    }

    private  ServiceRequestData storeServiceRequest(ServiceWsDTO serviceWsDTO){
        ServiceRequestData serviceRequestData=new ServiceRequestData();
        BeanUtils.copyProperties(serviceWsDTO,serviceRequestData);
    return serviceRequestData;
    }

}