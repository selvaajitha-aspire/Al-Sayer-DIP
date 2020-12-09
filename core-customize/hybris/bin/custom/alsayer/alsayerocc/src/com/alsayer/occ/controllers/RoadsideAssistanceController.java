
/*
 * Author: Archana Prasad
 * Roadside Assistance Controller
 */

package com.alsayer.occ.controllers;

import com.alsayer.facades.data.DriverDetailsData;
import com.alsayer.facades.data.RsaRequestData;
import com.alsayer.facades.data.VehicleData;
import com.alsayer.facades.roadsideassistance.RoadSideAssistanceFacade;
import com.alsayer.occ.constants.AlsayeroccConstants;
import com.alsayer.occ.dto.DriverDetailsWsDTO;
import com.alsayer.occ.dto.ResponseWsDTO;
import com.alsayer.occ.dto.RsaRequestWsDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.hybris.platform.cmsfacades.dto.MediaFileDto;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdAndUserIdParam;
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
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping(value = "{baseSiteId}/rsa")
@Api(tags = "")
public class RoadsideAssistanceController {

    final static Logger LOG = LoggerFactory.getLogger(RoadsideAssistanceController.class);

    private static final String ERROR_MSG = "error";
    private static final String SUCCESS_MSG = "success";
    private static final String ERROR_STATUS = "ERROR";
    private static final String SUCCESS_STATUS = "SUCCESS";

    @Resource
    private RoadSideAssistanceFacade roadsideAssistanceFacade;


    @Resource
    private DataMapper dataMapper;

    private static final String BASIC_FIELD_SET = "BASIC";


    @Secured(
            {"ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERGROUP"})
    @RequestMapping(value = "/saveDetails", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    @ApiOperation(value = "", notes = "Services for logged in user")
    @ApiBaseSiteIdAndUserIdParam
    public ResponseWsDTO displayServiceDetails(@RequestParam("form") final String form, @RequestParam("attachments") final MultipartFile attachments) {
        ResponseWsDTO response = new ResponseWsDTO();
        try {
            LOG.debug(form + "" + SUCCESS_STATUS);
            ObjectMapper mapper = new ObjectMapper();
            RsaRequestWsDTO data = mapper.readValue(form, RsaRequestWsDTO.class);
            List<MediaFileDto> mediaFileDtoList = new ArrayList<>();
            MediaFileDto media = null;
            if (null != attachments && attachments.getSize() > 0) {

                media = getFile(attachments, attachments.getInputStream());
                mediaFileDtoList.add(media);
                data.setAttachments(mediaFileDtoList);

            }
            response.setData(form);
            response.setStatus(SUCCESS_STATUS);
            response.setMessage(SUCCESS_MSG);
            roadsideAssistanceFacade.storeServiceRequest(storeServiceRequest(data));
        } catch (IllegalArgumentException | IOException ex) {
            LOG.error(AlsayeroccConstants.RSA_SAVE_ERROR, ex.getMessage());
            response.setStatus(ERROR_STATUS);
            response.setMessage(ERROR_MSG);
            response.setErrors(ERROR_MSG);
        }
        return response;
    }


    @Secured(
            {"ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERGROUP"})
    @RequestMapping(value = "/saveRSADetails", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    @ApiOperation(value = "", notes = "Services for logged in user")
    @ApiBaseSiteIdAndUserIdParam
    public ResponseWsDTO saveRSADetails(@RequestPart("form") final RsaRequestWsDTO data,
                                        @RequestPart(name = "attachments", required = false) final MultipartFile attachments) {
        ResponseWsDTO response = new ResponseWsDTO();
        try {
            //LOG.debug(form + "" + SUCCESS_STATUS);
            ObjectMapper mapper = new ObjectMapper();
            //RsaRequestWsDTO data = mapper.readValue(form, RsaRequestWsDTO.class);
            List<MediaFileDto> mediaFileDtoList = new ArrayList<>();
            MediaFileDto media = null;
            if (null != attachments && attachments.getSize() > 0) {

                media = getFile(attachments, attachments.getInputStream());
                mediaFileDtoList.add(media);
                data.setAttachments(mediaFileDtoList);

            }
            response.setData(data.toString());
            response.setStatus(SUCCESS_STATUS);
            response.setMessage(SUCCESS_MSG);
            roadsideAssistanceFacade.storeServiceRequest(storeServiceRequest(data));
        } catch (IllegalArgumentException | IOException ex) {
            LOG.error(AlsayeroccConstants.RSA_SAVE_ERROR, ex.getMessage());
            response.setStatus(ERROR_STATUS);
            response.setMessage(ERROR_MSG);
            response.setErrors(ERROR_MSG);
        }
        return response;
    }

    public MediaFileDto getFile(final MultipartFile file, final InputStream inputStream) {
        final MediaFileDto mediaFile = new MediaFileDto();
        mediaFile.setInputStream(inputStream);
        mediaFile.setName(file.getOriginalFilename());
        mediaFile.setSize(file.getSize());
        mediaFile.setMime(file.getContentType());
        return mediaFile;
    }

    private RsaRequestData storeServiceRequest(RsaRequestWsDTO serviceWsDTO) {
        RsaRequestData serviceRequestData = new RsaRequestData();
        BeanUtils.copyProperties(serviceWsDTO, serviceRequestData);
        VehicleData vehicleData = new VehicleData();
        List<MediaFileDto> list = new ArrayList<>();
        for (MediaFileDto mediaFileDto : serviceWsDTO.getAttachments()) {
            MediaFileDto fileDto = new MediaFileDto();
            BeanUtils.copyProperties(mediaFileDto, fileDto);
            list.add(fileDto);
        }
        BeanUtils.copyProperties(serviceWsDTO.getVehicle(), vehicleData);
        serviceRequestData.setVehicle(vehicleData);
        serviceRequestData.setAttachments(list);
        return serviceRequestData;
    }

    @Secured(
            {"ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERGROUP"})
    @RequestMapping(value = "/getDriver", method = RequestMethod.GET,
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    @ApiOperation(value = "Get Vehicles of Customer", notes = "Only registered users can get driver details")
    public DriverDetailsWsDTO getDriverDetails(@ApiParam(value = "Response configuration. This is the list of fields that should be returned in the response body.", allowableValues = "BASIC, DEFAULT, FULL")
                                               @RequestParam(defaultValue = BASIC_FIELD_SET) final String fields) {
        DriverDetailsData driverDetails = roadsideAssistanceFacade.getDriverDetails();
        if (driverDetails != null) {
            DriverDetailsWsDTO driverWsDTO = dataMapper.map(driverDetails, DriverDetailsWsDTO.class, fields);

            return driverWsDTO;
        }
        return new DriverDetailsWsDTO();
    }

}