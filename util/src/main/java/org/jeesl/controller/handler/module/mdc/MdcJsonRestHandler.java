package org.jeesl.controller.handler.module.mdc;

import org.jeesl.api.facade.io.JeeslIoAttributeFacade;
import org.jeesl.api.rest.rs.module.mdc.JeeslMdcRestInterface;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.io.IoAttributeFactoryBuilder;
import org.jeesl.factory.builder.module.MdcFactoryBuilder;
import org.jeesl.factory.ejb.io.attribute.EjbAttributeContainerFactory;
import org.jeesl.factory.ejb.io.attribute.EjbAttributeDataFactory;
import org.jeesl.factory.ejb.module.mdc.EjbMdcDataFactory;
import org.jeesl.factory.json.module.attribute.JsonAttributeContainerFactory;
import org.jeesl.factory.json.module.attribute.JsonAttributeSetFactory;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeCategory;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeContainer;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeCriteria;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeData;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeItem;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeOption;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeSet;
import org.jeesl.interfaces.model.module.mdc.collection.JeeslMdcCollection;
import org.jeesl.interfaces.model.module.mdc.collection.JeeslMdcScope;
import org.jeesl.interfaces.model.module.mdc.collection.JeeslMdcStatus;
import org.jeesl.interfaces.model.module.mdc.data.JeeslMdcData;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.model.json.module.attribute.JsonAttributeData;
import org.jeesl.model.json.module.attribute.JsonAttributeSet;
import org.jeesl.model.json.module.mdc.JsonMdcCollection;
import org.jeesl.model.json.module.mdc.JsonMdcContainer;
import org.jeesl.model.json.module.mdc.JsonMdcData;
import org.jeesl.model.json.module.mdc.JsonMdcEnrolment;
import org.jeesl.util.db.cache.EjbIdCache;
import org.jeesl.util.query.json.JsonAttributeQueryProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MdcJsonRestHandler<L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
								R extends JeeslTenantRealm<L,D,R,?>,
								COLLECTION extends JeeslMdcCollection<?,SCOPE,STATUS,ASET>,

								SCOPE extends JeeslMdcScope<L,D,?,SCOPE,?>,
								STATUS extends JeeslMdcStatus<L,D,STATUS,?>,
								
								CDATA extends JeeslMdcData<COLLECTION,ACON>,
								
								CAT extends JeeslAttributeCategory<L,D,R,CAT,?>,
								CATEGORY extends JeeslStatus<L,D,CATEGORY>,
								CRITERIA extends JeeslAttributeCriteria<L,D,R,CAT,TYPE,OPTION>,
								TYPE extends JeeslStatus<L,D,TYPE>,
								OPTION extends JeeslAttributeOption<L,D,CRITERIA>,
								ASET extends JeeslAttributeSet<L,D,R,CAT,AITEM>,
								AITEM extends JeeslAttributeItem<CRITERIA,ASET>,
								ACON extends JeeslAttributeContainer<ASET,ADATA>,
								ADATA extends JeeslAttributeData<CRITERIA,OPTION,ACON>>
		implements JeeslMdcRestInterface
{
	final static Logger logger = LoggerFactory.getLogger(MdcJsonRestHandler.class);
	
	private final JeeslIoAttributeFacade<L,D,R,CAT,CRITERIA,TYPE,OPTION,ASET,AITEM,ACON,ADATA> fAttribute;
	
	private final MdcFactoryBuilder<L,D,LOC,?,COLLECTION,SCOPE,STATUS,CDATA,ASET,ACON> fbMdc;
	private final IoAttributeFactoryBuilder<L,D,R,CAT,CRITERIA,TYPE,OPTION,ASET,AITEM,ACON,ADATA> fbAttribute;
	
	private final EjbMdcDataFactory<COLLECTION,CDATA,ACON> efCollectionData;
	private final EjbAttributeContainerFactory<ASET,ACON> efAttributeContainer;
	private final EjbAttributeDataFactory<CRITERIA,OPTION,ACON,ADATA> efAttributeData;
	
	private final JsonAttributeSetFactory<L,D,LOC,R,CAT,CATEGORY,CRITERIA,TYPE,OPTION,ASET,AITEM> jfAttributeSet;
	private final JsonAttributeContainerFactory<L,D,LOC,R,CAT,CATEGORY,CRITERIA,TYPE,OPTION,ASET,AITEM,ACON,ADATA> jfAttributeContainer;
	
	
	public MdcJsonRestHandler(MdcFactoryBuilder<L,D,LOC,?,COLLECTION,SCOPE,STATUS,CDATA,ASET,ACON> fbMdc,
								IoAttributeFactoryBuilder<L,D,R,CAT,CRITERIA,TYPE,OPTION,ASET,AITEM,ACON,ADATA> fbAttribute,
								JeeslIoAttributeFacade<L,D,R,CAT,CRITERIA,TYPE,OPTION,ASET,AITEM,ACON,ADATA> fAttribute,
								String localeCode)
	{
		this.fbMdc=fbMdc;
		this.fbAttribute=fbAttribute;
		this.fAttribute=fAttribute;
		
		jfAttributeSet = new JsonAttributeSetFactory<>(localeCode,JsonAttributeQueryProvider.set());					jfAttributeSet.lazy(fAttribute,fbAttribute);
		jfAttributeContainer = new JsonAttributeContainerFactory<>(localeCode,JsonAttributeQueryProvider.container());	jfAttributeContainer.lazy(fAttribute,fbAttribute);
		
		efCollectionData = fbMdc.ejbData();
		efAttributeContainer = fbAttribute.ejbContainer();
		efAttributeData = fbAttribute.ejbData();
	}
	
	public JsonMdcContainer enrolment(String token)
	{
		JsonMdcContainer json = new JsonMdcContainer();
		JsonMdcEnrolment enrolment = new JsonMdcEnrolment();
		enrolment.setToken(token);
		json.setEnrolment(enrolment);
		
		COLLECTION eCollection = fAttribute.all(fbMdc.getClassActivity(),1).get(0);
		JsonMdcCollection jCollection = new JsonMdcCollection();
		jCollection.setId(eCollection.getId());
		jCollection.setName(eCollection.getName());
		
		jCollection.setValidFrom(eCollection.getValidFrom());
		jCollection.setValidUntil(eCollection.getValidUntil());
//		jCollection.setTest3(new Date());
		
		json.setCollection(jCollection);
		
		return json;
	}
	public JsonMdcContainer download(Long id)
	{
		JsonMdcContainer json = new JsonMdcContainer();
		
		COLLECTION eCollection = fAttribute.all(fbMdc.getClassActivity(),1).get(0);
		JsonMdcCollection jCollection = new JsonMdcCollection();
		jCollection.setName(eCollection.getName());
		jCollection.setCollectionSet(jfAttributeSet.build(eCollection.getCollectionSet()));
		json.setCollection(jCollection);
		
		return json;
	}
	
	public JsonAttributeSet attriubteSetHousehold(ASET set)
	{
		return jfAttributeSet.build(set);
	}

	public JsonMdcCollection collection(COLLECTION collection)
	{
		JsonMdcCollection json = new JsonMdcCollection();
		json.setName(collection.getName());
		json.setCollectionSet(jfAttributeSet.build(collection.getCollectionSet()));
		
		return json;
	}
	
	public JsonMdcData upload(COLLECTION collection, JsonMdcData data) throws JeeslConstraintViolationException, JeeslLockingException
	{
		EjbIdCache<CRITERIA> cacheCriteria = new EjbIdCache<>(fbAttribute.getClassCriteria());
		cacheCriteria.populate(fAttribute.fAttributeCriteria(collection.getCollectionSet()));
		
		EjbIdCache<OPTION> cacheOption = new EjbIdCache<>(fbAttribute.getClassOption(),fAttribute);
		
		ACON aContainer = fAttribute.save(efAttributeContainer.build(collection.getCollectionSet()));
		for(JsonAttributeData jData : data.getCollectionContainer().getDatas())
		{
			if(cacheCriteria.contains(jData.getCriteria().getId()))
			{
				CRITERIA aCriteria = cacheCriteria.ejb(jData.getCriteria().getId());
				ADATA d = efAttributeData.build(aContainer,aCriteria,jData,cacheOption);
				fAttribute.save(d);
			}
		}
		
		CDATA cData = efCollectionData.build(collection);
		cData.setCollectionContainer(aContainer);
		fAttribute.save(cData);
		
		JsonMdcData result = new JsonMdcData();
		return result;
	}

	@Override public JsonMdcData upload(JsonMdcData data)
	{
		logger.warn("NYI");
		return null;
	}
}