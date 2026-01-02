package org.jeesl.web.mbean.prototype.module.its;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.module.its.JeeslItsCacheBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.module.JeeslItsFacade;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.module.ItsFactoryBuilder;
import org.jeesl.interfaces.bean.sb.bean.SbSingleBean;
import org.jeesl.interfaces.bean.sb.handler.SbSingleSelection;
import org.jeesl.interfaces.model.module.its.config.JeeslItsConfig;
import org.jeesl.interfaces.model.module.its.config.JeeslItsConfigOption;
import org.jeesl.interfaces.model.module.its.issue.JeeslItsIssue;
import org.jeesl.interfaces.model.module.its.issue.JeeslItsIssueStatus;
import org.jeesl.interfaces.model.module.its.task.JeeslItsTask;
import org.jeesl.interfaces.model.module.its.task.JeeslItsTaskType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
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

public abstract class AbstractItsIssueBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
    										R extends JeeslTenantRealm<L,D,R,?>, RREF extends EjbWithId,
    										C extends JeeslItsConfig<L,D,R,O>,
    										O extends JeeslItsConfigOption<L,D,O,?>,
    										I extends JeeslItsIssue<R,I,IS>,
    										IS extends JeeslItsIssueStatus<L,D,R,IS,?>,
    										T extends JeeslItsTask<I,TT,?>,
    										TT extends JeeslItsTaskType<L,D,TT,?>>
					extends AbstractAdminBean<L,D,LOC>
					implements Serializable, SbSingleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractItsIssueBean.class);
	
	private JeeslItsFacade<L,D,R,C,O,I,IS,T,TT> fIssue;
	private JeeslItsCacheBean<L,D,R,RREF,IS> bItsCache;

	protected final ItsFactoryBuilder<L,D,R,C,O,I,IS,T,TT> fbIssue;

	private final TreeHelper<I> thIssue;
	
	private TreeNode tree; public TreeNode getTree() {return tree;}
    private TreeNode node; public TreeNode getNode() {return node;} public void setNode(TreeNode node) {this.node = node;}

    protected R realm;
    protected RREF rref; public RREF getRref() {return rref;}
	protected I root;
    protected I issue;  public I getIssue() {return issue;} public void setType(I issue) {this.issue = issue;}

	public AbstractItsIssueBean(ItsFactoryBuilder<L,D,R,C,O,I,IS,T,TT> fbIssue/*, SvgFactoryBuilder<L,D,G,GT,F,FS> fbSvg*/)
	{
		super(fbIssue.getClassL(),fbIssue.getClassD());	
		this.fbIssue=fbIssue;
		
		thIssue  = TreeHelper.instance();
	}

	protected void postConstructIssue(JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage,
									JeeslItsCacheBean<L,D,R,RREF,IS> bItsCache,
									JeeslItsFacade<L,D,R,C,O,I,IS,T,TT> fIssue,
									R realm)
	{
		super.initJeeslAdmin(bTranslation,bMessage);
		this.bItsCache=bItsCache;
		this.fIssue=fIssue;
		this.realm=realm;
	}
	
	protected void updateRealmReference(RREF rref)
	{
		this.rref=rref;
		bItsCache.reloadRealm(realm,rref);
		reloadTree();
	}
	
	@Override public void selectSbSingle(SbSingleSelection handler, EjbWithId item) throws JeeslLockingException, JeeslConstraintViolationException
	{
		reloadTree();
	}
	
	@SuppressWarnings("unchecked")
	protected void reloadTree()
	{
		List<Long> expandedNodes = thIssue.findNodes(this.tree, node -> node.isExpanded()).stream().map(node -> (I)node.getData()).filter(data -> data != null).map(data -> data.getId()).collect(Collectors.toList());
		
		
		if(bItsCache.getMapStatus().containsKey(rref) && !bItsCache.getMapStatus().isEmpty())
		{
			IS status = bItsCache.getMapStatus().get(rref).get(0);
			root = fIssue.fcAItsRoot(realm,rref,status);
		}
		else {return;}
		
		tree = new DefaultTreeNode();
		this.issue = null;
		TreeHelper.buildTree(this.fIssue, this.tree, this.fIssue.allForParent(this.fbIssue.getClassIssue(), this.root), this.fbIssue.getClassIssue());
		
		thIssue.findNodes(this.tree, node -> node.getData() != null && expandedNodes.contains(((I)node.getData()).getId())).forEach(node -> node.setExpanded(true));
	}
	
	private void reset(boolean rIssue)
	{
		if(rIssue) {issue=null;}
	}
	
	public void addIssue()
	{
		I parent=null; if(issue!=null) {parent = issue;} else {parent = root;}
		issue = fbIssue.ejbIssue().build(realm,rref,parent);
	}
	
	public void cancelIssue()
	{
		if (this.node != null)
		{
			this.node.setSelected(false);
		}
		this.issue = null;
	}
	
	public void saveIssue() throws JeeslConstraintViolationException, JeeslLockingException
	{
		issue = fIssue.save(issue);
		reloadTree();
	}
	
	public void deleteIssue() throws JeeslLockingException
	{
		try
		{
			fIssue.rm(issue);
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

        I parent = (I)dropNode.getData();
        int index=1;
        for(TreeNode n : dropNode.getChildren())
        {
        	I child =(I)n.getData();
    		child.setParent(parent);
    		child.setPosition(index);
    		fIssue.save(child);
    		index++;
        }
    }

    @SuppressWarnings("unchecked")
	public void onNodeSelect(NodeSelectEvent event)
    {
		logger.info("Selected "+event.getTreeNode().toString());
		issue = (I)event.getTreeNode().getData();
    }
}