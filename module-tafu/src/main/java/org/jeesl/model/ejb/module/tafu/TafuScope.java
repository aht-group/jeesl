package org.jeesl.model.ejb.module.tafu;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.module.tafu.JeeslTafuScope;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.system.tenant.TenantStatus;
import org.jeesl.model.ejb.system.tenant.TenantRealm;

@Entity
@DiscriminatorValue("tafuScope")
@EjbErNode(name="Scope",category="tafu",subset="moduleTafu")
public class TafuScope extends TenantStatus implements JeeslTafuScope<IoLang,IoDescription,TenantRealm,TafuScope,IoGraphic>
{
	public static final long serialVersionUID=1;
		
	@Override public boolean equals(Object object) {return (object instanceof TafuScope) ? id == ((TafuScope) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(19,21).append(id).toHashCode();}
}