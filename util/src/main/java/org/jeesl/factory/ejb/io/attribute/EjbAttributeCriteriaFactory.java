package org.jeesl.factory.ejb.io.attribute;

import java.util.Objects;

import org.jeesl.factory.builder.io.IoAttributeFactoryBuilder;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeCategory;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeCriteria;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeSet;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbAttributeCriteriaFactory<L extends JeeslLang, D extends JeeslDescription,
										R extends JeeslTenantRealm<L,D,R,?>,
										CAT extends JeeslAttributeCategory<L,D,R,CAT,?>,
										CRITERIA extends JeeslAttributeCriteria<L,D,R,CAT,TYPE,?,SET>,
										TYPE extends JeeslAttributeType<L,D,TYPE,?>,
										SET extends JeeslAttributeSet<L,D,R,CAT,?>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbAttributeCriteriaFactory.class);
	
	private final IoAttributeFactoryBuilder<L,D,R,CAT,CRITERIA,TYPE,?,SET,?,?,?> fbAttribute;
    
	public EjbAttributeCriteriaFactory(IoAttributeFactoryBuilder<L,D,R,CAT,CRITERIA,TYPE,?,SET,?,?,?> fbAttribute)
	{       
        this.fbAttribute = fbAttribute;
	}
	
	public <RREF extends EjbWithId> CRITERIA build(R realm, RREF rref, CAT category, TYPE type)
	{
		CRITERIA ejb = null;
		try
		{
			ejb = fbAttribute.getClassCriteria().newInstance();
			ejb.setRealm(realm);
			ejb.setRref(rref.getId());
			ejb.setCategory(category);
			ejb.setType(type);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
	
	public void converter(JeeslFacade facade, CRITERIA ejb)
	{
		if(Objects.nonNull(ejb.getCategory())) {ejb.setCategory(facade.find(fbAttribute.getClassCat(),ejb.getCategory()));}
		if(Objects.nonNull(ejb.getType())) {ejb.setType(facade.find(fbAttribute.getClassType(),ejb.getType()));}
		if(Objects.nonNull(ejb.getNested())) {ejb.setNested(facade.find(fbAttribute.getClassSet(),ejb.getNested()));}
	}
}