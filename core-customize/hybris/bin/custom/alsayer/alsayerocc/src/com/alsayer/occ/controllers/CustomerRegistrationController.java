
package com.alsayer.occ.controllers;

import com.alsayer.facades.customer.AlsayerCustomerFacade;
import com.alsayer.occ.constants.AlsayeroccConstants;
import com.alsayer.occ.dto.AlsayerUserSignUpWsDTO;
import com.alsayer.occ.dto.CustomerRegistrationResultDTO;
import com.alsayer.occ.dto.ECCCustomerWsDTO;
import de.hybris.platform.commercefacades.customergroups.CustomerGroupFacade;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commercefacades.user.data.RegisterData;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commerceservices.customer.TokenInvalidatedException;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.webservicescommons.cache.CacheControl;
import de.hybris.platform.webservicescommons.cache.CacheControlDirective;
import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceValidationException;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import de.hybris.platform.webservicescommons.mapping.FieldSetLevelHelper;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdParam;
import de.hybris.platform.webservicescommons.swagger.ApiFieldsParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Authorization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Locale;


@Controller
@RequestMapping(value = "{baseSiteId}/register")
@CacheControl(directive = CacheControlDirective.PRIVATE)
@Api(tags = "Customer")
public class CustomerRegistrationController
{

    final static Logger LOG = LoggerFactory.getLogger(CustomerRegistrationController.class);
    public static final String USER_MAPPER_CONFIG = "firstName,lastName,titleCode,currency(isocode),language(isocode)";

    private static final String ERROR_MSG = "error";
    private static final String SUCCESS_MSG = "success";
    private static final String ERROR_STATUS = "";
    private static final String SUCCESS_STATUS = "SUCCESS_STATUS";
    protected static final String DEFAULT_FIELD_SET = FieldSetLevelHelper.DEFAULT_LEVEL;



    @Resource(name="customerFacade")
    private AlsayerCustomerFacade customerFacade;
    @Resource(name = "alsayerSignUpDTOValidator")
    private Validator alsayerSignUpDTOValidator;
    @Resource(name = "guestConvertingDTOValidator")
    private Validator guestConvertingDTOValidator;
    @Resource(name = "passwordStrengthValidator")
    private Validator passwordStrengthValidator;
    @Resource(name = "customerGroupFacade")
    private CustomerGroupFacade customerGroupFacade;
    @Resource(name = "HttpRequestUserSignUpDTOPopulator")
    private Populator<HttpServletRequest, AlsayerUserSignUpWsDTO> httpRequestUserSignUpDTOPopulator;
    @Resource(name = "putUserDTOValidator")
    private Validator putUserDTOValidator;


    @Resource
    private DataMapper dataMapper;

    private static final String BASIC_FIELD_SET = "BASIC";


    @RequestMapping(value = "/getCustomerDetails/{id}",method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(value = HttpStatus.CREATED)
    @ResponseBody
    @ApiOperation(value = "To get the customer details", notes = "Only registered users will get the information from ECC")
    public ECCCustomerWsDTO getCustomerDetails(@PathVariable("id") final String code,
                                        @ApiFieldsParam(defaultValue = BASIC_FIELD_SET)
                                        final HttpServletRequest httpRequest, final HttpServletResponse httpResponse)
    {
        ECCCustomerWsDTO eccCustomerWsDTO = customerFacade.getCustomerECCDetails(code);
        customerFacade.sendOTP();
        return eccCustomerWsDTO;

    }


    @Secured({ "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT" })
    @RequestMapping(value = "/activateUser", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE })
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    @ApiOperation(nickname = "activateUser", value = "Activate the customer on clicking the activation link.", notes = "Activate the customer on clicking the activation link.", authorizations = {
            @Authorization(value = "oauth2_client_credentials") })
    @ApiBaseSiteIdParam
    public CustomerRegistrationResultDTO activateUser(
            @ApiParam(value = "Request body parameter that contains details such as token and new password", required = true)
            @RequestParam(value = "token", required = false) final String token,
            @ApiFieldsParam @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
            throws TokenInvalidatedException, UnsupportedEncodingException {
        LOG.info("Executing method doResetPassword");
        //String token = URLDecoder.decode(code, "UTF-8");
        customerFacade.activateUser(token);

        CustomerRegistrationResultDTO customerRegistrationResultDTO = new CustomerRegistrationResultDTO();

        customerRegistrationResultDTO.setReason("Token is valid");
        customerRegistrationResultDTO.setStatus("Success");

        return customerRegistrationResultDTO;

    }


    @RequestMapping(value="/createUser",method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ResponseStatus(value = HttpStatus.CREATED)
    @ResponseBody
    @ApiOperation(nickname = "createUser", value = " Registers a customer", notes = "Registers a customer. Requires the following "
            + "parameters: password, name, arabicName, mobileNumber,otp,civilId.")
    @ApiBaseSiteIdParam
    public CustomerRegistrationResultDTO createUser(@ApiParam(value = "User's object.", required = true) @RequestBody final AlsayerUserSignUpWsDTO user,
                                                    @ApiFieldsParam @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields,
                                                    final HttpServletRequest httpRequest, final HttpServletResponse httpResponse)
    {
        validate(user, "user", alsayerSignUpDTOValidator);
        final RegisterData registerData = getDataMapper()
                .map(user, RegisterData.class, "login, password, name, arabicName, mobile,civilId,uid,oneTimePassword");
        boolean userExists = false;
        CustomerRegistrationResultDTO customerRegistrationResultDTO = new CustomerRegistrationResultDTO();
        try
        {
           boolean result =  customerFacade.validateOTP(registerData);
            System.out.println(result);
            if(result == false)
            {
                customerRegistrationResultDTO.setReason("OTP is Invalid");
                customerRegistrationResultDTO.setStatus("Failure");
                return  customerRegistrationResultDTO;
            }
            customerFacade.register(registerData);
        }
        catch (final DuplicateUidException | ParseException ex)
        {
            userExists = true;
            LOG.debug("Duplicated UID", ex);
        }
        //Sending record for Synchronisation
        //customerFacade.eccRecordSynchronization(registerData);
        //Fetching All values regarding the customer and vehicles
        //customerFacade.fetchECCCustomerRecord(registerData);

        final String userId = user.getUid().toLowerCase(Locale.ENGLISH);
       httpResponse.setHeader(AlsayeroccConstants.LOCATION, getAbsoluteLocationURL(httpRequest, userId));
        //final CustomerData customerData = getCustomerData(registerData, userExists, userId);
        customerRegistrationResultDTO.setReason("OTP is valid");
        customerRegistrationResultDTO.setStatus("Success");
        return customerRegistrationResultDTO;
    }

    protected CustomerData getCustomerData(final RegisterData registerData, final boolean userExists, final String userId)
    {
        final CustomerData customerData;
        if (userExists)
        {
            customerData = customerFacade.nextDummyCustomerData(registerData);
        }
        else
        {
            customerData = customerFacade.getUserForUID(userId);
        }
        return customerData;
    }

    protected String getAbsoluteLocationURL(final HttpServletRequest httpRequest, final String uid)
    {
        final String requestURL = httpRequest.getRequestURL().toString();
        final String encodedUid = UriUtils.encodePathSegment(uid, StandardCharsets.UTF_8.name());
        return UriComponentsBuilder.fromHttpUrl(requestURL).pathSegment(encodedUid).build().toString();
    }


    protected void validate(final Object object, final String objectName, final Validator validator)
    {
        final Errors errors = new BeanPropertyBindingResult(object, objectName);
        validator.validate(object, errors);
        if (errors.hasErrors())
        {
            throw new WebserviceValidationException(errors);
        }
    }




    protected DataMapper getDataMapper()
    {
        return dataMapper;
    }

    protected void setDataMapper(final DataMapper dataMapper)
    {
        this.dataMapper = dataMapper;
    }




}