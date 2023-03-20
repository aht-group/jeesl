package org.jeesl.model.ejb.system.job;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.system.job.JeeslJobStatus;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoStatus;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;
import org.jeesl.model.ejb.io.locale.IoDescription;

@Entity
@DiscriminatorValue("systemJobStatus")
@EjbErNode(name="Status",category="job",subset="sp,systemJob,spNsds",level=+2)
public class SystemJobStatus extends IoStatus implements JeeslJobStatus<IoLang,IoDescription,SystemJobStatus,IoGraphic>
{
	public static final long serialVersionUID=1;

	@Override public List<String> getFixedCodes()
	{
		List<String> fixed = new ArrayList<>();
		for(JeeslJobStatus.Code c : JeeslJobStatus.Code.values()){fixed.add(c.toString());}
		return fixed;
	}
	
	@Override public boolean equals(Object object){return (object instanceof SystemJobStatus) ? id == ((SystemJobStatus) object).getId() : (object==this);}
	@Override public int hashCode() {return new HashCodeBuilder(21,43).append(id).toHashCode();}
}