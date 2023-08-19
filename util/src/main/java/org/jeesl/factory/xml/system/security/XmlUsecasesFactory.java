package org.jeesl.factory.xml.system.security;

import java.util.List;

import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.model.system.security.util.JeeslSecurityCategory;
import org.jeesl.model.xml.system.security.Usecases;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityUsecase;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityAction;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityTemplate;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlUsecasesFactory <L extends JeeslLang,
								D extends JeeslDescription, 
								C extends JeeslSecurityCategory<L,D>,
								R extends JeeslSecurityRole<L,D,C,V,U,A,USER>,
								V extends JeeslSecurityView<L,D,C,R,U,A>,
								U extends JeeslSecurityUsecase<L,D,C,R,V,A>,
								A extends JeeslSecurityAction<L,D,R,V,U,AT>,
								AT extends JeeslSecurityTemplate<L,D,C>,
								USER extends JeeslUser<R>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlUsecasesFactory.class);
		
	private Usecases q;
	
	public XmlUsecasesFactory(Usecases q)
	{
		this.q=q;
	}
	

	public  Usecases build(List<U> usecases)
	{
		XmlUsecaseFactory<L,D,C,R,V,U,A,AT,USER> f = new XmlUsecaseFactory<L,D,C,R,V,U,A,AT,USER>(q.getUsecase().get(0));
		
		Usecases xml = build();
		for(U usecase : usecases)
		{
			xml.getUsecase().add(f.build(usecase));
		}
		return xml;
	}
	
	public static Usecases build()
	{
		return new Usecases();
	}
}