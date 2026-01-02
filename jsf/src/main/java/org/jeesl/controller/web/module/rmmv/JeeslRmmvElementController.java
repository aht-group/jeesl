package org.jeesl.controller.web.module.rmmv;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.module.JeeslRmmvFacade;
import org.jeesl.controller.web.AbstractJeeslLocaleWebController;
import org.jeesl.controller.web.util.AbstractLogMessage;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.module.RmmvFactoryBuilder;
import org.jeesl.factory.ejb.module.rmmv.EjbRmmvConfigFactory;
import org.jeesl.factory.ejb.util.EjbIdFactory;
import org.jeesl.interfaces.bean.sb.bean.SbSingleBean;
import org.jeesl.interfaces.bean.sb.handler.SbSingleSelection;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.controller.web.module.rmmv.JeeslRmmvElementCallback;
import org.jeesl.interfaces.model.module.rmmv.JeeslRmmvModule;
import org.jeesl.interfaces.model.module.rmmv.JeeslRmmvModuleConfig;
import org.jeesl.interfaces.model.module.rmmv.JeeslRmmvClassification;
import org.jeesl.interfaces.model.module.rmmv.JeeslRmmvElement;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.jsf.handler.sb.SbSingleHandler;
import org.jeesl.jsf.helper.TreeHelper;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.TreeDragDropEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslRmmvElementController <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
											R extends JeeslTenantRealm<L,D,R,?>, RREF extends EjbWithId,
											E extends JeeslRmmvElement<L,R,E,EC>,
											EC extends JeeslRmmvClassification<L,R,EC,?>,
											MOD extends JeeslRmmvModule<?,?,MOD,?>,
											MC extends JeeslRmmvModuleConfig<E,MOD>>
		extends AbstractJeeslLocaleWebController<L,D,LOC>
		implements SbSingleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslRmmvElementController.class);
	
	private final JeeslRmmvElementCallback<MC> callback;
	private JeeslRmmvFacade<L,D,R,E,EC,MOD,MC,?,?,?> fRmmv;
	
	private final RmmvFactoryBuilder<L,D,LOC,R,E,EC,MOD,MC,?,?,?> fbRmmv;
	private final EjbRmmvConfigFactory<E,MOD,MC> efConfig;
	
	private final SbSingleHandler<LOC> sbhLocale; public SbSingleHandler<LOC> getSbhLocale() {return sbhLocale;}
	
	private final Set<E> treePath;
	
	private final List<MOD> modules; public List<MOD> getModules() {return modules;}
	private final List<MC> configs; public List<MC> getConfigs() {return configs;}
	private final List<EC> classifications; public List<EC> getClassifications() {return classifications;}

	protected R realm;
	protected RREF rref;
	private TreeNode tree; public TreeNode getTree() {return tree;}
	private TreeNode node; public TreeNode getNode() {return node;} public void setNode(TreeNode node) {this.node = node;}
	
	private E element; public E getElement() {return element;} public void setElement(E element) {this.element = element;}
	private MC config; public MC getConfig() {return config;} public void setConfig(MC config) {this.config = config;}
	
	public JeeslRmmvElementController(final JeeslRmmvElementCallback<MC> callback,
										final RmmvFactoryBuilder<L,D,LOC,R,E,EC,MOD,MC,?,?,?> fbRmmv)
	{
		super(fbRmmv.getClassL(),fbRmmv.getClassD());
		this.callback=callback;
		this.fbRmmv=fbRmmv;
        
		efConfig = fbRmmv.ejbConfig();
		
		sbhLocale = new SbSingleHandler<>(fbRmmv.getClassLocale(),this);
		
		treePath = new HashSet<>();
		
		modules = new ArrayList<>();
		configs = new ArrayList<>();
		classifications = new ArrayList<>();
	}
	
	public void postConstructTreeElement(JeeslRmmvFacade<L,D,R,E,EC,MOD,MC,?,?,?> fRmmv,
									JeeslLocaleProvider<LOC> lp, JeeslFacesMessageBean bMessage,
									R realm)
	{
		super.postConstructLocaleWebController(lp,bMessage);
		this.fRmmv=fRmmv;
		this.realm=realm;
		
		sbhLocale.setList(lp.getLocales());
		sbhLocale.setDefault();
		
		modules.addAll(fRmmv.allOrderedPositionVisible(fbRmmv.getClassModule()));
	}
	
	public void updateRealmReference(RREF rref)
	{
		this.rref=rref;
		tree = new DefaultTreeNode("Root", null);
	
		reloadTree();
		classifications.addAll(fRmmv.all(fbRmmv.getClassClasification(),realm,rref));
	}
	
	@Override
	public void selectSbSingle(SbSingleSelection handler, EjbWithId item) throws JeeslLockingException, JeeslConstraintViolationException
	{
		// TODO Auto-generated method stub
	}
	
	private void reset(boolean rElement, boolean rConfigs, boolean rConfig)
	{
		if(rElement) {element=null;}
		if(rConfigs) {configs.clear();}
		if(rConfig) {config=null;}
	}

	private void reloadTree()
	{
		tree.getChildren().clear();
		List<E> list = fRmmv.all(fbRmmv.getClassTreeElement(),realm,rref);
		logger.debug(fbRmmv.getClassTreeElement().getSimpleName()+" "+list.size());
		TreeHelper.buildTree(tree,list,treePath);
	}
	
	public void addElement()
	{
		this.reset(true,true,true);
		element = fbRmmv.ejbElement().build(realm,rref);
		element.setName(efLang.buildEmpty(lp.getLocales()));
	}
	
	public void saveElement() throws JeeslConstraintViolationException, JeeslLockingException
	{
		boolean isNew = EjbIdFactory.isUnSaved(element);
		element = fRmmv.save(element);
		if(isNew)
		{
			tree.getChildren().add(new DefaultTreeNode(element));
		}
		treePath.clear();TreeHelper.fillPath(treePath,element);
		this.reloadTree();
	}
	
	public void onDragDrop(TreeDragDropEvent event) throws JeeslConstraintViolationException, JeeslLockingException
	{
		TreeHelper.persistDragDropEvent(fRmmv,event);
    }
	
	@SuppressWarnings("unchecked")
	public void onNodeSelect(NodeSelectEvent event)
	{
		this.reset(true,true,true);
		logger.info("Selected "+event.getTreeNode().toString());
		element = (E)event.getTreeNode().getData();
		element = fRmmv.find(fbRmmv.getClassTreeElement(),element);
		element = efLang.persistMissingLangs(fRmmv,lp.getLocales(),element);
		
		reloadConfgs();
	}
	
	protected void reloadConfgs()
	{
		this.reset(false,true,false);
		configs.addAll(fRmmv.allForParent(fbRmmv.getClassConfig(),element));
	}
	
	public void selectConfig()
	{
		logger.info(AbstractLogMessage.selectEntity(config));
		callback.postConfigSelect(config);
	}
	
	public void addConfig()
	{
		logger.info(AbstractLogMessage.createEntity(fbRmmv.getClassConfig()));
		config = fbRmmv.ejbConfig().build(element,configs);
	}
	
	public void saveConfig() throws JeeslConstraintViolationException, JeeslLockingException
	{
		logger.info(AbstractLogMessage.saveEntity(config));
		efConfig.converter(fRmmv,config);
		config = fRmmv.save(config);
		reloadConfgs();
		callback.postConfigSave(config);
	}
}