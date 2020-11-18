/*
 * Author: archana.prasad
 */
package com.alsayer.core.event.sms.gateway;

import com.alsayer.core.enums.ServiceStatus;
import com.alsayer.core.model.RsaRequestModel;
import com.alsayer.core.model.SMSGatewayProcessModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
import de.hybris.platform.servicelayer.model.ModelService;
import org.springframework.beans.factory.annotation.Required;


/**
 * Listener for "SMS Gateway" functionality event.
 */
public class SMSGatewayEventListener extends AbstractEventListener<SMSGatewayEvent>
{

	private ModelService modelService;
	private BusinessProcessService businessProcessService;


	protected BusinessProcessService getBusinessProcessService()
	{
		return businessProcessService;
	}

	@Required
	public void setBusinessProcessService(final BusinessProcessService businessProcessService)
	{
		this.businessProcessService = businessProcessService;
	}

	/**
	 * @return the modelService
	 */
	protected ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}


	protected void onEvent(final SMSGatewayEvent event)
	{

		final String mobile = event.getProcess().getMobile();
		final String message= event.getProcess().getMessage();
		final SMSGatewayProcessModel smsGatewayProcessModel = getBusinessProcessService().createProcess(
				"smsGatewayProcess" + event.getProcess() + "-" + System.currentTimeMillis(),
				"smsGatewayProcess");
		smsGatewayProcessModel.setMobile(mobile);
		smsGatewayProcessModel.setMessage(message);
		getModelService().save(smsGatewayProcessModel);
		getBusinessProcessService().startProcess(smsGatewayProcessModel);
	}

}
