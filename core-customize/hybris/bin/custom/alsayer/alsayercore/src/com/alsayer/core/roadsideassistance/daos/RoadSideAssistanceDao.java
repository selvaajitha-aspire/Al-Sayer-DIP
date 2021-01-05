package com.alsayer.core.roadsideassistance.daos;

import com.alsayer.core.model.RsaRequestModel;

public interface RoadSideAssistanceDao {
    public boolean saveServiceRequestinDB(RsaRequestModel serviceRequest);

}

