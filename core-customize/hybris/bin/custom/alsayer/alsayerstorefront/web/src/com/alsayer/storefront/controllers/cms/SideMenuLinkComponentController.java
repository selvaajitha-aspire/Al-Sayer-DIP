package com.alsayer.storefront.controllers.cms;

import com.alsayer.core.model.SideMenuLinkComponentModel;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;

public class SideMenuLinkComponentController extends AbstractAcceleratorCMSComponentController<SideMenuLinkComponentModel>  {

    @Override
    protected void fillModel(HttpServletRequest request, Model model, SideMenuLinkComponentModel component) {
        model.addAttribute("link",component.getUrl());
        model.addAttribute("media",component.getMedia());
        model.addAttribute("name",component.getName());
    }
}
