package org.jeesl.controller.web.module.aom;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoGraphicFacade;
import org.jeesl.api.facade.module.JeeslAomFacade;
import org.jeesl.controller.web.AbstractJeeslLocaleWebController;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.module.AomFactoryBuilder;
import org.jeesl.factory.builder.system.SvgFactoryBuilder;
import org.jeesl.interfaces.bean.sb.bean.SbSingleBean;
import org.jeesl.interfaces.bean.sb.handler.SbSingleSelection;
import org.jeesl.interfaces.cache.module.aom.JeeslAomAssetCache;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomAssetType;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomView;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphicType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.jsf.handler.sb.SbSingleHandler;
import org.jeesl.jsf.helper.TreeHelper;
import org.jeesl.model.ejb.system.tenant.TenantIdentifier;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.NodeCollapseEvent;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.TreeDragDropEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.primefaces.model.file.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslAomTypeController <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
										G extends JeeslGraphic<GT,?,?>, GT extends JeeslGraphicType<L,D,GT,G>,
										REALM extends JeeslTenantRealm<L,D,REALM,?>, RREF extends EjbWithId,
										ATYPE extends JeeslAomAssetType<L,D,REALM,ATYPE,VIEW,G>,
										VIEW extends JeeslAomView<L,D,REALM,G>>
					extends AbstractJeeslLocaleWebController<L,D,LOC>
					implements Serializable, SbSingleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslAomTypeController.class);

	private JeeslAomFacade<L,D,REALM,?,?,?,?,ATYPE,VIEW,?,?> fAsset;
	private JeeslIoGraphicFacade<?,G,GT,?,?> fGraphic;

	private JeeslAomAssetCache<REALM,?,ATYPE,VIEW> bCache;

	private final SvgFactoryBuilder<L,D,G,GT,?,?> fbSvg;
	private final AomFactoryBuilder<L,D,REALM,?,?,?,?,ATYPE,VIEW,?,?,?,?,?,?,?,?> fbAsset;

	private final SbSingleHandler<VIEW> sbhView; public SbSingleHandler<VIEW> getSbhView() {return sbhView;}

	private TreeNode tree; public TreeNode getTree() {return tree;}
    private TreeNode node; public TreeNode getNode() {return node;} public void setNode(TreeNode node) {this.node = node;}

    private RREF rref;
    private TenantIdentifier<REALM> identifier;
    private ATYPE type;  public ATYPE getType() {return type;} public void setType(ATYPE type) {this.type = type;}

	public JeeslAomTypeController(AomFactoryBuilder<L,D,REALM,?,?,?,?,ATYPE,VIEW,?,?,?,?,?,?,?,?> fbAsset,
									SvgFactoryBuilder<L,D,G,GT,?,?> fbSvg)
	{
		super(fbAsset.getClassL(),fbAsset.getClassD());
		this.fbAsset=fbAsset;
		this.fbSvg=fbSvg;
		sbhView = new SbSingleHandler<>(fbAsset.getClassAssetLevel(),this);
	}

	public void postConstructAssetType(JeeslLocaleProvider<LOC> lp, JeeslFacesMessageBean bMessage,
									JeeslAomAssetCache<REALM,?,ATYPE,VIEW> bCache,
									JeeslAomFacade<L,D,REALM,?,?,?,?,ATYPE,VIEW,?,?> fAsset,
									JeeslIoGraphicFacade<?,G,GT,?,?> fGraphic,
									REALM realm)
	{
		super.postConstructLocaleWebController(lp,bMessage);
		this.bCache=bCache;
		this.fAsset=fAsset;
		this.fGraphic=fGraphic;
		identifier = TenantIdentifier.instance(realm);
	}

	public void updateRealmReference(RREF rref)
	{
		identifier.withRref(rref);
		this.rref=rref;
		sbhView.clear();
		for(VIEW v : fAsset.fAomViews(identifier))
		{
			if(v.getTree().equals(JeeslAomView.Tree.hierarchy.toString())) {sbhView.getList().add(v);}
			else if(v.getTree().equals(JeeslAomView.Tree.type2.toString())) {sbhView.getList().add(v);}
		}
		sbhView.setDefault();
		reloadTree();
	}

	@Override public void selectSbSingle(SbSingleSelection handler, EjbWithId item) throws JeeslLockingException, JeeslConstraintViolationException
	{
		reloadTree();
	}

//	@SuppressWarnings("unchecked")
	private void reloadTree()
	{
		this.type = null;
//		List<Long> expandedNodes = TreeHelper.findNodes(this.tree, node -> node.isExpanded()).stream().map(node -> (ATYPE)node.getData()).filter(data -> data != null).map(data -> data.getId()).collect(Collectors.toList());

		tree = new DefaultTreeNode();
		tree.getChildren().clear();
			
		List<ATYPE> all = fAsset.fAomAssetTypes(identifier,sbhView.getSelection());
		logger.info("List.All: "+all.size());
		logger.debug(fbAsset.getClassAssetType().getSimpleName()+" "+all.size());
		TreeHelper.buildTree(tree,all);
		
//		List<ATYPE> list = this.fAsset.allForParent(this.fbAsset.getClassAssetType(), this.root);
//		logger.info("List: "+list.size());
//		TreeHelper.buildTree(this.fAsset, this.tree, list , this.fbAsset.getClassAssetType());
//
//		TreeHelper.findNodes(this.tree, node -> node.getData()!=null && expandedNodes.contains(((ATYPE)node.getData()).getId())).forEach(node -> node.setExpanded(true));
	}

	private void reset(boolean rType)
	{
		if(rType) {type=null;}
	}

	public void addType()
	{
		ATYPE parent=null; if(type!=null) {parent = type;}
		type = fbAsset.ejbType().build(identifier.getRealm(),rref,sbhView.getSelection(),parent, UUID.randomUUID().toString());
		type.setName(efLang.buildEmpty(lp.getLocales()));
		type.setDescription(efDescription.buildEmpty(lp.getLocales()));
	}

	public void cancelType()
	{
		if (this.node != null)
		{
			this.node.setSelected(false);
		}
		this.type = null;
	}

	public void saveType() throws JeeslConstraintViolationException, JeeslLockingException
	{
		type = fAsset.save(type);
		reloadTree();
		logger.warn("NYI Cache Update");
//		bCache.update(identifier.getRealm(),rref,sbhView.getSelection(),type);
	}

	public void deleteType() throws JeeslLockingException
	{
		try
		{
			fAsset.rm(type);
			logger.warn("NYI Cache Delete");
//			bCache.delete(identifier.getRealm(),rref,sbhView.getSelection(),type);
			reloadTree();
			reset(true);
		}
		catch (JeeslConstraintViolationException e) {bMessage.constraintInUse(null);}
	}

	public void onNodeExpand(NodeExpandEvent event) {if(debugOnInfo) {logger.info("Expanded "+event.getTreeNode().toString());}}
    public void onNodeCollapse(NodeCollapseEvent event) {if(debugOnInfo) {logger.info("Collapsed "+event.getTreeNode().toString());}}

	@SuppressWarnings("unchecked")
	public void onDragDrop(TreeDragDropEvent event) throws JeeslConstraintViolationException, JeeslLockingException
	{
        TreeNode dragNode = event.getDragNode();
        TreeNode dropNode = event.getDropNode();
        int dropIndex = event.getDropIndex();
        logger.info("Dragged " + dragNode.getData() + "Dropped on " + dropNode.getData() + " at " + dropIndex);

        ATYPE parent = (ATYPE)dropNode.getData();
        int index=1;
        for(TreeNode n : dropNode.getChildren())
        {
        	ATYPE child =(ATYPE)n.getData();
    		child.setParent(parent);
    		child.setPosition(index);
    		fAsset.save(child);
    		index++;
        }
    }

    @SuppressWarnings("unchecked")
	public void onNodeSelect(NodeSelectEvent event)
    {
		logger.info("Selected "+event.getTreeNode().toString());
		type = (ATYPE)event.getTreeNode().getData();
		type = efLang.persistMissingLangs(fAsset,lp,type);
		type = efDescription.persistMissingLangs(fAsset,lp.getLocales(),type);
    }

	public void handleFileUpload(FileUploadEvent event) throws JeeslConstraintViolationException, JeeslLockingException
	{
		UploadedFile file = event.getFile();
		logger.info("Received file with a size of " +file.getSize());
		if(type.getGraphic()==null)
		{
			GT gt = fAsset.fByEnum(fbSvg.getClassGraphicType(), JeeslGraphicType.Code.svg);
			G g = fbSvg.efGraphic().build(gt);
			g = fAsset.save(g);
			type.setGraphic(g);
			type = fAsset.save(type);
			type.getGraphic().setData(file.getContent());
			type = fAsset.save(type);
		}
		else
		{
			try
			{
				G g = fGraphic.fGraphic(fbAsset.getClassAssetType(),type);
				g.setData(file.getContent());
				fAsset.save(g);
			}
			catch (JeeslNotFoundException e) {e.printStackTrace();}
		}
	}
}