package org.jeesl.model.ejb.io.db.pg.replication;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.io.db.JeeslDbReplicationState;
import org.jeesl.interfaces.model.io.db.JeeslDbReplicationSync;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoStatus;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;
import org.jeesl.model.ejb.io.locale.IoDescription;

@Entity
@DiscriminatorValue("ioDbReplicationSync")
@EjbErNode(name="Sync",category="system",subset="db",level=3)
public class IoDbReplicationSync extends IoStatus implements JeeslDbReplicationSync<IoLang,IoDescription,IoDbReplicationSync,IoGraphic>
{
	public static final long serialVersionUID=1;

	@Override public List<String> getFixedCodes()
	{
		List<String> fixed = new ArrayList<>();
		for(JeeslDbReplicationState.Code c : JeeslDbReplicationState.Code.values()){fixed.add(c.toString());}
		return fixed;
	}

	@Override public boolean equals(Object object) {return (object instanceof IoDbReplicationSync) ? id == ((IoDbReplicationSync) object).getId() : (object == this);}
	@Override public int hashCode(){return new HashCodeBuilder(17,37).append(id).toHashCode();}
}