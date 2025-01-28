package org.jeesl.model.ejb.system.security.oauth;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.system.security.login.JeeslSecurityMfaType;
import org.jeesl.interfaces.model.system.security.oauth.JeeslSecurityOauthKeyType;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoStatus;

@Entity
@DiscriminatorValue("securityOauthKeyType")
@EjbErNode(name="MFA Type",category="systemSecurity",subset="systemSecurity")
public class SecurityOauthKeyType extends IoStatus implements JeeslSecurityOauthKeyType<IoLang,IoDescription,SecurityOauthKeyType,IoGraphic>											
{
	public static final long serialVersionUID=1;

	@Override public List<String> getFixedCodes()
	{
		List<String> fixed = new ArrayList<>();
		for(JeeslSecurityMfaType.Code code : JeeslSecurityMfaType.Code.values()) {fixed.add(code.toString());}
		return fixed;
	}

	@Override public boolean equals(Object object) {return (object instanceof SecurityOauthKeyType) ? id == ((SecurityOauthKeyType) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(29,11).append(id).toHashCode();}
}