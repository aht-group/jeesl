package org.jeesl.web.mbean.prototype.module.bb;

import java.io.Serializable;
import java.util.List;

import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.module.JeeslBbFacade;
import org.jeesl.controller.web.util.AbstractLogMessage;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.module.BbFactoryBuilder;
import org.jeesl.interfaces.bean.sb.bean.SbSingleBean;
import org.jeesl.interfaces.bean.sb.handler.SbSingleSelection;
import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkupType;
import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkup;
import org.jeesl.interfaces.model.module.bb.JeeslBbBoard;
import org.jeesl.interfaces.model.module.bb.JeeslBbPublishingMode;
import org.jeesl.interfaces.model.module.bb.post.JeeslBbPost;
import org.jeesl.interfaces.model.module.bb.post.JeeslBbThread;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.text.EjbWithEmail;
import org.jeesl.jsf.handler.sb.SbSingleHandler;
import org.jeesl.web.mbean.prototype.system.AbstractAdminBean;
import org.primefaces.event.NodeCollapseEvent;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.TreeDragDropEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractBbConfigBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
									SCOPE extends JeeslStatus<L,D,SCOPE>,
									BB extends JeeslBbBoard<L,D,SCOPE,BB,PUB,USER>,
									PUB extends JeeslStatus<L,D,PUB>,
									THREAD extends JeeslBbThread<BB>,
									POST extends JeeslBbPost<THREAD,M,USER>,
									M extends JeeslIoMarkup<MT>,
									MT extends JeeslIoMarkupType<L,D,MT,?>,
									USER extends EjbWithEmail>
					extends AbstractAdminBean<L,D,LOC>
					implements Serializable,SbSingleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractBbConfigBean.class);
	
	protected JeeslBbFacade<L,D,SCOPE,BB,PUB,THREAD,POST,M,MT,USER> fBb;
	
	private final BbFactoryBuilder<L,D,SCOPE,BB,PUB,THREAD,POST,M,MT,USER> fbBb;
	
	protected final SbSingleHandler<SCOPE> sbhScope; public SbSingleHandler<SCOPE> getSbhScope() {return sbhScope;}
	
	private List<BB> boards; public List<BB> getBoards() {return boards;} public void setBoards(List<BB> boards) {this.boards = boards;}
	private List<PUB> publishings; public List<PUB> getPublishings() {return publishings;} public void setPublishings(List<PUB> publishings) {this.publishings = publishings;}

	protected long refId;
	private BB board; public BB getBoard() {return board;} public void setBoard(BB board) {this.board = board;}
	private TreeNode tree; public TreeNode getTree() {return tree;}
    private TreeNode node; public TreeNode getNode() {return node;} public void setNode(TreeNode node) {this.node = node;}
	
	public AbstractBbConfigBean(BbFactoryBuilder<L,D,SCOPE,BB,PUB,THREAD,POST,M,MT,USER> fbBb)
	{
		super(fbBb.getClassL(),fbBb.getClassD());
		this.fbBb=fbBb;
		sbhScope = new SbSingleHandler<SCOPE>(fbBb.getClassScope(),this);
	}

	protected void postConstructBb(JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage, JeeslBbFacade<L,D,SCOPE,BB,PUB,THREAD,POST,M,MT,USER> fBb)
	{
		super.initJeeslAdmin(bTranslation,bMessage);
		this.fBb=fBb;
		pageConfig();
		reloadBoards();
		
		publishings = fBb.allOrderedPositionVisible(fbBb.getClassPublishing());
	}
	
	//This method can be overriden
	protected void pageConfig()
	{
		refId = 0;
		sbhScope.setList(fBb.allOrderedPositionVisible(fbBb.getClassScope()));
		sbhScope.setDefault();
	}
	
	@Override public void selectSbSingle(SbSingleSelection handler, EjbWithId item) throws JeeslLockingException, JeeslConstraintViolationException
	{
		reloadBoards();
	}
	
	private void reloadBoards()
	{
		boards = fBb.fBulletinBoards(sbhScope.getSelection(), refId);
		logger.info(AbstractLogMessage.reloaded(fbBb.getClassBoard(),boards));
		tree = new DefaultTreeNode(null, null);
		for(BB b : boards)
		{
			if(b.getParent()==null)
			{
				TreeNode n = new DefaultTreeNode(b, tree);
				buildTree(n,b);
			}
		}
	}
	
	private void buildTree(TreeNode nParent, BB bbParent)
	{
		for(BB b : boards)
		{
			if(bbParent.equals(b.getParent()))
			{
				TreeNode n = new DefaultTreeNode(b,nParent);
				buildTree(n,b);
			}
		}
	}
	
	public void addBoard() throws JeeslNotFoundException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.createEntity(fbBb.getClassBoard()));}
		PUB publishing = fBb.fByCode(fbBb.getClassPublishing(), JeeslBbPublishingMode.Code.closed);
		board = fbBb.bb().build(null,sbhScope.getSelection(),refId,publishing);
	}
	
	public void saveBoard() throws JeeslConstraintViolationException, JeeslLockingException
	{
		board.setPublishing(fBb.find(fbBb.getClassPublishing(),board.getPublishing()));
		board = fBb.save(board);
		reloadBoards();
	}
	
    @SuppressWarnings("unchecked")
	public void onSectionSelect(NodeSelectEvent event)
    {
		logger.info("Selected "+event.getTreeNode().toString());
		board = (BB)event.getTreeNode().getData();
		board = fBb.find(fbBb.getClassBoard(),board);
    }
	
	@SuppressWarnings("unchecked")
	public void onDragDrop(TreeDragDropEvent event) throws JeeslConstraintViolationException, JeeslLockingException
	{
        TreeNode dragNode = event.getDragNode();
        TreeNode dropNode = event.getDropNode();
        int dropIndex = event.getDropIndex();
        logger.info("Dragged " + dragNode.getData() + "Dropped on " + dropNode.getData() + " at " + dropIndex);
        
        logger.info("Childs of "+dropNode.getData());
        BB parent = (BB)dropNode.getData();
        int index=1;
        for(TreeNode n : dropNode.getChildren())
        {
    		BB child =(BB)n.getData();
//    		BB db = fBb.find(child,false);
//    		efS.update(db,child);
    		child.setParent(parent);
    		child.setPosition(index);
    		fBb.save(child);
    		index++;
        }  
    }
	
	public void onNodeExpand(NodeExpandEvent event) {if(debugOnInfo) {logger.info("Expanded "+event.getTreeNode().toString());}}
    public void onNodeCollapse(NodeCollapseEvent event) {if(debugOnInfo) {logger.info("Collapsed "+event.getTreeNode().toString());}}
}