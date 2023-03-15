package org.jeesl.model.ejb.io.label.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionCategory;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoStatus;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;
import org.jeesl.model.ejb.io.locale.IoDescription;

@Entity
@DiscriminatorValue("ioLabelCategory")
@EjbErNode(name="Category",category="revision",subset="revision",level=4)
public class IoLabelCategory extends IoStatus implements JeeslRevisionCategory<IoLang,IoDescription,IoLabelCategory,IoGraphic>
{
	public static final long serialVersionUID=1;

	@Override public boolean equals(Object object) {return (object instanceof IoLabelCategory) ? id == ((IoLabelCategory) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,51).append(id).toHashCode();}
}