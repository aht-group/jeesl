package org.jeesl.model.ejb.system.job.maintenance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatusFixedCode;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoStatus;

@Entity
@DiscriminatorValue("systemJobCleaning")
@EjbErNode(name="Status",category="project",level=3,subset="project")
public class SystemJobCleaning extends IoStatus
								implements Serializable,EjbPersistable,EjbWithCode,JeeslStatusFixedCode,
												JeeslStatus<IoLang,IoDescription,SystemJobCleaning>
{
	public static final long serialVersionUID=1;
	
	public enum Code{operation,support}
	
	@Override public List<String> getFixedCodes()
	{
		List<String> fixed = new ArrayList<>();
		for(Code c : Code.values()){fixed.add(c.toString());}
		return fixed;
	}
	
	@Override public boolean equals(Object object){return (object instanceof SystemJobCleaning) ? id == ((SystemJobCleaning) object).getId() : (object == this);}
	@Override public int hashCode(){return new HashCodeBuilder(21,51).append(id).toHashCode();}
}