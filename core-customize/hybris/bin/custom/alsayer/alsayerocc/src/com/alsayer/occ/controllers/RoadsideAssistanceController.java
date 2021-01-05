
/*
 * Author: Archana Prasad
 * Roadside Assistance Controller
 */

package com.alsayer.occ.controllers;

import com.alsayer.facades.data.RsaRequestData;
import com.alsayer.facades.data.VehicleData;
import com.alsayer.facades.roadsideassistance.RoadSideAssistanceFacade;
import com.alsayer.occ.constants.AlsayeroccConstants;
import com.alsayer.occ.dto.ResponseWsDTO;
import com.alsayer.occ.dto.RsaRequestWsDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.hybris.platform.cmsfacades.dto.MediaFileDto;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CountryData;
import de.hybris.platform.commercefacades.user.data.RegionData;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdAndUserIdParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
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
        final ResponseWsDTO response = new ResponseWsDTO();
        try {
            //LOG.debug(form + "" + SUCCESS_STATUS);
            //RsaRequestWsDTO data = mapper.readValue(form, RsaRequestWsDTO.class);
            final List<MediaFileDto> mediaFileDtoList = new ArrayList<>();

            if (null != attachments && attachments.getSize() > 0) {
                final MediaFileDto media = getFile(attachments, attachments.getInputStream());
                mediaFileDtoList.add(media);
            }
            data.setAttachments(mediaFileDtoList);
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
        final RsaRequestData serviceRequestData = new RsaRequestData();
        BeanUtils.copyProperties(serviceWsDTO, serviceRequestData);
        final VehicleData vehicleData = new VehicleData();
        final List<MediaFileDto> list = new ArrayList<>();
        for (final MediaFileDto mediaFileDto : serviceWsDTO.getAttachments()) {
            final MediaFileDto fileDto = new MediaFileDto();
            BeanUtils.copyProperties(mediaFileDto, fileDto);
            list.add(fileDto);
        }
        BeanUtils.copyProperties(serviceWsDTO.getVehicle(), vehicleData);
        serviceRequestData.setVehicle(vehicleData);
        serviceRequestData.setAttachments(list);
        AddressData addressData = new AddressData();
        CountryData countryData = new CountryData();
        RegionData regionData = new RegionData();
        BeanUtils.copyProperties(serviceWsDTO.getAddress(), addressData, "defaultAddress", "shippingAddress", "billingAddress", "firstName", "lastName", "visibleInAddressBook");
        BeanUtils.copyProperties(serviceWsDTO.getAddress().getCountry(), countryData);
        BeanUtils.copyProperties(serviceWsDTO.getAddress().getRegion(), regionData);
        addressData.setRegion(regionData);
        addressData.setCountry(countryData);
        serviceRequestData.setAddress(addressData);
        return serviceRequestData;
    }

}