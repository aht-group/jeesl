package org.jeesl.controllerl.web.g.module.cl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.module.JeeslChecklistFacade;
import org.jeesl.controller.web.AbstractJeeslWebController;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.module.ChecklistFactoryBuilder;
import org.jeesl.interfaces.bean.sb.bean.SbSingleBean;
import org.jeesl.interfaces.bean.sb.bean.SbToggleBean;
import org.jeesl.interfaces.bean.sb.handler.SbToggleSelection;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.model.module.cl.JeeslChecklist;
import org.jeesl.interfaces.model.module.cl.JeeslChecklistTopic;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public class JeeslClChecklistGwc <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
    								R extends JeeslTenantRealm<L,D,R,?>, RREF extends EjbWithId,
    								CL extends JeeslChecklist<L,R,TO>,
    								TO extends JeeslChecklistTopic<L,?,R,TO,?>
    										>
					extends AbstractJeeslWebController<L,D,LOC>
					implements Serializable, SbSingleBean, SbToggleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslClChecklistGwc.class);
	
	private JeeslChecklistFacade<L,D,R,CL,TO> fCl;
//
	protected final ChecklistFactoryBuilder<L,D,R,CL,TO> fbCl;
//	
//	private final EjbTaskFactory<R,T,TS,SC,M,MT> efTask;
	
    protected R realm;
    protected RREF rref; public RREF getRref() {return rref;}



    private final List<CL> lists; public List<CL> getLists() {return lists;}

	private CL list; public CL getList() {return list;} public void setList(CL list) {this.list = list;}

	public JeeslClChecklistGwc(ChecklistFactoryBuilder<L,D,R,CL,TO> fbCl)
	{
		super(fbCl.getClassL(),fbCl.getClassD());	
		this.fbCl=fbCl;
		
		
		
		lists = new ArrayList<>();
		
	}

	public void postConstructChecklist(JeeslLocaleProvider<LOC> lp, JeeslFacesMessageBean bMessage ,R realm,
										JeeslChecklistFacade<L,D,R,CL,TO> fCl
										)
	{
		super.postConstructWebController(lp,bMessage);
		this.fCl=fCl;
		this.realm=realm;
		
		
	}
	
	public void updateRealmReference(RREF rref)
	{
		this.rref=rref;
	
		reload();
	}
	
	@Override public void selectSbSingle(EjbWithId item) throws JeeslLockingException, JeeslConstraintViolationException
	{
		reload();
	}
	
	@Override public void toggled(SbToggleSelection handler, Class<?> c) throws JeeslLockingException, JeeslConstraintViolationException
	{
		// TODO Auto-generated method stub
	}
	
	public void cancelTask() {reset(false,true);}
	private void reset(boolean rLists, boolean rList)
	{
		if(rLists) {lists.clear();}
		if(rList) {list = null;}
	}
	
	protected void reload()
	{
		this.reset(true,false);
		lists.addAll(fCl.all(fbCl.getClassChecklist(),realm,rref));
    }
	
	public void selectList()
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.selectEntity(list));}
		list = efLang.persistMissingLangs(fCl, lp.getLocales(),list);
	}
	
	public void addList()
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.createEntity(fbCl.getClassChecklist()));}
		list = fbCl.ejbChecklist().build(realm, rref);
		list.setName(efLang.buildEmpty(lp.getLocales()));
	}
	
	public void saveList() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.saveEntity(list));}
		fbCl.ejbChecklist().converter(fCl,list);
		list = fCl.save(list);
		this.reload();
	}
}