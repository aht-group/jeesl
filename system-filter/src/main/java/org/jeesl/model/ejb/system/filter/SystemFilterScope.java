package org.jeesl.model.ejb.system.filter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.system.filter.JeeslFilterScope;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoStatus;

@Entity
@DiscriminatorValue("systemFilterScope")
@EjbErNode(name="Type",category="user",subset="systemUser",level=2)
public class SystemFilterScope extends IoStatus implements JeeslFilterScope<IoLang,IoDescription,SystemFilterScope,IoGraphic>
{
	public static final long serialVersionUID=1;


	@Override public boolean equals(Object object) {return (object instanceof SystemFilterScope) ? id == ((SystemFilterScope) object).getId() : (object == this);}
	@Override public int hashCode(){return new HashCodeBuilder(27,43).append(id).toHashCode();}
}