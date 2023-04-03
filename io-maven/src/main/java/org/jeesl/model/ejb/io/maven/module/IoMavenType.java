package org.jeesl.model.ejb.io.maven.module;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.io.maven.module.JeeslMavenType;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoStatus;

@Entity
@DiscriminatorValue("ioMavenType")
@EjbErNode(name="Usage",category="ioMaven",subset="ioMaven",level=3)
public class IoMavenType extends IoStatus implements JeeslMavenType<IoLang,IoDescription,IoMavenType,IoGraphic>
{
	public static final long serialVersionUID=1;

	@Override public List<String> getFixedCodes()
	{
		List<String> fixed = new ArrayList<>();
		for(JeeslMavenType.Code c : JeeslMavenType.Code.values()){fixed.add(c.toString());}
		return fixed;
	}
	
	@Override public boolean equals(Object object) {return (object instanceof IoMavenType) ? id == ((IoMavenType) object).getId() : (object == this);}
	@Override public int hashCode(){return new HashCodeBuilder(17,37).append(id).toHashCode();}
}