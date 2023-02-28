package org.jeesl.model.ejb.system.constraint.core;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.system.constraint.core.JeeslConstraintType;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoStatus;

@Entity
@DiscriminatorValue("systemConstraintType")
@EjbErNode(name="Type",category="system",subset="mail",level=3)
public class SystemConstraintType extends IoStatus implements JeeslConstraintType<IoLang,IoDescription,SystemConstraintType,IoGraphic>
{
	public static final long serialVersionUID=1;

	@Override public List<String> getFixedCodes()
	{
		List<String> fixed = new ArrayList<String>();
		for(JeeslConstraintType.Code c : JeeslConstraintType.Code.values()){fixed.add(c.toString());}
		return fixed;
	}

	@Override public boolean equals(Object object) {return (object instanceof SystemConstraintType) ? id == ((SystemConstraintType) object).getId() : (object == this);}
	@Override public int hashCode(){return new HashCodeBuilder(17,37).append(id).toHashCode();}
}