package org.jeesl.web.controller.util.tree;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.system.graphic.JeeslGraphicFacade;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.system.SvgFactoryBuilder;
import org.jeesl.factory.ejb.util.EjbIdFactory;
import org.jeesl.interfaces.bean.sb.bean.SbSingleBean;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphicType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.util.tree.JeeslTreeClassification;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.jsf.handler.sb.SbSingleHandler;
import org.jeesl.jsf.helper.TreeHelper;
import org.jeesl.web.AbstractJeeslWebController;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.TreeDragDropEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.primefaces.model.file.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractTreeClassificationController <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
											G extends JeeslGraphic<GT,?,?>, GT extends JeeslGraphicType<L,D,GT,G>,
											R extends JeeslTenantRealm<L,D,R,?>, RREF extends EjbWithId,
											C extends JeeslTreeClassification<L,R,C,G>>
		extends AbstractJeeslWebController<L,D,LOC>
		implements SbSingleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractTreeClassificationController.class);
	
	private JeeslFacade facade;
	private JeeslGraphicFacade<L,D,?,G,GT,?,?> fGraphic;
	
	protected final SvgFactoryBuilder<L,D,G,GT,?,?> fbSvg;
	protected final Class<C> cClassification;
	
	protected final SbSingleHandler<LOC> sbhLocale; public SbSingleHandler<LOC> getSbhLocale() {return sbhLocale;}

	private final Set<C> treePath;
	
	private TreeNode tree; public TreeNode getTree() {return tree;}
	private TreeNode node; public TreeNode getNode() {return node;} public void setNode(TreeNode node) {this.node = node;}
	
	protected C classification;  public C getClassification() {return classification;} public void setClassification(C classification) {this.classification = classification;}
	protected R realm;
	protected RREF rref;
	
	public AbstractTreeClassificationController(final SvgFactoryBuilder<L,D,G,GT,?,?> fbSvg, final Class<LOC> cLocale, final Class<C> cClassification)
	{
		super(fbSvg.getClassL(),fbSvg.getClassD());
		this.fbSvg=fbSvg;
		this.cClassification=cClassification;
		
		sbhLocale = new SbSingleHandler<>(cLocale,this);
		
		treePath = new HashSet<>();
		
	}
	
	public void postConstructTreeClassification(JeeslFacade facade,
									JeeslGraphicFacade<L,D,?,G,GT,?,?> fGraphic,
									JeeslLocaleProvider<LOC> lp, JeeslFacesMessageBean bMessage,
									R realm)
	{
		super.postConstructWebController(lp,bMessage);
		this.facade=facade;
		this.fGraphic=fGraphic;
		
		this.realm=realm;
		
		sbhLocale.setList(lp.getLocales());
		sbhLocale.setDefault();
	}
	
	public void cancelClassification() {}
	protected void reset(boolean rClassification)
	{
		if(rClassification) {classification=null;}
	}
	
	public void updateRealmReference(RREF rref)
	{
		this.rref=rref;
		tree = new DefaultTreeNode("Root", null);
	
		reloadTree();
	}
	
	private void reloadTree()
	{
		tree.getChildren().clear();
		List<C> list = facade.all(cClassification,realm,rref);
		logger.debug(cClassification.getSimpleName()+" "+list.size());
		TreeHelper.buildTree(tree,list,treePath);
	}
	
	public void saveClassification() throws JeeslConstraintViolationException, JeeslLockingException
	{
		boolean isNew = EjbIdFactory.isUnSaved(classification);
		classification = facade.save(classification);
		if(isNew)
		{
			tree.getChildren().add(new DefaultTreeNode(classification));
		}
		treePath.clear();TreeHelper.fillPath(treePath,classification);
		this.reloadTree();
	}
	
	public void onDragDrop(TreeDragDropEvent event) throws JeeslConstraintViolationException, JeeslLockingException
	{
		TreeHelper.persistDragDropEvent(facade,event);
    }
	
	@SuppressWarnings("unchecked")
	public void onNodeSelect(NodeSelectEvent event)
	{
		this.reset(true);
		logger.info("Selected "+event.getTreeNode().toString());
		classification = (C)event.getTreeNode().getData();
		classification = facade.find(cClassification,classification);
		classification = efLang.persistMissingLangs(facade,lp.getLocales(),classification);
	}
	
	public void handleFileUpload(FileUploadEvent event) throws JeeslConstraintViolationException, JeeslLockingException
	{
		UploadedFile file = event.getFile();
		logger.info("Received file with a size of " +file.getSize());
		if(classification.getGraphic()==null)
		{
			GT gt = fGraphic.fByEnum(fbSvg.getClassGraphicType(), JeeslGraphicType.Code.svg);
			G g = fbSvg.efGraphic().build(gt);
			g = facade.save(g);
			classification.setGraphic(g);
			classification = facade.save(classification);
			classification.getGraphic().setData(file.getContent());
			classification = facade.save(classification);
		}
		else
		{
			try
			{
				G g = fGraphic.fGraphic(cClassification,classification);
				g.setData(file.getContent());
				facade.save(g);
			}
			catch (JeeslNotFoundException e) {e.printStackTrace();}
		}
	}
}