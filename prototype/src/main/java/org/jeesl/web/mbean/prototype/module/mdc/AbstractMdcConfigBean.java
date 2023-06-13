package org.jeesl.web.mbean.prototype.module.mdc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.module.JeeslMdcFacade;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.module.MdcFactoryBuilder;
import org.jeesl.factory.ejb.module.mdc.EjbMdcCollectionFactory;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public abstract class AbstractMdcConfigBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
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
	final static Logger logger = LoggerFactory.getLogger(AbstractMdcConfigBean.class);

	private final EjbMdcCollectionFactory<R,COLLECTION,SCOPE,STATUS,ASET> efActivity;
	
	private final List<COLLECTION> activities; public List<COLLECTION> getActivities() {return activities;}
	private final List<SCOPE> scopes; public List<SCOPE> getScopes() {return scopes;}
	private final List<STATUS> status; public List<STATUS> getStatus() {return status;}
	private final List<ASET> attributes; public List<ASET> getAttributes() {return attributes;}
	
	private COLLECTION activity; public COLLECTION getActivity() {return activity;} public void setActivity(COLLECTION activity) {this.activity = activity;}

	public AbstractMdcConfigBean(MdcFactoryBuilder<L,D,LOC,R,COLLECTION,SCOPE,STATUS,CDATA,ASET,ACON> fbMdc)
	{
		super(fbMdc);
		
		efActivity = fbMdc.ejbActivity();
		
		activities = new ArrayList<>();
		scopes = new ArrayList<>();
		status = new ArrayList<>();
		attributes = new ArrayList<>();
	}

	protected void postConstructMdcConfig(JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage,
											JeeslMdcFacade<L,D,R,COLLECTION,SCOPE,STATUS> fMdc,
											R realm)
	{
		super.postConstructMdc(bTranslation,bMessage,fMdc,realm);
		status.addAll(fMdc.allOrderedPositionVisible(fbMdc.getClassStatus()));
	}

	@Override protected void updatedRealmReference()
	{
		scopes.clear();scopes.addAll(fMdc.all(fbMdc.getClassScope(),realm,rref));
		attributes.clear();attributes.addAll(fMdc.all(fbMdc.getClassAttributeSet()));
		reload();
	}

	private void reload()
	{
		activities.clear();
		activities.addAll(fMdc.all(fbMdc.getClassActivity(),realm,rref));
	}
	
	public void selectActivity()
	{
		logger.info(AbstractLogMessage.selectEntity(activity));
	}
	
	public void addActivity()
	{
		logger.info(AbstractLogMessage.createEntity(fbMdc.getClassActivity()));
		activity = efActivity.build(realm,rref);
	}
	
	public void saveActivity() throws JeeslConstraintViolationException, JeeslLockingException
	{
		logger.info(AbstractLogMessage.saveEntity(activity));
		efActivity.converter(fMdc,activity);
		activity = fMdc.save(activity);
		reload();
	}
}