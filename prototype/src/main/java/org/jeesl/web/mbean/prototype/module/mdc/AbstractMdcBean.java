package org.jeesl.web.mbean.prototype.module.mdc;

import java.io.Serializable;

import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.module.JeeslMdcFacade;
import org.jeesl.factory.builder.module.MdcFactoryBuilder;
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
import org.jeesl.web.mbean.prototype.system.AbstractAdminBean;
import org.openfuxml.content.ofx.Section;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractMdcBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
								R extends JeeslTenantRealm<L,D,R,?>, RREF extends EjbWithId,
								COLLECTION extends JeeslMdcCollection<R,SCOPE,STATUS,ASET>,
								SCOPE extends JeeslMdcScope<L,D,R,SCOPE,?>,
								STATUS extends JeeslMdcStatus<L,D,STATUS,?>,
								
								CDATA extends JeeslMdcData<COLLECTION,ACON>,
								
								ACRIT extends JeeslAttributeCriteria<L,D,R,?,?,?,ASET>,
								ASET extends JeeslAttributeSet<L,D,R,?,?>,
								AITEM extends JeeslAttributeItem<ACRIT,ASET>,
								ACON extends JeeslAttributeContainer<ASET,ADATA>,
								ADATA extends JeeslAttributeData<ACRIT,?,?>
								>
					extends AbstractAdminBean<L,D,LOC>
					implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractMdcBean.class);
	
	protected final MdcFactoryBuilder<L,D,LOC,R,COLLECTION,SCOPE,STATUS,CDATA,ASET,ACON> fbMdc;

	protected JeeslMdcFacade<L,D,R,COLLECTION,SCOPE,STATUS> fMdc;
	
	
	
	protected R realm;
	protected RREF rref;
	
	
	protected Section ofxUser; public Section getOfxUser() {return ofxUser;}
	
	public AbstractMdcBean(MdcFactoryBuilder<L,D,LOC,R,COLLECTION,SCOPE,STATUS,CDATA,ASET,ACON> fbMdc)
	{
		super(fbMdc.getClassL(),fbMdc.getClassD());
		this.fbMdc=fbMdc;
	}

	protected void postConstructMdc(JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage,
									JeeslMdcFacade<L,D,R,COLLECTION,SCOPE,STATUS> fMdc,
									R realm)
	{
		super.initJeeslAdmin(bTranslation,bMessage);
		this.fMdc=fMdc;
		this.realm=realm;
	}
	
	protected void updateRealmReference(RREF rref)
	{
		this.rref=rref;
	
		updatedRealmReference();
	}
	protected abstract void updatedRealmReference();
	

}