package org.jeesl.model.ejb.io.label.ui;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.io.label.entity.JeeslLabelNumberPrecision;
import org.jeesl.interfaces.model.io.label.revision.core.JeeslRevisionScopeType;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoStatus;

@Entity
@DiscriminatorValue("ioLabelNumberPrecision")
@EjbErNode(name="Type",category="revision",subset="revision",level=4)
public class IoLabelNumberPrecision extends IoStatus implements JeeslLabelNumberPrecision<IoLang,IoDescription,IoLabelNumberPrecision,IoGraphic>
{
	public static final long serialVersionUID=1;
	

	@Override public List<String> getFixedCodes()
	{
		List<String> fixed = new ArrayList<String>();
		for(JeeslRevisionScopeType.Scope c : JeeslRevisionScopeType.Scope.values()){fixed.add(c.toString());}
		return fixed;
	}
	
	@Override public boolean equals(Object object) {return (object instanceof IoLabelNumberPrecision) ? id == ((IoLabelNumberPrecision) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,51).append(id).toHashCode();}
}