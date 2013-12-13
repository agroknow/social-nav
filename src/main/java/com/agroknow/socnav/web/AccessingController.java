package com.agroknow.socnav.web;

import com.agroknow.socnav.domain.Accessing;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/accessings")
@Controller
@RooWebScaffold(path = "accessings", formBackingObject = Accessing.class)
@RooWebFinder
public class AccessingController {
}
