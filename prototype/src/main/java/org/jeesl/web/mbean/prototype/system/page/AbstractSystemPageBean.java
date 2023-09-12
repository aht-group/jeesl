package org.jeesl.web.mbean.prototype.system.page;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.system.JeeslSecurityFacade;
import org.jeesl.factory.builder.system.SecurityFactoryBuilder;
import org.jeesl.factory.ejb.system.security.EjbSecurityMenuFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.security.context.JeeslSecurityContext;
import org.jeesl.interfaces.model.system.security.context.JeeslSecurityMenu;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityView;
import org.jeesl.web.mbean.prototype.system.AbstractAdminBean;
import org.primefaces.event.NodeCollapseEvent;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractSystemPageBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
											V extends JeeslSecurityView<L,D,?,?,?,?>,
											CTX extends JeeslSecurityContext<L,D>,
											M extends JeeslSecurityMenu<L,V,CTX,M>>
		extends AbstractAdminBean<L,D,LOC>
		implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractSystemPageBean.class);
	
	private final SecurityFactoryBuilder<L,D,?,?,V,?,?,?,?,M,?,?,?,?,?,?> fbSecurity;
	private final EjbSecurityMenuFactory<V,CTX,M> efMenu;
	
	private JeeslSecurityFacade<?,?,V,?,?,M,?> fSecurity;
	
	private TreeNode tree; public TreeNode getTree() {return tree;}
	private TreeNode node; public TreeNode getNode() {return node;} public void setNode(TreeNode node) {this.node = node;}
	private M menu; public M getMenu() {return menu;}
	
	public AbstractSystemPageBean(SecurityFactoryBuilder<L,D,?,?,V,?,?,?,CTX,M,?,?,?,?,?,?> fbSecurity)
	{
		super(fbSecurity.getClassL(),fbSecurity.getClassD());
		this.fbSecurity=fbSecurity;
		efMenu = fbSecurity.ejbMenu();
	}
	
	public void postConstructSystemPage(JeeslSecurityFacade<?,?,V,?,?,M,?> fSecurity, JeeslTranslationBean<L,D,?> bTranslation, JeeslFacesMessageBean bMessage)
	{
		this.fSecurity=fSecurity;
		reload();
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
	
	public void onNodeExpand(NodeExpandEvent event) {if(debugOnInfo) {logger.info("Expanded "+event.getTreeNode().toString());}}
    public void onNodeCollapse(NodeCollapseEvent event) {if(debugOnInfo) {logger.info("Collapsed "+event.getTreeNode().toString());}}
	
    @SuppressWarnings("unchecked")
	public void onNodeSelect(NodeSelectEvent event)
    {
    		logger.info("Selected "+event.getTreeNode().toString());
    		menu = (M)event.getTreeNode().getData();
    }
}