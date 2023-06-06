package org.jeesl.model.ejb.io.attribute;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeCategory;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.system.tenant.TenantRealm;
import org.jeesl.model.ejb.system.tenant.TenantStatus;

@Entity
@DiscriminatorValue("ioAttributeCategory")
@EjbErNode(name="Category",category="ioAttribute",subset="ioAttribute")
public class IoAttributeCategory extends TenantStatus implements JeeslAttributeCategory<IoLang,IoDescription,TenantRealm,IoAttributeCategory,IoGraphic>
{
	public static final long serialVersionUID=1;
		
	@Override public boolean equals(Object object) {return (object instanceof IoAttributeCategory) ? id == ((IoAttributeCategory) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(19,21).append(id).toHashCode();}
}