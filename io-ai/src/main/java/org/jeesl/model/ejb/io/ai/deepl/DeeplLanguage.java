package org.jeesl.model.ejb.io.ai.deepl;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.io.ai.JeeslIoAiLanguage;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoStatus;

@Entity
@DiscriminatorValue("deeplLanguage")
@EjbErNode(name="Locale",category="ts",subset="qc")
public class DeeplLanguage extends IoStatus implements JeeslIoAiLanguage<IoLang,IoDescription,DeeplLanguage,IoGraphic>
{
	public static final long serialVersionUID=1;
	
	public enum Code{de,en,fr}
	
	@Override public boolean equals(Object object) {return (object instanceof DeeplLanguage) ? id == ((DeeplLanguage) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(29,11).append(id).toHashCode();}
}