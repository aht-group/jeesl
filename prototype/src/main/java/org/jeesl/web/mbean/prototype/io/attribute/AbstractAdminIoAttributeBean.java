package org.jeesl.web.mbean.prototype.io.attribute;

import java.io.Serializable;
import java.util.Comparator;

import org.jeesl.api.bean.JeeslAttributeBean;
import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoAttributeFacade;
import org.jeesl.factory.builder.io.IoAttributeFactoryBuilder;
import org.jeesl.factory.ejb.io.attribute.EjbAttributeCriteriaFactory;
import org.jeesl.factory.ejb.io.attribute.EjbAttributeItemFactory;
import org.jeesl.factory.ejb.io.attribute.EjbAttributeOptionFactory;
import org.jeesl.factory.ejb.io.attribute.EjbAttributeSetFactory;
import org.jeesl.interfaces.bean.sb.bean.SbToggleBean;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeCategory;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeContainer;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeCriteria;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeData;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeItem;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeOption;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeSet;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.jsf.handler.sb.SbMultiHandler;
import org.jeesl.util.comparator.ejb.system.io.attribute.AttributeCriteriaComparator;
import org.jeesl.web.mbean.prototype.system.AbstractAdminBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractAdminIoAttributeBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
													R extends JeeslTenantRealm<L,D,R,?>, RREF extends EjbWithId,
													CAT extends JeeslAttributeCategory<L,D,R,CAT,?>,
													CATEGORY extends JeeslStatus<L,D,CATEGORY>,
													CRITERIA extends JeeslAttributeCriteria<L,D,R,CAT,CATEGORY,TYPE,OPTION>,
													TYPE extends JeeslStatus<L,D,TYPE>,
													OPTION extends JeeslAttributeOption<L,D,CRITERIA>,
													SET extends JeeslAttributeSet<L,D,R,CAT,CATEGORY,ITEM>,
													ITEM extends JeeslAttributeItem<CRITERIA,SET>,
													CONTAINER extends JeeslAttributeContainer<SET,DATA>,
													DATA extends JeeslAttributeData<CRITERIA,OPTION,CONTAINER>>
					extends AbstractAdminBean<L,D,LOC>
					implements Serializable,SbToggleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractAdminIoAttributeBean.class);
	
	protected JeeslIoAttributeFacade<L,D,R,CAT,CATEGORY,CRITERIA,TYPE,OPTION,SET,ITEM,CONTAINER,DATA> fAttribute;
	protected JeeslAttributeBean<L,D,R,CAT,CATEGORY,CRITERIA,TYPE,OPTION,SET,ITEM,CONTAINER,DATA> bAttribute;
	
	protected final IoAttributeFactoryBuilder<L,D,R,CAT,CATEGORY,CRITERIA,TYPE,OPTION,SET,ITEM,CONTAINER,DATA> fbAttribute;
	
	protected final SbMultiHandler<CAT> sbhCat; public SbMultiHandler<CAT> getSbhCat() {return sbhCat;}
	
	protected final EjbAttributeCriteriaFactory<L,D,R,CAT,CATEGORY,CRITERIA,TYPE> efCriteria;
	protected final EjbAttributeOptionFactory<CRITERIA,OPTION> efOption;
	protected final EjbAttributeSetFactory<L,D,R,CAT,CATEGORY,SET,ITEM> efSet;
	protected final EjbAttributeItemFactory<CRITERIA,SET,ITEM> efItem;
	
	protected final Comparator<CRITERIA> cpCriteria;
	protected R realm;
	protected RREF rref;
	protected long refId;

	public AbstractAdminIoAttributeBean(IoAttributeFactoryBuilder<L,D,R,CAT,CATEGORY,CRITERIA,TYPE,OPTION,SET,ITEM,CONTAINER,DATA> fbAttribute)
	{
		super(fbAttribute.getClassL(),fbAttribute.getClassD());
		this.fbAttribute=fbAttribute;
		refId = 0;
		
		efCriteria = fbAttribute.ejbCriteria();
		efOption = fbAttribute.ejbOption();
		efSet = fbAttribute.ejbSet();
		efItem = fbAttribute.ejbItem();
		
		cpCriteria = fbAttribute.cpCriteria(AttributeCriteriaComparator.Type.position);
		
//		sbhCategory = new SbMultiHandler<CATEGORY>(fbAttribute.getClassCategory(),this);
		sbhCat = new SbMultiHandler<CAT>(fbAttribute.getClassCat(),this);
	}
	
	protected void postConstructAttribute(R realm,
			JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage,
			JeeslAttributeBean<L,D,R,CAT,CATEGORY,CRITERIA,TYPE,OPTION,SET,ITEM,CONTAINER,DATA> bAttribute,
			JeeslIoAttributeFacade<L,D,R,CAT,CATEGORY,CRITERIA,TYPE,OPTION,SET,ITEM,CONTAINER,DATA> fAttribute)
	{
		super.initJeeslAdmin(bTranslation,bMessage);
		this.realm=realm;
		this.fAttribute=fAttribute;
		this.bAttribute=bAttribute;
	}
	
	protected void initAttribute(JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage,
									JeeslAttributeBean<L,D,R,CAT,CATEGORY,CRITERIA,TYPE,OPTION,SET,ITEM,CONTAINER,DATA> bAttribute,
									JeeslIoAttributeFacade<L,D,R,CAT,CATEGORY,CRITERIA,TYPE,OPTION,SET,ITEM,CONTAINER,DATA> fAttribute)
	{
		super.initJeeslAdmin(bTranslation,bMessage);
		this.fAttribute=fAttribute;
		this.bAttribute=bAttribute;
		reloadCategories();
	}
	
	protected abstract void reloadCategories();
}