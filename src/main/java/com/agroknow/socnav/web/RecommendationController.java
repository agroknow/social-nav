package com.agroknow.socnav.web;

import com.agroknow.socnav.domain.Recommendation;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/recommendations")
@Controller
@RooWebScaffold(path = "recommendations", formBackingObject = Recommendation.class)
@RooWebFinder
public class RecommendationController {
}
