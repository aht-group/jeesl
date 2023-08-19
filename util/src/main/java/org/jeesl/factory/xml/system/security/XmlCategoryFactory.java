package org.jeesl.factory.xml.system.security;

import org.jeesl.factory.xml.system.lang.XmlDescriptionsFactory;
import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.model.system.security.util.JeeslSecurityCategory;
import org.jeesl.model.xml.system.security.Category;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityUsecase;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityAction;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityTemplate;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.aht.Query;

public class XmlCategoryFactory <L extends JeeslLang,D extends JeeslDescription,
									C extends JeeslSecurityCategory<L,D>,
									R extends JeeslSecurityRole<L,D,C,V,U,A,USER>,
									V extends JeeslSecurityView<L,D,C,R,U,A>,
									U extends JeeslSecurityUsecase<L,D,C,R,V,A>,
									A extends JeeslSecurityAction<L,D,R,V,U,AT>,
									AT extends JeeslSecurityTemplate<L,D,C>,
									USER extends JeeslUser<R>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlRoleFactory.class);
		
	private String lang;
	private org.jeesl.model.xml.system.security.Category q;
	
	public XmlCategoryFactory(Query q){this(q.getLang(),q.getCategory());}
	public XmlCategoryFactory(String lang,org.jeesl.model.xml.system.security.Category q)
	{
		this.lang=lang;
		this.q=q;
	}
	
	public org.jeesl.model.xml.system.security.Category build(C category)
	{
		Category xml = new Category();
		if(q.isSetCode()){xml.setCode(category.getCode());}
		if(q.isSetPosition()){xml.setPosition(category.getPosition());}
		if(q.isSetVisible()){xml.setVisible(category.isVisible());}
		if(q.isSetDocumentation() && category.getDocumentation()!=null){xml.setDocumentation(category.getDocumentation());}
		
		if(q.isSetLangs())
		{
			XmlLangsFactory<L> f = new XmlLangsFactory<L>(q.getLangs());
			xml.setLangs(f.getUtilsLangs(category.getName()));
		}
		
		if(q.isSetDescriptions())
		{
			XmlDescriptionsFactory<D> f = new XmlDescriptionsFactory<D>(q.getDescriptions());
			xml.setDescriptions(f.create(category.getDescription()));
		}
		
		if(q.isSetLabel() && lang!=null)
		{
			if(category.getName()!=null)
			{
				if(category.getName().containsKey(lang)){xml.setLabel(category.getName().get(lang).getLang());}
				else
				{
					String msg = "No translation "+lang+" available in "+category;
					logger.warn(msg);
					xml.setLabel(msg);
				}
			}
			else
			{
				String msg = "No @name available in "+category;
				logger.warn(msg);
				xml.setLabel(msg);
			}
		}
		
		return xml;
	}
	
	public static org.jeesl.model.xml.system.security.Category build()
	{
		Category xml = new Category();
		
		return xml;
	}
}