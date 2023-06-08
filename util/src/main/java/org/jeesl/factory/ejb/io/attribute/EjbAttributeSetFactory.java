package org.jeesl.factory.ejb.io.attribute;

import org.jeesl.factory.builder.io.IoAttributeFactoryBuilder;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeCategory;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeItem;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeSet;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbAttributeSetFactory<L extends JeeslLang, D extends JeeslDescription,
									R extends JeeslTenantRealm<L,D,R,?>,
									CAT extends JeeslAttributeCategory<L,D,R,CAT,?>,
									
									SET extends JeeslAttributeSet<L,D,R,CAT,ITEM>,
									ITEM extends JeeslAttributeItem<?,SET>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbAttributeSetFactory.class);
	
	private final IoAttributeFactoryBuilder<L,D,R,CAT,?,?,?,SET,ITEM,?,?> fbAttribute;
    
	public EjbAttributeSetFactory(IoAttributeFactoryBuilder<L,D,R,CAT,?,?,?,SET,ITEM,?,?> fbAttribute)
	{       
        this.fbAttribute = fbAttribute;
	}
	
	public <RREF extends EjbWithId> SET build(R realm, RREF rref, CAT category)
	{
		SET ejb = null;
		try
		{
			ejb = fbAttribute.getClassSet().newInstance();
			ejb.setRealm(realm);
			ejb.setRref(rref.getId());
			ejb.setCategory2(category);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
	
//	public SET build1(CATEGORY category, Long refId)
//	{
//		SET ejb = null;
//		try
//		{
//			ejb = fbAttribute.getClassSet().newInstance();
//			ejb.setRefId(refId);
//			ejb.setCategory(category);
//		}
//		catch (InstantiationException e) {e.printStackTrace();}
//		catch (IllegalAccessException e) {e.printStackTrace();}
//		
//		return ejb;
//	}
//	public SET clone1(SET source, String codeSuffix)
//	{
//		SET clone = this.build1(source.getCategory(),source.getRefId());
//		clone.setCode(source.getCode()+codeSuffix);
//		return clone;
//	}
	
	public void converter(JeeslFacade facade, SET ejb)
	{
//		if(ejb.getCategory()!=null) {ejb.setCategory(facade.find(fbAttribute.getClassCategory(),ejb.getCategory()));}
		if(ejb.getCategory2()!=null) {ejb.setCategory2(facade.find(fbAttribute.getClassCat(),ejb.getCategory2()));}
	}
}