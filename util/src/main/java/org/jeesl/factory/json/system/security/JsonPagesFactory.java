package org.jeesl.factory.json.system.security;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.jeesl.api.facade.system.JeeslSecurityFacade;
import org.jeesl.factory.builder.system.SecurityFactoryBuilder;
import org.jeesl.factory.ejb.system.security.EjbSecurityMenuFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityUsecase;
import org.jeesl.interfaces.model.system.security.context.JeeslSecurityContext;
import org.jeesl.interfaces.model.system.security.context.JeeslSecurityMenu;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityAction;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityArea;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityTemplate;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityView;
import org.jeesl.interfaces.model.system.security.user.JeeslSecurityUser;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.model.system.security.util.JeeslSecurityCategory;
import org.jeesl.model.json.system.security.JsonSecurityPage;
import org.jeesl.model.json.system.security.JsonSecurityPages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonPagesFactory<L extends JeeslLang, D extends JeeslDescription,
									C extends JeeslSecurityCategory<L,D>,
									R extends JeeslSecurityRole<L,D,C,V,U,A>,
									V extends JeeslSecurityView<L,D,C,R,U,A>,
									U extends JeeslSecurityUsecase<L,D,C,R,V,A>,
									A extends JeeslSecurityAction<L,D,R,V,U,AT>,
									AT extends JeeslSecurityTemplate<L,D,C>,
									CTX extends JeeslSecurityContext<L,D>,
									M extends JeeslSecurityMenu<L,V,CTX,M>,
									AR extends JeeslSecurityArea<L,D,V>,
									UJ extends JeeslSecurityUser,
									USER extends JeeslUser<R>>
{
	final static Logger logger = LoggerFactory.getLogger(JsonPagesFactory.class);

	private final SecurityFactoryBuilder<L,D,C,R,V,U,A,AT,CTX,M,AR,?,?,?,?,?,?,UJ,USER> fbSecurity;
	private final EjbSecurityMenuFactory<V,CTX,M> efMenu;
	private final JsonPageFactory<L,D,C,V,CTX,M> jfPage;
	
	public JsonPagesFactory(SecurityFactoryBuilder<L,D,C,R,V,U,A,AT,CTX,M,AR,?,?,?,?,?,?,UJ,USER> fbSecurity)
	{
		this.fbSecurity=fbSecurity;
		efMenu = fbSecurity.ejbMenu();
		jfPage = fbSecurity.jsonPage();
	}
	
	public static JsonSecurityPages build() {return new JsonSecurityPages();}
	
	
	
	public JsonSecurityPages childrenOfRoot(JeeslSecurityFacade<C,R,V,U,A,CTX,M,USER> fSecurity, CTX context, int depth)
	{
		JsonSecurityPages pages = build();
		List<M> menus = fSecurity.allForParent(fbSecurity.getClassMenu(), JeeslSecurityMenu.Attributes.context, context);
		Map<M,List<M>> children = efMenu.toMapChild(menus);
		List<M> roots = efMenu.toListRoot(menus);
		addChildren(pages, roots, children, 1, depth);
		return pages;
	}
	public JsonSecurityPages childrenOf(JeeslSecurityFacade<C,R,V,U,A,CTX,M,USER> fSecurity, CTX context, String viewCode, int depth)
	{
		JsonSecurityPages pages = build();
		List<M> menus = fSecurity.allForParent(fbSecurity.getClassMenu(), JeeslSecurityMenu.Attributes.context, context);
		Map<M,List<M>> children = efMenu.toMapChild(menus);
		for(M menu : menus)
		{
			if(Objects.nonNull(menu.getView()) && Objects.equals(viewCode, menu.getView().getCode()))
			{
				addChildren(pages, children.get(menu), children, 1, depth);
				break;
			}
		}
		return pages;
	}

	private void addChildren(JsonSecurityPages pages, List<M> menus, Map<M,List<M>> children, int level, int depth)
	{
		if(depth<=0 || level>depth || menus==null) {return;}
		for(M child : menus)
		{
			if(Objects.isNull(child.getView())) {continue;}
			JsonSecurityPage page = jfPage.build(child.getView());
			page.setCode(child.getView().getCode());
			pages.getList().add(page);
			addChildren(pages, children.get(child), children, level+1, depth);
		}
	}
}
