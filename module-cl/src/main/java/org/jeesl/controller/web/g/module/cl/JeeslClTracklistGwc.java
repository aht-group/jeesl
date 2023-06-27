package org.jeesl.controller.web.g.module.cl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.module.JeeslChecklistFacade;
import org.jeesl.controller.web.AbstractJeeslWebController;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.module.ChecklistFactoryBuilder;
import org.jeesl.factory.ejb.module.cl.EjbTracklistFactory;
import org.jeesl.interfaces.bean.sb.bean.SbSingleBean;
import org.jeesl.interfaces.bean.sb.bean.SbToggleBean;
import org.jeesl.interfaces.bean.sb.handler.SbToggleSelection;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.model.module.cl.JeeslClCategory;
import org.jeesl.interfaces.model.module.cl.JeeslClCheckItem;
import org.jeesl.interfaces.model.module.cl.JeeslClChecklist;
import org.jeesl.interfaces.model.module.cl.JeeslClTracklist;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.jsf.handler.ui.edit.UiEditBooleanHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public class JeeslClTracklistGwc <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
    								R extends JeeslTenantRealm<L,D,R,?>, RREF extends EjbWithId,
    								CAT extends JeeslClCategory<L,?,R,CAT,?>,
    								CL extends JeeslClChecklist<L,R,CAT>,
    								CI extends JeeslClCheckItem<L,CL,?>,
    								TL extends JeeslClTracklist<L,R>>
					extends AbstractJeeslWebController<L,D,LOC>
					implements Serializable, SbSingleBean, SbToggleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslClTracklistGwc.class);
	
	private JeeslChecklistFacade<L,D,R,CAT,CL> fCl;

	private final ChecklistFactoryBuilder<L,D,R,CAT,CL,CI,TL,?,?> fbCl;
	private EjbTracklistFactory<R,CAT,TL> ejbTracklist;

	private final UiEditBooleanHandler ehMagnet; public UiEditBooleanHandler getEhMagnet() {return ehMagnet;}
	
    protected R realm;
    protected RREF rref; public RREF getRref() {return rref;}

    private final List<CAT> categories; public List<CAT> getCategories() {return categories;}
    private final List<TL> lists; public List<TL> getLists() {return lists;}
    private final List<CL> checkLists; public List<CL> getCheckLists() {return checkLists;}

	private TL list; public TL getList() {return list;} public void setList(TL list) {this.list = list;}
	
	public JeeslClTracklistGwc(ChecklistFactoryBuilder<L,D,R,CAT,CL,CI,TL,?,?> fbCl)
	{
		super(fbCl.getClassL(),fbCl.getClassD());
		this.fbCl=fbCl;
		
		ejbTracklist = fbCl.ejbTracklist();
		
		ehMagnet = new UiEditBooleanHandler();

		categories = new ArrayList<>();
		lists = new ArrayList<>();
		checkLists = new ArrayList<>();
	}

	public void postConstruct(JeeslLocaleProvider<LOC> lp, JeeslFacesMessageBean bMessage ,R realm,
										JeeslChecklistFacade<L,D,R,CAT,CL> fCl)
	{
		super.postConstructWebController(lp,bMessage);
		this.fCl=fCl;
		this.realm=realm;
	}
	
	public void updateRealmReference(RREF rref)
	{
		this.rref=rref;
		
		categories.clear();
		categories.addAll(fCl.all(fbCl.getClassTopic(),realm,rref));
		
		checkLists.clear();
		checkLists.addAll(fCl.all(fbCl.getClassChecklist(),realm,rref));
		
		this.reloadLists();
	}
	
	@Override public void selectSbSingle(EjbWithId item) throws JeeslLockingException, JeeslConstraintViolationException
	{
		this.reloadLists();
	}
	
	@Override public void toggled(SbToggleSelection handler, Class<?> c) throws JeeslLockingException, JeeslConstraintViolationException
	{
		// TODO Auto-generated method stub
	}
	
	public void cancelList() {reset(false, true);}
	public void cancelItem() {reset(false, false);}
	private void reset(boolean rLists, boolean rList)
	{
		if(rLists) {lists.clear();}
		if(rList) {list = null;}
	}
	
//	public void reorderItems() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fCl,items); this.reloadItems();}
	
	private void reloadLists()
	{
		this.reset(true,false);
		lists.addAll(fCl.all(fbCl.getClassTracklist(),realm,rref));
    }
	
	public void selectList()
	{
		this.reset(false, false);
		if(debugOnInfo) {logger.info(AbstractLogMessage.selectEntity(list));}
		list = efLang.persistMissingLangs(fCl, lp.getLocales(),list);
//		this.reloadItems();
	}
	
	public void addList()
	{
		this.reset(false, true);
		if(debugOnInfo) {logger.info(AbstractLogMessage.createEntity(fbCl.getClassChecklist()));}
		list = ejbTracklist.build(realm, rref);
		list.setName(efLang.buildEmpty(lp.getLocales()));
	}
	
	public void saveList() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.saveEntity(list));}
		ejbTracklist.converter(fCl,list);
		list = fCl.save(list);
		this.reloadLists();
	}
	
	public void deleteList() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.saveEntity(list));}
		fCl.rm(list);
		this.reset(false, true);
		this.reloadLists();
	}
}