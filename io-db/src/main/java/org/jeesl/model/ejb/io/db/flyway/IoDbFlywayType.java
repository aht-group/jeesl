package org.jeesl.model.ejb.io.db.flyway;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.io.db.JeeslDbConnectionState;
import org.jeesl.interfaces.model.io.db.flyway.JeeslIoDbFlywayType;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoStatus;

@Entity
@DiscriminatorValue("ioDbFlywayType")
@EjbErNode(name="Flyway Type",category="db",level=3)
public class IoDbFlywayType extends IoStatus implements JeeslIoDbFlywayType<IoLang,IoDescription,IoDbFlywayType,IoGraphic>
{
	public static final long serialVersionUID=1;
		
	@Override public List<String> getFixedCodes()
	{
		List<String> fixed = new ArrayList<String>();
		for(JeeslDbConnectionState.Code code : JeeslDbConnectionState.Code.values()){fixed.add(code.toString());}
		return fixed;
	}

	@Override public boolean equals(Object object) {return (object instanceof IoDbFlywayType) ? id == ((IoDbFlywayType) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(21,45).append(id).toHashCode();}
}