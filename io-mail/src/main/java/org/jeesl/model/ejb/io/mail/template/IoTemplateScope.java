package org.jeesl.model.ejb.io.mail.template;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.io.mail.template.JeeslIoTemplateScope;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoStatus;

@Entity
@DiscriminatorValue("ioTemplateScope")
@EjbErNode(name="Type",category="system",subset="template",level=3)
public class IoTemplateScope extends IoStatus implements JeeslIoTemplateScope<IoLang,IoDescription,IoTemplateScope,IoGraphic>
{
	public static final long serialVersionUID=1;

	@Override public boolean equals(Object object) {return (object instanceof IoTemplateScope) ? id == ((IoTemplateScope) object).getId() : (object == this);}
	@Override public int hashCode(){return new HashCodeBuilder(17,37).append(id).toHashCode();}
}