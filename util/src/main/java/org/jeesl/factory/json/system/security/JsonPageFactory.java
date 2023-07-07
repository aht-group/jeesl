package org.jeesl.factory.json.system.security;

import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityCategory;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityContext;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityMenu;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityView;
import org.jeesl.model.json.system.security.JsonSecurityPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonPageFactory<L extends JeeslLang, D extends JeeslDescription,
									C extends JeeslSecurityCategory<L,D>,
									V extends JeeslSecurityView<L,D,C,?,?,?>,
									CTX extends JeeslSecurityContext<L,D>,
									M extends JeeslSecurityMenu<L,V,CTX,M>>
{
	final static Logger logger = LoggerFactory.getLogger(JsonPageFactory.class);
	
	public static JsonSecurityPage build() {return new JsonSecurityPage();}
	
	public JsonSecurityPage build(V view)
	{
		JsonSecurityPage json = build();
		json.setAccessViaPublic(view.getAccessPublic());
		return json;
	}
}