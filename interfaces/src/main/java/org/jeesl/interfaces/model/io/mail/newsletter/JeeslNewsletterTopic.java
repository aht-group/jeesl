package org.jeesl.interfaces.model.io.mail.newsletter;

import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslMcsStatus;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;

public interface JeeslNewsletterTopic <L extends JeeslLang, D extends JeeslDescription,
										R extends JeeslTenantRealm<L,D,R,G>,
										S extends JeeslMcsStatus<L,D,R,S,G>,
										G extends JeeslGraphic<L,D,?,?,?>>
			extends JeeslMcsStatus<L,D,R,S,G>
					
{
	public enum Attributes{realm,rref}
}