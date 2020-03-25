package org.jeesl.web.mbean.prototype.system.security;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.jeesl.api.bean.JeeslSecurityBean;
import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.system.JeeslSecurityFacade;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.system.SecurityFactoryBuilder;
import org.jeesl.factory.ejb.system.security.EjbSecurityMenuFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.security.doc.JeeslSecurityHelp;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityAction;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityArea;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityCategory;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityMenu;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityTemplate;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityUsecase;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityView;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.jsf.helper.TreeHelper;
import org.jeesl.model.xml.system.navigation.Menu;
import org.jeesl.model.xml.system.navigation.MenuItem;
import org.primefaces.event.NodeCollapseEvent;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.TreeDragDropEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractAdminSecurityMenuBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
											C extends JeeslSecurityCategory<L,D>,
											R extends JeeslSecurityRole<L,D,C,V,U,A,USER>,
											V extends JeeslSecurityView<L,D,C,R,U,A>,
											U extends JeeslSecurityUsecase<L,D,C,R,V,A>,
											A extends JeeslSecurityAction<L,D,R,V,U,AT>,
											AT extends JeeslSecurityTemplate<L,D,C>,
											M extends JeeslSecurityMenu<V,M>,
											AR extends JeeslSecurityArea<L,D,V>,
											H extends JeeslSecurityHelp<L,D,V>,
											USER extends JeeslUser<R>>
		extends AbstractAdminSecurityBean<L,D,LOC,C,R,V,U,A,AT,M,AR,H,USER>
		implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractAdminSecurityMenuBean.class);
	
	private final EjbSecurityMenuFactory<V,M> efMenu;
	
	private TreeNode tree; public TreeNode getTree() {return tree;}
	private TreeNode node; public TreeNode getNode() {return node;} public void setNode(TreeNode node) {this.node = node;}
	private M menu; public M getMenu() {return menu;}
	
	public AbstractAdminSecurityMenuBean(SecurityFactoryBuilder<L,D,C,R,V,U,A,AT,M,AR,H,USER> fbSecurity)
	{
		super(fbSecurity);
		efMenu = fbSecurity.ejbMenu();
	}
	
	public void initSuper(JeeslSecurityFacade<L,D,C,R,V,U,A,AT,M,USER> fSecurity, JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage, JeeslSecurityBean<L,D,C,R,V,U,A,AT,M,USER> bSecurity)
	{
		super.postConstructSecurity(fSecurity,bTranslation,bMessage,bSecurity);
		opViews = fSecurity.all(fbSecurity.getClassView());
		
		if(fSecurity.all(fbSecurity.getClassMenu(),1).isEmpty()) {firstInit();}
		Map<V,M> map = efMenu.toMapView(fSecurity.all(fbSecurity.getClassMenu()));
		
		for(V v : opViews)
		{
			if(!map.containsKey(v))
			{
				try
				{
					M m = efMenu.create(v);
					fSecurity.save(m);
				}
				catch (JeeslConstraintViolationException e) {e.printStackTrace();}
				catch (JeeslLockingException e) {e.printStackTrace();}
			}
		}
		reload();
	}
	
	protected void firstInit() {}
	protected void firstInit(Menu xml)
	{
		firstInit(null,xml.getMenuItem());
	}
	
	private void firstInit(M parent, List<MenuItem> list)
	{
		int i = 1;
		for(MenuItem item : list)
		{
			try
			{
				V v = fSecurity.fByCode(fbSecurity.getClassView(),item.getView().getCode());
				M m = efMenu.create(v);
				m.setPosition(i);i++;
				m.setParent(parent);
				m = fSecurity.save(m);
				if(!item.getMenuItem().isEmpty()) {firstInit(m,item.getMenuItem());}
			}
			catch (JeeslNotFoundException e) {logger.error(e.getMessage());}
			catch (JeeslConstraintViolationException e) {logger.error("Duplicate for "+item.getCode());e.printStackTrace();}
			catch (JeeslLockingException e) {e.printStackTrace();}
		}
	}
	
	public void reload()
    {
		List<M> list = fSecurity.all(fbSecurity.getClassMenu());
		Map<M,List<M>> map = efMenu.toMapChild(list);
	    	tree = new DefaultTreeNode(null, null);
	    	
	    	buildTree(tree, efMenu.toListRoot(list),map);
    }
	    
	private void buildTree(TreeNode parent, List<M> items, Map<M,List<M>> map)
	{
		for(M menu : items)
		{
			TreeNode n = new DefaultTreeNode(menu, parent);
			if(map.containsKey(menu))
			{
				buildTree(n, map.get(menu),map);
			}
		}
	}
	
	public void expandTree()
	{
		TreeHelper.setExpansion(this.node != null ? this.node : this.tree, true);
	}
	
	public void collapseTree()
	{
		TreeHelper.setExpansion(this.tree,  false);
	}
	
	public boolean isExpanded()
	{
		return this.tree != null && this.tree.getChildren().stream().filter(node -> node.isExpanded()).count() > 1;
	}
	
	public void onNodeExpand(NodeExpandEvent event) {if(debugOnInfo) {logger.info("Expanded "+event.getTreeNode().toString());}}
    public void onNodeCollapse(NodeCollapseEvent event) {if(debugOnInfo) {logger.info("Collapsed "+event.getTreeNode().toString());}}
	
	@SuppressWarnings("unchecked")
	public void onDragDrop(TreeDragDropEvent event) throws JeeslConstraintViolationException, JeeslLockingException
	{
        TreeNode dragNode = event.getDragNode();
        TreeNode dropNode = event.getDropNode();
        int dropIndex = event.getDropIndex();
        if(debugOnInfo) {logger.info("Dragged " + dragNode.getData() + "Dropped on " + dropNode.getData() + " at " + dropIndex);}
        
        M parent = (M)dropNode.getData();
        int index=1;
        for(TreeNode n : dropNode.getChildren())
        {
        		M child =(M)n.getData();
        		child = fSecurity.find(fbSecurity.getClassMenu(),child);
        		child.setParent(parent);
        		child.setPosition(index);
        		fSecurity.save(child);
        		index++;
        }
        propagateChanges();
	}	
	protected abstract void propagateChanges();
	
    @SuppressWarnings("unchecked")
	public void onNodeSelect(NodeSelectEvent event)
    {
    		logger.info("Selected "+event.getTreeNode().toString());
    		menu = (M)event.getTreeNode().getData();
    }
}