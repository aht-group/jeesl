package org.jeesl.model.ejb.io.locale;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;

@Entity
@DiscriminatorValue("ioLocale")
@EjbErNode(name="Locale",category="ts",subset="qc")
public class IoLocale extends IoStatus implements JeeslLocale<IoLang,IoDescription,IoLocale,IoGraphic>
{
	public static final long serialVersionUID=1;
	
	@Override public boolean equals(Object object) {return (object instanceof IoLocale) ? id == ((IoLocale) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(29,11).append(id).toHashCode();}
}