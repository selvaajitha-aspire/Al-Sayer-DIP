package com.alsayer.occ.controllers;

import com.alsayer.core.servicehistory.service.ServiceHistoryService;
import com.alsayer.facades.data.ServiceHistoryData;
import com.alsayer.occ.dto.ServiceHistoryWSDTO;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping(value = "{baseSiteId}/service-history")
@Api(tags = "")
public class ServiceHistoryController {

    final static Logger LOG = LoggerFactory.getLogger(RsaRequestController.class);


    private static final String BASIC_FIELD_SET = "BASIC";

    @Resource
    private ServiceHistoryService serviceHistoryService;

    @Resource
    private DataMapper dataMapper;

    @Secured(
            { "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERGROUP" })
    @RequestMapping(value = "/getMyServiceHistoryByChassisNo/{chassisNo}", method = RequestMethod.GET,
            consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    @ApiOperation(value = "Get Service History of Logged-in Customer by Vehicle Chassis No", notes = "Can be used by customer only to get his own service history")
    public List<ServiceHistoryWSDTO> getMyServiceHistoryByChassisNo(@ApiParam(value = "Response configuration. This is the list of fields that should be returned in the response body.", allowableValues = "BASIC, DEFAULT, FULL")
                                                              @RequestParam(defaultValue = BASIC_FIELD_SET) final String fields,
                                                                    @ApiParam(value = "Vehicle chassis number") @PathVariable("chassisNo") String chassisNo)
    {
        List<ServiceHistoryData> serviceHistoryDataList =serviceHistoryService.getMyServiceHistoryByChassisNo(chassisNo);
        List<ServiceHistoryWSDTO> serviceHistoryWSDTOList= dataMapper.mapAsList(serviceHistoryDataList, ServiceHistoryWSDTO.class, fields);
        return serviceHistoryWSDTOList;
    }

}
