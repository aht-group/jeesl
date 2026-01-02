package org.jeesl.controller.web.g.module.news;

import java.util.List;
import java.util.Objects;

import org.jeesl.api.bean.callback.JeeslFileRepositoryCallback;
import org.jeesl.api.bean.callback.module.news.JeeslNewsRegistryCallback;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.module.JeeslNewsFacade;
import org.jeesl.controller.web.AbstractJeeslLocaleWebController;
import org.jeesl.controller.web.util.AbstractLogMessage;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.module.NewsFactoryBuilder;
import org.jeesl.interfaces.bean.sb.bean.SbSingleBean;
import org.jeesl.interfaces.bean.sb.handler.SbSingleSelection;
import org.jeesl.interfaces.controller.handler.system.io.JeeslFileRepositoryHandler;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkup;
import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkupType;
import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.module.news.JeeslNewsCategory;
import org.jeesl.interfaces.model.module.news.JeeslNewsFeed;
import org.jeesl.interfaces.model.module.news.JeeslNewsItem;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.jsf.handler.sb.SbSingleHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslNewsRegistryGwc <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
									R extends JeeslTenantRealm<L,D,R,?>, RREF extends EjbWithId,
									FEED extends JeeslNewsFeed<L,D,R>,
									CATEGORY extends JeeslNewsCategory<L,D,R,CATEGORY,?>,
									ITEM extends JeeslNewsItem<L,FEED,CATEGORY,USER,M,FRC>,
									USER extends JeeslSimpleUser,
									M extends JeeslIoMarkup<MT>,
									MT extends JeeslIoMarkupType<L,D,MT,?>,
									FRC extends JeeslFileContainer<?,?>>
					extends AbstractJeeslLocaleWebController<L,D,LOC>
					implements SbSingleBean,JeeslFileRepositoryCallback
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslNewsRegistryGwc.class);
	
	private final JeeslNewsRegistryCallback callback;
	
	protected JeeslNewsFacade<L,D,R,FEED,CATEGORY,ITEM,USER,M,MT,FRC> fNews;
	private final NewsFactoryBuilder<L,D,LOC,R,FEED,CATEGORY,ITEM,USER,M,MT> fbNews;
	
	protected final SbSingleHandler<CATEGORY> sbhCategory; public SbSingleHandler<CATEGORY> getSbhCategory() {return sbhCategory;}
	protected final SbSingleHandler<LOC> sbhLocale; public SbSingleHandler<LOC> getSbhLocale() {return sbhLocale;}
	
	private JeeslFileRepositoryHandler<LOC,?,FRC,?> frh; public JeeslFileRepositoryHandler<LOC,?,FRC,?> getFrh() {return frh;}
	
	private List<ITEM> items; public List<ITEM> getItems() {return items;}
	
	private R realm;
	private FEED feed;
	private USER author;
	private ITEM item; public ITEM getItem() {return item;} public void setItem(ITEM item) {this.item = item;}

	public JeeslNewsRegistryGwc(JeeslNewsRegistryCallback callback, NewsFactoryBuilder<L,D,LOC,R,FEED,CATEGORY,ITEM,USER,M,MT> fbNews)
	{
		super(fbNews.getClassL(),fbNews.getClassD());
		this.callback=callback;
		this.fbNews=fbNews;
		
		sbhCategory = new SbSingleHandler<>(fbNews.getClassCategory(),this);
		sbhLocale = new SbSingleHandler<>(fbNews.getClassLocale(),this);
	}

	public void postConstructNews(JeeslLocaleProvider<LOC> lp, JeeslFacesMessageBean bMessage,
										JeeslNewsFacade<L,D,R,FEED,CATEGORY,ITEM,USER,M,MT,FRC> fNews,
										R realm, USER author,
										JeeslFileRepositoryHandler<LOC,?,FRC,?> frh)
	{
		super.postConstructLocaleWebController(lp,bMessage);
		this.fNews=fNews;
		this.realm=realm;
		this.author=author;
		this.frh=frh;
		
		frh.setLocales(lp.getLocales());
		sbhLocale.setList(lp.getLocales());
		sbhLocale.setDefault();
		frh.setLocale(sbhLocale.getSelection());

	}
	
	public void updateRealmReference(RREF rref)
	{
		sbhCategory.clear();
		sbhCategory.setList(fNews.all(fbNews.getClassCategory(),realm,rref));
		sbhCategory.setDefault();
		
		try {feed = fNews.fByRref(fbNews.getClassFeed(),realm,rref);}
		catch (JeeslNotFoundException e)
		{
			try
			{
				feed = fNews.save(fbNews.ejbFeed().build(realm,rref));
			}
			catch (JeeslConstraintViolationException | JeeslLockingException e1) {e1.printStackTrace();}
		}
		
		reloadNews();
	}
	
	@SuppressWarnings("unchecked")
	@Override public void selectSbSingle(SbSingleSelection handler, EjbWithId item) throws JeeslLockingException, JeeslConstraintViolationException
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
		items = fNews.allForParent(fbNews.getClassItem(),feed);
//		active.clear();
//		for(NEWS n : list){active.put(n,false);}
//		for(NEWS n : fNews.fActiveNews()){active.put(n,true);}	
	}
	
	public void addItem() throws JeeslNotFoundException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.createEntity(fbNews.getClassItem()));}
		MT markupType = fNews.fByEnum(fbNews.getClassMarkupType(),JeeslIoMarkupType.Code.xhtml);
		item = fbNews.ejbItem().build(feed,sbhLocale.getList(),markupType,author);
		
		if(sbhCategory.isSelected()) {item.setCategory(sbhCategory.getSelection());}
		
		frh.reset();
		callback.callbackPostAddItem();
		
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
		if(Objects.nonNull(bMessage)) {bMessage.growlSaved(item);}
		frh.init(item);
	}
	
	public void deleteItem() throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException
	{
//		if(debugOnInfo){logger.info(AbstractLogMessage.rmEntity(news));}
//		fNews.rm(news);
//		news=null;
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