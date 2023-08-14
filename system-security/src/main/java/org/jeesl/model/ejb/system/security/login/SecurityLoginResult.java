package org.jeesl.model.ejb.system.security.login;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.system.security.login.JeeslSecurityLoginResult;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoStatus;

@Entity
@DiscriminatorValue("securityLoginResult")
@EjbErNode(name="Result",category="systemSecurity",subset="systemSecurity")
public class SecurityLoginResult extends IoStatus implements JeeslSecurityLoginResult<IoLang,IoDescription,SecurityLoginResult,IoGraphic>											
{
	public static final long serialVersionUID=1;

	@Override public List<String> getFixedCodes()
	{
		List<String> fixed = new ArrayList<>();
		for(JeeslSecurityLoginResult.Code code : JeeslSecurityLoginResult.Code.values()) {fixed.add(code.toString());}
		return fixed;
	}

	@Override public boolean equals(Object object) {return (object instanceof SecurityLoginResult) ? id == ((SecurityLoginResult) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(29,11).append(id).toHashCode();}
}