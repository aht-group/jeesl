package org.jeesl.controller.web.module.news;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import org.jeesl.api.bean.callback.JeeslFileRepositoryCallback;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.module.JeeslNewsFacade;
import org.jeesl.controller.web.AbstractJeeslWebController;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.module.NewsFactoryBuilder;
import org.jeesl.interfaces.bean.sb.bean.SbSingleBean;
import org.jeesl.interfaces.controller.handler.system.io.JeeslFileRepositoryHandler;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsMarkupType;
import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.module.news.JeeslNewsCategory;
import org.jeesl.interfaces.model.module.news.JeeslNewsFeed;
import org.jeesl.interfaces.model.module.news.JeeslNewsItem;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.locale.JeeslMarkup;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.jsf.handler.sb.SbSingleHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public class JeeslNewsRegistryController <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
									R extends JeeslTenantRealm<L,D,R,?>, RREF extends EjbWithId,
									FEED extends JeeslNewsFeed<L,D,R>,
									CATEGORY extends JeeslNewsCategory<L,D,R,CATEGORY,?>,
									ITEM extends JeeslNewsItem<L,FEED,CATEGORY,USER,M,FRC>,
									USER extends EjbWithId,
									M extends JeeslMarkup<MT>,
									MT extends JeeslIoCmsMarkupType<L,D,MT,?>,
									FRC extends JeeslFileContainer<?,?>
//,FRM extends JeeslFileMeta<D,FRC,?,?>
>
					extends AbstractJeeslWebController<L,D,LOC>
					implements Serializable,SbSingleBean,JeeslFileRepositoryCallback
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslNewsRegistryController.class);
	
	protected JeeslNewsFacade<L,D,R,FEED,CATEGORY,ITEM,USER,M,MT,FRC> fNews;
	private final NewsFactoryBuilder<L,D,LOC,R,FEED,CATEGORY,ITEM,USER,M,MT> fbNews;
	
	protected final SbSingleHandler<CATEGORY> sbhCategory; public SbSingleHandler<CATEGORY> getSbhCategory() {return sbhCategory;}
	protected final SbSingleHandler<LOC> sbhLocale; public SbSingleHandler<LOC> getSbhLocale() {return sbhLocale;}
	
	private JeeslFileRepositoryHandler<LOC,?,FRC,?> frh; public JeeslFileRepositoryHandler<LOC,?,FRC,?> getFrh() {return frh;}
	
	private List<ITEM> items; public List<ITEM> getItems() {return items;}
	
	protected R realm;
	protected RREF rref;
	private USER author;
	private ITEM item; public ITEM getItem() {return item;} public void setItem(ITEM item) {this.item = item;}

	public JeeslNewsRegistryController(NewsFactoryBuilder<L,D,LOC,R,FEED,CATEGORY,ITEM,USER,M,MT> fbNews)
	{
		super(fbNews.getClassL(),fbNews.getClassD());
		this.fbNews=fbNews;
		
		sbhCategory = new SbSingleHandler<>(fbNews.getClassCategory(),this);
		sbhLocale = new SbSingleHandler<>(fbNews.getClassLocale(),this);
	}

	public void postConstructNews(JeeslLocaleProvider<LOC> lp, JeeslFacesMessageBean bMessage,
										JeeslNewsFacade<L,D,R,FEED,CATEGORY,ITEM,USER,M,MT,FRC> fNews,
										R realm, USER author,
										JeeslFileRepositoryHandler<LOC,?,FRC,?> frh)
	{
		super.postConstructWebController(lp,bMessage);
		this.fNews=fNews;
		this.realm=realm;
		this.author=author;
		this.frh=frh;
		
		frh.setLocales(lp.getLocales());
		sbhLocale.setList(lp.getLocales());
		sbhLocale.setDefault();
		frh.setLocale(sbhLocale.getSelection());
		
//		categories = fNews.allOrderedPositionVisible(fbNews.getClassCategory());
//		active = new HashMap<NEWS,Boolean>();
	}
	
	public void updateRealmReference(RREF rref)
	{
		this.rref=rref;
		
		sbhCategory.clear();
		sbhCategory.setList(fNews.all(fbNews.getClassCategory(),realm,rref));
		
		reloadNews();
	}
	
	@SuppressWarnings("unchecked")
	@Override public void selectSbSingle(EjbWithId item) throws JeeslLockingException, JeeslConstraintViolationException
	{
		if(item.getClass().getName().equals(fbNews.getClassLocale().getName()))
		{
			frh.setLocale((LOC)item);
			frh.reset();
			if(Objects.nonNull(item)) {frh.init(this.item);}
		}
	}
	
	public void cancelItem() {this.reset(true);}
	private void reset(boolean rItem)
	{
		if(rItem) {item=null; frh.reset();}
	}
	
	private void reloadNews()
	{
		items = fNews.all(fbNews.getClassItem());
//		active.clear();
//		for(NEWS n : list){active.put(n,false);}
//		for(NEWS n : fNews.fActiveNews()){active.put(n,true);}	
	}
	
	public void addItem() throws JeeslNotFoundException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.createEntity(fbNews.getClassItem()));}
		MT markupType = fNews.fByEnum(fbNews.getClassMarkupType(),JeeslIoCmsMarkupType.Code.xhtml);
		item = fbNews.ejbItem().build(realm,rref,sbhLocale.getList(),markupType,author);
//		news.setName(efLang.createEmpty(localeCodes));
//		news.setDescription(efDescription.createEmpty(localeCodes));
		frh.reset();
	}
	
	public void selectItem() throws JeeslNotFoundException, JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.selectEntity(item));}
//		news = fNews.find(fbNews.getClassNews(),news);
//		news = efLang.persistMissingLangs(fNews,bTranslation.getLocales(),news);
//		news = efDescription.persistMissingLangs(fNews,bTranslation.getLocales(),news);
		frh.init(item);
	}
	
	public void saveItem() throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.saveEntity(item));}
//		if(news.getCategory()!=null){news.setCategory(fNews.find(fbNews.getClassCategory(), news.getCategory()));}
		item = fNews.save(item);
		reloadNews();
		bMessage.growlSuccessSaved();
		frh.init(item);
	}
	
	public void deleteItem() throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException
	{
//		if(debugOnInfo){logger.info(AbstractLogMessage.rmEntity(news));}
//		fNews.rm(news);
//		news=null;
//		bMessage.growlSuccessRemoved();
//		reloadNews();
	}

	@Override public void callbackFrMetaSelected() {}
	@Override public void callbackFrContainerSaved(EjbWithId id) throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo){logger.info("callbackFrContainerSaved: "+id.toString());}
		item.setFrContainer(frh.getContainer());
		item = fNews.save(item);
	}
}