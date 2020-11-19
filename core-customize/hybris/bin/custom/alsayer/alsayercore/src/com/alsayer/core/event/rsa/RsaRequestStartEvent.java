/*
 * Author: Archana Prasad
 * Service Request Event
 */
package com.alsayer.core.event.rsa;

import com.alsayer.core.enums.ServiceStatus;
import com.alsayer.core.model.RsaRequestModel;
import com.alsayer.core.model.RsaRequestProcessModel;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;


public class RsaRequestStartEvent extends AbstractEvent
{

	private final RsaRequestProcessModel process;
	private final ServiceStatus serviceStatus;
    private  RsaRequestModel serviceRequest;

	public RsaRequestStartEvent(RsaRequestProcessModel process) {
		this.process = process;
		if (process != null) {
            this.serviceRequest= getProcess().getRsaRequest();
			this.serviceStatus = getProcess().getServiceState();
		} else {
			this.serviceStatus = null;
		}

	}

	public RsaRequestProcessModel getProcess() {
		return this.process;
	}

	public ServiceStatus getServiceStatus() {
		return this.serviceStatus;
	}
}
