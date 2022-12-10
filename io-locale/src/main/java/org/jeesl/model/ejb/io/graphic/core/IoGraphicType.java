package org.jeesl.model.ejb.io.graphic.core;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphicType;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoStatus;

@Entity
@DiscriminatorValue("ioGraphicType")
@EjbErNode(name="Type",category="io",subset="ioGraphic",level=3)
public class IoGraphicType extends IoStatus implements JeeslGraphicType<IoLang,IoDescription,IoGraphicType,IoGraphic>
{
	public static final long serialVersionUID=1;


	@Override public List<String> getFixedCodes()
	{
		List<String> fixed = new ArrayList<String>();
		for(JeeslGraphicType.Code c : JeeslGraphicType.Code.values()){fixed.add(c.toString());}
		return fixed;
	}
	
	@Override public boolean equals(Object object) {return (object instanceof IoGraphicType) ? id == ((IoGraphicType) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(37,19).append(id).toHashCode();}
}