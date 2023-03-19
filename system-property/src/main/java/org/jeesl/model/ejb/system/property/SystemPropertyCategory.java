package org.jeesl.model.ejb.system.property;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.system.property.JeeslPropertyCategory;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoStatus;

@Entity
@DiscriminatorValue("systemPropertyCategory")
@EjbErNode(name="Category",level=3,subset="systemProperty")
public class SystemPropertyCategory extends IoStatus implements JeeslPropertyCategory<IoLang,IoDescription,SystemPropertyCategory,IoGraphic>
{
	public static final long serialVersionUID=1;
	
	@Override public boolean equals(Object object){return (object instanceof SystemPropertyCategory) ? id == ((SystemPropertyCategory) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,43).append(id).toHashCode();}
}