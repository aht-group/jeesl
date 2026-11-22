package org.jeesl.factory.json.system.security;

import java.util.Objects;

import org.jeesl.factory.txt.system.locale.TxtMultiLangFactory;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityRole;
import org.jeesl.model.json.system.security.JsonSecurityRole;

public class JsonSecurityRoleFactory <L extends JeeslLang, R extends JeeslSecurityRole<L,?,?,?,?,?>>
{
	private final JsonSecurityRole query;
	
	private TxtMultiLangFactory<L> txtLabel;

	public static <L extends JeeslLang, R extends JeeslSecurityRole<L,?,?,?,?,?>>
			JsonSecurityRoleFactory<L,R>  instance(String localeCode, JsonSecurityRole query)
	{
		return new JsonSecurityRoleFactory<>(localeCode,query);
	}
	
	private JsonSecurityRoleFactory(String localeCode, JsonSecurityRole query)
	{
		this.query = query;
		
		if(Objects.nonNull(query.getLabel())) {txtLabel = TxtMultiLangFactory.instance(localeCode);}
	}

	public JsonSecurityRole build(R role)
	{
		if(Objects.isNull(role)) {return null;}

		JsonSecurityRole json = new JsonSecurityRole();

		if(Objects.nonNull(query.getId())) {json.setId(Long.valueOf(role.getId()));}
		if(Objects.nonNull(query.getCode())) {json.setCode(role.getCode());}
		if(Objects.nonNull(query.getLabel())) {json.setLabel(txtLabel.label(role.getName()));}

		return json;
	}
}
