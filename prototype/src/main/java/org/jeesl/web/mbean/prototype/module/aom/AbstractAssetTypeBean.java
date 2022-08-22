package org.jeesl.web.mbean.prototype.module.aom;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.module.aom.JeeslAssetCacheBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.module.JeeslAssetFacade;
import org.jeesl.api.facade.system.graphic.JeeslGraphicFacade;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.module.AomFactoryBuilder;
import org.jeesl.factory.builder.system.SvgFactoryBuilder;
import org.jeesl.interfaces.bean.sb.bean.SbSingleBean;
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
import org.jeesl.interfaces.model.system.graphic.component.JeeslGraphicComponent;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphicType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.locale.JeeslMarkup;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.jsf.handler.sb.SbSingleHandler;
import org.jeesl.jsf.helper.TreeHelper;
import org.jeesl.web.mbean.prototype.system.AbstractAdminBean;
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

public abstract class AbstractAssetTypeBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
										S extends EjbWithId, G extends JeeslGraphic<L,D,GT,F,FS>, GT extends JeeslGraphicType<L,D,GT,G>,
										F extends JeeslGraphicComponent<L,D,G,GT,F,FS>, FS extends JeeslStatus<L,D,FS>,
										REALM extends JeeslTenantRealm<L,D,REALM,?>, RREF extends EjbWithId,
										COMPANY extends JeeslAomCompany<REALM,SCOPE>,
										SCOPE extends JeeslAomScope<L,D,SCOPE,?>,
										ASSET extends JeeslAomAsset<REALM,ASSET,COMPANY,ASTATUS,ATYPE>,
										ASTATUS extends JeeslAomAssetStatus<L,D,ASTATUS,?>,
										ATYPE extends JeeslAomAssetType<L,D,REALM,ATYPE,VIEW,G>,
										VIEW extends JeeslAomView<L,D,REALM,G>,
										EVENT extends JeeslAomEvent<COMPANY,ASSET,ETYPE,ESTATUS,M,USER,FRC>,
										ETYPE extends JeeslAomEventType<L,D,ETYPE,?>,
										ESTATUS extends JeeslAomEventStatus<L,D,ESTATUS,?>,
										M extends JeeslMarkup<MT>,
										MT extends JeeslIoCmsMarkupType<L,D,MT,?>,
										USER extends JeeslSimpleUser,
										FRC extends JeeslFileContainer<?,?>,
										UP extends JeeslAomEventUpload<L,D,UP,?>>
					extends AbstractAdminBean<L,D,LOC>
					implements Serializable, SbSingleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractAssetTypeBean.class);

	private JeeslAssetFacade<L,D,REALM,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE,VIEW,EVENT,ETYPE,ESTATUS,M,MT,USER,FRC,UP> fAsset;
	private JeeslGraphicFacade<L,D,S,G,GT,F,FS> fGraphic;

	private JeeslAssetCacheBean<L,D,REALM,RREF,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE,VIEW,ETYPE,UP> bCache;

	private final SvgFactoryBuilder<L,D,G,GT,F,FS> fbSvg;
	private final AomFactoryBuilder<L,D,REALM,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE,VIEW,EVENT,ETYPE,ESTATUS,M,MT,USER,FRC,UP> fbAsset;

	private final SbSingleHandler<VIEW> sbhView; public SbSingleHandler<VIEW> getSbhView() {return sbhView;}

	private TreeNode tree; public TreeNode getTree() {return tree;}
    private TreeNode node; public TreeNode getNode() {return node;} public void setNode(TreeNode node) {this.node = node;}

    private REALM realm;
    private RREF rref;
    private ATYPE root;
    private ATYPE type;  public ATYPE getType() {return type;} public void setType(ATYPE type) {this.type = type;}

	public AbstractAssetTypeBean(AomFactoryBuilder<L,D,REALM,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE,VIEW,EVENT,ETYPE,ESTATUS,M,MT,USER,FRC,UP> fbAsset, SvgFactoryBuilder<L,D,G,GT,F,FS> fbSvg)
	{
		super(fbAsset.getClassL(),fbAsset.getClassD());
		this.fbAsset=fbAsset;
		this.fbSvg=fbSvg;
		sbhView = new SbSingleHandler<>(fbAsset.getClassAssetLevel(),this);

	}

	protected void postConstructAssetType(JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage,
									JeeslAssetCacheBean<L,D,REALM,RREF,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE,VIEW,ETYPE,UP> bCache,
									JeeslAssetFacade<L,D,REALM,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE,VIEW,EVENT,ETYPE,ESTATUS,M,MT,USER,FRC,UP> fAsset,
									JeeslGraphicFacade<L,D,S,G,GT,F,FS> fGraphic,
									REALM realm)
	{
		super.initJeeslAdmin(bTranslation,bMessage);
		this.bCache=bCache;
		this.fAsset=fAsset;
		this.fGraphic=fGraphic;
		this.realm=realm;
	}

	protected void updateRealmReference(RREF rref)
	{
		this.rref=rref;
		sbhView.clear();
		for(VIEW v : fAsset.fAomViews(realm,rref))
		{
			if(v.getTree().equals(JeeslAomView.Tree.hierarchy.toString())) {sbhView.getList().add(v);}
			else if(v.getTree().equals(JeeslAomView.Tree.type2.toString())) {sbhView.getList().add(v);}
		}
		sbhView.setDefault();
		reloadTree();
	}

	@Override public void selectSbSingle(EjbWithId item) throws JeeslLockingException, JeeslConstraintViolationException
	{
		reloadTree();
	}

	@SuppressWarnings("unchecked")
	private void reloadTree()
	{
		List<Long> expandedNodes = TreeHelper.findNodes(this.tree, node -> node.isExpanded()).stream().map(node -> (ATYPE)node.getData()).filter(data -> data != null).map(data -> data.getId()).collect(Collectors.toList());

		root = fAsset.fcAomRootType(realm,rref,sbhView.getSelection());

		tree = new DefaultTreeNode();
		this.type = null;
		TreeHelper.buildTree(this.fAsset, this.tree, this.fAsset.allForParent(this.fbAsset.getClassAssetType(), this.root), this.fbAsset.getClassAssetType());

		TreeHelper.findNodes(this.tree, node -> node.getData() != null && expandedNodes.contains(((ATYPE)node.getData()).getId())).forEach(node -> node.setExpanded(true));
	}

	private void reset(boolean rType)
	{
		if(rType) {type=null;}
	}

	public void addType()
	{
		ATYPE parent=null; if(type!=null) {parent = type;} else {parent = root;}
		type = fbAsset.ejbType().build(realm,rref,sbhView.getSelection(),parent, UUID.randomUUID().toString());
		type.setName(efLang.createEmpty(bTranslation.getLocales()));
		type.setDescription(efDescription.createEmpty(bTranslation.getLocales()));
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
		bCache.update(realm,rref,sbhView.getSelection(),type);
	}

	public void deleteType() throws JeeslLockingException
	{
		try
		{
			fAsset.rm(type);
			bCache.delete(realm,rref,sbhView.getSelection(),type);
			reloadTree();
			reset(true);
		}
		catch (JeeslConstraintViolationException e) {bMessage.errorConstraintViolationInUse();}
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
		type = efLang.persistMissingLangs(fAsset,bTranslation.getLocales(),type);
		type = efDescription.persistMissingLangs(fAsset,bTranslation.getLocales(),type);
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