package org.jeesl.model.ejb.module.hd.resolution;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.module.hd.resolution.JeeslHdScope;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoStatus;

@Entity
@DiscriminatorValue("helpdeskScope")
@EjbErNode(name="Scope",category="hd",subset="moduleHd")
public class HdScope extends IoStatus implements JeeslHdScope<IoLang,IoDescription,HdScope,IoGraphic>
{
	public static final long serialVersionUID=1;
	
	@Override public List<String> getFixedCodes()
	{
		List<String> fixed = new ArrayList<String>();
		for(JeeslHdScope.Code c : JeeslHdScope.Code.values()){fixed.add(c.toString());}
		return fixed;
	}
	
	@Override public boolean equals(Object object) {return (object instanceof HdScope) ? id == ((HdScope) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(19,21).append(id).toHashCode();}
}