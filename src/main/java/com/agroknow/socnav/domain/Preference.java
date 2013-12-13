package com.agroknow.socnav.domain;

import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.equals.RooEquals;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooEquals
@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(table = "preferences", finders = { "findPreferencesByRating" })
public class Preference {

    @NotNull
    @Min(1L)
    private int dimension;

    @NotNull
    private float value;

    @ManyToOne
    private Rating rating;
}
