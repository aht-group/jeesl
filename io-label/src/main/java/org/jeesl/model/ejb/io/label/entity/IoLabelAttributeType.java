package org.jeesl.model.ejb.io.label.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionAttributeType;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoStatus;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;
import org.jeesl.model.ejb.io.locale.IoDescription;

@Entity
@DiscriminatorValue("ioLabelAttributeType")
@EjbErNode(name="Attribute Type",category="revision",subset="revision",level=4)
public class IoLabelAttributeType extends IoStatus implements JeeslRevisionAttributeType<IoLang,IoDescription,IoLabelAttributeType,IoGraphic>
{
	public static final long serialVersionUID=1;

	@Override public List<String> getFixedCodes()
	{
		List<String> fixed = new ArrayList<String>();
		for(JeeslRevisionAttributeType.Code c : JeeslRevisionAttributeType.Code.values()){fixed.add(c.toString());}
		return fixed;
	}

	@Override public boolean equals(Object object) {return (object instanceof IoLabelAttributeType) ? id == ((IoLabelAttributeType) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,51).append(id).toHashCode();}
}