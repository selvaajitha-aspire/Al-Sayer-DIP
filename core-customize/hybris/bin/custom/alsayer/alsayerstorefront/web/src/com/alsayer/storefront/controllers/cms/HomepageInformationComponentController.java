package com.alsayer.storefront.controllers.cms;

import com.alsayer.storefront.controllers.ControllerConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.cms.AbstractCMSComponentController;
import com.alsayer.core.model.HomepageInformationComponentModel;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;

@Controller("HomepageInformationComponentController")
@Scope("tenant")
@RequestMapping(value = ControllerConstants.Actions.Cms.HomepageInformationComponent)

public class HomepageInformationComponentController extends AbstractCMSComponentController<HomepageInformationComponentModel> {

    @Override
    protected void fillModel(HttpServletRequest request, Model model, HomepageInformationComponentModel component) {
        model.addAttribute("infoHeader",component.getHeader());
        model.addAttribute("infoType",component.getInfoType());
        model.addAttribute("link",component.getLink());
        model.addAttribute("media",component.getMedia());
        model.addAttribute("content",component.getContent());
    }

    @Override
    protected String getView(HomepageInformationComponentModel component) {
        return ControllerConstants.Views.Cms.ComponentPrefix + StringUtils.lowerCase(getTypeCode(component));
    }
}
