/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.alsayer.webservices.facades;

import de.hybris.platform.core.servicelayer.data.SearchPageData;

import java.util.List;

import com.alsayer.webservices.data.UserData;
import com.alsayer.webservices.dto.SampleWsDTO;
import com.alsayer.webservices.dto.TestMapWsDTO;


public interface SampleFacades
{
	SampleWsDTO getSampleWsDTO(final String value);

	UserData getUser(String id);

	List<UserData> getUsers();

	SearchPageData<UserData> getUsers(SearchPageData<?> params);

	TestMapWsDTO getMap();
}
