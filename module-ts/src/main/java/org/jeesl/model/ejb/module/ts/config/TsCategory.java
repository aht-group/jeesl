package org.jeesl.model.ejb.module.ts.config;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.module.ts.config.JeeslTsCategory;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoStatus;

@Entity
@DiscriminatorValue("timeseriesCategory")
@EjbErNode(name="Category",category="ts",subset="moduleTs")
public class TsCategory extends IoStatus implements JeeslTsCategory<IoLang,IoDescription,TsCategory,IoGraphic>
{
	public static final long serialVersionUID=1;
	
	@Override public boolean equals(Object object) {return (object instanceof TsCategory) ? id == ((TsCategory) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(19,21).append(id).toHashCode();}
}