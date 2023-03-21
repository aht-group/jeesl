package org.jeesl.model.ejb.system.job.template;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.system.job.JeeslJobCategory;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoStatus;

@Entity
@DiscriminatorValue("systemJobCategory")
@EjbErNode(name="Category")
public class SystemJobCategory extends IoStatus implements JeeslJobCategory<IoLang,IoDescription,SystemJobCategory,IoGraphic>
{
	public static final long serialVersionUID=1;
	
	public static enum Code{mail};
	
	@Override public boolean equals(Object object){return (object instanceof SystemJobCategory) ? id == ((SystemJobCategory) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(21,43).append(id).toHashCode();}
}