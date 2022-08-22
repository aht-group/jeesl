package org.jeesl.web.controller.module.rmmv;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.module.JeeslRmmvFacade;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.module.RmmvFactoryBuilder;
import org.jeesl.factory.ejb.util.EjbIdFactory;
import org.jeesl.interfaces.bean.sb.bean.SbSingleBean;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.controller.web.module.rmmv.JeeslRmmvElementCallback;
import org.jeesl.interfaces.model.module.rmmv.JeeslRmmvTreeElement;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.jsf.handler.sb.SbSingleHandler;
import org.jeesl.jsf.helper.TreeHelper;
import org.jeesl.web.AbstractJeeslWebController;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.TreeDragDropEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslRmmvTreeElementController <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
											R extends JeeslTenantRealm<L,D,R,?>, RREF extends EjbWithId,
											TE extends JeeslRmmvTreeElement<L,R,TE>>
		extends AbstractJeeslWebController<L,D,LOC>
		implements SbSingleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslRmmvTreeElementController.class);
	
	private final JeeslRmmvElementCallback callback;
	private JeeslRmmvFacade<L,D,R,TE> fEh;
//	private JeeslAppCalendarBean<L,D,CALENDAR,ZONE,CT,ITEM,IT> bCalendar;
	
	private final RmmvFactoryBuilder<L,D,LOC,R,TE> fbEh;
//	private EjbTimeZoneFactory<ZONE,ITEM> efZone;
	
	protected final SbSingleHandler<LOC> sbhLocale; public SbSingleHandler<LOC> getSbhLocale() {return sbhLocale;}
	
	private final Set<TE> treePath;
	
	protected R realm;
	protected RREF rref;
	private TreeNode tree; public TreeNode getTree() {return tree;}
	private TreeNode node; public TreeNode getNode() {return node;} public void setNode(TreeNode node) {this.node = node;}
//	private ZONE zone; public ZONE getZone() {return zone;} public void setZone(ZONE zone) {this.zone = zone;}
	
	private TE element; public TE getElement() {return element;} public void setElement(TE element) {this.element = element;}

	public JeeslRmmvTreeElementController(final JeeslRmmvElementCallback callback, final RmmvFactoryBuilder<L,D,LOC,R,TE> fbRmmv)
	{
		super(fbRmmv.getClassL(),fbRmmv.getClassD());
		this.callback=callback;
		this.fbEh=fbRmmv;
        
		sbhLocale = new SbSingleHandler<>(fbEh.getClassLocale(),this);
		
		treePath = new HashSet<>();
	}
	
	public void postConstructTreeElement(JeeslRmmvFacade<L,D,R,TE> fEh,
//										JeeslAppCalendarBean<L,D,CALENDAR,ZONE,CT,ITEM,IT> bCalendar,
									JeeslLocaleProvider<LOC> lp, JeeslFacesMessageBean bMessage,
									R realm)
	{
		super.postConstructWebController(lp);
		this.fEh=fEh;
		this.realm=realm;
		
		sbhLocale.setList(lp.getLocales());
		sbhLocale.setDefault();
	}
	
	public void updateRealmReference(RREF rref)
	{
		this.rref=rref;
		tree = new DefaultTreeNode("Root", null);
	
		reloadTree();
	}
	
	@Override
	public void selectSbSingle(EjbWithId item) throws JeeslLockingException, JeeslConstraintViolationException
	{
		// TODO Auto-generated method stub
		
	}

	private void reloadTree()
	{
		tree.getChildren().clear();
		List<TE> list = fEh.all(fbEh.getClassTreeElement(),realm,rref);
//		PositionComparator.
		logger.info(fbEh.getClassTreeElement().getSimpleName()+" "+list.size());
		
		TreeHelper.buildTree(tree,list,treePath);
	}
	
	public void addElement()
	{
		element = fbEh.ejbElement().build(realm,rref);
		element.setName(efLang.createEmpty(lp.getLocales()));
	}
	
	public void saveElement() throws JeeslConstraintViolationException, JeeslLockingException
	{
		boolean isNew = EjbIdFactory.isUnSaved(element);
		element = fEh.save(element);
		if(isNew)
		{
			tree.getChildren().add(new DefaultTreeNode(element));
		}
		treePath.clear();TreeHelper.fillPath(treePath,element);
		this.reloadTree();
	}
	
	public void onDragDrop(TreeDragDropEvent event) throws JeeslConstraintViolationException, JeeslLockingException
	{
		TreeHelper.persistDragDropEvent(fEh,event);
    }
	
	 @SuppressWarnings("unchecked")
	 public void onNodeSelect(NodeSelectEvent event)
	 {
		logger.info("Selected "+event.getTreeNode().toString());
		element = (TE)event.getTreeNode().getData();
		element = fEh.find(fbEh.getClassTreeElement(),element);
		element = efLang.persistMissingLangs(fEh,lp.getLocales(),element);
		
	 }
	
//	public void selectZone()
//	{
//		zone = fCalendar.find(fbCalendar.getClassZone(),zone);
//	}
//	
//	public void addZone()
//	{
//		zone = efZone.build();
//		zone.setName(efLang.createEmpty(langs));
//	}
//	
//	public void saveZone() throws JeeslNotFoundException, JeeslConstraintViolationException, JeeslLockingException
//	{
//		if(EjbTimeZoneFactory.supportedCode(zone.getCode()))
//		{
//			zone = fCalendar.save(zone);
//			bCalendar.reloadZones(fCalendar);
//			bMessage.growlSuccessSaved();
//		}
//		else
//		{
//			bMessage.errorText("TS not supported");
//		}
//	}
}