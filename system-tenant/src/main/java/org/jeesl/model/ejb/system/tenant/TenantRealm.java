package org.jeesl.model.ejb.system.tenant;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoStatus;

@Entity
@DiscriminatorValue("tenantRealm")
@EjbErNode(name="Realm",category="systemTenant",subset="systemTenant",level=4)
public class TenantRealm extends IoStatus implements JeeslTenantRealm<IoLang,IoDescription,TenantRealm,IoGraphic>
{
	public static final long serialVersionUID=1;


	@Override public boolean equals(Object object) {return (object instanceof TenantRealm) ? id == ((TenantRealm) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,51).append(id).toHashCode();}
}