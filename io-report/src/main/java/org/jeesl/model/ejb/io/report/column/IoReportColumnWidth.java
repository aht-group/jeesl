package org.jeesl.model.ejb.io.report.column;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.io.report.setting.JeeslReportSetting;
import org.jeesl.interfaces.model.io.report.style.JeeslReportColumnWidth;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoStatus;

@Entity
@DiscriminatorValue("ioReportColumnWidth")
@EjbErNode(name="Width",category="system",subset="report",level=3)

public class IoReportColumnWidth extends IoStatus
								implements JeeslReportColumnWidth<IoLang,IoDescription,IoReportColumnWidth,IoGraphic>,
											JeeslReportSetting
{
	public static final long serialVersionUID=1;

	@Override public List<String> getFixedCodes()
	{
		List<String> fixed = new ArrayList<String>();
		for(JeeslReportColumnWidth.Code c : JeeslReportColumnWidth.Code.values()){fixed.add(c.toString());}
		return fixed;
	}

	@Override public boolean equals(Object object) {return (object instanceof IoReportColumnWidth) ? id == ((IoReportColumnWidth) object).getId() : (object == this);}
	@Override public int hashCode(){return new HashCodeBuilder(17,37).append(id).toHashCode();}
}