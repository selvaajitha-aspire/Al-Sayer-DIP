package com.alsayer.core.actions.service;

import com.alsayer.core.enums.ServiceStatus;
import com.alsayer.core.model.ServiceRequestModel;
import com.alsayer.core.model.ServiceRequestProcessModel;
import de.hybris.platform.processengine.action.AbstractSimpleDecisionAction;
import de.hybris.platform.task.RetryLaterException;
import org.apache.log4j.Logger;

public class CreateServiceRequestAction extends AbstractSimpleDecisionAction<ServiceRequestProcessModel> {

    private static final Logger LOG = Logger.getLogger(CreateServiceRequestAction.class);


    @Override
    public Transition executeAction(ServiceRequestProcessModel process) {
        final ServiceRequestModel serviceRequest = process.getServiceRequest();
        if (serviceRequest != null)
        {
            try
            {
                // Check if the Service Request is Cancelled
                if (ServiceStatus.CANCELLED.equals(serviceRequest.getStatus())|| ServiceStatus.FAILED.equals(serviceRequest.getStatus()))
                {
                    return Transition.NOK;
                }
                else
                {

                    return Transition.OK;
                }
            }
            catch (final Exception e)
            {
                if (LOG.isDebugEnabled())
                {
                    LOG.debug(e);
                }
                return Transition.NOK;
            }
        }
        return Transition.NOK;

    }

}
