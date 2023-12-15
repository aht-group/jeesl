package org.jeesl.factory.xml.system.security;

import java.util.Objects;

import org.jeesl.factory.xml.system.lang.XmlDescriptionsFactory;
import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityUsecase;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityAction;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityTemplate;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityView;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.model.system.security.util.JeeslSecurityCategory;
import org.jeesl.model.xml.system.security.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.util.io.StringUtil;

public class XmlRoleFactory<L extends JeeslLang, D extends JeeslDescription, 
							C extends JeeslSecurityCategory<L,D>,
							R extends JeeslSecurityRole<L,D,C,V,U,A>,
							V extends JeeslSecurityView<L,D,C,R,U,A>,
							U extends JeeslSecurityUsecase<L,D,C,R,V,A>,
							A extends JeeslSecurityAction<L,D,R,V,U,AT>,
							AT extends JeeslSecurityTemplate<L,D,C>,
							USER extends JeeslUser<R>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlRoleFactory.class);
		
	private String localeCode;
	private Role q;
	
	private XmlLangsFactory<L> xfLangs;
	private XmlDescriptionsFactory<D> xfDescriptions;
	
	private XmlViewsFactory<L,D,C,R,V,U> xfView;
	private XmlActionsFactory<L,D,C,R,V,U,A,AT> xfAction;
	private XmlUsecasesFactory<L,D,C,R,V,U,A,AT,USER> xfUsecase;
	
	public XmlRoleFactory(Role q){this(null,q);}
	public XmlRoleFactory(String localeCode, Role q)
	{
		this.localeCode=localeCode;
		this.q=q;
		
		if(Objects.nonNull(q.getLangs())) {xfLangs = new XmlLangsFactory<L>(q.getLangs());}
		if(Objects.nonNull(q.getDescriptions())) {xfDescriptions = new XmlDescriptionsFactory<D>(q.getDescriptions());}
		if(q.isSetViews()) {xfView = new XmlViewsFactory<>(q.getViews());}
		if(q.isSetActions()) {xfAction = new XmlActionsFactory<>(q.getActions());}
		if(q.isSetUsecases()) {xfUsecase = new XmlUsecasesFactory<L,D,C,R,V,U,A,AT,USER>(q.getUsecases());}
	}
	
	public static Role build() {return new Role();}
		
	public Role build(R role)
	{
		Role xml = new Role();
		if(q.isSetId()){xml.setId(role.getId());}
		if(Objects.nonNull(q.getCode())) {xml.setCode(role.getCode());}
		if(q.isSetPosition()){xml.setPosition(role.getPosition());}
		if(q.isSetVisible()){xml.setVisible(role.isVisible());}
		if(q.isSetDocumentation() && role.getDocumentation()!=null){xml.setDocumentation(role.getDocumentation());}
		
		if(Objects.nonNull(q.getLangs())){xml.setLangs(xfLangs.getUtilsLangs(role.getName()));}
		if(Objects.nonNull(q.getDescriptions())) {xml.setDescriptions(xfDescriptions.create(role.getDescription()));}
		
		if(Objects.nonNull(q.getViews())) {xml.setViews(xfView.build(role.getViews()));}
		if(Objects.nonNull(q.getActions())) {xml.setActions(xfAction.build(role.getActions()));}
		if(Objects.nonNull(q.getUsecases())) {xml.setUsecases(xfUsecase.build(role.getUsecases()));}
		
		if(Objects.nonNull(q.getLabel()) && localeCode!=null && role.getName().containsKey(localeCode))
		{
			xml.setLabel(role.getName().get(localeCode).getLang());
		}
			
		return xml;
	}
	
	public static Role create(String code, String label)
	{
		Role xml = new Role();
		xml.setCode(code);
		xml.setLabel(label);
		return xml;
	}
	
	public static Role label(String label)
	{
		Role xml = new Role();
		xml.setLabel(label);
		return xml;
	}
	
    public static org.jeesl.model.xml.system.security.Role build(String code)
    {
    	org.jeesl.model.xml.system.security.Role role = new org.jeesl.model.xml.system.security.Role();
    	role.setCode(code);
    	return role;
    }
}