package org.jeesl.controller.report.system;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.jeesl.api.facade.system.JeeslSecurityFacade;
import org.jeesl.controller.util.comparator.ejb.system.security.SecurityViewComparator;
import org.jeesl.factory.builder.system.SecurityFactoryBuilder;
import org.jeesl.factory.xml.system.navigation.XmlMenuFactory;
import org.jeesl.factory.xml.system.navigation.XmlMenuItemFactory;
import org.jeesl.factory.xml.system.security.XmlSecurityFactory;
import org.jeesl.factory.xml.system.security.XmlViewFactory;
import org.jeesl.factory.xml.system.util.text.XmlDescriptionFactory;
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
import org.jeesl.model.xml.system.navigation.Menu;
import org.jeesl.model.xml.system.navigation.MenuItem;
import org.jeesl.model.xml.system.security.Security;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslMenuStructureReport <L extends JeeslLang,
								D extends JeeslDescription,
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
	final static Logger logger = LoggerFactory.getLogger(JeeslMenuStructureReport.class);

	private final JeeslSecurityFacade<C,R,V,U,A,M,USER> fSecurity;
	private final SecurityFactoryBuilder<L,D,C,R,V,U,A,AT,CTX,M,?,?,?,?,?,USER> fbSecurity;
	
	private final String localeCode;
	
//	private org.jeesl.factory.xml.system.security.XmlViewFactory<L,D,C,R,V,U,A,AT,USER> xfView;
	private Comparator<V> comparatorView;
	
	public JeeslMenuStructureReport(String localeCode, JeeslSecurityFacade<C,R,V,U,A,M,USER> fSecurity, SecurityFactoryBuilder<L,D,C,R,V,U,A,AT,CTX,M,?,?,?,?,?,USER> fbSecurity)
	{
		this.localeCode=localeCode;
		this.fSecurity=fSecurity;
		this.fbSecurity=fbSecurity;
		
		comparatorView = (new SecurityViewComparator<V>()).factory(SecurityViewComparator.Type.position);
	}
	
	public Security build()
	{
		Menu menu = XmlMenuFactory.build();
		List<V> views;
		views = fSecurity.all(fbSecurity.getClassView());
		Collections.sort(views,comparatorView);
		for(V view : views)
		{
			menu.getMenuItem().add(build(view));
		}
		
		return XmlSecurityFactory.build(menu);
	}
	
	private MenuItem build(V view)
	{
		MenuItem item = XmlMenuItemFactory.build();
		
		item.setView(XmlViewFactory.build(view.getCategory().getPosition()+"."+view.getPosition()));
		item.getView().setLabel(view.getCategory().getName().get(localeCode).getLang());
		
		item.setCode(view.getCode());
		item.setName(view.getName().get(localeCode).getLang());
		item.setDescription(XmlDescriptionFactory.build(view.getDescription().get(localeCode).getLang()));
		return item;
	}
}