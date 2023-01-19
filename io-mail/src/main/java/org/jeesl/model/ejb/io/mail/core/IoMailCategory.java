package org.jeesl.model.ejb.io.mail.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatusFixedCode;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoStatus;
import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;

@Entity
@DiscriminatorValue("ioMailCategory")
@EjbErNode(name="Category",category="system",subset="mail",level=3)
public class IoMailCategory extends IoStatus
								implements Serializable,EjbPersistable,EjbWithCode,JeeslStatusFixedCode,
											JeeslStatus<IoLang,IoDescription,IoMailCategory>
{
	public static final long serialVersionUID=1;
	public static enum Code{test};
	
	@Override public List<String> getFixedCodes()
	{
		List<String> fixed = new ArrayList<>();
		for(IoMailCategory.Code c : IoMailCategory.Code.values()){fixed.add(c.toString());}
		return fixed;
	}
	
	@Override public boolean equals(Object object) {return (object instanceof IoMailCategory) ? id == ((IoMailCategory) object).getId() : (object == this);}
	@Override public int hashCode(){return new HashCodeBuilder(17,37).append(id).toHashCode();}
}