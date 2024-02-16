package org.jeesl.model.ejb.io.ai.openai;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.io.ai.openai.JeeslIoAiOpenAiLength;
import org.jeesl.interfaces.model.io.ai.openai.JeeslIoAiOpenAiGeneration;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoStatus;

@Entity
@DiscriminatorValue("ioAiOpenAiLength")
@EjbErNode(name="Type",category="survey",subset="survey")
public class IoOpenAiOutputLength extends IoStatus implements JeeslIoAiOpenAiLength<IoLang,IoDescription,IoOpenAiOutputLength,IoGraphic>
{
	public static final long serialVersionUID=1;
	
	@Override public List<String> getFixedCodes()
	{
		List<String> fixed = new ArrayList<>();
		for(JeeslIoAiOpenAiGeneration.Code c : JeeslIoAiOpenAiGeneration.Code.values()){fixed.add(c.toString());}
		return fixed;
	}
	
	@Override public boolean equals(Object object) {return (object instanceof IoOpenAiOutputLength) ? id == ((IoOpenAiOutputLength) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(19,21).append(id).toHashCode();}
}