package org.jeesl.model.ejb.module.hd.resolution;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.module.hd.resolution.JeeslHdLevel;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.system.tenant.TenantRealm;
import org.jeesl.model.ejb.system.tenant.TenantStatus;

@Entity
@DiscriminatorValue("helpdeskLevel")
@EjbErNode(name="Level",category="hd",subset="moduleHd")
public class HdLevel extends TenantStatus implements JeeslHdLevel<IoLang,IoDescription,TenantRealm,HdLevel,IoGraphic>
{
	public static final long serialVersionUID=1;
	
	
	
	@Override public boolean equals(Object object) {return (object instanceof HdLevel) ? id == ((HdLevel) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(19,21).append(id).toHashCode();}
}