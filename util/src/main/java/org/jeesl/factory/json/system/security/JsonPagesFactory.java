package org.jeesl.factory.json.system.security;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.jeesl.api.facade.system.JeeslSecurityFacade;
import org.jeesl.factory.builder.system.SecurityFactoryBuilder;
import org.jeesl.factory.ejb.system.security.EjbSecurityMenuFactory;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityAction;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityArea;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityCategory;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityMenu;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityTemplate;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityUsecase;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityView;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.model.json.system.security.JsonSecurityPage;
import org.jeesl.model.json.system.security.JsonSecurityPages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.interfaces.model.status.UtilsDescription;
import net.sf.ahtutils.interfaces.model.status.UtilsLang;

public class JsonPagesFactory<L extends UtilsLang, D extends UtilsDescription,
									C extends JeeslSecurityCategory<L,D>,
									R extends JeeslSecurityRole<L,D,C,V,U,A,USER>,
									V extends JeeslSecurityView<L,D,C,R,U,A>,
									U extends JeeslSecurityUsecase<L,D,C,R,V,A>,
									A extends JeeslSecurityAction<L,D,R,V,U,AT>,
									AT extends JeeslSecurityTemplate<L,D,C>,
									M extends JeeslSecurityMenu<V,M>,
									AR extends JeeslSecurityArea<L,D,V>,
									USER extends JeeslUser<R>>
{
	final static Logger logger = LoggerFactory.getLogger(JsonPagesFactory.class);
	
	private final SecurityFactoryBuilder<L,D,C,R,V,U,A,AT,M,AR,USER> fbSecurity;
	private final EjbSecurityMenuFactory<V,M> efMenu;
	private final JsonPageFactory<L,D,C,V,M> jfPage;
	
	public JsonPagesFactory(SecurityFactoryBuilder<L,D,C,R,V,U,A,AT,M,AR,USER> fbSecurity)
	{
		this.fbSecurity=fbSecurity;
		efMenu = fbSecurity.ejbMenu();
		jfPage = fbSecurity.jsonPage();
	}
	
	public static JsonSecurityPages build() {return new JsonSecurityPages();}
	
	public JsonSecurityPages hierarchy(JeeslSecurityFacade<L,D,C,R,V,U,A,AT,M,USER> fSecurity)
	{
		JsonSecurityPages pages = build();
		
		List<M> list = fSecurity.all(fbSecurity.getClassMenu());
		Map<V,V> map = efMenu.toMapParent(list);
		
		for(V v : efMenu.toListView(list))
		{
			V item = v;
			List<V> hierarchy = new ArrayList<V>();
			while(item!=null)
			{
				hierarchy.add(item);
				if(map.containsKey(item)) {item=map.get(item);}
				else {item=null;}
			}
			Collections.reverse(hierarchy);
			
			JsonSecurityPage page = jfPage.build(v);
			if(hierarchy.size()>=1) {page.setS1(hierarchy.get(0).getCode());} else {page.setS1("-");}
			if(hierarchy.size()>=2) {page.setS2(hierarchy.get(1).getCode());} else {page.setS2("-");}
			if(hierarchy.size()>=3) {page.setS3(hierarchy.get(2).getCode());} else {page.setS3("-");}
			pages.getList().add(page);
//			if(pages.getList().size()==5) {break;}
		}
		
		return pages;
	}
}