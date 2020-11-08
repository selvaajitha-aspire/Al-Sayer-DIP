
/*
 * Author: Archana Prasad
 * Roadside Assistance Controller
 */

package com.alsayer.occ.controllers;

import com.alsayer.facades.data.DriverDetailsData;
import com.alsayer.facades.data.RsaRequestData;
import com.alsayer.facades.roadsideassistance.RoadSideAssistanceFacade;
import com.alsayer.occ.dto.DriverDetailsWsDTO;
import com.alsayer.occ.dto.ResponseWsDTO;
import com.alsayer.occ.dto.RsaRequestWsDTO;
import com.alsayer.facades.data.VehicleData;
import de.hybris.platform.webservicescommons.dto.error.ErrorWsDTO;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@Controller
@RequestMapping(value = "{baseSiteId}/rsa")
@Api(tags = "")
public class RoadsideAssistanceController {

    final static Logger LOG = LoggerFactory.getLogger(RoadsideAssistanceController.class);

    private static final String ERROR_MSG = "error";
    private static final String SUCCESS_MSG = "success";
    private static final String ERROR_STATUS = "";
    private static final String SUCCESS_STATUS = "SUCCESS_STATUS";

    @Resource
    private RoadSideAssistanceFacade roadsideAssistanceFacade;


    @Resource
    private DataMapper dataMapper;

    private static final String BASIC_FIELD_SET = "BASIC";

    @Secured(
            {"ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERGROUP"})
    @RequestMapping(value = "/saveDetails", method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    @ApiOperation(value = "", notes = "Services for logged in user")
    public ResponseWsDTO displayServiceDetails(@RequestBody RsaRequestWsDTO data) {
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

    private  RsaRequestData storeServiceRequest(RsaRequestWsDTO serviceWsDTO){
        RsaRequestData serviceRequestData=new RsaRequestData();
        BeanUtils.copyProperties(serviceWsDTO,serviceRequestData);
        VehicleData vehicleData = new VehicleData();
        BeanUtils.copyProperties(serviceWsDTO.getVehicle(),vehicleData);
        serviceRequestData.setVehicle(vehicleData);
    return serviceRequestData;
    }

    @Secured(
            { "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERGROUP" })
    @RequestMapping(value = "/getDriver", method = RequestMethod.GET,
            consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    @ApiOperation(value = "Get Vehicles of Customer", notes = "Only registered users can get driver details")
    public DriverDetailsWsDTO getDriverDetails(@ApiParam(value = "Response configuration. This is the list of fields that should be returned in the response body.", allowableValues = "BASIC, DEFAULT, FULL")
                                        @RequestParam(defaultValue = BASIC_FIELD_SET) final String fields)
    {
        DriverDetailsData driverDetails= roadsideAssistanceFacade.getDriverDetails();
        if(driverDetails!=null)
        {
            DriverDetailsWsDTO  driverWsDTO = dataMapper.map(driverDetails, DriverDetailsWsDTO.class, fields);

            return driverWsDTO;
        }
        return new DriverDetailsWsDTO();
    }

}