package org.jeesl.model.ejb.system.job.cache;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.system.job.cache.JeeslJobExpiration;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoStatus;

@Entity
@DiscriminatorValue("systemJobExpiration")
@EjbErNode(name="Expiration")
public class SystemJobExpiration extends IoStatus implements JeeslJobExpiration<IoLang,IoDescription,SystemJobExpiration,IoGraphic>
{
	public static final long serialVersionUID=1;


	@Override public boolean equals(Object object){return (object instanceof SystemJobExpiration) ? id == ((SystemJobExpiration) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(21,43).append(id).toHashCode();}
}