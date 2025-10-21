package org.jeesl.controller.web.module.aom;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.api.bean.callback.JeeslFileRepositoryCallback;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoFrFacade;
import org.jeesl.api.facade.module.JeeslAomFacade;
import org.jeesl.controller.handler.NullNumberBinder;
import org.jeesl.controller.util.comparator.ejb.module.aom.EjbAssetComparator;
import org.jeesl.controller.util.comparator.ejb.module.aom.EjbEventComparator;
import org.jeesl.controller.web.AbstractJeeslLocaleWebController;
import org.jeesl.controller.web.util.AbstractLogMessage;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.io.IoFileRepositoryFactoryBuilder;
import org.jeesl.factory.builder.module.AomFactoryBuilder;
import org.jeesl.factory.ejb.module.asset.EjbAssetEventFactory;
import org.jeesl.factory.ejb.module.asset.EjbAssetFactory;
import org.jeesl.interfaces.bean.sb.bean.SbSingleBean;
import org.jeesl.interfaces.bean.sb.bean.SbToggleBean;
import org.jeesl.interfaces.bean.sb.handler.SbToggleSelection;
import org.jeesl.interfaces.bean.th.ThMultiFilter;
import org.jeesl.interfaces.bean.th.ThMultiFilterBean;
import org.jeesl.interfaces.cache.module.aom.JeeslAomCache;
import org.jeesl.interfaces.controller.handler.system.io.JeeslFileRepositoryHandler;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkup;
import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkupType;
import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.io.fr.JeeslFileMeta;
import org.jeesl.interfaces.model.io.fr.JeeslFileStorage;
import org.jeesl.interfaces.model.io.fr.JeeslFileStorageType;
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
import org.jeesl.interfaces.model.system.security.user.JeeslSecurityUser;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.jsf.handler.sb.SbMultiHandler;
import org.jeesl.jsf.handler.sb.SbSingleHandler;
import org.jeesl.jsf.handler.th.ThMultiFilterHandler;
import org.jeesl.jsf.handler.ui.edit.UiEditBooleanHandler;
import org.jeesl.jsf.helper.TreeHelper;
import org.jeesl.model.ejb.system.tenant.TenantIdentifier;
import org.jeesl.util.query.cq.CqLiteral;
import org.jeesl.util.query.cq.CqOrdering;
import org.jeesl.util.query.ejb.io.EjbIoFrQuery;
import org.jeesl.util.query.ejb.module.EjbAomQuery;
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

public class JeeslAomAssetGwc <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
									REALM extends JeeslTenantRealm<L,D,REALM,?>,
									COMPANY extends JeeslAomCompany<REALM,SCOPE>,
									SCOPE extends JeeslAomScope<L,D,SCOPE,?>,
									ASSET extends JeeslAomAsset<REALM,ASSET,COMPANY,ASTATUS,ATYPE>,
									ASTATUS extends JeeslAomAssetStatus<L,D,ASTATUS,?>,
									ATYPE extends JeeslAomAssetType<L,D,REALM,ATYPE,VIEW,?>,
									VIEW extends JeeslAomView<L,D,REALM,?>,
									EVENT extends JeeslAomEvent<COMPANY,ASSET,ETYPE,ESTATUS,M,USER,FRC>,
									ETYPE extends JeeslAomEventType<L,D,ETYPE,?>,
									ESTATUS extends JeeslAomEventStatus<L,D,ESTATUS,?>,
									M extends JeeslIoMarkup<MT>,
									MT extends JeeslIoMarkupType<L,D,MT,?>,
									USER extends JeeslSecurityUser,
									FRS extends JeeslFileStorage<L,D,?,FRST,?>,
									FRST extends JeeslFileStorageType<L,D,FRST,?>,
									FRC extends JeeslFileContainer<FRS,FRM>,
									FRM extends JeeslFileMeta<D,FRC,?,?>,
									UP extends JeeslAomEventUpload<L,D,UP,?>>
					extends AbstractJeeslLocaleWebController<L,D,LOC>
					implements ThMultiFilterBean,SbToggleBean,SbSingleBean,JeeslFileRepositoryCallback
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslAomAssetGwc.class);

	private enum Loop{treeAllForParent}

	protected JeeslAomFacade<L,D,REALM,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE,VIEW,EVENT,ESTATUS> fAom;
	private JeeslIoFrFacade <L,D,?,FRS,FRST,?,FRC,FRM,?,?,?,?> fFr;
	private JeeslAomCache<REALM,COMPANY,SCOPE,ASTATUS,ATYPE,VIEW,ETYPE> cache; public JeeslAomCache<REALM,COMPANY,SCOPE,ASTATUS,ATYPE,VIEW,ETYPE> getCache() {return cache;}

	private final AomFactoryBuilder<L,D,REALM,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE,VIEW,EVENT,ETYPE,ESTATUS,M,MT,USER,FRC,UP> fbAsset;
	private final IoFileRepositoryFactoryBuilder<L,D,LOC,?,?,?,?,FRC,FRM,?,?,?,?> fbFr;

	private final TreeHelper<ASSET> thAsset;
	private final EjbAssetFactory<REALM,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE> efAsset;
	private final EjbAssetEventFactory<COMPANY,ASSET,EVENT,ETYPE,ESTATUS,M,MT> efEvent;

	private final Comparator<ASSET> cpAsset;

	private final UiHelperAsset<L,D,REALM,COMPANY,SCOPE,EVENT,ETYPE> uiHelper; public UiHelperAsset<L,D,REALM,COMPANY,SCOPE,EVENT,ETYPE> getUiHelper() {return uiHelper;}
	private final UiEditBooleanHandler ehGallery; public UiEditBooleanHandler getEhGallery() {return ehGallery;}
    private final NullNumberBinder nnb; public NullNumberBinder getNnb() {return nnb;}
    private final ThMultiFilterHandler<ETYPE> thfEventType; public ThMultiFilterHandler<ETYPE> getThfEventType() {return thfEventType;}
    private final SbMultiHandler<ETYPE> sbhEventType; public SbMultiHandler<ETYPE> getSbhEventType() {return sbhEventType;}
    private final SbSingleHandler<VIEW> sbhView; public SbSingleHandler<VIEW> getSbhView() {return sbhView;}
    private JeeslFileRepositoryHandler<LOC,?,FRC,?> frh; public JeeslFileRepositoryHandler<LOC,?,FRC,?> getFrh() {return frh;}  public void setFrh(JeeslFileRepositoryHandler<LOC,?,FRC,?> frh) {this.frh = frh;}

    private final AssetEventLazyModel<REALM,SCOPE,ASSET,ATYPE,EVENT,ETYPE,ESTATUS,USER> lazyEvents; public AssetEventLazyModel<REALM,SCOPE,ASSET,ATYPE,EVENT,ETYPE,ESTATUS,USER> getLazyEvents() {return lazyEvents;}

	private final Set<ASSET> path;

	private TreeNode tree; public TreeNode getTree() {return tree;}
    private TreeNode node; public TreeNode getNode() {return node;} public void setNode(TreeNode node) {this.node = node;}

    private TenantIdentifier<REALM> identifier; public TenantIdentifier<REALM> getIdentifier() {return identifier;}
	private final JeeslAomCacheKey<REALM,SCOPE> key; public JeeslAomCacheKey<REALM,SCOPE> getKey() {return key;}

    private ASSET asset; public ASSET getAsset() {return asset;} public void setAsset(ASSET asset) {this.asset = asset;}
    private EVENT event; public EVENT getEvent() {return event;} public void setEvent(EVENT event) {this.event = event;}
    private FRM preview; public FRM getPreview() {return preview;}
	private MT markupType;

	public JeeslAomAssetGwc(AomFactoryBuilder<L,D,REALM,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE,VIEW,EVENT,ETYPE,ESTATUS,M,MT,USER,FRC,UP> fbAsset,
			IoFileRepositoryFactoryBuilder<L,D,LOC,?,?,?,?,FRC,FRM,?,?,?,?> fbFr)
	{
		super(fbAsset.getClassL(),fbAsset.getClassD());
		this.fbAsset=fbAsset;
		this.fbFr=fbFr;

		uiHelper = new UiHelperAsset<>();
		ehGallery = UiEditBooleanHandler.instance().value(false);
		nnb = new NullNumberBinder();

		thfEventType = new ThMultiFilterHandler<>(this);
		sbhEventType = new SbMultiHandler<>(fbAsset.getClassEventType(),this);
		lazyEvents = new AssetEventLazyModel<>(fbAsset.cpEvent(EjbEventComparator.Type.recordDesc),thfEventType,sbhEventType);

		thAsset = TreeHelper.instance();
		efAsset = fbAsset.ejbAsset();
		efEvent = fbAsset.ejbEvent();

		cpAsset = fbAsset.cpAsset(EjbAssetComparator.Type.position);

		path = new HashSet<>();
		sbhView = new SbSingleHandler<>(fbAsset.getClassAssetLevel(),this);
		key = new JeeslAomCacheKey<>();
	}

	public void postConstructAsset(JeeslLocaleProvider<LOC> lp, JeeslFacesMessageBean bMessage,
						JeeslAomCache<REALM,COMPANY,SCOPE,ASTATUS,ATYPE,VIEW,ETYPE> cache,
						JeeslAomFacade<L,D,REALM,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE,VIEW,EVENT,ESTATUS> fAom,
						JeeslIoFrFacade <L,D,?,FRS,FRST,?,FRC,FRM,?,?,?,?> fFr,
						REALM realm)
	{
		super.postConstructLocaleWebController(lp,bMessage);
		this.fAom=fAom;
		this.fFr=fFr;
		this.cache=cache;
		uiHelper.setCacheBean(cache);

		identifier = TenantIdentifier.instance(realm);

		markupType = fAom.fByEnum(fbAsset.getClassMarkupType(),JeeslIoMarkupType.Code.xhtml);

		thfEventType.getList().addAll(cache.getEventType());
		thfEventType.selectAll();

		sbhEventType.getList().addAll(cache.getEventType());
		sbhEventType.selectAll();
	}

	public <RREF extends EjbWithId> void updateRealmReference(RREF rref)
	{
		identifier.withRref(rref);
		key.update(identifier,cache.getScopes());

		sbhView.setList(fAom.fAomViews(identifier));
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

	@Override public void toggled(SbToggleSelection handler, Class<?> c) throws JeeslLockingException, JeeslConstraintViolationException
	{
		if(asset!=null) {lazyEvents.reloadScope(fAom,asset);}
	}

	public void cancelEvent() {this.reset(false,false,false,true);}
	private void reset(boolean rAsset, boolean rPreview, boolean rEvents, boolean rEvent)
	{
		if(rAsset) {asset=null; }
		if(rPreview) {preview=null;}
		if(rEvents) {lazyEvents.clear();}
		if(rEvent) {event=null;}
	}

	private void reloadTree()
	{
		if(Objects.nonNull(jogger)) {jogger.start("reloadTree");}
		if(debugOnInfo) {logger.info("Loading root: realm:"+identifier.getRealm().toString()+" rref:"+identifier.getId());}

		EjbAomQuery<REALM,SCOPE,ASSET,ATYPE,EVENT,ESTATUS> query = new EjbAomQuery<>();
		query.tenant(identifier);
		query.add(CqOrdering.ascending(JeeslAomAsset.Attributes.position));

		List<ASSET> assets = fAom.fAomAssets(query);
		if(Objects.nonNull(jogger)) {jogger.milestone(fbAsset.getClassAsset(),"fAomAssets(identifier)",assets.size());}

		tree = new DefaultTreeNode();
		TreeHelper.buildTree(tree,assets);
		if(Objects.nonNull(jogger)) {jogger.milestone(tree.getClass(),"TreeHelper.buildTree",assets.size());}

//		root = fAom.fcAssetRoot(identifier.getRealm(),identifier);
//		if(Objects.nonNull(jogger)) {jogger.milestone("root");}

//		buildTree(tree,fAom.allForParent(fbAsset.getClassAsset(), root));
//		if(Objects.nonNull(jogger)) {jogger.milestone("tree");}
	}

	public void expandTree()
	{
		thAsset.setExpansion(this.node != null ? this.node : this.tree, true);
	}

	public void expandTree(int levels)
	{
		TreeNode root = this.node;
		if (root == null)
		{
			root = this.tree;
			levels++;
		}
		thAsset.setExpansion(root, true, levels);
	}

	public void collapseTree()
	{
		thAsset.setExpansion(this.tree,  false);
	}

	public boolean isExpanded()
	{
		return this.tree != null && this.tree.getChildren().stream().filter(node -> node.isExpanded()).count() > 0;
	}

	public void addAsset()
	{
		ASSET parent = null; if(asset!=null) {parent = asset;} //else {parent = root;}
		ASTATUS status = fAom.fByEnum(fbAsset.getClassStatus(),JeeslAomAssetStatus.Code.na);
		if(debugOnInfo)
		{
			StringBuilder sb = new StringBuilder();
			sb.append("fcAomRootType ");
			sb.append("realm:").append(identifier.getRealm().toString());
			sb.append(" rref:").append(identifier.getId());
			sb.append(" alevel:");if(sbhView.getSelection()!=null) {sb.append(sbhView.getSelection());}else {sb.append("null");}
			logger.info(sb.toString());
		}
		reset(true,true,true,true);
		asset = efAsset.build(identifier.getRealm(),identifier,parent,status,null);
	}

	public void saveAsset() throws JeeslConstraintViolationException, JeeslLockingException
	{
		efAsset.converter(fAom,asset);
		asset = fAom.save(asset);
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
//      logger.info("DropNode/Parent: "+parent.getName());
        int index=1;
        for(TreeNode n : dropNode.getChildren())
        {
    		ASSET child =(ASSET)n.getData();
    		child.setParent(parent);
    		child.setPosition(index);
//    		logger.info("Child: "+index+" "+child.getName());
    		fAom.save(child);
    		index++;
        }
    }

    @SuppressWarnings("unchecked")
	public void onNodeSelect(NodeSelectEvent event)
    {
		asset = (ASSET)event.getTreeNode().getData();
		this.selectAsset();
    }

    public void selectAsset()
    {
    	reset(false,true,true,true);
    	logger.info(AbstractLogMessage.selectEntity(asset));
    	this.reloadEvents();
    	this.reloadPreview();
    }

	private void reloadEvents()
	{
		lazyEvents.reloadScope(fAom,asset);
	}

	private void reloadPreview()
	{
		preview=null;

		List<FRC> list = fbFr.ejbContainer().toListContainer(lazyEvents.getList());
		logger.info(fbFr.getClassContainer().getSimpleName()+" "+list.size());
		if(ObjectUtils.isNotEmpty(list))
		{
			EjbIoFrQuery<FRS,FRST,FRC> query = new EjbIoFrQuery<>();
			query.addIoFrContainer(list);
			query.addCqLiteral(CqLiteral.exact(JeeslAomEventUpload.Code.preview,CqLiteral.path(JeeslFileMeta.Attributes.category)));
			List<FRM> metas = fFr.fIoFrMetas(query);

			if(!metas.isEmpty()) {preview = metas.get(0);}
		}
	}
	
	public void toggleGallery()
	{
		ehGallery.toggle();
		logger.info("Load Gallery "+ehGallery.isAllow());
	}

    public void addEvent() throws JeeslConstraintViolationException, JeeslLockingException
    {
    	logger.info(AbstractLogMessage.createEntity(fbAsset.getClassEvent()));

    	event = efEvent.build(asset,null,markupType);
    	event.setStatus(fAom.fByEnum(fbAsset.getClassEventStatus(), JeeslAomEventStatus.Code.planned));
    	event.setType(fAom.fByEnum(fbAsset.getClassEventType(), JeeslAomEventType.Code.check));

    	efEvent.ejb2nnb(event,nnb);
    	uiHelper.update(key,event);
    	if(frh!=null) {frh.init(event);}
    }

    public void selectEvent() throws JeeslConstraintViolationException, JeeslLockingException
    {
    	logger.info(AbstractLogMessage.selectEntity(event));
    	event = fAom.load(event);
    	efEvent.ejb2nnb(event,nnb);
    	Collections.sort(event.getAssets(),cpAsset);
    	uiHelper.update(key,event);
    	if(frh!=null) {frh.init(event);}
    }

    public void saveEvent() throws JeeslConstraintViolationException, JeeslLockingException
    {
    	logger.info(AbstractLogMessage.saveEntity(event));
    	efEvent.converter(fAom,event);
    	efEvent.nnb2ejb(event,nnb);
    	event = fAom.save(event);
    	thfEventType.memoryUpdate();
    	reloadEvents();
    	thfEventType.memoryApply();
    	uiHelper.update(key,event);
    	if(frh!=null) {frh.init(event);}
    }

    public void removeEvent() throws JeeslConstraintViolationException, JeeslLockingException
    {
    	event.getAssets().remove(asset);
    	fAom.save(event);
    	reset(false,false,true,true);
    	reloadEvents();
    }

    public void cloneEvent()
    {
    	efEvent.converter(fAom,event);
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

		TreeNode n = thAsset.getNode(tree,ddEvent.getDragId(),3);
		ASSET a = (ASSET)n.getData();
		logger.info(a.toString());

		if(!event.getAssets().contains(a))
		{
			event.getAssets().add(a);
			event = fAom.save(event);
		}
	}

    @Override public void callbackFrMetaSelected() {}
	@Override public void callbackFrContainerSaved(EjbWithId id) throws JeeslConstraintViolationException, JeeslLockingException
	{
		event.setFrContainer(frh.getContainer());
		event = fAom.save(event);
	}
}