package org.jeesl.factory.xml.system.navigation;

import org.jeesl.controller.util.comparator.primitive.BooleanComparator;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityUsecase;
import org.jeesl.interfaces.model.system.security.context.JeeslSecurityContext;
import org.jeesl.interfaces.model.system.security.context.JeeslSecurityMenu;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityAction;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityTemplate;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityView;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.model.system.security.util.JeeslSecurityCategory;
import org.jeesl.model.xml.system.navigation.MenuItem;
import org.jeesl.model.xml.system.security.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlMenuItemFactory <L extends JeeslLang, D extends JeeslDescription,
								C extends JeeslSecurityCategory<L,D>,
								R extends JeeslSecurityRole<L,D,C,V,U,A>,
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
		xml.setVisible(BooleanComparator.active(mi.isVisible()));
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