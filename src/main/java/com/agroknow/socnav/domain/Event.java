package com.agroknow.socnav.domain;

import com.agroknow.socnav.reference.SharingLevelType;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(mappedSuperclass = true, inheritanceType = "TABLE_PER_CLASS")
public abstract class Event {
	
	@Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator="generatorName")  
    @TableGenerator(name="generatorName", allocationSize=1)// this should 100 or 1000 in production level
    @Column(name = "id")
    private Long id;
	
	@NotNull
    @ManyToOne(cascade = { CascadeType.PERSIST }, optional = false)
	private User user;

    @NotNull
    @ManyToOne(cascade = { CascadeType.PERSIST }, optional = false)
    private Item item;
	
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date updated_at;

    @Enumerated(EnumType.STRING)
    private SharingLevelType sharing_level;

    private String session_id;

    private String domain;

    private String ip_address;
    
    protected void beforeSaveEvent() {
        if (this.getSharing_level() == null) {
            this.setSharing_level(SharingLevelType.Public);
        }
        if (this.getUpdated_at() == null) {
        	Date date = new java.util.Date();
            this.setUpdated_at(new Timestamp(date.getTime()));
        }
        this.setUser(User.getUniqueUser(this.user));
        this.setItem(Item.getUniqueItem(this.item));
    }
}
