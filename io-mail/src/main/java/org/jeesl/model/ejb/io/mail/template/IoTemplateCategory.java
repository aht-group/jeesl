package org.jeesl.model.ejb.io.mail.template;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoStatus;

@Entity
@DiscriminatorValue("ioTemplateCategory")
@EjbErNode(name="Category",category="system",subset="template",level=3)
public class IoTemplateCategory extends IoStatus
								implements Serializable,EjbPersistable,EjbWithCode,
											JeeslStatus<IoLang,IoDescription,IoTemplateCategory>
{
	public static final long serialVersionUID=1;

	@Override public boolean equals(Object object) {return (object instanceof IoTemplateCategory) ? id == ((IoTemplateCategory) object).getId() : (object == this);}
	@Override public int hashCode(){return new HashCodeBuilder(17,37).append(id).toHashCode();}
}