package org.jeesl.web.mbean.prototype.io.dms;

import java.io.Serializable;
import java.util.List;

import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoDmsFacade;
import org.jeesl.controller.web.util.AbstractLogMessage;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.io.IoDmsFactoryBuilder;
import org.jeesl.factory.ejb.util.EjbIdFactory;
import org.jeesl.interfaces.bean.sb.handler.SbSingleSelection;
import org.jeesl.interfaces.model.io.dms.JeeslIoDms;
import org.jeesl.interfaces.model.io.dms.JeeslIoDmsDocument;
import org.jeesl.interfaces.model.io.dms.JeeslIoDmsLayer;
import org.jeesl.interfaces.model.io.dms.JeeslIoDmsSection;
import org.jeesl.interfaces.model.io.dms.JeeslIoDmsView;
import org.jeesl.interfaces.model.io.domain.JeeslDomainSet;
import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.io.fr.JeeslFileStorage;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeContainer;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeItem;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeSet;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.jsf.handler.sb.SbSingleHandler;
import org.primefaces.event.NodeCollapseEvent;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.TreeDragDropEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractAdminDmsTreeBean <L extends JeeslLang,D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
											DMS extends JeeslIoDms<L,D,STORAGE,AS,DS,S>,
											STORAGE extends JeeslFileStorage<L,D,?,?,?>,
											AS extends JeeslAttributeSet<L,D,?,?,?>,
											DS extends JeeslDomainSet<L,D,?>,
											S extends JeeslIoDmsSection<L,D,S>,
											F extends JeeslIoDmsDocument<L,S,FC,AC>,
											VIEW extends JeeslIoDmsView<L,D,DMS>,
											LAYER extends JeeslIoDmsLayer<VIEW,AI>,
											FC extends JeeslFileContainer<?,?>,
											AI extends JeeslAttributeItem<?,AS>,
											AC extends JeeslAttributeContainer<?,?>>
					extends AbstractDmsBean<L,D,LOC,DMS,STORAGE,AS,DS,S,F,VIEW,LAYER,FC,AI,AC>
					implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractAdminDmsTreeBean.class);

	protected final SbSingleHandler<DMS> sbhDms; public SbSingleHandler<DMS> getSbhDms() {return sbhDms;}

	private TreeNode tree; public TreeNode getTree() {return tree;}
	private TreeNode node; public TreeNode getNode() {return node;} public void setNode(TreeNode node) {this.node = node;}
	private S section; public S getSection() {return section;} public void setSection(S section) {this.section = section;}

	public AbstractAdminDmsTreeBean(IoDmsFactoryBuilder<L,D,LOC,DMS,STORAGE,S,F,VIEW,LAYER> fbDms)
	{
		super(fbDms);
		sbhDms = new SbSingleHandler<DMS>(fbDms.getClassDms(),this);
	}

	protected void initDmsConfig(JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage,JeeslIoDmsFacade<L,D,LOC,DMS,STORAGE,AS,DS,S,F,VIEW,FC,AC> fDms)
	{
		super.initDms(bTranslation,bMessage,fDms);
		initPageConfiguration();
		super.initLocales();
		sbhDms.silentCallback();
	}
	protected abstract void initPageConfiguration();


	@Override @SuppressWarnings("unchecked")
	public void selectSbSingle(SbSingleSelection handler, EjbWithId item) throws JeeslLockingException, JeeslConstraintViolationException
	{
		logger.info(AbstractLogMessage.selectEntity(item));
		this.dm = (DMS)item;
		reloadTree();
	}

	private void reloadTree()
	{
		if(debugOnInfo) {logger.info("Reloading Tree");}
		S root = fDms.load(dm.getRoot(),true);
		tree = new DefaultTreeNode(root, null);
		buildTree(tree,root.getSections());
	}

	private void buildTree(TreeNode parent, List<S> sections)
	{
		for(S s : sections)
		{
			TreeNode n = new DefaultTreeNode(s, parent);
			if(!s.getSections().isEmpty()) {buildTree(n,s.getSections());}
		}
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
        S parent = (S)dropNode.getData();
        int index=1;
        for(TreeNode n : dropNode.getChildren())
        {
        		S child =(S)n.getData();
        		S db = fDms.load(child,false);
        		efSection.update(db,child);
        		child.setSection(parent);
        		child.setPosition(index);
        		fDms.save(child);
        		index++;
        }
    }

    @SuppressWarnings("unchecked")
	public void onSectionSelect(NodeSelectEvent event)
    {
    		logger.info("Selected "+event.getTreeNode().toString());
    		section = (S)event.getTreeNode().getData();
    		section = efLang.persistMissingLangs(fDms, sbhLocale.getList(), section);
    		section = efDescription.persistMissingLangs(fDms, sbhLocale.getList(), section);
    		S db = fDms.load(section,false);
    		efSection.update(db,section);
    }


	public void addSection()
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.createEntity(fbDms.getClassSection()));}
		section = efSection.build(dm.getRoot());
		section.setName(efLang.buildEmpty(sbhLocale.getList()));
		section.setDescription(efDescription.buildEmpty(sbhLocale.getList()));
	}

	public void saveSection() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.saveEntity(section));}
		boolean appendToTree = EjbIdFactory.isUnSaved(section);
		section = fDms.save(section);
		if(appendToTree) {new DefaultTreeNode(section, tree);}
//		reloadSection();
	}

	public void deleteSection()
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.deleteEntity(section));}
		reloadTree();
	}

	public void cancelSection()
	{
		if(debugOnInfo){logger.info("Action canceled.");}
	}
}