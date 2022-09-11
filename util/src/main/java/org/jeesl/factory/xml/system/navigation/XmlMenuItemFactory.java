package org.jeesl.factory.xml.system.navigation;

import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityAction;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityCategory;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityContext;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityMenu;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityTemplate;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityUsecase;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityView;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.model.xml.system.navigation.MenuItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.security.View;

public class XmlMenuItemFactory <L extends JeeslLang, D extends JeeslDescription,
								C extends JeeslSecurityCategory<L,D>,
								R extends JeeslSecurityRole<L,D,C,V,U,A,USER>,
								V extends JeeslSecurityView<L,D,C,R,U,A>,
								U extends JeeslSecurityUsecase<L,D,C,R,V,A>,
								A extends JeeslSecurityAction<L,D,R,V,U,AT>,
								AT extends JeeslSecurityTemplate<L,D,C>,
								CTX extends JeeslSecurityContext<L,D>,
								M extends JeeslSecurityMenu<L,V,CTX,M>,
								USER extends JeeslUser<R>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlMenuItemFactory.class);
	
	private String localeCode; public void setLocaleCode(String localeCode) {this.localeCode = localeCode;}

	public XmlMenuItemFactory(String localeCode)
	{
		this.localeCode=localeCode;
	}
	
	public MenuItem build(M menu)
	{
		/*
		logger.warn("localeCode:"+localeCode);
		logger.warn("menu!=null:"+(menu!=null));
		logger.warn("menu.getView()!=null:"+(menu.getView()!=null));
		logger.warn("menu.getView().getName()!=null:"+(menu.getView().getName()!=null));
		logger.warn("menu.getView().getName().containsKey(localeCode):"+(menu.getView().getName().containsKey(localeCode)));
		*/
		
		MenuItem xml = build();
		xml.setVisible(menu.getView().isVisible());
		xml.setCode(menu.getView().getCode());
		if(menu.getView().getName().containsKey(localeCode)) {xml.setName(menu.getView().getName().get(localeCode).getLang());}
		else {xml.setName("??");}
		xml.setHref(menu.getView().getUrlMapping());
		xml.setActive(false);
		return xml;
	}
	
	public static MenuItem clone(MenuItem item)
	{
		MenuItem xml = build();
		xml.setName(item.getName());
		xml.setHref(item.getHref());
		xml.setCode(item.getCode());

		return xml;
	}
	
	public static MenuItem build(MenuItem mi)
	{
		MenuItem xml = build();
		xml.setVisible(mi.isSetVisible() && mi.isVisible());
		xml.setActive(mi.isSetActive() && mi.isActive());
		xml.setCode(mi.getCode());
		xml.setHref(mi.getHref());
		xml.setName(mi.getName());
		
		return xml;
	}
	
	public static MenuItem create(String label)
	{
		MenuItem xml = build();
		xml.setName(label);
		return xml;
	}
	
	public static MenuItem dynamic(String dynamicCode, String urlParameter, String label)
	{
		View view = new View();
		view.setCode(dynamicCode);
		view.setUrlParameter(urlParameter);
		view.setLabel(label);

		MenuItem item = new MenuItem();
		item.setCode(dynamicCode+view.getUrlParameter());
		item.setView(view);
		return item;
	}
	
	public static MenuItem buildItem(String label, String href)
	{
		MenuItem mi = build();
		mi.setName(label);
		mi.setHref(href);
		return mi;
	}
	
	public static MenuItem build()
	{
		return new MenuItem();
	}
}