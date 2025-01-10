package org.jeesl.interfaces.model.system.security.context;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPosition;
import org.jeesl.interfaces.model.with.system.locale.EjbWithDescription;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLang;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslSecurityContext<L extends JeeslLang, D extends JeeslDescription>
			extends Serializable,EjbSaveable,EjbRemoveable,
						EjbWithLang<L>,EjbWithDescription<D>,
						EjbWithCode,EjbWithPosition
{
	public enum Code {core}
	
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
	
	String getPageFavIcon();
	void setPageFavIcon(String pageFavIcon);
	
	String getPageBackground();
	void setPageBackground(String pageBackground);
	
	String getLinkImpressum();
	void setLinkImpressum(String linkImpressum);
	
	String getEmailAddress();
	void setEmailAddress(String emailAddress);

	String getEmailName();
	void setEmailName(String emailName);
	
	String getMfaLabel();
	void setMfaLabel(String mfaLabel);
	
	Long getContextId();
	void setContextId(Long contextId);
}