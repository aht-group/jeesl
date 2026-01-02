package org.jeesl.controller.web.system.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.jeesl.api.bean.JeeslSecurityBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoCmsFacade;
import org.jeesl.api.facade.system.JeeslSecurityFacade;
import org.jeesl.controller.web.AbstractJeeslLocaleWebController;
import org.jeesl.controller.web.util.AbstractLogMessage;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.io.IoCmsFactoryBuilder;
import org.jeesl.factory.builder.system.SecurityFactoryBuilder;
import org.jeesl.factory.ejb.system.security.EjbSecurityMenuFactory;
import org.jeesl.interfaces.bean.sb.bean.SbSingleBean;
import org.jeesl.interfaces.bean.sb.handler.SbSingleSelection;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.controller.web.system.security.JeeslSecurityMenuCallback;
import org.jeesl.interfaces.model.io.cms.JeeslIoCms;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsSection;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityUsecase;
import org.jeesl.interfaces.model.system.security.context.JeeslSecurityContext;
import org.jeesl.interfaces.model.system.security.context.JeeslSecurityMenu;
import org.jeesl.interfaces.model.system.security.doc.JeeslSecurityOnlineHelp;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityAction;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityView;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.model.system.security.util.JeeslSecurityCategory;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.jsf.handler.PositionListReorderer;
import org.jeesl.jsf.handler.sb.SbSingleHandler;
import org.jeesl.jsf.helper.TreeHelper;
import org.jeesl.util.query.cq.CqOrdering;
import org.jeesl.util.query.ejb.system.EjbSecurityQuery;
import org.primefaces.event.DragDropEvent;
import org.primefaces.event.NodeCollapseEvent;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.TreeDragDropEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslSecurityMenuController <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
											C extends JeeslSecurityCategory<L,D>,
											R extends JeeslSecurityRole<L,D,C,V,U,A>,
											V extends JeeslSecurityView<L,D,C,R,U,A>,
											U extends JeeslSecurityUsecase<L,D,C,R,V,A>,
											A extends JeeslSecurityAction<L,D,R,V,U,?>,
											CTX extends JeeslSecurityContext<L,D>,
											M extends JeeslSecurityMenu<L,V,CTX,M>,
											OH extends JeeslSecurityOnlineHelp<V,DC,DS>,
											DC extends JeeslIoCms<L,D,LOC,?,DS>,
											DS extends JeeslIoCmsSection<L,DS>,
											USER extends JeeslUser<R>>
		extends AbstractJeeslLocaleWebController<L,D,LOC>
		implements SbSingleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslSecurityMenuController.class);

	private final SecurityFactoryBuilder<L,D,?,?,V,?,?,?,CTX,M,?,?,?,?,OH,DC,DS,?,?> fbSecurity;
	private JeeslSecurityFacade<C,R,V,U,A,CTX,M,USER> fSecurity;
	private JeeslSecurityBean<?,V,?,?,?,CTX,M,?> bSecurity;
	
	protected JeeslIoCmsFacade<L,D,LOC,?,DC,?,DS,?,?,?,?,?,?,?,?> fCms;

	private final TreeHelper<M> thMenu;
	private final EjbSecurityMenuFactory<V,CTX,M> efMenu;

	private List<V> opViews; public List<V> getOpViews(){return opViews;}
	
	protected final SbSingleHandler<CTX> sbhContext; public SbSingleHandler<CTX> getSbhContext() {return sbhContext;}

	private TreeNode tree; public TreeNode getTree() {return tree;}
	private TreeNode node; public TreeNode getNode() {return node;} public void setNode(TreeNode node) {this.node = node;}

	private final List<OH> helps; public List<OH> getHelps() {return helps;}
	protected final List<DC> documents; public List<DC> getDocuments() {return documents;}

	private M menu; public M getMenu() {return menu;}
	private DC document;
	private OH help; public OH getHelp() {return help;} public void setHelp(OH help) {this.help = help;}

	private boolean disabledMenuImportFromDefaultContext; public boolean isDisabledMenuImportFromDefaultContext() {return disabledMenuImportFromDefaultContext;}

	public JeeslSecurityMenuController(JeeslSecurityMenuCallback callback,
										SecurityFactoryBuilder<L,D,?,?,V,?,?,?,CTX,M,?,?,?,?,OH,DC,DS,?,?> fbSecurity,
										IoCmsFactoryBuilder<L,D,LOC,?,DC,?,DS,?,?,?,?,?,?,?,?> fbCms)
	{
		super(fbSecurity.getClassL(),fbSecurity.getClassD());
		this.fbSecurity=fbSecurity;

		sbhContext = new SbSingleHandler<>(fbSecurity.getClassContext(),this);

		efMenu = fbSecurity.ejbMenu();
		thMenu = TreeHelper.instance();
		
		helps = new ArrayList<>();
		documents = new ArrayList<>();
	}

	public void postConstructMenu(JeeslLocaleProvider<LOC> lp, JeeslFacesMessageBean bMessage,
									JeeslSecurityFacade<C,R,V,U,A,CTX,M,USER> fSecurity,
									JeeslSecurityBean<?,V,?,?,?,CTX,M,?> bSecurity,
									JeeslIoCmsFacade<L,D,LOC,?,DC,?,DS,?,?,?,?,?,?,?,?> fCms)
	{
		super.postConstructLocaleWebController(lp,bMessage);
		this.fSecurity=fSecurity;
		this.bSecurity=bSecurity;
		
		opViews = fSecurity.all(fbSecurity.getClassView());
		this.fCms = fCms;

		sbhContext.setList(fSecurity.allOrderedPosition(fbSecurity.getClassContext()));
		sbhContext.setDefault();

		buildMenu(createMissingItems());

		try {reloadDocuments();}
		catch (JeeslNotFoundException e) {e.printStackTrace();}
	}

	protected void reloadDocuments()  throws JeeslNotFoundException{};

	@Override public void selectSbSingle(SbSingleSelection handler, EjbWithId ejb)
	{
		buildMenu(createMissingItems());
	}

	private List<M> createMissingItems()
	{
		List<M> list = new ArrayList<>();
		if(sbhContext.isSelected())
		{
			EjbSecurityQuery<C,R,V,U,A,CTX,USER> query = new EjbSecurityQuery<>();
			query.add(sbhContext.getSelection());
			query.addRootFetch(JeeslSecurityMenu.Attributes.context);
			query.orderBy(CqOrdering.ascending(JeeslSecurityMenu.Attributes.parent,JeeslSecurityMenu.Attributes.id));
			query.orderBy(CqOrdering.ascending(JeeslSecurityMenu.Attributes.position));
			
			list.addAll(fSecurity.fSecurityMenus(query));
			if(debugOnInfo) {logger.info(fbSecurity.getClassMenu().getSimpleName()+": "+list.size()+" in context "+sbhContext.getSelection().getCode());}
		}
		else
		{
			list.addAll(fSecurity.all(fbSecurity.getClassMenu()));
			if(debugOnInfo) {logger.info(fbSecurity.getClassMenu().getSimpleName()+": "+list.size());}
		}

		if(!list.isEmpty() || fSecurity.all(fbSecurity.getClassMenu(),1).isEmpty())
		{
			// We check if we have to create missing items if
			// - we have already items for this context or
			// - there are not items at all (initial start)
			Map<V,M> map = efMenu.toMapView(list);
			for(V v : opViews)
			{
//				if(debugOnInfo) {logger.info(v.toString()+" already available: "+map.containsKey(v));}
				if(!map.containsKey(v))
				{
					try
					{
						M m = efMenu.build(v);
						if(sbhContext.isSelected()) {m.setContext(sbhContext.getSelection());}
						V parentView = searchParentView(m, opViews);
						if(Objects.nonNull(parentView) && map.containsKey(parentView))
						{
							m.setParent(map.get(parentView));
						}
						m = fSecurity.save(m);
						list.add(m);
					}
					catch (JeeslConstraintViolationException e) {e.printStackTrace();}
					catch (JeeslLockingException e) {e.printStackTrace();}
				}
			}
		}
		return list;
	}

	private V searchParentView(M m, List<V> opViews)
	{
		V parentView = null;
		String mappingUrl = m.getView().getUrlMapping();
		if(Objects.nonNull(mappingUrl))
		{
			int iend = mappingUrl.lastIndexOf("/");
			if(iend > 0)
			{
				String parentUrlMapping = mappingUrl.substring(0, iend);
				Optional<V> matchingParentView = opViews.stream().filter(p -> p.getUrlMapping().equals(parentUrlMapping)).findFirst();
				parentView = matchingParentView.orElse(null);
			}
		}
		return parentView;
	}

	private void buildMenu(List<M> list)
    {
		if(sbhContext.isSelected()) {disabledMenuImportFromDefaultContext = !list.isEmpty();}
		else {disabledMenuImportFromDefaultContext = true;}

		Map<M,List<M>> map = efMenu.toMapChild(list);
	    tree = new DefaultTreeNode(null, null);

	    buildTree(tree, efMenu.toListRoot(list),map);

	    if(debugOnInfo) {logger.info("Reloaded Menu with "+list.size()+" elements. sbhContext.isSelected():"+sbhContext.isSelected()+" disabledMenuImportFromDefaultContext:"+disabledMenuImportFromDefaultContext);}
    }
	
	public void deleteItemsFromContext() throws IllegalArgumentException, JeeslConstraintViolationException
	{
		CTX core = fSecurity.fByEnum(fbSecurity.getClassContext(), JeeslSecurityContext.Code.core);
		if(sbhContext.isSelected() && sbhContext.getSelection().equals(core)) {throw new IllegalStateException("Not allowd to ");}
		
		List<M> list = new ArrayList<>();
		List<M> delete = new ArrayList<>();
		Set<M> parents = new HashSet<>();
		list.add(null);
		int loop=1;
		
		while(!list.isEmpty())
		{
			list.clear();
			parents.clear();
			delete.clear();
			
			EjbSecurityQuery<C,R,V,U,A,CTX,USER> query = new EjbSecurityQuery<>();
			query.add(sbhContext.getSelection());
			list.addAll(fSecurity.fSecurityMenus(query));
			
			logger.info("Loop "+loop+" with "+list.size());
			for(M m : list)
			{
				if(Objects.nonNull(m.getParent())) {parents.add(m.getParent());}
			}
			for(M m : list)
			{
				if(!parents.contains(m)) {delete.add(m);}
			}
			
			fSecurity.rm(delete);
			loop++;	
		}
	}

	public void importFromDefaultContext()
	{
		try
		{
			if(sbhContext.isSelected())
			{
				CTX defaultCtx = fSecurity.fByCode(fbSecurity.getClassContext(), "core");
				CTX currentCtx = sbhContext.getSelection();
				if(!defaultCtx.equals(currentCtx))
				{
					List<M> list = new ArrayList<>();
					list.addAll(fSecurity.allForParent(fbSecurity.getClassMenu(), JeeslSecurityMenu.Attributes.context,defaultCtx));
					Map<M,List<M>> map = efMenu.toMapChild(list);

					Map<M,M> defaultVsCurrentMap = new HashMap<>();
					logger.info("copying menu items....");
					for (M m :  list)
					{
						M newMenu = efMenu.build();
						newMenu.setPosition(m.getPosition());
						newMenu.setVisible(m.getVisible());
						newMenu.setView(m.getView());
						newMenu.setContext(currentCtx);
						newMenu = fSecurity.save(newMenu);
						//logger.info("copying ids from = " + m.getId() +" to " + newMenu.getId());
						defaultVsCurrentMap.put(m, newMenu);
					}
					logger.info("copying menu items....done");
					
					logger.info("Updating menu parents....");
					for (Map.Entry<M,List<M>> defautlMenuEntry : map.entrySet())
					{
						M currentParentMenu= fSecurity.find(fbSecurity.getClassMenu(),defaultVsCurrentMap.get(defautlMenuEntry.getKey()));
						for (M m :  defautlMenuEntry.getValue())
						{
							M currentMenu = fSecurity.find(fbSecurity.getClassMenu(), defaultVsCurrentMap.get(m));
							//logger.info("new MenuId:" + currentMenu.getId() + "parent :" + currentParentMenu.getId());
							currentMenu.setParent(currentParentMenu);
							fSecurity.update(currentMenu);
						}
					}
					logger.info("Updating menu parents....done");
					buildMenu(createMissingItems());
				}
			}
		}
		catch (JeeslNotFoundException | JeeslConstraintViolationException | JeeslLockingException e)
		{
			logger.info("import of menu item failed : " + e.getMessage());
		}
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

	public void expandTree() {thMenu.setExpansion(this.node!=null ? this.node : this.tree, true);}
	public void collapseTree() {thMenu.setExpansion(this.tree,  false);}
	public boolean isExpanded() {return this.tree != null && this.tree.getChildren().stream().filter(node -> node.isExpanded()).count() > 1;}
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
	protected void propagateChanges()
	{
		logger.warn("NYI");
	}

    @SuppressWarnings("unchecked")
	public void onNodeSelect(NodeSelectEvent event)
    {
		logger.info("Selected "+event.getTreeNode().toString());
		menu = (M)event.getTreeNode().getData();
		menu = fSecurity.find(fbSecurity.getClassMenu(),menu);

		reloadHelps();
    }
    
    public void saveMenu() throws JeeslConstraintViolationException, JeeslLockingException
    {
    	logger.info(AbstractLogMessage.saveEntity(menu));
    	menu = fSecurity.save(menu);
    	propagateChanges();
    }

    private void reloadHelps()
    {
    	helps.clear();
		helps.addAll(fSecurity.allForParent(fbSecurity.getClassOnlineHelp(),menu.getView()));
    }

    // Handler Tree-Select
	private TreeNode helpTree; public TreeNode getHelpTree() {return helpTree;}
	private TreeNode helpNode; public TreeNode getHelpNode() {return helpNode;} public void setHelpNode(TreeNode helpNode) {this.helpNode = helpNode;}

    public void addHelp(DC doc)
    {
    	this.document=doc;
    	logger.info(document.toString());

    	DS root = this.fCms.load(document.getRoot(), true);

		this.helpTree = new DefaultTreeNode(root, null);
		buildTree(this.helpTree, root.getSections());
    }

    private void buildTree(TreeNode parent, List<DS> sections)
	{
		for(DS s : sections)
		{
			TreeNode n = new DefaultTreeNode(s, parent);
			if(!s.getSections().isEmpty()) {buildTree(n,s.getSections());}
		}
	}

	public void expandHelp() {thMenu.setExpansion(this.helpNode!=null ? this.helpNode : this.helpTree, true);}
	public void collapseHelp() {thMenu.setExpansion(this.helpTree,  false);}
	public boolean isHelpExpanded() {return this.helpTree != null && this.helpTree.getChildren().stream().filter(node -> node.isExpanded()).count() > 1;}

	public void onHelpNodeSelect(NodeSelectEvent event) {if(debugOnInfo) {logger.info("Expanded "+event.getTreeNode().toString());}}
	public void onHelpExpand(NodeExpandEvent event) {if(debugOnInfo) {logger.info("Expanded "+event.getTreeNode().toString());}}
    public void onHelpCollapse(NodeCollapseEvent event) {if(debugOnInfo) {logger.info("Collapsed "+event.getTreeNode().toString());}}

    public void reorderHelp() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fSecurity, helps);}

    public void selectHelp(){if(debugOnInfo) {logger.info(AbstractLogMessage.selectEntity(help));}}
    public void removeHelp() throws JeeslConstraintViolationException
    {
    	if(debugOnInfo) {logger.info(AbstractLogMessage.deleteEntity(help));}
    	fSecurity.rm(help);
    	reloadHelps();
    }

    // Handler Tree-Select

    @SuppressWarnings("unchecked")
	public void onHelpDrop(DragDropEvent ddEvent) throws JeeslConstraintViolationException, JeeslLockingException
    {
    	if(debugOnInfo) {logger.info("DRAG "+ddEvent.getDragId());}
    	if(debugOnInfo) {logger.info("DROP "+ddEvent.getDropId());}
		Object data = ddEvent.getData();
		if(debugOnInfo) {if(data==null) {logger.info("data = null");} else {logger.info("Data "+data.getClass().getSimpleName());}}

		TreeNode n = thMenu.getNode(helpTree,ddEvent.getDragId(),3);
		DS section = (DS)n.getData();
		logger.info(section.toString());

		help = fbSecurity.ejbHelp().build(menu.getView(),document,section,helps);
		help = fSecurity.save(help);
		reloadHelps();
    }
}