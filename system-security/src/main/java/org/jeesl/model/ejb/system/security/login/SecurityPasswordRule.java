package org.jeesl.model.ejb.system.security.login;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.system.security.pwd.JeeslSecurityPasswordRule;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoStatus;

@Entity
@DiscriminatorValue("securityPasswordRule")
@EjbErNode(name="Rule",category="ts",subset="qc")
public class SecurityPasswordRule extends IoStatus implements JeeslSecurityPasswordRule<IoLang,IoDescription,SecurityPasswordRule,IoGraphic>								
{
	public static final long serialVersionUID=1;

	@Override public List<String> getFixedCodes()
	{
		List<String> fixed = new ArrayList<>();
		for(JeeslSecurityPasswordRule.Code code : JeeslSecurityPasswordRule.Code.values()){fixed.add(code.toString());}
		return fixed;
	}

	@Override public boolean equals(Object object) {return (object instanceof SecurityPasswordRule) ? id == ((SecurityPasswordRule) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(29,11).append(id).toHashCode();}
}