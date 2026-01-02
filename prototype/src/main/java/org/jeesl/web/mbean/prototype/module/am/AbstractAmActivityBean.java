package org.jeesl.web.mbean.prototype.module.am;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.module.JeeslAmFacade;
import org.jeesl.controller.web.util.AbstractLogMessage;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.module.AmFactoryBuilder;
import org.jeesl.factory.ejb.module.am.EjbActivityFactory;
import org.jeesl.factory.ejb.module.am.EjbActivityProjectFactory;
import org.jeesl.factory.ejb.util.EjbIdFactory;
import org.jeesl.interfaces.bean.sb.bean.SbSingleBean;
import org.jeesl.interfaces.bean.sb.bean.SbToggleBean;
import org.jeesl.interfaces.bean.sb.handler.SbSingleSelection;
import org.jeesl.interfaces.bean.sb.handler.SbToggleSelection;
import org.jeesl.interfaces.model.module.am.JeesAmProject;
import org.jeesl.interfaces.model.module.am.JeeslAmActivity;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.jsf.handler.sb.SbSingleHandler;
import org.jeesl.jsf.helper.TreeHelper;
import org.jeesl.web.mbean.prototype.system.AbstractAdminBean;
import org.primefaces.event.NodeCollapseEvent;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.TreeDragDropEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractAmActivityBean <L extends JeeslLang,D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
										REALM extends JeeslTenantRealm<L,D,REALM,?>,
										ACTIVITY extends JeeslAmActivity<L,D,REALM,ACTIVITY,PROJ>,
										PROJ extends JeesAmProject<L,D,REALM,ACTIVITY,PROJ>
										>
					extends AbstractAdminBean<L,D,LOC>
					implements Serializable,SbToggleBean,SbSingleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractAmActivityBean.class);

	private final AmFactoryBuilder<L,D,LOC,REALM,ACTIVITY,PROJ> fbAm;

	protected JeeslAmFacade<L,D,LOC,REALM,ACTIVITY,PROJ> fAm;

	private final TreeHelper<ACTIVITY> thActivity;

	protected String[] amLocales; public String[] getAmLocales() {return amLocales;}

	protected final EjbActivityFactory<REALM,ACTIVITY,PROJ> efActivity;
	protected final EjbActivityProjectFactory<REALM,ACTIVITY,PROJ> efProject;

	protected final SbSingleHandler<PROJ> sbhProject; public SbSingleHandler<PROJ> getSbhProject() {return sbhProject;}
	private final SbSingleHandler<LOC> sbhLocale; public SbSingleHandler<LOC> getSbhLocale() {return sbhLocale;}


	protected PROJ project; public PROJ getProject() {return project;} public void setProject(PROJ project) {this.project = project;}
	private ACTIVITY activity; public ACTIVITY getActivity() {return activity;} public void setActivity(ACTIVITY activity) {this.activity = activity;}
	private List<ACTIVITY> activityItems; public List<ACTIVITY> getActivityItems() {return activityItems;} public void setActivityItems(List<ACTIVITY> activityItems) {this.activityItems = activityItems;}


	private TreeNode tree; public TreeNode getTree() {return tree;}
    private TreeNode node; public TreeNode getNode() {return node;} public void setNode(TreeNode node) {this.node = node;}
	private String currentLocaleCode; public String getCurrentLocaleCode() {return currentLocaleCode;} public void setCurrentLocaleCode(String currentLocaleCode) {this.currentLocaleCode = currentLocaleCode;}


	public AbstractAmActivityBean(AmFactoryBuilder<L,D,LOC,REALM,ACTIVITY,PROJ> fbAm)
	{
		super(fbAm.getClassL(),fbAm.getClassD());
		this.fbAm=fbAm;
		
		thActivity = TreeHelper.instance();

		efProject = fbAm.ejbProject();
		efActivity = fbAm.ejbActivity();

		sbhProject = new SbSingleHandler<>(fbAm.getClassProject(),this);
		sbhLocale = new SbSingleHandler<>(fbAm.getClassLocale(),this);

		activityItems = new ArrayList<>();
	}

	protected void postConstructAm(JeeslTranslationBean<L,D,LOC> bTranslation, String currentLocaleCode,
									List<LOC> locales, JeeslFacesMessageBean bMessage,
									JeeslAmFacade<L,D,LOC,REALM,ACTIVITY,PROJ> fAm)
	{
		super.initJeeslAdmin(bTranslation,bMessage);
		this.setCurrentLocaleCode(currentLocaleCode);
		this.fAm=fAm;
		sbhProject.setList(fAm.allOrderedPosition(fbAm.getClassProject()));
		sbhProject.setDefault();
		project = sbhProject.getSelection();
		reloadProject();
	}


	@Override public void toggled(SbToggleSelection handler, Class<?> c) throws JeeslLockingException, JeeslConstraintViolationException
	{
		logger.info(AbstractLogMessage.toggled(c));
	}

	@SuppressWarnings("unchecked")
	@Override public void selectSbSingle(SbSingleSelection handler, EjbWithId ejb)
	{
		if(ejb==null) {project=null;}
		else if(JeesAmProject.class.isAssignableFrom(ejb.getClass()))
		{
			project = (PROJ)ejb;
			if(EjbIdFactory.isSaved(project))
			{
				reloadProject();
			}
			logger.info("Twice:"+sbhProject.getTwiceSelected()+" for "+project.toString());
		}
		else
		{
			logger.info("NOT Assignable");
		}
		reset(true);
	}

	private void reset(boolean rActivity)
	{
		if(rActivity) {activity=null;}
	}



	private void reloadProject()
	{
		if(Objects.nonNull(project))
		{
			project = fAm.find(fbAm.getClassProject(),project);
			reloadTree();
		}
	}

	private void reloadTree()
	{
		ACTIVITY root = project.getRoot();
		if(Objects.isNull(root)) {root = efActivity.build(project, null); root.setName(project.getName());}

		tree = new DefaultTreeNode(root, null);
		List<ACTIVITY> activities = fAm.allOrderedPositionParent(fbAm.getClassActivity(),root,true);
		if(Objects.nonNull(tree) && Objects.nonNull(activities))
		{
			buildTree(tree,activities);
		}
	}

	private void buildTree(TreeNode parent, List<ACTIVITY> activities)
	{
		for(ACTIVITY s : activities)
		{
			TreeNode n = new DefaultTreeNode(s, parent);
			List<ACTIVITY> childActivities = fAm.allOrderedPositionParent(fbAm.getClassActivity(),s,true);
			if(!childActivities.isEmpty()) {buildTree(n,childActivities);}
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
        ACTIVITY parent = (ACTIVITY)dropNode.getData();
        int index=1;
        for(TreeNode n : dropNode.getChildren())
        {
        	ACTIVITY child =(ACTIVITY)n.getData();
        	ACTIVITY db = fAm.find(fbAm.getClassActivity(),child);
    		efActivity.update(db,child);
    		child.setParent(parent);
    		child.setPosition(index);
    		fAm.save(child);
    		index++;
        }
    }

    @SuppressWarnings("unchecked")
	public void onActivitySelect(NodeSelectEvent event)
    {
		logger.info("Selected "+event.getTreeNode().toString());
		activity = (ACTIVITY)event.getTreeNode().getData();
		ACTIVITY db = fAm.find(fbAm.getClassActivity(),activity);
		efActivity.update(db,activity);
		reloadActivity();
    }

	public void addActivity()
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.createEntity(fbAm.getClassActivity()));}
		activity = efActivity.build(project.getRoot());
		activity.setStructural(true);
		activity.setName(efLang.createEmpty(localeCodes));
		activity.setDescription(efDescription.createEmpty(localeCodes));
	}

	public void saveActivity() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.saveEntity(activity));}
		boolean appendToTree = EjbIdFactory.isUnSaved(activity);

		activity = fAm.save(activity);
		if(appendToTree) {new DefaultTreeNode(activity,tree);}
		reloadActivity();
	}

	protected void reloadActivity()
	{
		List<ACTIVITY> activityItemstest = fAm.allForParent(fbAm.getClassActivity(), activity);
		activityItems.clear();

		for (ACTIVITY activityItem : activityItemstest)
		{
			if(!activityItem.getStructural())
			{
				activityItems.add(activityItem);
			}
		}
	}

	public void rmActivity()
	{
		fAm.deleteActivityTree(activity);
		activity = null;
		reloadTree();
	}

	public void cancelActivity()
	{
		activity = null;
	}

	public void expandTree()
	{
		thActivity.setExpansion(this.node != null ? this.node : this.tree, true);
	}

	public void collapseTree()
	{
		thActivity.setExpansion(this.tree,  false);
	}

	public boolean isExpanded()
	{
		return this.tree != null && this.tree.getChildren().stream().filter(node -> node.isExpanded()).count() > 1;
	}

}