/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.alsayer.storefront.controllers.cms;

import com.alsayer.core.model.ServiceTileBannerComponentModel;
import com.alsayer.storefront.controllers.ControllerConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.cms.AbstractCMSComponentController;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;

@Controller("ServiceTileBannerComponentController")
@Scope("tenant")
@RequestMapping(value = ControllerConstants.Actions.Cms.ServiceTileBannerComponent)
public class ServiceTileBannerComponentController extends AbstractCMSComponentController<ServiceTileBannerComponentModel>
{
	@Override
	protected void fillModel(final HttpServletRequest request, final Model model, final ServiceTileBannerComponentModel component)
	{
		model.addAttribute("color",component.getColor());
	}

	protected String getView(ServiceTileBannerComponentModel component){
		return ControllerConstants.Views.Cms.ComponentPrefix + StringUtils.lowerCase(getTypeCode(component));
	}
}
