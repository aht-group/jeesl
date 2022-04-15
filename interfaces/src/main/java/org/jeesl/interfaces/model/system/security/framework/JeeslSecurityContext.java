package org.jeesl.interfaces.model.system.security.framework;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPosition;
import org.jeesl.interfaces.model.with.system.locale.EjbWithDescription;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLang;

public interface JeeslSecurityContext<L extends JeeslLang, D extends JeeslDescription>
			extends Serializable,EjbSaveable,EjbRemoveable,
						EjbWithLang<L>,EjbWithDescription<D>,
						EjbWithCode,EjbWithPosition
{
	String getServerName();
	void setServerName(String serverName);

	String getPageTitle();
	void setPageTitle(String pageTitle);

	String getPagePrefix();
	void setPagePrefix(String pagePrefix);

	String getPageHeadline();
	void setPageHeadline(String pageHeadline);
	
	String getPageCss();
	void setPageCss(String pageCss);

	String getPageLogo();
	void setPageLogo(String pageLogo);
	
	String getPageBackground();
	void setPageBackground(String pageBackground);
	
	String getLinkImpressum();
	void setLinkImpressum(String linkImpressum);
	
	String getEmailAddress();
	void setEmailAddress(String emailAddress);

	String getEmailName();
	void setEmailName(String emailName);
	
	Long getContextId();
	void setContextId(Long contextId);
}