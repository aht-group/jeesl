package org.jeesl.model.ejb.system.constraint.core;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.system.constraint.core.JeeslConstraintCategory;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoStatus;

@Entity
@DiscriminatorValue("systemConstraintCategory")
@EjbErNode(name="Category",category="lcf",level=3,subset="lcf")
public class SystemConstraintCategory extends IoStatus implements JeeslConstraintCategory<IoLang,IoDescription,SystemConstraintCategory,IoGraphic>
{
	public static final long serialVersionUID=1;

	@Override public boolean equals(Object object){return (object instanceof SystemConstraintCategory) ? id == ((SystemConstraintCategory) object).getId() : (object == this);}
	@Override public int hashCode(){return new HashCodeBuilder(23,43).append(id).toHashCode();}
}