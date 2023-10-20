package org.jeesl.model.ejb.io.ai.openai;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.io.ai.JeeslIoAiOpenAiModel;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoStatus;

@Entity
@DiscriminatorValue("ioAiOpenAiModel")
@EjbErNode(name="Type",category="survey",subset="survey")
public class IoAiOpenAiModel extends IoStatus implements JeeslIoAiOpenAiModel<IoLang,IoDescription,IoAiOpenAiModel,IoGraphic>
{
	public static final long serialVersionUID=1;
	
	public static enum Code{chartBarHorizontal};
	
	@Override public boolean equals(Object object) {return (object instanceof IoAiOpenAiModel) ? id == ((IoAiOpenAiModel) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(19,21).append(id).toHashCode();}
}