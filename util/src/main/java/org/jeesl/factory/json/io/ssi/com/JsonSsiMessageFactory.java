package org.jeesl.factory.json.io.ssi.com;

import org.jeesl.api.facade.system.JeeslSystemConstraintFacade;
import org.jeesl.interfaces.model.system.constraint.core.JeeslConstraint;
import org.jeesl.interfaces.model.system.constraint.core.JeeslConstraintScope;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.json.io.ssi.core.JsonSsiMessage;

public class JsonSsiMessageFactory	
{
	public static JsonSsiMessage build(){return new JsonSsiMessage();}
	
	public static JsonSsiMessage build(String description)
	{
		JsonSsiMessage json = build();
		json.setDescription(description);
		return json;
	}
	
	public static <E extends Enum<E>, LOC extends Enum<LOC>,
				L extends JeeslLang, D extends JeeslDescription,
				SCOPE extends JeeslConstraintScope<L,D,SCOPE,?,CONSTRAINT,LEVEL,TYPE,?>,
				CONSTRAINT extends JeeslConstraint<L,D,SCOPE,?,CONSTRAINT,LEVEL,TYPE,?>,
				LEVEL extends JeeslStatus<L,D,LEVEL>,
				TYPE extends JeeslStatus<L,D,TYPE>>
		JsonSsiMessage build(LOC loc, JeeslSystemConstraintFacade<L,D,?,?,SCOPE,?,CONSTRAINT,LEVEL,TYPE,?> facade, Class<?> c, E code)
	{
		JsonSsiMessage json = new JsonSsiMessage();
	
		json.setCode(code.toString());
		
		CONSTRAINT constraint = facade.fSystemConstraint(c,code);
		if(constraint!=null)
		{
			json.setType(constraint.getType().getName().get(loc.toString()).getLang());
			json.setLevel(constraint.getLevel().getName().get(loc.toString()).getLang());
			json.setLabel(constraint.getName().get(loc.toString()).getLang());
			json.setDescription(constraint.getDescription().get(loc.toString()).getLang());
		}
		else
		{
			json.setDescription("Missing Error definition for "+c.getSimpleName()+" "+code.toString());
		}
	
		return json;
	}
}