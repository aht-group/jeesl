package org.jeesl.web.mbean.prototype.module.mdc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.module.JeeslMdcFacade;
import org.jeesl.factory.builder.io.IoAttributeFactoryBuilder;
import org.jeesl.factory.builder.module.MdcFactoryBuilder;
import org.jeesl.factory.ejb.module.mdc.EjbMdcDataFactory;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeContainer;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeCriteria;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeData;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeItem;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeSet;
import org.jeesl.interfaces.model.module.mdc.collection.JeeslMdcCollection;
import org.jeesl.interfaces.model.module.mdc.collection.JeeslMdcScope;
import org.jeesl.interfaces.model.module.mdc.collection.JeeslMdcStatus;
import org.jeesl.interfaces.model.module.mdc.data.JeeslMdcData;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.pojo.map.generic.Nested2Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractMdcDataBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
								R extends JeeslTenantRealm<L,D,R,?>, RREF extends EjbWithId,
								COLLECTION extends JeeslMdcCollection<R,SCOPE,STATUS,ASET>,
								SCOPE extends JeeslMdcScope<L,D,R,SCOPE,?>,
								STATUS extends JeeslMdcStatus<L,D,STATUS,?>,

								CDATA extends JeeslMdcData<COLLECTION,ACON>,

								ACRIT extends JeeslAttributeCriteria<L,D,R,?,?,?,ASET>,
								ASET extends JeeslAttributeSet<L,D,R,?,AITEM>,
								AITEM extends JeeslAttributeItem<ACRIT,ASET>,
								ACON extends JeeslAttributeContainer<ASET,ADATA>,
								ADATA extends JeeslAttributeData<ACRIT,?,ACON>
								>
					extends AbstractMdcBean<L,D,LOC,R,RREF,COLLECTION,SCOPE,STATUS,CDATA,ACRIT,ASET,AITEM,ACON,ADATA>
					implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractMdcDataBean.class);

	private final IoAttributeFactoryBuilder<L,D,R,?,?,?,?,ASET,AITEM,ACON,ADATA> fbAttribute;

	private final EjbMdcDataFactory<COLLECTION,CDATA,ACON> efData;

	private final Nested2Map<ACON,ACRIT,ADATA> n2Data; public Nested2Map<ACON, ACRIT, ADATA> getN2Data() {return n2Data;}

	private final List<COLLECTION> collections; public List<COLLECTION> getCollections() {return collections;}
	private final List<CDATA> datas; public List<CDATA> getDatas() {return datas;}
	private final List<AITEM> items; public List<AITEM> getItems() {return items;}

	private COLLECTION collection; public COLLECTION getCollection() {return collection;} public void setCollection(COLLECTION collection) {this.collection = collection;}
	private CDATA data; public CDATA getData() {return data;} public void setData(CDATA data) {this.data = data;}

	public AbstractMdcDataBean(MdcFactoryBuilder<L,D,LOC,R,COLLECTION,SCOPE,STATUS,CDATA,ASET,ACON> fbMdc,
								IoAttributeFactoryBuilder<L,D,R,?,?,?,?,ASET,AITEM,ACON,ADATA> fbAttribute)
	{
		super(fbMdc);
		this.fbAttribute=fbAttribute;

		efData = fbMdc.ejbData();

		n2Data = new Nested2Map<>();

		collections = new ArrayList<>();
		datas = new ArrayList<>();
		items = new ArrayList<>();
	}

	protected void postConstructMdcData(JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage,
											JeeslMdcFacade<L,D,R,COLLECTION,SCOPE,STATUS> fMdc,
											R realm)
	{
		super.postConstructMdc(bTranslation,bMessage,fMdc,realm);
	}

	@Override protected void updatedRealmReference()
	{
		reload();
	}

	protected void reload()
	{
		collections.clear();
		collections.addAll(fMdc.all(fbMdc.getClassActivity(),realm,rref));
		if(!collections.isEmpty())
		{
			collection = collections.get(0);
			reloadCollection();
		}
	}

	protected void reloadCollection()
	{
		datas.clear(); datas.addAll(fMdc.allForParent(fbMdc.getClassData(),collection));
		items.clear(); items.addAll(fMdc.allForParent(fbAttribute.getClassItem(),collection.getCollectionSet()));

		List<ACON> containers = efData.toCollectionContainer(datas);
		n2Data.clear();
		for(ADATA d : fMdc.allForParents(fbAttribute.getClassData(), containers))
		{
			n2Data.put(d.getContainer(),d.getCriteria(),d);
		}
	}
}