package org.jeesl.web.mbean.prototype.module.bb;

import java.io.Serializable;
import java.util.ArrayList;
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
import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkup;
import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkupType;
import org.jeesl.interfaces.model.module.bb.JeeslBbBoard;
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
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractBbPostBean <L extends JeeslLang,D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
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
	final static Logger logger = LoggerFactory.getLogger(AbstractBbPostBean.class);

	protected JeeslBbFacade<L,D,SCOPE,BB,PUB,THREAD,POST,M,MT,USER> fBb;

	private final BbFactoryBuilder<L,D,SCOPE,BB,PUB,THREAD,POST,M,MT,USER> fbBb;

	protected final SbSingleHandler<SCOPE> sbhScope; public SbSingleHandler<SCOPE> getSbhScope() {return sbhScope;}

	private List<BB> boards; public List<BB> getBoards() {return boards;} public void setBoards(List<BB> boards) {this.boards = boards;}
	private final List<THREAD> threads; public List<THREAD> getThreads() {return threads;}
	private final List<POST> postings; public List<POST> getPostings() {return postings;}

	protected long refId;
	private USER user;
	protected MT markupType;

	private BB board; public BB getBoard() {return board;} public void setBoard(BB board) {this.board = board;}
	private THREAD thread; public THREAD getThread() {return thread;} public void setThread(THREAD thread) {this.thread = thread;}
	private POST posting; public POST getPosting() {return posting;} public void setPosting(POST posting) {this.posting = posting;}

	private TreeNode tree; public TreeNode getTree() {return tree;}
    private TreeNode node; public TreeNode getNode() {return node;} public void setNode(TreeNode node) {this.node = node;}

	public AbstractBbPostBean(BbFactoryBuilder<L,D,SCOPE,BB,PUB,THREAD,POST,M,MT,USER> fbBb)
	{
		super(fbBb.getClassL(),fbBb.getClassD());
		this.fbBb=fbBb;
		sbhScope = new SbSingleHandler<SCOPE>(fbBb.getClassScope(),this);
		threads = new ArrayList<THREAD>();
		postings = new ArrayList<POST>();
	}

	protected void postConstructBb(JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage,
									JeeslBbFacade<L,D,SCOPE,BB,PUB,THREAD,POST,M,MT,USER> fBb,
									USER user)
	{
		super.initJeeslAdmin(bTranslation,bMessage);
		this.fBb=fBb;
		this.user=user;
		pageConfig();
		reloadBoards();
		try {markupType = fBb.fByCode(fbBb.getClassMarkupType(),JeeslIoMarkupType.Code.xhtml);}
		catch (JeeslNotFoundException e) {e.printStackTrace();}
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
		reset(true,true,true);
	}

	private void reset(boolean rBoard, boolean rThread, boolean rPosting)
	{
		if(rBoard) {board=null;}
		if(rThread) {thread=null;}
		if(rPosting) {posting=null;}
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

    @SuppressWarnings("unchecked")
	public void onBoardSelect(NodeSelectEvent event)
    {
		logger.info("Selected "+event.getTreeNode().toString());
		board = (BB)event.getTreeNode().getData();
		board = fBb.find(fbBb.getClassBoard(),board);
		reloadThreads();
		reset(false,true,true);
    }

	private void reloadThreads()
	{
		threads.clear();
		threads.addAll(fBb.allForParent(fbBb.getClassThread(), board));
	}

    public void selectThread()
    {
    	reset(false,false,true);
    	reloadPostings();
    }

    public void addThread()
    {
    	thread = fbBb.ejbThread().build(board);
    	posting = fbBb.ejbPost().build(thread,markupType,user);
    }

    public void saveThread() throws JeeslConstraintViolationException, JeeslLockingException
    {
    	logger.info(AbstractLogMessage.saveEntity(thread));
    	thread = fBb.save(thread);

    	logger.info(AbstractLogMessage.saveEntity(posting));
    	posting.setThread(thread);
    	posting = fBb.save(posting);
    	reloadThreads();
    	reloadPostings();
    }

    private void reloadPostings()
    {
    	postings.clear();
    	postings.addAll(fBb.allForParent(fbBb.getClassPost(), thread));
    }

    public void postingToThreads()
    {
    	logger.info("postingToThread");
    	reset(false,true,true);
    }

    public void addPosting()
    {
    	logger.info(AbstractLogMessage.createEntity(fbBb.getClassPost()));
    	reset(false,false,true);
    	posting = fbBb.ejbPost().build(thread,markupType,user);
    }

    public void savePosting() throws JeeslConstraintViolationException, JeeslLockingException
    {
    	logger.info(AbstractLogMessage.createEntity(fbBb.getClassPost()));
    	posting = fBb.save(posting);
    	reloadPostings();
    }

	public void onNodeExpand(NodeExpandEvent event) {if(debugOnInfo) {logger.info("Expanded "+event.getTreeNode().toString());}}
    public void onNodeCollapse(NodeCollapseEvent event) {if(debugOnInfo) {logger.info("Collapsed "+event.getTreeNode().toString());}}
}