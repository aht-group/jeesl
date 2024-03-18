package org.jeesl.model.ejb.io.maven.ee;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.io.maven.ee.JeeslMavenEeStandard;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoStatus;

@Entity
@DiscriminatorValue("ioMavenEeStandard")
@EjbErNode(name="Usage",category="ioMaven",subset="ioMaven",level=3)
public class IoMavenEeStandard extends IoStatus implements JeeslMavenEeStandard<IoLang,IoDescription,IoMavenEeStandard,IoGraphic>
{
	public static final long serialVersionUID=1;
	
	@Override public boolean equals(Object object) {return (object instanceof IoMavenEeStandard) ? id == ((IoMavenEeStandard) object).getId() : (object == this);}
	@Override public int hashCode(){return new HashCodeBuilder(17,37).append(id).toHashCode();}
}