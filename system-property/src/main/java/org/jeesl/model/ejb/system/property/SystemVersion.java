package org.jeesl.model.ejb.system.property;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.system.property.JeeslSystemVersion;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoStatus;

@Entity
@DiscriminatorValue("systemVersion")
@EjbErNode(name="Version",level=3,subset="systemProperty")
public class SystemVersion extends IoStatus implements JeeslSystemVersion<IoLang,IoDescription,SystemVersion,IoGraphic>
{
	public static final long serialVersionUID=1;
	
	@Override public boolean equals(Object object){return (object instanceof SystemVersion) ? id == ((SystemVersion) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,43).append(id).toHashCode();}
}