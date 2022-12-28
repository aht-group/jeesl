package org.jeesl.controller.web.module.aom;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.jeesl.api.bean.callback.JeeslFileRepositoryCallback;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.module.JeeslAomFacade;
import org.jeesl.controller.handler.NullNumberBinder;
import org.jeesl.controller.web.AbstractJeeslWebController;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.module.AomFactoryBuilder;
import org.jeesl.factory.ejb.module.asset.EjbAssetEventFactory;
import org.jeesl.factory.ejb.module.asset.EjbAssetFactory;
import org.jeesl.interfaces.bean.sb.bean.SbSingleBean;
import org.jeesl.interfaces.bean.sb.bean.SbToggleBean;
import org.jeesl.interfaces.bean.th.ThMultiFilter;
import org.jeesl.interfaces.bean.th.ThMultiFilterBean;
import org.jeesl.interfaces.cache.module.aom.JeeslAomCache;
import org.jeesl.interfaces.controller.handler.system.io.JeeslFileRepositoryHandler;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsMarkupType;
import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomAsset;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomAssetStatus;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomAssetType;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomView;
import org.jeesl.interfaces.model.module.aom.company.JeeslAomCompany;
import org.jeesl.interfaces.model.module.aom.company.JeeslAomScope;
import org.jeesl.interfaces.model.module.aom.event.JeeslAomEvent;
import org.jeesl.interfaces.model.module.aom.event.JeeslAomEventStatus;
import org.jeesl.interfaces.model.module.aom.event.JeeslAomEventType;
import org.jeesl.interfaces.model.module.aom.event.JeeslAomEventUpload;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.locale.JeeslMarkup;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.jsf.handler.sb.SbMultiHandler;
import org.jeesl.jsf.handler.sb.SbSingleHandler;
import org.jeesl.jsf.handler.th.ThMultiFilterHandler;
import org.jeesl.jsf.helper.TreeHelper;
import org.jeesl.model.ejb.system.tenant.TenantIdentifier;
import org.jeesl.util.comparator.ejb.module.asset.EjbAssetComparator;
import org.jeesl.util.comparator.ejb.module.asset.EjbEventComparator;
import org.jeesl.web.model.module.aom.AssetEventLazyModel;
import org.jeesl.web.ui.module.aom.UiHelperAsset;
import org.primefaces.event.DragDropEvent;
import org.primefaces.event.NodeCollapseEvent;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.TreeDragDropEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public class JeeslAomAssetController <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
										REALM extends JeeslTenantRealm<L,D,REALM,?>, RREF extends EjbWithId,
										COMPANY extends JeeslAomCompany<REALM,SCOPE>,
										SCOPE extends JeeslAomScope<L,D,SCOPE,?>,
										ASSET extends JeeslAomAsset<REALM,ASSET,COMPANY,ASTATUS,ATYPE>,
										ASTATUS extends JeeslAomAssetStatus<L,D,ASTATUS,?>,
										ATYPE extends JeeslAomAssetType<L,D,REALM,ATYPE,VIEW,?>,
										VIEW extends JeeslAomView<L,D,REALM,?>,
										EVENT extends JeeslAomEvent<COMPANY,ASSET,ETYPE,ESTATUS,M,USER,FRC>,
										ETYPE extends JeeslAomEventType<L,D,ETYPE,?>,
										ESTATUS extends JeeslAomEventStatus<L,D,ESTATUS,?>,
										M extends JeeslMarkup<MT>,
										MT extends JeeslIoCmsMarkupType<L,D,MT,?>,
										USER extends JeeslSimpleUser,
										FRC extends JeeslFileContainer<?,?>,
										UP extends JeeslAomEventUpload<L,D,UP,?>>
					extends AbstractJeeslWebController<L,D,LOC>
					implements Serializable,ThMultiFilterBean,SbToggleBean,SbSingleBean,JeeslFileRepositoryCallback
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslAomAssetController.class);
	
	protected JeeslAomFacade<L,D,REALM,COMPANY,ASSET,ASTATUS,ATYPE,VIEW,EVENT,ETYPE,ESTATUS,UP> fAsset;
//	private JeeslAssetCacheBean<REALM,RREF,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE,ALEVEL,ETYPE,UP> bCache;
	private JeeslAomCache<REALM,COMPANY,SCOPE,ATYPE,VIEW,ETYPE> cache;
	
	private final AomFactoryBuilder<L,D,REALM,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE,VIEW,EVENT,ETYPE,ESTATUS,M,MT,USER,FRC,UP> fbAsset;
	
	private final EjbAssetFactory<REALM,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE> efAsset;
	private final EjbAssetEventFactory<COMPANY,ASSET,EVENT,ETYPE,ESTATUS,M,MT,USER,FRC> efEvent;
	
	private final Comparator<ASSET> cpAsset;
	
	private final UiHelperAsset<L,D,REALM,RREF,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE,VIEW,EVENT,ETYPE,ESTATUS> uiHelper; public UiHelperAsset<L,D,REALM,RREF,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE,VIEW,EVENT,ETYPE,ESTATUS> getUiHelper() {return uiHelper;}
    private final NullNumberBinder nnb; public NullNumberBinder getNnb() {return nnb;}
    private final ThMultiFilterHandler<ETYPE> thfEventType; public ThMultiFilterHandler<ETYPE> getThfEventType() {return thfEventType;}
    private final SbMultiHandler<ETYPE> sbhEventType; public SbMultiHandler<ETYPE> getSbhEventType() {return sbhEventType;}
    private final SbSingleHandler<VIEW> sbhView; public SbSingleHandler<VIEW> getSbhView() {return sbhView;}
    private JeeslFileRepositoryHandler<?,FRC,?> frh; public JeeslFileRepositoryHandler<?,FRC,?> getFrh() {return frh;}  public void setFrh(JeeslFileRepositoryHandler<?,FRC,?> frh) {this.frh = frh;}
    
    private final AssetEventLazyModel<ASSET,EVENT,ETYPE,ESTATUS,USER> lazyEvents; public AssetEventLazyModel<ASSET,EVENT,ETYPE,ESTATUS,USER> getLazyEvents() {return lazyEvents;}
    
	private final Set<ASSET> path;

	private TreeNode tree; public TreeNode getTree() {return tree;}
    private TreeNode node; public TreeNode getNode() {return node;} public void setNode(TreeNode node) {this.node = node;}

    private TenantIdentifier<REALM> identifier; public TenantIdentifier<REALM> getIdentifier() {return identifier;}
	private JeeslAomCacheKey<REALM,SCOPE> key; public JeeslAomCacheKey<REALM,SCOPE> getKey() {return key;}
	protected RREF rref; public RREF getRref() {return rref;}

	private ASSET root;
    private ASSET asset; public ASSET getAsset() {return asset;} public void setAsset(ASSET asset) {this.asset = asset;}
    private EVENT event; public EVENT getEvent() {return event;} public void setEvent(EVENT event) {this.event = event;}
    private MT markupType;
    
	public JeeslAomAssetController(AomFactoryBuilder<L,D,REALM,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE,VIEW,EVENT,ETYPE,ESTATUS,M,MT,USER,FRC,UP> fbAsset)
	{
		super(fbAsset.getClassL(),fbAsset.getClassD());
		this.fbAsset=fbAsset;
		
		uiHelper = new UiHelperAsset<>();
		nnb = new NullNumberBinder();
		
		thfEventType = new ThMultiFilterHandler<>(this);
		sbhEventType = new SbMultiHandler<>(fbAsset.getClassEventType(),this);
		lazyEvents = new AssetEventLazyModel<>(fbAsset.cpEvent(EjbEventComparator.Type.recordDesc),thfEventType,sbhEventType);
		
		efAsset = fbAsset.ejbAsset();
		efEvent = fbAsset.ejbEvent();
		
		cpAsset = fbAsset.cpAsset(EjbAssetComparator.Type.position);
		
		path = new HashSet<>();
		sbhView = new SbSingleHandler<>(fbAsset.getClassAssetLevel(),this);
		key = new JeeslAomCacheKey<>();
	}
	
	public void postConstructAsset(JeeslLocaleProvider<LOC> lp, JeeslFacesMessageBean bMessage,
						JeeslAomCache<REALM,COMPANY,SCOPE,ATYPE,VIEW,ETYPE> cache,
						JeeslAomFacade<L,D,REALM,COMPANY,ASSET,ASTATUS,ATYPE,VIEW,EVENT,ETYPE,ESTATUS,UP> fAsset,
						REALM realm)
	{
		super.postConstructWebController(lp,bMessage);
		this.fAsset=fAsset;
		this.cache=cache;
		uiHelper.setCacheBean(cache);
		
		identifier = TenantIdentifier.instance(realm);
	
		markupType = fAsset.fByEnum(fbAsset.getClassMarkupType(),JeeslIoCmsMarkupType.Code.xhtml);
		
		thfEventType.getList().addAll(cache.getEventType());
		thfEventType.selectAll();
		
		sbhEventType.getList().addAll(cache.getEventType());
		sbhEventType.preSelect(	JeeslAomEventType.Code.maintenance,
								JeeslAomEventType.Code.check,
								JeeslAomEventType.Code.renew,
								JeeslAomEventType.Code.malfunction
								);
	}
	
	public void updateRealmReference(RREF rref)
	{
		identifier.withRref(rref);
		key.update(identifier,cache.getScopes());
		this.rref=rref;
		
		sbhView.setList(fAsset.fAomViews(identifier));
		sbhView.setDefault();
		reloadTree();
	}
	
	@Override public void selectSbSingle(EjbWithId item) throws JeeslLockingException, JeeslConstraintViolationException
	{
		reloadTree();
	}
	
	@Override public void filtered(ThMultiFilter filter) throws JeeslLockingException, JeeslConstraintViolationException
	{
		logger.info("TH Filter");
	}
	
	@Override public void toggled(Class<?> c) throws JeeslLockingException, JeeslConstraintViolationException
	{
		if(asset!=null) {lazyEvents.reloadScope(fAsset,asset);}
	}
	
	public void cancelEvent() {this.reset(false,false,true);}
	private void reset(boolean rAsset, boolean rEvents, boolean rEvent)
	{
		if(rAsset) {asset=null;}
		if(rEvents) {lazyEvents.clear();}
		if(rEvent) {event=null;}
	}
	
	private void reloadTree()
	{
		if(Objects.nonNull(jogger)) {jogger.start("reloadTree");}
		if(debugOnInfo) {logger.info("Loading root: realm:"+identifier.getRealm().toString()+" rref:"+identifier.getId());}
		root = fAsset.fcAssetRoot(identifier.getRealm(),identifier);
		if(Objects.nonNull(jogger)) {jogger.milestone("root");}
		
		tree = new DefaultTreeNode(root, null);
		buildTree(tree,fAsset.allForParent(fbAsset.getClassAsset(), root));
		if(Objects.nonNull(jogger)) {jogger.milestone("tree");}
	}
	
	private void buildTree(TreeNode parent, List<ASSET> assets)
	{
		for(ASSET a : assets)
		{
			TreeNode n = new DefaultTreeNode(a,parent);
			n.setExpanded(path.contains(a));
			List<ASSET> childs = fAsset.allForParent(fbAsset.getClassAsset(),a);
			if(!childs.isEmpty())
			{
				buildTree(n,childs);
			}
		}
	}
	
	public void expandTree()
	{
		TreeHelper.setExpansion(this.node != null ? this.node : this.tree, true);
	}
	
	public void expandTree(int levels)
	{
		TreeNode root = this.node;
		if (root == null)
		{
			root = this.tree;
			levels++;
		}
		TreeHelper.setExpansion(root, true, levels);
	}
	
	public void collapseTree()
	{
		TreeHelper.setExpansion(this.tree,  false);
	}
	
	public boolean isExpanded()
	{
		return this.tree != null && this.tree.getChildren().stream().filter(node -> node.isExpanded()).count() > 1;
	}
	
	public void addAsset()
	{
		ASSET parent = null; if(asset!=null) {parent = asset;} else {parent = root;}
		ASTATUS status = fAsset.fByEnum(fbAsset.getClassStatus(),JeeslAomAssetStatus.Code.na);
		if(debugOnInfo)
		{
			StringBuilder sb = new StringBuilder();
			sb.append("fcAomRootType ");
			sb.append("realm:").append(identifier.getRealm().toString());
			sb.append(" rref:").append(identifier.getId());
			sb.append(" alevel:");if(sbhView.getSelection()!=null) {sb.append(sbhView.getSelection());}else {sb.append("null");}
			logger.info(sb.toString());
		}
		reset(true,true,true);
		asset = efAsset.build(identifier.getRealm(),identifier,parent,status,null);
	}
	
	public void saveAsset() throws JeeslConstraintViolationException, JeeslLockingException
	{
		efAsset.converter(fAsset,asset);
		asset = fAsset.save(asset);
		path.clear();addPath(asset);
		
		reloadTree();
	}
	
	private void addPath(ASSET asset)
	{
		path.add(asset);
		if(asset.getParent()!=null) {addPath(asset.getParent());}
	}
	
	public void onNodeExpand(NodeExpandEvent event) {if(debugOnInfo) {logger.info("Expanded "+event.getTreeNode().toString());}}
    public void onNodeCollapse(NodeCollapseEvent event) {if(debugOnInfo) {logger.info("Collapsed "+event.getTreeNode().toString());}}
	
	@SuppressWarnings("unchecked")
	public void onDragDrop(TreeDragDropEvent event) throws JeeslConstraintViolationException, JeeslLockingException
	{
        TreeNode dragNode = event.getDragNode();
        TreeNode dropNode = event.getDropNode();
        int dropIndex = event.getDropIndex();
        logger.info("Dragged " + dragNode.getData() + " Dropped on " + dropNode.getData() + " at " + dropIndex);
        
        ASSET parent = (ASSET)dropNode.getData();
        int index=1;
        for(TreeNode n : dropNode.getChildren())
        {
    		ASSET child =(ASSET)n.getData();
    		child.setParent(parent);
    		child.setPosition(index);
    		fAsset.save(child);
    		index++;
        }  
    }

    @SuppressWarnings("unchecked")
	public void onNodeSelect(NodeSelectEvent event)
    {
    	reset(true,true,true);
		logger.info("Selected "+event.getTreeNode().toString());
		asset = (ASSET)event.getTreeNode().getData();
		reloadEvents();
    }
    
	private void reloadEvents()
	{
		lazyEvents.reloadScope(fAsset,asset);
	}
    
    public void addEvent() throws JeeslConstraintViolationException, JeeslLockingException
    {
    	logger.info(AbstractLogMessage.createEntity(fbAsset.getClassEvent()));
    	
    	event = efEvent.build(asset,cache.getEventType().get(0),markupType);
    	efEvent.ejb2nnb(event,nnb);
    	uiHelper.update(key,event);
    	if(frh!=null) {frh.init(event);}
    }
    
    public void selectEvent() throws JeeslConstraintViolationException, JeeslLockingException
    {
    	logger.info(AbstractLogMessage.selectEntity(event));
    	event = fAsset.load(event);
    	efEvent.ejb2nnb(event,nnb);
    	Collections.sort(event.getAssets(),cpAsset);
    	uiHelper.update(key,event);
    	if(frh!=null) {frh.init(event);}
    }
    
    public void saveEvent() throws JeeslConstraintViolationException, JeeslLockingException
    {
    	logger.info(AbstractLogMessage.saveEntity(event));
    	efEvent.converter(fAsset,event);
    	efEvent.nnb2ejb(event,nnb);
    	event = fAsset.save(event);
    	thfEventType.memoryUpdate();
    	reloadEvents();
    	thfEventType.memoryApply();
    	uiHelper.update(key,event);
    	if(frh!=null) {frh.init(event);}
    }
    
    public void removeEvent() throws JeeslConstraintViolationException, JeeslLockingException
    {
    	event.getAssets().remove(asset);
    	fAsset.save(event);
    	reset(false,true,true);
    	reloadEvents();
    }
    
    public void cloneEvent()
    {
    	efEvent.converter(fAsset,event);
    	event = efEvent.clone(event,markupType);
    	efEvent.ejb2nnb(event,nnb);
    	uiHelper.update(key,event);
    }
    
    @SuppressWarnings("unchecked")
	public void onDrop(DragDropEvent<ASSET> ddEvent) throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException
	{
    	logger.info("DRAG "+ddEvent.getDragId());
		logger.info("DROP "+ddEvent.getDropId());
		Object data = ddEvent.getData();
		if(data==null) {logger.info("data = null");}
		else{logger.info("Data "+data.getClass().getSimpleName());}
		
		TreeNode n = TreeHelper.getNode(tree,ddEvent.getDragId(),3);
		ASSET a = (ASSET)n.getData();
		logger.info(a.toString());
		
		if(!event.getAssets().contains(a))
		{
			event.getAssets().add(a);
			event = fAsset.save(event);
		}
	}
  
	@Override public void callbackFrContainerSaved(EjbWithId id) throws JeeslConstraintViolationException, JeeslLockingException
	{
		event.setFrContainer(frh.getContainer());
		event = fAsset.save(event);
	}
	@Override public void callbackFrMetaSelected() {}
}