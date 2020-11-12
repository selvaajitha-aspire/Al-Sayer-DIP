/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.alsayer.core.constants;

/**
 * Global class for all AlsayerCore constants. You can add global constants for your extension into this class.
 */
public final class AlsayerCoreConstants extends GeneratedAlsayerCoreConstants
{
	public static final String EXTENSIONNAME = "alsayercore";
	public static final String SCPI_URL = "scpi.commerce.url";
	public static final String SCPI_CUSTDETAILS_URL="scpi.commerce.custdetails.url";
	public static final String SCPI_VEHICLE_INFO_URL="scpi.commerce.vehicleinfo.url";
	public static final String SCPI_WTY_INFO_URL= "scpi.commerce.wtyinfo.url";
    public static final String OTP_TIME = "scpi.commerce.otp.time" ;
    public static final String CUST_DETAILS_RESPONSE="Customer Details Service response : ";
	public static final String VEHICLE_RESPONSE="Vehicle Set Response: ";
	public static final String WTY_DETAILS_RESPONSE="Warranty Details Response";
	public static final String CUST_REG_PROCESS="customerRegistrationEmailProcess started";
	public static final String TOKEN_ERR_MSG="User for token not found";
	public static final String EXPRED_TOKEN_ERR_MSG="Token Expired";


    private AlsayerCoreConstants()
	{
		//empty
	}

	// implement here constants used by this extension
	public static final String QUOTE_BUYER_PROCESS = "quote-buyer-process";
	public static final String QUOTE_SALES_REP_PROCESS = "quote-salesrep-process";
	public static final String QUOTE_USER_TYPE = "QUOTE_USER_TYPE";
	public static final String QUOTE_SELLER_APPROVER_PROCESS = "quote-seller-approval-process";
	public static final String QUOTE_TO_EXPIRE_SOON_EMAIL_PROCESS = "quote-to-expire-soon-email-process";
	public static final String QUOTE_EXPIRED_EMAIL_PROCESS = "quote-expired-email-process";
	public static final String QUOTE_POST_CANCELLATION_PROCESS = "quote-post-cancellation-process";


}
