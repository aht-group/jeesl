package org.jeesl.model.ejb.module.hd.event;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.module.hd.event.JeeslHdEventType;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoStatus;

@Entity
@DiscriminatorValue("helpdeskEventType")
@EjbErNode(name="Status",category="hd",subset="moduleHd")
public class HdEventType extends IoStatus implements JeeslHdEventType<IoLang,IoDescription,HdEventType,IoGraphic>
{
	public static final long serialVersionUID=1;
	
	@Override public List<String> getFixedCodes()
	{
		List<String> fixed = new ArrayList<String>();
		for(JeeslHdEventType.Code c : JeeslHdEventType.Code.values()){fixed.add(c.toString());}
		return fixed;
	}
	
	@Override public boolean equals(Object object) {return (object instanceof HdEventType) ? id == ((HdEventType) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(19,21).append(id).toHashCode();}
}