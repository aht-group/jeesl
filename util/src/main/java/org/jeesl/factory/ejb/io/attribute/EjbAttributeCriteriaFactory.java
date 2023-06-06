package org.jeesl.factory.ejb.io.attribute;

import org.jeesl.factory.builder.io.IoAttributeFactoryBuilder;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeCategory;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeCriteria;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbAttributeCriteriaFactory<L extends JeeslLang, D extends JeeslDescription,
										R extends JeeslTenantRealm<L,D,R,?>,
										CAT extends JeeslAttributeCategory<L,D,R,CAT,?>,
										CATEGORY extends JeeslStatus<L,D,CATEGORY>,
										CRITERIA extends JeeslAttributeCriteria<L,D,R,CAT,CATEGORY,TYPE,?>,
										TYPE extends JeeslStatus<L,D,TYPE>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbAttributeCriteriaFactory.class);
	
	private final IoAttributeFactoryBuilder<L,D,R,CAT,CATEGORY,CRITERIA,TYPE,?,?,?,?,?> fbAttribute;
    
	public EjbAttributeCriteriaFactory(IoAttributeFactoryBuilder<L,D,R,CAT,CATEGORY,CRITERIA,TYPE,?,?,?,?,?> fbAttribute)
	{       
        this.fbAttribute = fbAttribute;
	}
    
	@Deprecated
	public CRITERIA build(CATEGORY category, TYPE type, long refId)
	{
		CRITERIA ejb = null;
		try
		{
			ejb = fbAttribute.getClassCriteria().newInstance();
			ejb.setRefId(refId);
			ejb.setCategory(category);
			ejb.setType(type);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
	
	public <RREF extends EjbWithId> CRITERIA build(R realm, RREF rref, CAT category, TYPE type)
	{
		CRITERIA ejb = null;
		try
		{
			ejb = fbAttribute.getClassCriteria().newInstance();
			ejb.setRealm(realm);
			ejb.setRref(rref.getId());
			ejb.setCategory2(category);
			ejb.setType(type);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
	
	public void converter(JeeslFacade facade, CRITERIA ejb)
	{
//		if(ejb.getCategory()!=null) {ejb.setCategory(facade.find(fbAttribute.getClassCategory(),ejb.getCategory()));}
		if(ejb.getCategory2()!=null) {ejb.setCategory2(facade.find(fbAttribute.getClassCat(),ejb.getCategory2()));}
		if(ejb.getType()!=null) {ejb.setType(facade.find(fbAttribute.getClassType(),ejb.getType()));}
	}
	
}