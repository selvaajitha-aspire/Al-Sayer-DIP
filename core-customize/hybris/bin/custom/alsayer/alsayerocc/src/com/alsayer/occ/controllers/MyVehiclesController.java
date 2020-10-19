
/*
 * Author: Archana Prasad
 * My Vehicles Controller
 */

package com.alsayer.occ.controllers;

import com.alsayer.facades.data.VehicleData;
import com.alsayer.facades.vehicles.impl.DefaultMyVehiclesFacade;
import com.alsayer.occ.dto.VehicleListWsDTO;
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
import java.util.List;


@Controller
@RequestMapping(value = "{baseSiteId}/my-vehicles")
@Api(tags = "")
public class MyVehiclesController {

    final static Logger LOG = LoggerFactory.getLogger(MyVehiclesController.class);

    private static final String ERROR_MSG = "error";
    private static final String SUCCESS_MSG = "success";
    private static final String ERROR_STATUS = "";
    private static final String SUCCESS_STATUS = "SUCCESS_STATUS";

    @Resource
    private DefaultMyVehiclesFacade myVehiclesFacade;


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
        List<VehicleData> dataList = myVehiclesFacade.getVehicles();
        VehicleListWsDTO vehicleList = new VehicleListWsDTO();
        if(CollectionUtils.isNotEmpty(dataList))
        {
            vehicleList.setVehicleList(dataMapper.mapAsList(dataList, VehicleWsDTO.class, fields));
        }
        return vehicleList;
    }

}