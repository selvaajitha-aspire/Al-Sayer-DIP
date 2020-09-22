/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.alsayer.storefront.controllers.cms;

import com.alsayer.storefront.controllers.ControllerConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.cms.AbstractCMSComponentController;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.alsayer.core.model.ServiceTileCMSComponentModel;
import javax.servlet.http.HttpServletRequest;

@Controller("ServiceTileCMSComponentController")
@Scope("tenant")
@RequestMapping(value = ControllerConstants.Actions.Cms.ServiceTileComponent)
public class ServiceTileCMSComponentController extends AbstractCMSComponentController<ServiceTileCMSComponentModel>
{
	@Override
	protected void fillModel(final HttpServletRequest request, final Model model, final ServiceTileCMSComponentModel component)
	{
	    model.addAttribute("title",component.getTitle());
		model.addAttribute("color",component.getColor());
		model.addAttribute("icon",component.getIcon());
	}

	protected String getView(ServiceTileCMSComponentModel component){
		return ControllerConstants.Views.Cms.ComponentPrefix + StringUtils.lowerCase(getTypeCode(component));
	}
}
