package org.jeesl.model.ejb.io.graphic.core;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.system.graphic.component.JeeslGraphicShape;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoStatus;

@Entity
@DiscriminatorValue("graphicSY")
@EjbErNode(name="Style",category="symbol",subset="sld",level=3)
public class IoGraphicShape extends IoStatus implements JeeslGraphicShape<IoLang,IoDescription,IoGraphicShape,IoGraphic>
{
	public static final long serialVersionUID=1;

	@Override public List<String> getFixedCodes()
	{
		List<String> fixed = new ArrayList<String>();
		for(JeeslGraphicShape.Code c : JeeslGraphicShape.Code.values()){fixed.add(c.toString());}
		return fixed;
	}
	
	@Override public boolean equals(Object object) {return (object instanceof IoGraphicShape) ? id == ((IoGraphicShape) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,3).append(id).toHashCode();}
}