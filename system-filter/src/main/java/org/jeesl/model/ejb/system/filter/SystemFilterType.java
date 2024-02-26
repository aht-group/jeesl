package org.jeesl.model.ejb.system.filter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.system.filter.JeeslFilterType;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoStatus;

@Entity
@DiscriminatorValue("systemFilterType")
@EjbErNode(name="Type",category="user",subset="systemUser",level=2)
public class SystemFilterType extends IoStatus implements JeeslFilterType<IoLang,IoDescription,SystemFilterType,IoGraphic>
{
	public static final long serialVersionUID=1;
	
	public static enum Code{project,indicator};

	@Override public boolean equals(Object object) {return (object instanceof SystemFilterType) ? id == ((SystemFilterType) object).getId() : (object == this);}
	@Override public int hashCode(){return new HashCodeBuilder(27,43).append(id).toHashCode();}
}