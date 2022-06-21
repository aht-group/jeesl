package org.jeesl.web.mbean.prototype.module.news;

import java.io.Serializable;
import java.util.List;

import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.module.JeeslNewsFacade;
import org.jeesl.controller.handler.sb.SbSingleHandler;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.module.NewsFactoryBuilder;
import org.jeesl.interfaces.bean.sb.SbSingleBean;
import org.jeesl.interfaces.model.module.news.JeeslNewsCategory;
import org.jeesl.interfaces.model.module.news.JeeslNewsItem;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.web.mbean.prototype.system.AbstractAdminBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractNewsItemBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
									R extends JeeslTenantRealm<L,D,R,?>, RREF extends EjbWithId,
									CATEGORY extends JeeslNewsCategory<L,D,R,CATEGORY,?>,
									ITEM extends JeeslNewsItem<L,D,R,CATEGORY,USER>,
									USER extends EjbWithId>
					extends AbstractAdminBean<L,D,LOC>
					implements Serializable,SbSingleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractNewsItemBean.class);
	
	protected JeeslNewsFacade<L,D,R,CATEGORY,ITEM,USER> fNews;
	private final NewsFactoryBuilder<L,D,R,CATEGORY,ITEM,USER> fbNews;
	
	protected final SbSingleHandler<CATEGORY> sbhCategory; public SbSingleHandler<CATEGORY> getSbhCategory() {return sbhCategory;}
	
	
	private List<ITEM> items; public List<ITEM> getItems() {return items;}
	
	protected R realm;
	protected RREF rref;
	private ITEM item; public ITEM getItem() {return item;} public void setItem(ITEM item) {this.item = item;}

	public AbstractNewsItemBean(NewsFactoryBuilder<L,D,R,CATEGORY,ITEM,USER> fbNews)
	{
		super(fbNews.getClassL(),fbNews.getClassD());
		this.fbNews=fbNews;
		
		sbhCategory = new SbSingleHandler<>(fbNews.getClassCategory(),this);
	}

	protected void postConstructNews(JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage,
										JeeslNewsFacade<L,D,R,CATEGORY,ITEM,USER> fNews,
										R realm,
										USER reporter)
	{
		super.initJeeslAdmin(bTranslation,bMessage);
		this.fNews=fNews;
		this.realm=realm;
//		categories = fNews.allOrderedPositionVisible(fbNews.getClassCategory());
//		active = new HashMap<NEWS,Boolean>();
	}
	
	protected void updateRealmReference(RREF rref)
	{
		this.rref=rref;
		
		sbhCategory.clear();
		sbhCategory.setList(fNews.all(fbNews.getClassCategory(),realm,rref));
		
		reloadNews();
	}
	
	@Override public void selectSbSingle(EjbWithId item) throws JeeslLockingException, JeeslConstraintViolationException
	{
		// TODO Auto-generated method stub
		
	}
	
	public void cancelItem()
	{
		item = null;
	}
	
	private void reloadNews()
	{
//		items = fNews.allOrdered(fbNews.getClassNews(),JeeslSystemNews.Attributes.validFrom.toString(),false);
//		active.clear();
//		for(NEWS n : list){active.put(n,false);}
//		for(NEWS n : fNews.fActiveNews()){active.put(n,true);}	
	}
	
	public void addItem() throws JeeslNotFoundException
	{
//		if(debugOnInfo){logger.info(AbstractLogMessage.addEntity(fbNews.getClassNews()));}
//		news = efNews.build(null,user);
//		news.setName(efLang.createEmpty(localeCodes));
//		news.setDescription(efDescription.createEmpty(localeCodes));
	}
	
	public void selectItem() throws JeeslNotFoundException
	{
//		if(debugOnInfo){logger.info(AbstractLogMessage.selectEntity(news));}
//		news = fNews.find(fbNews.getClassNews(),news);
//		news = efLang.persistMissingLangs(fNews,bTranslation.getLocales(),news);
//		news = efDescription.persistMissingLangs(fNews,bTranslation.getLocales(),news);
	}
	
	public void saveItem() throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException
	{
//		if(debugOnInfo){logger.info(AbstractLogMessage.saveEntity(news));}
//		if(news.getCategory()!=null){news.setCategory(fNews.find(fbNews.getClassCategory(), news.getCategory()));}
//		news = fNews.save(news);
//		reloadNews();
//		bMessage.growlSuccessSaved();
	}
	
	public void deleteItem() throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException
	{
//		if(debugOnInfo){logger.info(AbstractLogMessage.rmEntity(news));}
//		fNews.rm(news);
//		news=null;
//		bMessage.growlSuccessRemoved();
//		reloadNews();
	}
}