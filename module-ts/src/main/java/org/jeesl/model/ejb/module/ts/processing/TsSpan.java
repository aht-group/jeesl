package org.jeesl.model.ejb.module.ts.processing;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.module.ts.config.JeeslTsDimension;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoStatus;

@Entity
@DiscriminatorValue("timeseriesSpan")
@EjbErNode(name="Dimension",category="ts",subset="moduleTs")
public class TsSpan extends IoStatus implements JeeslTsDimension<IoLang,IoDescription,TsSpan,IoGraphic>
{
	public static final long serialVersionUID=1;
	
	
	
	@Override public List<String> getFixedCodes()
	{
		List<String> fixed = new ArrayList<String>();
		for(JeeslTsDimension.Code c : JeeslTsDimension.Code.values()){fixed.add(c.toString());}
		return fixed;
	}
	
	@Override public boolean equals(Object object) {return (object instanceof TsSpan) ? id == ((TsSpan) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(23,7).append(id).toHashCode();}
}