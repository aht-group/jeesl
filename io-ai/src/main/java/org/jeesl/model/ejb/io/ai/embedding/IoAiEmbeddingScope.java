package org.jeesl.model.ejb.io.ai.embedding;

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
@DiscriminatorValue("ioAiEmbeddingScope")
@EjbErNode(name="Type",category="survey",subset="survey")
public class IoAiEmbeddingScope extends IoStatus implements JeeslIoAiOpenAiModel<IoLang,IoDescription,IoAiEmbeddingScope,IoGraphic>
{
	public static final long serialVersionUID=1;
	
	public static enum Code{chartBarHorizontal};
	
	@Override public boolean equals(Object object) {return (object instanceof IoAiEmbeddingScope) ? id == ((IoAiEmbeddingScope) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(19,21).append(id).toHashCode();}
}