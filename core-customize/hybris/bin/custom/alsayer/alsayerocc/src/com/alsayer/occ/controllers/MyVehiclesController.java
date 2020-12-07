
/*
 * Author: Archana Prasad
 * My Vehicles Controller
 */

package com.alsayer.occ.controllers;

import com.alsayer.facades.data.VehicleData;
import com.alsayer.facades.vehicles.MyVehiclesFacade;
import com.alsayer.occ.dto.VehicleListWsDTO;
import com.alsayer.occ.dto.VehicleWsDTO;
import com.alsayer.occ.dto.WarrantyWsDTO;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;


@Controller
@RequestMapping(value = "{baseSiteId}/myVehicles")
@Api(tags = "")
public class MyVehiclesController {

    final static Logger LOG = LoggerFactory.getLogger(MyVehiclesController.class);

    @Resource
    private MyVehiclesFacade myVehiclesFacade;


    @Resource
    private DataMapper dataMapper;

    private static final String BASIC_FIELD_SET = "BASIC";

    @Secured(
            {"ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERGROUP"})
    @RequestMapping(value = "/getVehicles", method = RequestMethod.GET,
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    @ApiOperation(value = "Get Vehicles of Customer", notes = "Only registered users can get vehicles")
    public VehicleListWsDTO getVehicles(@ApiParam(value = "Response configuration. This is the list of fields that should be returned in the response body.", allowableValues = "BASIC, DEFAULT, FULL")
                                        @RequestParam(defaultValue = BASIC_FIELD_SET) final String fields) {
        return getVehicleWsDtoList(fields);
    }

    private VehicleListWsDTO getVehicleWsDtoList(final String fields) {
        final List<VehicleData> dataList = myVehiclesFacade.getVehicles();
        final VehicleListWsDTO vehicleList = new VehicleListWsDTO();
        final List<VehicleWsDTO> vehicleWsDTOList = new LinkedList<>();
        dataList.forEach(x -> {
            final VehicleWsDTO vehicleWsDTO = dataMapper.map(x, VehicleWsDTO.class, fields);
            if (x.getWarranties() != null)
            {
                vehicleWsDTO.setWarranties(dataMapper.mapAsList(x.getWarranties(), WarrantyWsDTO.class, fields));
            }
            vehicleWsDTOList.add(vehicleWsDTO);
        });
        vehicleList.setVehicleList(vehicleWsDTOList);
        return vehicleList;
    }



}