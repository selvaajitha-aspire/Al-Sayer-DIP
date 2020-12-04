
/*
 * Author: Archana Prasad
 * My Vehicles Controller
 */

package com.alsayer.occ.controllers;

import com.alsayer.facades.data.VehicleData;
import com.alsayer.facades.vehicles.MyVehiclesFacade;
import com.alsayer.facades.vehicles.impl.DefaultMyVehiclesFacade;
import com.alsayer.occ.dto.VehicleListWsDTO;
import com.alsayer.occ.dto.VehicleWsDTO;
import com.alsayer.occ.dto.WarrantyWsDTO;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import de.hybris.platform.webservicescommons.mapping.FieldSetLevelHelper;
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
        List<VehicleData> dataList = myVehiclesFacade.getVehicles();
            return getVehicleWsDtoList(dataList);

    }

    private VehicleListWsDTO getVehicleWsDtoList(List<VehicleData> dataList) {
        VehicleListWsDTO vehicleList = new VehicleListWsDTO();
        VehicleWsDTO vehicleWsDTO =new VehicleWsDTO();
        List<VehicleWsDTO> vehicleWsDTOList=new LinkedList<>();
        for(VehicleData data : dataList) {
            vehicleWsDTO.setChassisNumber(data.getChassisNumber());
            vehicleWsDTO.setModline(data.getModline());
            vehicleWsDTO.setModyear(data.getModyear());
            vehicleWsDTO.setPlateNumber(data.getPlateNumber());
            if(CollectionUtils.isNotEmpty(data.getWarranties())) {
                vehicleWsDTO.setWarranties(dataMapper.mapAsList(data.getWarranties(),WarrantyWsDTO.class, FieldSetLevelHelper.DEFAULT_LEVEL));
            }
            vehicleWsDTOList.add(vehicleWsDTO);
        }
        vehicleList.setVehicleList(vehicleWsDTOList);
        return vehicleList;
    }



}