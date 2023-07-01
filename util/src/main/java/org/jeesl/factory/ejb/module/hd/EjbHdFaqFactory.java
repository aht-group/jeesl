package org.jeesl.factory.ejb.module.hd;

import org.jeesl.factory.builder.module.HdFactoryBuilder;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.module.hd.JeeslHdCategory;
import org.jeesl.interfaces.model.module.hd.resolution.JeeslHdFaq;
import org.jeesl.interfaces.model.module.hd.resolution.JeeslHdScope;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbHdFaqFactory<R extends JeeslTenantRealm<?,?,R,?>,
							CAT extends JeeslHdCategory<?,?,R,CAT,?>,
							FAQ extends JeeslHdFaq<?,?,R,CAT,SCOPE>,
							SCOPE extends JeeslHdScope<?,?,SCOPE,?>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbHdFaqFactory.class);
	
	private final HdFactoryBuilder<?,?,?,R,?,CAT,?,?,?,?,?,?,?,?,FAQ,SCOPE,?,?,?,?,?> fbHd;

    public EjbHdFaqFactory(HdFactoryBuilder<?,?,?,R,?,CAT,?,?,?,?,?,?,?,?,FAQ,SCOPE,?,?,?,?,?> fbHd)
    {
        this.fbHd = fbHd;
    }
	
	public <RREF extends EjbWithId> FAQ build(R realm, RREF rref, CAT category, SCOPE scope)
	{
		try
		{
			FAQ ejb = fbHd.getClassFaq().newInstance();
			ejb.setRealm(realm);
			ejb.setRref(rref.getId());
			ejb.setCategory(category);
			ejb.setScope(scope);
		    return ejb;
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		return null;
    }
	
	public void converter(JeeslFacade facade, FAQ faq)
	{
		if(faq.getCategory()!=null) {faq.setCategory(facade.find(fbHd.getClassCategory(),faq.getCategory()));}
		if(faq.getScope()!=null) {faq.setScope(facade.find(fbHd.getClassScope(),faq.getScope()));}
	}
}