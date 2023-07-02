package org.jeesl.model.ejb.system.security;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityIcon;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoStatus;

@Entity
@DiscriminatorValue("securityIcon")
@EjbErNode(name="Icon",level=3,subset="systemSecurity")
public class SecurityIcon extends IoStatus implements JeeslSecurityIcon<IoLang,IoDescription,SecurityIcon,IoGraphic>
{
	public static final long serialVersionUID=1;
	
	@Override public List<String> getFixedCodes()
	{
		List<String> fixed = new ArrayList<>();
		for(JeeslSecurityIcon.Code code : JeeslSecurityIcon.Code.values()){fixed.add(code.toString());}
		return fixed;
	}
	
	@Override public boolean equals(Object object){return (object instanceof SecurityIcon) ? id == ((SecurityIcon) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,43).append(id).toHashCode();}
}