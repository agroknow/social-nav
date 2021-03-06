// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.agroknow.socnav.domain;

import com.agroknow.socnav.domain.Rating;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

privileged aspect Rating_Roo_Equals {
    
    public boolean Rating.equals(Object obj) {
        if (!(obj instanceof Rating)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        Rating rhs = (Rating) obj;
        return new EqualsBuilder().append(domain, rhs.domain).append(id, rhs.id).append(ip_address, rhs.ip_address).append(item, rhs.item).append(preference_avg, rhs.preference_avg).append(preference_dimensions, rhs.preference_dimensions).append(session_id, rhs.session_id).append(sharing_level, rhs.sharing_level).append(updated_at, rhs.updated_at).append(user, rhs.user).isEquals();
    }
    
    public int Rating.hashCode() {
        return new HashCodeBuilder().append(domain).append(id).append(ip_address).append(item).append(preference_avg).append(preference_dimensions).append(session_id).append(sharing_level).append(updated_at).append(user).toHashCode();
    }
    
}
