package org.jeesl.model.ejb.io.db.pg.connection;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.io.db.pg.connection.JeeslDbConnectionColumn;
import org.jeesl.interfaces.model.io.db.pg.connection.JeeslDbPoolStatistic;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoStatus;

@Entity
@DiscriminatorValue("ioDbPoolStatistic")
@EjbErNode(name="Pool Statistic",category="io",subset="db",level=3)
public class IoDbPoolStatistic extends IoStatus implements JeeslDbPoolStatistic<IoLang,IoDescription,IoDbPoolStatistic,IoGraphic>
{
	public static final long serialVersionUID=1;
	
	@Override public List<String> getFixedCodes()
	{
		List<String> fixed = new ArrayList<>();
		for(JeeslDbConnectionColumn.Code c : JeeslDbConnectionColumn.Code.values()){fixed.add(c.toString());}
		return fixed;
	}
	
	@Override public boolean equals(Object object) {return (object instanceof IoDbPoolStatistic) ? id == ((IoDbPoolStatistic) object).getId() : (object == this);}
	@Override public int hashCode(){return new HashCodeBuilder(17,37).append(id).toHashCode();}
}