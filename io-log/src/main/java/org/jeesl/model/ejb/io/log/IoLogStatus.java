package org.jeesl.model.ejb.io.log;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.io.logging.JeeslIoLogStatus;
import org.jeesl.interfaces.model.io.mail.core.JeeslMailStatus;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoStatus;

@Entity
@DiscriminatorValue("ioLogStatus")
@EjbErNode(name="Status",category="systemIo",subset="ioLog",level=3)

public class IoLogStatus extends IoStatus implements JeeslIoLogStatus<IoLang,IoDescription,IoLogStatus,IoGraphic>
{
	public static final long serialVersionUID=1;

	@Override public List<String> getFixedCodes()
	{
		List<String> fixed = new ArrayList<>();
		for(JeeslMailStatus.Code c : JeeslMailStatus.Code.values()){fixed.add(c.toString());}
		return fixed;
	}

	@Override public boolean equals(Object object) {return (object instanceof IoLogStatus) ? id == ((IoLogStatus) object).getId() : (object == this);}
	@Override public int hashCode(){return new HashCodeBuilder(17,37).append(id).toHashCode();}
}