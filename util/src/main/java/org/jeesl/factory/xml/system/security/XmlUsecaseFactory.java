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
import org.jeesl.model.xml.system.security.Usecase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlUsecaseFactory <L extends JeeslLang,
								D extends JeeslDescription, 
								C extends JeeslSecurityCategory<L,D>,
								R extends JeeslSecurityRole<L,D,C,V,U,A>,
								V extends JeeslSecurityView<L,D,C,R,U,A>,
								U extends JeeslSecurityUsecase<L,D,C,R,V,A>,
								A extends JeeslSecurityAction<L,D,R,V,U,AT>,
								AT extends JeeslSecurityTemplate<L,D,C>,
								USER extends JeeslUser<R>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlUsecaseFactory.class);
		
	private Usecase q;
	
	public XmlUsecaseFactory(Usecase q)
	{
		this.q=q;
	}
	

	public Usecase build(U ejb)
	{
		Usecase xml = new Usecase();
		if(Objects.nonNull(q.getCode())) {xml.setCode(ejb.getCode());}
		if(Objects.nonNull(q.getPosition())) {xml.setPosition(ejb.getPosition());}
		if(q.isSetVisible()){xml.setVisible(ejb.isVisible());}
		if(q.isSetDocumentation() && ejb.getDocumentation()!=null){xml.setDocumentation(ejb.getDocumentation());}
		
		if(Objects.nonNull(q.getLangs()))
		{
			XmlLangsFactory<L> f = new XmlLangsFactory<L>(q.getLangs());
			xml.setLangs(f.getUtilsLangs(ejb.getName()));
		}
		
		if(Objects.nonNull(q.getDescriptions()))
		{
			XmlDescriptionsFactory<D> f = new XmlDescriptionsFactory<D>(q.getDescriptions());
			xml.setDescriptions(f.create(ejb.getDescription()));
		}
		
		if(Objects.nonNull(q.getViews()))
		{
			XmlViewsFactory<L,D,C,R,V,U> f = new XmlViewsFactory<>(q.getViews());
			xml.setViews(f.build(ejb.getViews()));
		}
		
		if(Objects.nonNull(q.getActions()))
		{
			XmlActionsFactory<L,D,C,R,V,U,A,AT> f = new XmlActionsFactory<>(q.getActions());
			xml.setActions(f.build(ejb.getActions()));
		}
		
		return xml;
	}
	
	public static Usecase build(String code)
	{
		Usecase xml = new Usecase();
		xml.setCode(code);
		return xml;
	}
}