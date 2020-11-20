package com.alsayer.storefront.controllers.cms;

import com.alsayer.storefront.controllers.ControllerConstants;
import de.hybris.platform.acceleratorcms.model.components.SimpleBannerComponentModel;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import com.alsayer.core.model.HomepageBannerComponentModel;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("HomepageBannerComponentController")
@Scope("tenant")
@RequestMapping(value = ControllerConstants.Actions.Cms.HomepageBannerComponent)
public class HomepageBannerComponentController extends AbstractAcceleratorCMSComponentController<HomepageBannerComponentModel> {

    @Override
    protected void fillModel(HttpServletRequest request, Model model, HomepageBannerComponentModel component) {
        model.addAttribute("description",component.getDescription());
        model.addAttribute("link",component.getUrlLink());
        model.addAttribute("media",component.getMedia());
        model.addAttribute("name",component.getName());
    }
}
