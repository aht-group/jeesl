package org.jeesl.factory.ejb.module.mdc;

import org.jeesl.factory.builder.module.MdcFactoryBuilder;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeSet;
import org.jeesl.interfaces.model.module.mdc.collection.JeeslMdcCollection;
import org.jeesl.interfaces.model.module.mdc.collection.JeeslMdcScope;
import org.jeesl.interfaces.model.module.mdc.collection.JeeslMdcStatus;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbMdcCollectionFactory<R extends JeeslTenantRealm<?,?,R,?>,
									COLLECTION extends JeeslMdcCollection<R,SCOPE,STATUS,AS>,
									SCOPE extends JeeslMdcScope<?,?,R,SCOPE,?>,
									STATUS extends JeeslMdcStatus<?,?,STATUS,?>,
									AS extends JeeslAttributeSet<?,?,R,?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbMdcCollectionFactory.class);

	private final MdcFactoryBuilder<?,?,?,R,COLLECTION,SCOPE,STATUS,?,AS,?> fbMdc;

	public EjbMdcCollectionFactory(MdcFactoryBuilder<?,?,?,R,COLLECTION,SCOPE,STATUS,?,AS,?> fbMdc)
	{
		this.fbMdc = fbMdc;
	}

	public <RREF extends EjbWithId> COLLECTION build(R realm, RREF rref)
	{
		COLLECTION ejb = null;
		try
		{
			ejb = fbMdc.getClassActivity().newInstance();
			ejb.setRealm(realm);
			ejb.setRref(rref.getId());
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}

		return ejb;
	}
	
	public  void converter(JeeslFacade facade, COLLECTION ejb)
	{
		if(ejb.getScope()!=null) {ejb.setScope(facade.find(fbMdc.getClassScope(),ejb.getScope()));}
		if(ejb.getStatus()!=null) {ejb.setStatus(facade.find(fbMdc.getClassStatus(),ejb.getStatus()));}
		if(ejb.getCollectionSet()!=null) {ejb.setCollectionSet(facade.find(fbMdc.getClassAttributeSet(),ejb.getCollectionSet()));}
	}
}