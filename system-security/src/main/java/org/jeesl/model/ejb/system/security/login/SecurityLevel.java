package org.jeesl.model.ejb.system.security.login;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.system.security.login.JeeslSecurityLevel;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoStatus;

@Entity
@DiscriminatorValue("securityLevel")
@EjbErNode(name="Level",category="systemSecurity",subset="systemSecurity")
public class SecurityLevel extends IoStatus implements JeeslSecurityLevel<IoLang,IoDescription,SecurityLevel,IoGraphic>											
{
	public static final long serialVersionUID=1;

	@Override public List<String> getFixedCodes()
	{
		List<String> fixed = new ArrayList<>();
		for(JeeslSecurityLevel.Code code : JeeslSecurityLevel.Code.values()) {fixed.add(code.toString());}
		return fixed;
	}

	@Override public boolean equals(Object object) {return (object instanceof SecurityLevel) ? id == ((SecurityLevel) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(29,11).append(id).toHashCode();}
}