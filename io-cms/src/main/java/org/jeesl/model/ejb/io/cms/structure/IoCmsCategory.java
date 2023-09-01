package org.jeesl.model.ejb.io.cms.structure;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsCategory;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoStatus;

@Entity
@DiscriminatorValue("ioCmsCategory")
@EjbErNode(name="Category",category="system",subset="mail",level=3)
public class IoCmsCategory extends IoStatus implements JeeslIoCmsCategory<IoLang,IoDescription,IoCmsCategory,IoGraphic>
{
	public static final long serialVersionUID=1;
	
	
	@Override public boolean equals(Object object) {return (object instanceof IoCmsCategory) ? id == ((IoCmsCategory) object).getId() : (object == this);}
	@Override public int hashCode(){return new HashCodeBuilder(17,37).append(id).toHashCode();}
}