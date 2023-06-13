package org.jeesl.model.ejb.io.attribute;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeType;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoStatus;

@Entity
@DiscriminatorValue("ioAttributeType")
@EjbErNode(name="Type",category="lcf",level=3,subset="moduleTs")
public class IoAttributeType extends IoStatus implements JeeslAttributeType<IoLang,IoDescription,IoAttributeType,IoGraphic>
{
	public static final long serialVersionUID=1;
	
	@Override public List<String> getFixedCodes()
	{
		List<String> fixed = new ArrayList<String>();
		for(JeeslAttributeType.Code c : JeeslAttributeType.Code.values()){fixed.add(c.toString());}
		return fixed;
	}
	
	@Override public boolean equals(Object object){return (object instanceof IoAttributeType) ? id == ((IoAttributeType) object).getId() : (object == this);}
	@Override public int hashCode(){return new HashCodeBuilder(23,43).append(id).toHashCode();}
}