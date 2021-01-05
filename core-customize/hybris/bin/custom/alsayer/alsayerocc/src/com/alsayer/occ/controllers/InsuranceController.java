package com.alsayer.occ.controllers;

import com.alsayer.facades.data.InsuranceData;
import com.alsayer.facades.insurance.InsurancesFacade;
import com.alsayer.occ.dto.InsuranceWSDTO;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;

@Controller
@RequestMapping(value = "{baseSiteId}/insurance")
@Api(tags = "")
public class InsuranceController {

    final static Logger LOG = LoggerFactory.getLogger(InsuranceController.class);

    private static final String ERROR_MSG = "error";
    private static final String SUCCESS_MSG = "success";
    private static final String ERROR_STATUS = "ERROR";
    private static final String SUCCESS_STATUS = "SUCCESS";

    @Resource
    private InsurancesFacade insurancesFacade;


    @Resource
    private DataMapper dataMapper;

    private static final String BASIC_FIELD_SET = "BASIC";


    @Secured(
            {"ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERGROUP"})
    @RequestMapping(value = "/getInsurances", method = RequestMethod.GET,
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    @ApiOperation(value = "Get Insurances of Vehicle", notes = "Only registered users can get insurance details")
    public List<InsuranceWSDTO> getDriverDetails(@ApiParam(value = "Response configuration. This is the list of fields that should be returned in the response body.", allowableValues = "BASIC, DEFAULT, FULL")
                                               @RequestParam(defaultValue = BASIC_FIELD_SET) final String fields) {
        List<InsuranceData> insuranceDataList = insurancesFacade.getInsurance();
        if (CollectionUtils.isNotEmpty(insuranceDataList)) {
            return dataMapper.mapAsList(insuranceDataList, InsuranceWSDTO.class, fields);
        }
        return new LinkedList<>();
    }



}
