package org.jeesl.model.ejb.io.mail.template;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.io.mail.template.JeeslTemplateTokenType;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoStatus;

@Entity
@DiscriminatorValue("ioTemplateTokenType")
@EjbErNode(name="Type",category="system",subset="template",level=3)

public class IoTemplateTokenType extends IoStatus implements JeeslTemplateTokenType<IoLang,IoDescription,IoTemplateTokenType,IoGraphic>
{
	public static final long serialVersionUID=1;

	@Override public List<String> getFixedCodes()
	{
		List<String> fixed = new ArrayList<>();
		for(JeeslTemplateTokenType.Code c : JeeslTemplateTokenType.Code.values()){fixed.add(c.toString());}
		return fixed;
	}

	@Override public boolean equals(Object object) {return (object instanceof IoTemplateTokenType) ? id == ((IoTemplateTokenType) object).getId() : (object == this);}
	@Override public int hashCode(){return new HashCodeBuilder(17,37).append(id).toHashCode();}
}