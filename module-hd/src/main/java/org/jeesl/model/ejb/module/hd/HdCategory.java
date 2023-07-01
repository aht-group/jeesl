package org.jeesl.model.ejb.module.hd;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.module.hd.JeeslHdCategory;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.system.tenant.TenantRealm;
import org.jeesl.model.ejb.system.tenant.TenantStatus;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;
import org.jeesl.model.ejb.io.locale.IoDescription;

@Entity
@DiscriminatorValue("helpdeskCategory")
@EjbErNode(name="Category",category="system",subset="moduleHd")
public class HdCategory extends TenantStatus implements JeeslHdCategory<IoLang,IoDescription,TenantRealm,HdCategory,IoGraphic>
{
	public static final long serialVersionUID=1;

	@Override public boolean equals(Object object) {return (object instanceof HdCategory) ? id == ((HdCategory) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(19,21).append(id).toHashCode();}
}