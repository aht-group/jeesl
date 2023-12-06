package org.jeesl.model.ejb.io.db.pg.connection;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.io.db.pg.connection.JeeslDbConnectionWait;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoStatus;

@Entity
@DiscriminatorValue("ioDbConnectionWait")
@EjbErNode(name="Connection Wait",category="db",level=3)
public class IoDbConnectionWait extends IoStatus implements JeeslDbConnectionWait<IoLang,IoDescription,IoDbConnectionWait,IoGraphic>
{
	public static final long serialVersionUID=1;

	@Override public List<String> getFixedCodes()
	{
		List<String> fixed = new ArrayList<String>();
		for(JeeslDbConnectionWait.Code code : JeeslDbConnectionWait.Code.values()){fixed.add(code.toString());}
		return fixed;
	}

	@Override public boolean equals(Object object) {return (object instanceof IoDbConnectionWait) ? id == ((IoDbConnectionWait) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(21,45).append(id).toHashCode();}
}