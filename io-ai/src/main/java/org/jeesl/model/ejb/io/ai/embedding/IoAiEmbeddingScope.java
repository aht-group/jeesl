package org.jeesl.model.ejb.io.ai.embedding;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.io.ai.JeeslIoAiEmbeddingScope;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoStatus;

@Entity
@DiscriminatorValue("ioAiEmbeddingScope")
@EjbErNode(name="Type",category="survey",subset="survey")
public class IoAiEmbeddingScope extends IoStatus implements JeeslIoAiEmbeddingScope<IoLang,IoDescription,IoAiEmbeddingScope,IoGraphic>
{
	public static final long serialVersionUID=1;


	@Override public List<String> getFixedCodes()
	{
		List<String> fixed = new ArrayList<>();
		for(JeeslIoAiEmbeddingScope.Code c : JeeslIoAiEmbeddingScope.Code.values()){fixed.add(c.toString());}
		return fixed;
	}

	@Override public boolean equals(Object object) {return (object instanceof IoAiEmbeddingScope) ? id == ((IoAiEmbeddingScope) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(19,21).append(id).toHashCode();}
}