/*
 * Author: Archana Prasad
 * Service Request Event Listener
 */
package com.alsayer.core.event.rsa;

import com.alsayer.core.enums.ServiceStatus;
import com.alsayer.core.model.ServiceRequestModel;
import com.alsayer.core.model.ServiceRequestProcessModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
import de.hybris.platform.servicelayer.model.ModelService;


/**
 * Listener for ServiceRequestStartEvent events.
 */
public class ServiceRequestStartEventListener extends AbstractEventListener<ServiceRequestStartEvent>
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
	protected void onEvent(final ServiceRequestStartEvent serviceRequestStartEvent)
	{
		final ServiceStatus serviceState = serviceRequestStartEvent.getProcess().getServiceState();
		final ServiceRequestModel serviceRequest=serviceRequestStartEvent.getProcess().getServiceRequest();
		final ServiceRequestProcessModel serviceRequestProcess = getBusinessProcessService().createProcess(
				"service-process-" + serviceRequest.getUid() + "-" + System.currentTimeMillis(),
				"service-process");
		serviceRequestProcess.setServiceRequest(serviceRequest);
        serviceRequestProcess.setServiceState(serviceState);
		getModelService().save(serviceRequestProcess);
		getBusinessProcessService().startProcess(serviceRequestProcess);
	}

}
