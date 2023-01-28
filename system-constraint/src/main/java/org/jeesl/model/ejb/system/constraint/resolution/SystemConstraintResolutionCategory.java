package org.jeesl.model.ejb.system.constraint.resolution;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPositionVisible;
import org.jeesl.interfaces.model.with.system.graphic.EjbWithGraphic;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoStatus;

@Entity
@DiscriminatorValue("systemConstraintResolutionCat")
@EjbErNode(name="Category",category="lcf",level=3,subset="lcf")
public class SystemConstraintResolutionCategory extends IoStatus
								implements Serializable,EjbPersistable,EjbWithCode,EjbWithPositionVisible,
											EjbWithGraphic<IoGraphic>,
											JeeslStatus<IoLang,IoDescription,SystemConstraintResolutionCategory>
{
	public static final long serialVersionUID=1;
	
	@Override public boolean equals(Object object){return (object instanceof SystemConstraintResolutionCategory) ? id == ((SystemConstraintResolutionCategory) object).getId() : (object == this);}
	@Override public int hashCode(){return new HashCodeBuilder(23,43).append(id).toHashCode();}
}