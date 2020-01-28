package org.jeesl.web.mbean.prototype.module.asset;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.module.JeeslAssetFacade;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.module.AssetFactoryBuilder;
import org.jeesl.factory.builder.system.SvgFactoryBuilder;
import org.jeesl.factory.ejb.module.asset.EjbAssetTypeFactory;
import org.jeesl.interfaces.model.module.asset.JeeslAsset;
import org.jeesl.interfaces.model.module.asset.JeeslAssetManufacturer;
import org.jeesl.interfaces.model.module.asset.JeeslAssetRealm;
import org.jeesl.interfaces.model.module.asset.JeeslAssetStatus;
import org.jeesl.interfaces.model.module.asset.JeeslAssetType;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphicFigure;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphicType;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.with.EjbWithGraphic;
import org.jeesl.web.mbean.prototype.admin.AbstractAdminBean;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.NodeCollapseEvent;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.TreeDragDropEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.interfaces.model.status.UtilsDescription;
import net.sf.ahtutils.interfaces.model.status.UtilsLang;
import net.sf.ahtutils.interfaces.model.status.UtilsStatus;
import net.sf.ahtutils.model.interfaces.with.EjbWithId;

public abstract class AbstractAssetTypeBean <L extends UtilsLang, D extends UtilsDescription, LOC extends JeeslLocale<L,D,LOC,?>,
										G extends JeeslGraphic<L,D,GT,F,FS>, GT extends UtilsStatus<GT,L,D>,
										F extends JeeslGraphicFigure<L,D,G,GT,F,FS>, FS extends UtilsStatus<FS,L,D>,
										REALM extends JeeslAssetRealm<L,D,REALM,?>, RREF extends EjbWithId,
										ASSET extends JeeslAsset<REALM,ASSET,STATUS,TYPE>,
										MANU extends JeeslAssetManufacturer,
										STATUS extends JeeslAssetStatus<L,D,STATUS,?>,
										TYPE extends JeeslAssetType<L,D,REALM,TYPE,G>>
					extends AbstractAdminBean<L,D>
					implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractAssetTypeBean.class);
	
	private JeeslAssetFacade<L,D,REALM,ASSET,MANU,STATUS,TYPE> fAsset;
	
	private final SvgFactoryBuilder<L,D,G,GT,F,FS> fbSvg;
	private final AssetFactoryBuilder<L,D,REALM,ASSET,MANU,STATUS,TYPE> fbAsset;
	
	private final EjbAssetTypeFactory<REALM,TYPE> efType;
	
	private TreeNode tree; public TreeNode getTree() {return tree;}
    private TreeNode node; public TreeNode getNode() {return node;} public void setNode(TreeNode node) {this.node = node;}

    private REALM realm;
    private RREF realmReference;
    private TYPE root;
    private TYPE type;  public TYPE getType() {return type;} public void setType(TYPE type) {this.type = type;}

	public AbstractAssetTypeBean(AssetFactoryBuilder<L,D,REALM,ASSET,MANU,STATUS,TYPE> fbAsset, SvgFactoryBuilder<L,D,G,GT,F,FS> fbSvg)
	{
		super(fbAsset.getClassL(),fbAsset.getClassD());
		this.fbAsset=fbAsset;
		this.fbSvg=fbSvg;
		
		efType = fbAsset.ejbType();
	}
	
	protected <E extends Enum<E>> void postConstructAssetType(JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage,
									JeeslAssetFacade<L,D,REALM,ASSET,MANU,STATUS,TYPE> fAsset,
									E eRealm, RREF realmReference
									)
	{
		super.initJeeslAdmin(bTranslation,bMessage);
		this.fAsset=fAsset;
		
		realm = fAsset.fByEnum(fbAsset.getClassRealm(),eRealm);
		this.realmReference=realmReference;
		
		reloadTree();
	}
	
	private void reloadTree()
	{
		root = fAsset.fcAssetRootType(realm,realmReference);
		
		tree = new DefaultTreeNode(root, null);
		buildTree(tree,fAsset.allForParent(fbAsset.getClassType(),root));
	}
	
	private void buildTree(TreeNode parent, List<TYPE> types)
	{
		for(TYPE t : types)
		{
			TreeNode n = new DefaultTreeNode(t,parent);
			List<TYPE> childs = fAsset.allForParent(fbAsset.getClassType(),t);
			if(!childs.isEmpty()){buildTree(n,childs);}
		}
	}
	
	public void addType()
	{
		TYPE parent = null; if(type!=null) {parent = type;} else {parent = root;}
		type = fbAsset.ejbType().build(realm, realmReference, parent, UUID.randomUUID().toString());
		type.setName(efLang.createEmpty(bTranslation.getLocales()));
		type.setDescription(efDescription.createEmpty(bTranslation.getLocales()));
	}
	
	public void saveType() throws JeeslConstraintViolationException, JeeslLockingException
	{
		efType.converter(type);
		type = fAsset.save(type);
		reloadTree();
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
        
        logger.info("Childs of "+dropNode.getData());
        TYPE parent = (TYPE)dropNode.getData();
        int index=1;
        for(TreeNode n : dropNode.getChildren())
        {
    		TYPE child =(TYPE)n.getData();
//    		TYPE db = fAsset.find(fbAsset.getClassType(),child);
//    		efS.update(db,child);
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
		type = (TYPE)event.getTreeNode().getData();
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
		}
		type.getGraphic().setData(file.getContents());
		type = fAsset.save(type);
	}
}