package com.agroknow.socnav.web;

import com.agroknow.socnav.domain.Preference;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/preferences")
@Controller
@RooWebScaffold(path = "preferences", formBackingObject = Preference.class)
@RooWebJson(jsonObject = Preference.class)
@RooWebFinder
public class PreferenceController {
}
