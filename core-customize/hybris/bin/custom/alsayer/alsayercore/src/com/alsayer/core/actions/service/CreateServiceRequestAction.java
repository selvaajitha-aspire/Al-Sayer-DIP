package com.alsayer.core.actions.service;

import com.alsayer.core.enums.ServiceStatus;
import com.alsayer.core.model.RsaRequestModel;
import com.alsayer.core.model.RsaRequestProcessModel;
import de.hybris.platform.processengine.action.AbstractSimpleDecisionAction;
import de.hybris.platform.task.RetryLaterException;
import org.apache.log4j.Logger;

public class CreateServiceRequestAction extends AbstractSimpleDecisionAction<RsaRequestProcessModel> {

    private static final Logger LOG = Logger.getLogger(CreateServiceRequestAction.class);


    @Override
    public Transition executeAction(RsaRequestProcessModel process) {
        final RsaRequestModel serviceRequest = process.getRsaRequest();
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
