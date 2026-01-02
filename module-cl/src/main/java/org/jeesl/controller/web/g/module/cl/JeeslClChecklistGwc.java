package org.jeesl.controller.web.g.module.cl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.module.JeeslChecklistFacade;
import org.jeesl.controller.web.AbstractJeeslLocaleWebController;
import org.jeesl.controller.web.util.AbstractLogMessage;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.module.ChecklistFactoryBuilder;
import org.jeesl.factory.ejb.io.cms.EjbMarkupFactory;
import org.jeesl.factory.ejb.module.cl.EjbCheckListFactory;
import org.jeesl.factory.ejb.module.cl.EjbCheckItemFactory;
import org.jeesl.interfaces.bean.sb.bean.SbSingleBean;
import org.jeesl.interfaces.bean.sb.bean.SbToggleBean;
import org.jeesl.interfaces.bean.sb.handler.SbSingleSelection;
import org.jeesl.interfaces.bean.sb.handler.SbToggleSelection;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkup;
import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkupType;
import org.jeesl.interfaces.model.module.cl.JeeslClCategory;
import org.jeesl.interfaces.model.module.cl.JeeslClCheckItem;
import org.jeesl.interfaces.model.module.cl.JeeslClChecklist;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.jsf.handler.PositionListReorderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslClChecklistGwc <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
    								R extends JeeslTenantRealm<L,D,R,?>, RREF extends EjbWithId,
    								CAT extends JeeslClCategory<L,?,R,CAT,?>,
    								CL extends JeeslClChecklist<L,R,CAT>,
    								
    								CI extends JeeslClCheckItem<L,CL,M>,
    								M extends JeeslIoMarkup<MT>,
									MT extends JeeslIoMarkupType<L,D,MT,?>
    										>
					extends AbstractJeeslLocaleWebController<L,D,LOC>
					implements Serializable, SbSingleBean, SbToggleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslClChecklistGwc.class);
	
	private JeeslChecklistFacade<CL,CI,?> fCl;

	private final ChecklistFactoryBuilder<L,D,R,CAT,CL,CI,?,?,?,M,MT> fbCl;
	private final EjbCheckListFactory<R,CAT,CL> ejbChecklist;
	private final EjbCheckItemFactory<CL,CI,M,MT> ejbChecklistItem;
	private final EjbMarkupFactory<LOC,M,MT> efMarkup;
	
    protected R realm;
    protected RREF rref; public RREF getRref() {return rref;}

    private final List<CAT> categories; public List<CAT> getCategories() {return categories;}
    private final List<CL> lists; public List<CL> getLists() {return lists;}
    private final List<CI> items; public List<CI> getItems() {return items;}

    private MT markupType;
	private CL list; public CL getList() {return list;} public void setList(CL list) {this.list = list;}
	private CI item; public CI getItem() {return item;} public void setItem(CI item) {this.item = item;}
	
	public JeeslClChecklistGwc(ChecklistFactoryBuilder<L,D,R,CAT,CL,CI,?,?,?,M,MT> fbCl)
	{
		super(fbCl.getClassL(),fbCl.getClassD());
		this.fbCl=fbCl;
		
		ejbChecklist = fbCl.ejbChecklist();
		ejbChecklistItem = fbCl.ejbChecklistItem();
		efMarkup = EjbMarkupFactory.instance(fbCl.getClassMarkup());
		
		categories = new ArrayList<>();
		lists = new ArrayList<>();
		items = new ArrayList<>();
		
	}

	public void postConstruct(JeeslLocaleProvider<LOC> lp, JeeslFacesMessageBean bMessage, R realm,
										JeeslChecklistFacade<CL,CI,?> fCl
										)
	{
		super.postConstructLocaleWebController(lp,bMessage);
		markupType = fCl.fByEnum(fbCl.getClassMarkupType(),JeeslIoMarkupType.Code.xhtml);
		this.fCl=fCl;
		this.realm=realm;
	}
	
	public void updateRealmReference(RREF rref)
	{
		this.rref=rref;
		this.reloadLists();
		
		categories.clear();
		categories.addAll(fCl.all(fbCl.getClassTopic(),realm,rref));
	}
	
	@Override public void selectSbSingle(SbSingleSelection handler, EjbWithId item) throws JeeslLockingException, JeeslConstraintViolationException
	{
		reloadLists();
	}
	
	@Override public void toggled(SbToggleSelection handler, Class<?> c) throws JeeslLockingException, JeeslConstraintViolationException
	{
		// TODO Auto-generated method stub
	}
	
	public void cancelList() {reset(false, true, true, true);}
	public void cancelItem() {reset(false, false, false, true);}
	private void reset(boolean rLists, boolean rList, boolean rItems, boolean rItem)
	{
		if(rLists) {lists.clear();}
		if(rList) {list = null;}
		if(rItems) {items.clear();}
		if(rItem) {item = null;}
	}
	
	public void reorderLists() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fCl,lists); this.reloadLists();}
	public void reorderItems() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fCl,items); this.reloadItems();}
	
	private void reloadLists()
	{
		this.reset(true,false,false,false);
		lists.addAll(fCl.all(fbCl.getClassCheckList(),realm,rref));
    }
	
	public void selectList()
	{
		this.reset(false, false, true, true);
		if(debugOnInfo) {logger.info(AbstractLogMessage.selectEntity(list));}
		list = efLang.persistMissingLangs(fCl, lp.getLocales(),list);
		this.reloadItems();
	}
	
	public void addList()
	{
		this.reset(false, true, true, true);
		if(debugOnInfo) {logger.info(AbstractLogMessage.createEntity(fbCl.getClassCheckList()));}
		list = ejbChecklist.build(realm, rref, lists);
		list.setName(efLang.buildEmpty(lp.getLocales()));
	}
	
	public void saveList() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.saveEntity(list));}
		ejbChecklist.converter(fCl,list);
		list = fCl.save(list);
		this.reloadLists();
	}
	
	public void deleteList() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.saveEntity(list));}
		fCl.rm(list);
		this.reset(false, true, true, true);
		this.reloadLists();
	}
	
	
	private void reloadItems()
	{
		this.reset(false,false,true,false);
		items.addAll(fCl.allForParent(fbCl.getClassCheckItem(),list));
    }
	
	public void selectItem()
	{
		this.reset(false, false, false, false);
		if(debugOnInfo) {logger.info(AbstractLogMessage.selectEntity(item));}
		item = efLang.persistMissingLangs(fCl,lp.getLocales(),item);
		item = efMarkup.persistMissingLangs(fCl,lp.getLocales(),item,markupType);
	}
	
	public void addItem()
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.createEntity(fbCl.getClassCheckItem()));}
		item = ejbChecklistItem.build(list,items);
		item.setName(efLang.buildEmpty(lp.getLocales()));
		item.setMarkup(efMarkup.build(lp.getLocales(), markupType));
	}
	
	public void saveItem() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.saveEntity(item));}
		ejbChecklistItem.converter(fCl,item);
		item = fCl.save(item);
		this.reloadItems();
	}
	
	public void deleteItem() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.saveEntity(item));}
		fCl.rm(item);
		this.reset(false, false, false, true);
		this.reloadItems();
	}
}