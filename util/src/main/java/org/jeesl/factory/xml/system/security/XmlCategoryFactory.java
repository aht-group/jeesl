package org.jeesl.factory.xml.system.security;

import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
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
import org.jeesl.model.xml.system.security.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.aht.Query;

public class XmlCategoryFactory <L extends JeeslLang,D extends JeeslDescription,
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
	private org.jeesl.model.xml.system.security.Category q;
	
	public XmlCategoryFactory(Query q){this(q.getLang(),q.getCategory());}
	public XmlCategoryFactory(String localeCode, org.jeesl.model.xml.system.security.Category q)
	{
		this.localeCode=localeCode;
		this.q=q;
	}
	
	public org.jeesl.model.xml.system.security.Category build(C category)
	{
		Category xml = new Category();
		if(Objects.nonNull(q.getCode())) {xml.setCode(category.getCode());}
		if(q.isSetPosition()){xml.setPosition(category.getPosition());}
		if(q.isSetVisible()){xml.setVisible(category.isVisible());}
		if(q.isSetDocumentation() && category.getDocumentation()!=null){xml.setDocumentation(category.getDocumentation());}
		
		if(Objects.nonNull(q.getLangs()))
		{
			XmlLangsFactory<L> f = new XmlLangsFactory<L>(q.getLangs());
			xml.setLangs(f.getUtilsLangs(category.getName()));
		}
		
		if(Objects.nonNull(q.getDescriptions()))
		{
			XmlDescriptionsFactory<D> f = new XmlDescriptionsFactory<D>(q.getDescriptions());
			xml.setDescriptions(f.create(category.getDescription()));
		}
		
		if(ObjectUtils.allNotNull(q.getLabel(),localeCode))
		{
			if(category.getName()!=null)
			{
				if(category.getName().containsKey(localeCode)){xml.setLabel(category.getName().get(localeCode).getLang());}
				else
				{
					String msg = "No translation "+localeCode+" available in "+category;
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