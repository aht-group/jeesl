package org.jeesl.controller.report.system;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.jeesl.api.facade.system.JeeslSecurityFacade;
import org.jeesl.controller.util.comparator.ejb.system.security.SecurityRoleComparator;
import org.jeesl.factory.builder.system.SecurityFactoryBuilder;
import org.jeesl.factory.xml.system.lang.XmlDescriptionFactory;
import org.jeesl.factory.xml.system.lang.XmlDescriptionsFactory;
import org.jeesl.factory.xml.system.security.XmlCategoryFactory;
import org.jeesl.factory.xml.system.security.XmlRoleFactory;
import org.jeesl.factory.xml.system.security.XmlRolesFactory;
import org.jeesl.factory.xml.system.security.XmlSecurityFactory;
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
import org.jeesl.model.xml.system.security.Category;
import org.jeesl.model.xml.system.security.Role;
import org.jeesl.model.xml.system.security.Roles;
import org.jeesl.model.xml.system.security.Security;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.status.Descriptions;

public class JeeslSecurityRoleReport <L extends JeeslLang, D extends JeeslDescription,
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
	final static Logger logger = LoggerFactory.getLogger(JeeslSecurityRoleReport.class);

	private final JeeslSecurityFacade<C,R,V,U,A,M,USER> fSecurity;
	private final SecurityFactoryBuilder<L,D,C,R,V,U,A,AT,?,M,?,?,?,?,?,USER> fbSecurity;
	
	private final String localeCode;
	protected boolean developmentMode; public void setDevelopmentMode(boolean developmentMode) {this.developmentMode = developmentMode;}

	//	private org.jeesl.factory.xml.system.security.XmlViewFactory<L,D,C,R,V,U,A,AT,USER> xfView;
	private Comparator<R> comparatorView;
	
	public JeeslSecurityRoleReport(String localeCode, SecurityFactoryBuilder<L,D,C,R,V,U,A,AT,?,M,?,?,?,?,?,USER> fbSecurity,
									JeeslSecurityFacade<C,R,V,U,A,M,USER> fSecurity)
	{
		this.localeCode=localeCode;
		this.fSecurity=fSecurity;
		this.fbSecurity=fbSecurity;
		
		developmentMode = false;
		comparatorView = (new SecurityRoleComparator<C,R>()).factory(SecurityRoleComparator.Type.position);
	}
	
	public Security build()
	{
		Roles roles = XmlRolesFactory.build();
		List<R> views;
		views = fSecurity.all(fbSecurity.getClassRole());
		Collections.sort(views,comparatorView);
		for(R role : views)
		{
			if(developmentMode && roles.getRole().size()==2) {break;}
			roles.getRole().add(build(role));
		}
		
		return XmlSecurityFactory.build(roles);
	}
	
	private Role build(R role)
	{
		Role xml = XmlRoleFactory.build(role.getCode());
		xml.setPosition(role.getPosition());
		xml.setLabel(role.getName().get(localeCode).getLang());
		
		Category cat = XmlCategoryFactory.build();
		cat.setPosition(role.getCategory().getPosition());
		cat.setLabel(role.getCategory().getName().get(localeCode).getLang());
		xml.setCategory(cat);
		
		Descriptions desc = XmlDescriptionsFactory.build(XmlDescriptionFactory.build(role.getDescription().get(localeCode).getLang()));
		xml.setDescriptions(desc);
		
		return xml;
	}
}