package org.jeesl.model.ejb.io.ssi.docker;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.io.ssi.docker.JeeslIoDockerState;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoStatus;

@Entity
@DiscriminatorValue("ioSsiDockerState")
@EjbErNode(name="State",category="ssi",subset="systemSsi")
public class IoDockerState extends IoStatus implements JeeslIoDockerState<IoLang,IoDescription,IoDockerState,IoGraphic>
{
	public static final long serialVersionUID=1;
	
	@Override public List<String> getFixedCodes()
	{
		List<String> fixed = new ArrayList<String>();
		for(JeeslIoDockerState.Code code : JeeslIoDockerState.Code.values()){fixed.add(code.toString());}
		return fixed;
	}
	
	@Override public boolean equals(Object object){return (object instanceof IoDockerState) ? id == ((IoDockerState) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,57).append(id).toHashCode();}
}