package org.jeesl.web.controller.module.rmmv;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.module.JeeslRmmvFacade;
import org.jeesl.api.facade.system.graphic.JeeslGraphicFacade;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.module.RmmvFactoryBuilder;
import org.jeesl.factory.builder.system.SvgFactoryBuilder;
import org.jeesl.factory.ejb.util.EjbIdFactory;
import org.jeesl.interfaces.bean.sb.bean.SbSingleBean;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.controller.web.module.rmmv.JeeslRmmvClassificationCallback;
import org.jeesl.interfaces.model.module.rmmv.JeeslRmmvClassification;
import org.jeesl.interfaces.model.module.rmmv.JeeslRmmvElement;
import org.jeesl.interfaces.model.module.rmmv.JeeslRmmvModule;
import org.jeesl.interfaces.model.module.rmmv.JeeslRmmvModuleConfig;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphicType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
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

public class JeeslRmmvClassificationController <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
											G extends JeeslGraphic<L,D,GT,?,?>, GT extends JeeslGraphicType<L,D,GT,G>,
											R extends JeeslTenantRealm<L,D,R,?>, RREF extends EjbWithId,
											E extends JeeslRmmvElement<L,R,E,EC>,
											EC extends JeeslRmmvClassification<L,R,EC,G>,
											MOD extends JeeslRmmvModule<?,?,MOD,?>,
											MC extends JeeslRmmvModuleConfig<E,MOD>>
		extends AbstractJeeslWebController<L,D,LOC>
		implements SbSingleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslRmmvClassificationController.class);
	
	@SuppressWarnings("unused")
	private final JeeslRmmvClassificationCallback callback;
	
	private JeeslRmmvFacade<L,D,R,E,EC,MOD,MC> fRmmv;
	private JeeslGraphicFacade<L,D,?,G,GT,?,?> fGraphic;
	
	private final RmmvFactoryBuilder<L,D,LOC,R,E,EC,MOD,MC> fbRmmv;
	private final SvgFactoryBuilder<L,D,G,GT,?,?> fbSvg;
	
	protected final SbSingleHandler<LOC> sbhLocale; public SbSingleHandler<LOC> getSbhLocale() {return sbhLocale;}
	
	private final Set<EC> treePath;

	protected R realm;
	protected RREF rref;
	private TreeNode tree; public TreeNode getTree() {return tree;}
	private TreeNode node; public TreeNode getNode() {return node;} public void setNode(TreeNode node) {this.node = node;}
	
	private EC classification;  public EC getClassification() {return classification;} public void setClassification(EC classification) {this.classification = classification;}

	public JeeslRmmvClassificationController(final JeeslRmmvClassificationCallback callback,
												final RmmvFactoryBuilder<L,D,LOC,R,E,EC,MOD,MC> fbRmmv,
												final SvgFactoryBuilder<L,D,G,GT,?,?> fbSvg)
	{
		super(fbRmmv.getClassL(),fbRmmv.getClassD());
		this.callback=callback;
		this.fbRmmv=fbRmmv;
		this.fbSvg=fbSvg;
        
		sbhLocale = new SbSingleHandler<>(fbRmmv.getClassLocale(),this);
		
		treePath = new HashSet<>();
	}
	
	public void postConstructTreeElement(JeeslRmmvFacade<L,D,R,E,EC,MOD,MC> fRmmv,
									JeeslGraphicFacade<L,D,?,G,GT,?,?> fGraphic,
									JeeslLocaleProvider<LOC> lp, JeeslFacesMessageBean bMessage,
									R realm)
	{
		super.postConstructWebController(lp);
		this.fRmmv=fRmmv;
		this.fGraphic=fGraphic;
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
	
	public void cancelClassification() {}
	private void reset(boolean rClassification)
	{
		if(rClassification) {classification=null;}
	}

	private void reloadTree()
	{
		tree.getChildren().clear();
		List<EC> list = fRmmv.all(fbRmmv.getClassClasification(),realm,rref);
		logger.debug(fbRmmv.getClassTreeElement().getSimpleName()+" "+list.size());
		TreeHelper.buildTree(tree,list,treePath);
	}
	
	public void addClassification()
	{
		this.reset(true);
		classification = fbRmmv.ejbClassification().build(realm,rref);
		classification.setName(efLang.createEmpty(lp.getLocales()));
	}
	
	public void saveClassification() throws JeeslConstraintViolationException, JeeslLockingException
	{
		boolean isNew = EjbIdFactory.isUnSaved(classification);
		classification = fRmmv.save(classification);
		if(isNew)
		{
			tree.getChildren().add(new DefaultTreeNode(classification));
		}
		treePath.clear();TreeHelper.fillPath(treePath,classification);
		this.reloadTree();
	}
	
	public void onDragDrop(TreeDragDropEvent event) throws JeeslConstraintViolationException, JeeslLockingException
	{
		TreeHelper.persistDragDropEvent(fRmmv,event);
    }
	
	@SuppressWarnings("unchecked")
	public void onNodeSelect(NodeSelectEvent event)
	{
		this.reset(true);
		logger.info("Selected "+event.getTreeNode().toString());
		classification = (EC)event.getTreeNode().getData();
		classification = fRmmv.find(fbRmmv.getClassClasification(),classification);
		classification = efLang.persistMissingLangs(fRmmv,lp.getLocales(),classification);
	}
	
	public void handleFileUpload(FileUploadEvent event) throws JeeslConstraintViolationException, JeeslLockingException
	{
		UploadedFile file = event.getFile();
		logger.info("Received file with a size of " +file.getSize());
		if(classification.getGraphic()==null)
		{
			GT gt = fGraphic.fByEnum(fbSvg.getClassGraphicType(), JeeslGraphicType.Code.svg);
			G g = fbSvg.efGraphic().build(gt);
			g = fRmmv.save(g);
			classification.setGraphic(g);
			classification = fRmmv.save(classification);
			classification.getGraphic().setData(file.getContent());
			classification = fRmmv.save(classification);
		}
		else
		{
			try
			{
				G g = fGraphic.fGraphic(fbRmmv.getClassClasification(),classification);
				g.setData(file.getContent());
				fRmmv.save(g);
			}
			catch (JeeslNotFoundException e) {e.printStackTrace();}
		}
	}
}