package org.jeesl.model.ejb.module.hd.resolution;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.module.hd.resolution.JeeslHdPriority;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.system.tenant.TenantStatus;
import org.jeesl.model.ejb.system.tenant.TenantRealm;

@Entity
@DiscriminatorValue("helpdeskPriority")
@EjbErNode(name="Priority",category="hd",subset="moduleHd")
public class HdPriority extends TenantStatus implements JeeslHdPriority<IoLang,IoDescription,TenantRealm,HdPriority,IoGraphic>
{
	public static final long serialVersionUID=1;
	
	@Override public boolean equals(Object object) {return (object instanceof HdPriority) ? id == ((HdPriority) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(19,21).append(id).toHashCode();}
}