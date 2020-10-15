/*
 * Author: Archana Prasad
 * Service Request Event
 */
package com.alsayer.core.event.rsa;

import com.alsayer.core.enums.ServiceStatus;
import com.alsayer.core.model.ServiceRequestModel;
import com.alsayer.core.model.ServiceRequestProcessModel;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;


public class ServiceRequestStartEvent extends AbstractEvent
{
	private static final long serialVersionUID = -8586595518929550780L;

	private final ServiceRequestProcessModel process;
	private final ServiceStatus serviceStatus;
    private  ServiceRequestModel serviceRequest;

	public ServiceRequestStartEvent(ServiceRequestProcessModel process) {
		this.process = process;
		if (process != null) {
            this.serviceRequest= getProcess().getServiceRequest();
			this.serviceStatus = getProcess().getServiceState();
		} else {
			this.serviceStatus = null;
		}

	}

	public ServiceRequestProcessModel getProcess() {
		return this.process;
	}

	public ServiceStatus getServiceStatus() {
		return this.serviceStatus;
	}
}
