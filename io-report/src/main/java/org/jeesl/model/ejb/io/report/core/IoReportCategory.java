package org.jeesl.model.ejb.io.report.core;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.io.report.JeeslIoReportCategory;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoStatus;

@Entity
@DiscriminatorValue("ioReportC")
@EjbErNode(name="Category",category="system",subset="report",level=3)
public class IoReportCategory extends IoStatus implements JeeslIoReportCategory<IoLang,IoDescription,IoReportCategory,IoGraphic>
{
	public static final long serialVersionUID=1;

	
	@Override public boolean equals(Object object) {return (object instanceof IoReportCategory) ? id == ((IoReportCategory) object).getId() : (object == this);}
	@Override public int hashCode(){return new HashCodeBuilder(17,37).append(id).toHashCode();}
}