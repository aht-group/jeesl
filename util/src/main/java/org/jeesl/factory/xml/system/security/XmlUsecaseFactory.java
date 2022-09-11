package org.jeesl.factory.xml.system.security;

import org.jeesl.factory.xml.system.lang.XmlDescriptionsFactory;
import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityView;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityTemplate;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.model.xml.system.security.Usecase;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityAction;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityUsecase;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityCategory;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlUsecaseFactory <L extends JeeslLang,
								D extends JeeslDescription, 
								C extends JeeslSecurityCategory<L,D>,
								R extends JeeslSecurityRole<L,D,C,V,U,A,USER>,
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
	

	public Usecase build(U usecase)
	{
		Usecase xml = new Usecase();
		if(q.isSetCode()){xml.setCode(usecase.getCode());}
		if(q.isSetPosition()){xml.setPosition(usecase.getPosition());}
		if(q.isSetVisible()){xml.setVisible(usecase.isVisible());}
		if(q.isSetDocumentation() && usecase.getDocumentation()!=null){xml.setDocumentation(usecase.getDocumentation());}
		
		if(q.isSetLangs())
		{
			XmlLangsFactory<L> f = new XmlLangsFactory<L>(q.getLangs());
			xml.setLangs(f.getUtilsLangs(usecase.getName()));
		}
		
		if(q.isSetDescriptions())
		{
			XmlDescriptionsFactory<D> f = new XmlDescriptionsFactory<D>(q.getDescriptions());
			xml.setDescriptions(f.create(usecase.getDescription()));
		}
		
		if(q.isSetViews())
		{
			XmlViewsFactory<L,D,C,R,V,U> f = new XmlViewsFactory<>(q.getViews());
			xml.setViews(f.build(usecase.getViews()));
		}
		
		if(q.isSetActions())
		{
			XmlActionsFactory<L,D,C,R,V,U,A,AT> f = new XmlActionsFactory<>(q.getActions());
			xml.setActions(f.build(usecase.getActions()));
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