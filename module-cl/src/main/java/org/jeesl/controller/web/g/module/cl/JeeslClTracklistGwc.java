package org.jeesl.controller.web.g.module.cl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.module.JeeslChecklistFacade;
import org.jeesl.controller.web.AbstractJeeslLocaleWebController;
import org.jeesl.controller.web.util.AbstractLogMessage;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.module.ChecklistFactoryBuilder;
import org.jeesl.factory.ejb.module.cl.EjbTrackItemFactory;
import org.jeesl.factory.ejb.module.cl.EjbTrackListFactory;
import org.jeesl.interfaces.bean.sb.bean.SbSingleBean;
import org.jeesl.interfaces.bean.sb.bean.SbToggleBean;
import org.jeesl.interfaces.bean.sb.handler.SbSingleSelection;
import org.jeesl.interfaces.bean.sb.handler.SbToggleSelection;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.model.module.cl.JeeslClCategory;
import org.jeesl.interfaces.model.module.cl.JeeslClCheckItem;
import org.jeesl.interfaces.model.module.cl.JeeslClChecklist;
import org.jeesl.interfaces.model.module.cl.JeeslClTrackItem;
import org.jeesl.interfaces.model.module.cl.JeeslClTrackStatus;
import org.jeesl.interfaces.model.module.cl.JeeslClTracklist;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.jsf.handler.ui.edit.UiEditBooleanHandler;
import org.jeesl.util.db.cache.EjbCodeCache;
import org.jeesl.util.query.ejb.module.EjbChecklistQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslClTracklistGwc <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
    								R extends JeeslTenantRealm<L,D,R,?>, RREF extends EjbWithId,
    								CAT extends JeeslClCategory<L,?,R,CAT,?>,
    								CL extends JeeslClChecklist<L,R,CAT>,
    								CI extends JeeslClCheckItem<L,CL,?>,
    								TL extends JeeslClTracklist<L,R,CL>,
    								TI extends JeeslClTrackItem<CI,TL,TS>,
    								TS extends JeeslClTrackStatus<L,D,TS,?>>
					extends AbstractJeeslLocaleWebController<L,D,LOC>
					implements Serializable, SbSingleBean, SbToggleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslClTracklistGwc.class);
	
	private JeeslChecklistFacade<CL,CI,TL> fCl;

	private final ChecklistFactoryBuilder<L,D,R,CAT,CL,CI,TL,TI,TS,?,?> fbCl;
	private final EjbTrackListFactory<R,CAT,TL> efTrackList;
	private final EjbTrackItemFactory<CI,TL,TI> efTrackItem;

	private final UiEditBooleanHandler ehMagnet; public UiEditBooleanHandler getEhMagnet() {return ehMagnet;}
	private final EjbCodeCache<TS> cStatus;
	
	private final Map<CL,Boolean> mapLinked; public Map<CL, Boolean> getMapLinked() {return mapLinked;}

	private final List<CAT> categories; public List<CAT> getCategories() {return categories;}
    private final List<TL> lists; public List<TL> getLists() {return lists;}
    private final List<CL> checkLists; public List<CL> getCheckLists() {return checkLists;}
    private final List<TI> items; public List<TI> getItems() {return items;}

    protected R realm;
    protected RREF rref; public RREF getRref() {return rref;}
	private TL list; public TL getList() {return list;} public void setList(TL list) {this.list = list;}
	
	public JeeslClTracklistGwc(ChecklistFactoryBuilder<L,D,R,CAT,CL,CI,TL,TI,TS,?,?> fbCl)
	{
		super(fbCl.getClassL(),fbCl.getClassD());
		this.fbCl=fbCl;
		
		efTrackList = fbCl.ejbTrackList();
		efTrackItem = fbCl.ejbTrackItem();
		
		ehMagnet = new UiEditBooleanHandler();
		cStatus = EjbCodeCache.instance(fbCl.getClassTrackStatus());
		
		mapLinked = new HashMap<>();
		
		categories = new ArrayList<>();
		lists = new ArrayList<>();
		checkLists = new ArrayList<>();
		items = new ArrayList<>();
	}

	public void postConstruct(JeeslLocaleProvider<LOC> lp, JeeslFacesMessageBean bMessage ,R realm,
										JeeslChecklistFacade<CL,CI,TL> fCl)
	{
		super.postConstructLocaleWebController(lp,bMessage);
		this.fCl=fCl;
		this.realm=realm;
		
		cStatus.facade(fCl);
	}
	
	public void updateRealmReference(RREF rref)
	{
		this.rref=rref;
		
		categories.clear();
		categories.addAll(fCl.all(fbCl.getClassTopic(),realm,rref));
		
		checkLists.clear();
		checkLists.addAll(fCl.all(fbCl.getClassCheckList(),realm,rref));
		
		if(debugOnInfo) {logger.info(fbCl.getClassCheckList().getSimpleName()+": "+checkLists.size());}
		
		this.reloadLists();
	}
	
	@Override public void selectSbSingle(SbSingleSelection handler, EjbWithId item) throws JeeslLockingException, JeeslConstraintViolationException
	{
		this.reloadLists();
	}
	
	@Override public void toggled(SbToggleSelection handler, Class<?> c) throws JeeslLockingException, JeeslConstraintViolationException
	{
		// TODO Auto-generated method stub
	}
	
	public void cancelList() {reset(false,true,true,true);}
	private void reset(boolean rTrackLists, boolean rList, boolean rLinkedLists, boolean rItems)
	{
		if(rTrackLists) {lists.clear();}
		if(rList) {list=null; ehMagnet.denyEdit();}
		if(rLinkedLists) {mapLinked.clear();}
		if(rItems) {items.clear();}
	}
	
//	public void reorderItems() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fCl,items); this.reloadItems();}
	
	private void reloadLists()
	{
		this.reset(true,false,false,false);
		lists.addAll(fCl.all(fbCl.getClassTrackList(),realm,rref));
    }
	
	private void reloadList()
	{
		this.reset(false,false,true,false);
		list = fCl.load(list);
		for(CL l : list.getChecklists())
		{
			mapLinked.put(l,true);
		}
	}
	
	public void selectList()
	{
		this.reset(false,false,true,true);
		if(debugOnInfo) {logger.info(AbstractLogMessage.selectEntity(list));}
		list = efLang.persistMissingLangs(fCl,lp,list);
		this.reloadList();
		this.reloadItems();
	}
	
	public void addList()
	{
		this.reset(false,true,true,true);
		if(debugOnInfo) {logger.info(AbstractLogMessage.createEntity(fbCl.getClassCheckList()));}
		list = efTrackList.build(realm, rref);
		list.setName(efLang.buildEmpty(lp.getLocales()));
	}
	
	public void saveList() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.saveEntity(list));}
		efTrackList.converter(fCl,list);
		list = fCl.save(list);
		this.reloadLists();
		this.reloadList();
	}
	
	public void deleteList() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.saveEntity(list));}
		fCl.rm(list);
		this.reset(false,true,true,true);
		this.reloadLists();
	}
	
	public void applyChecklist(CL cl, boolean link) throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo){logger.info("Link ("+link+") "+cl.toString());}

		if(link && !list.getChecklists().contains(cl))
		{
			logger.info("add");
			list.getChecklists().add(cl);
			mapLinked.put(cl,true);
		}
		else if(!link && list.getChecklists().contains(cl))
		{
			logger.info("remove");
			list.getChecklists().remove(cl);
			mapLinked.put(cl,false);
		}
		
		list = fCl.save(list);
		list = fCl.load(list);
	}
	
	public void applyItems() throws JeeslConstraintViolationException, JeeslLockingException
	{
		EjbChecklistQuery<CL,CI,TL> query = new EjbChecklistQuery<CL,CI,TL>(); 
		query.addCalendars(list.getChecklists());
		
		Set<CI> existing = efTrackItem.toLSetCheckItem(items);
		List<CI> available = fCl.fCheckItems(query);
		
		if(debugOnInfo) {logger.info(fbCl.getClassTrackItem().getSimpleName()+": "+existing.size());}
		if(debugOnInfo) {logger.info(fbCl.getClassCheckList().getSimpleName()+": "+list.getChecklists().size());}
		if(debugOnInfo) {logger.info(fbCl.getClassCheckItem().getSimpleName()+": "+available.size());}
		
		for(CI ci : available)
		{
			if(!existing.contains(ci))
			{
				TI ti = efTrackItem.build(list,ci);
				ti.setStatus(cStatus.ejb(JeeslClTrackStatus.Code.missing));
				ti = fCl.save(ti);
				items.add(ti);
			}
		}
	}
	
	private void reloadItems()
	{
		this.reset(false, false, false, true);
		items.addAll(fCl.allForParent(fbCl.getClassTrackItem(), list));
	}
}