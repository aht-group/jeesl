package org.jeesl.model.ejb.system.job.maintenance;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.system.job.maintenance.JeeslJobMaintenance;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoStatus;

@Entity
@DiscriminatorValue("systemJobMaintenance")
@EjbErNode(name="Maintenance")
public class SystemJobMaintenance extends IoStatus implements JeeslJobMaintenance<IoLang,IoDescription,SystemJobMaintenance,IoGraphic>
{
	public static final long serialVersionUID=1;
		
	@Override public boolean equals(Object object){return (object instanceof SystemJobMaintenance) ? id == ((SystemJobMaintenance) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(21,43).append(id).toHashCode();}
}