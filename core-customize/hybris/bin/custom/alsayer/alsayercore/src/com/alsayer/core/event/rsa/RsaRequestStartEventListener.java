/*
 * Author: Archana Prasad
 * Service Request Event Listener
 */
package com.alsayer.core.event.rsa;

import com.alsayer.core.enums.ServiceStatus;
import com.alsayer.core.model.RsaRequestModel;
import com.alsayer.core.model.RsaRequestProcessModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
import de.hybris.platform.servicelayer.model.ModelService;


/**
 * Listener for RsaRequestStartEvent events.
 */
public class RsaRequestStartEventListener extends AbstractEventListener<RsaRequestStartEvent>
{

	private ModelService modelService;
	private BusinessProcessService businessProcessService;

	/**
	 * @return the businessProcessService
	 */
	protected BusinessProcessService getBusinessProcessService()
	{
		return businessProcessService;
	}

	/**
	 * @param businessProcessService
	 *           the businessProcessService to set
	 */
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
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	@Override
	protected void onEvent(final RsaRequestStartEvent rsaRequestStartEvent)
	{
		final ServiceStatus serviceState = rsaRequestStartEvent.getProcess().getServiceState();
		final RsaRequestModel serviceRequest= rsaRequestStartEvent.getProcess().getRsaRequest();
		final RsaRequestProcessModel serviceRequestProcess = getBusinessProcessService().createProcess(
				"service-process-" + serviceRequest.getUid() + "-" + System.currentTimeMillis(),
				"service-process");
		serviceRequestProcess.setRsaRequest(serviceRequest);
        serviceRequestProcess.setServiceState(serviceState);
		getModelService().save(serviceRequestProcess);
		getBusinessProcessService().startProcess(serviceRequestProcess);
	}

}
