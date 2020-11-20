/*
 * Author: archana.prasad
 */
package com.alsayer.core.event.sms.gateway;

import com.alsayer.core.model.SMSGatewayProcessModel;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;

public class SMSGatewayEvent extends AbstractEvent
{
	private final SMSGatewayProcessModel process;
	private final String mobile ;
	private final String message ;

	public SMSGatewayEvent(SMSGatewayProcessModel process) {

		this.process = process;

		if (process != null) {
		this.mobile	= getProcess().getMobile();
		this.message=getProcess().getMessage();

		} else {
			this.mobile = null;
			this.message="";
		}

	}

	public SMSGatewayProcessModel getProcess() {
		return process;
	}
}
