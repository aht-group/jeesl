package org.jeesl.controller.web.g.module.hd;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoCmsFacade;
import org.jeesl.api.facade.module.JeeslHdFacade;
import org.jeesl.controller.web.AbstractJeeslLocaleWebController;
import org.jeesl.controller.web.util.AbstractLogMessage;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.io.IoCmsFactoryBuilder;
import org.jeesl.factory.builder.module.HdFactoryBuilder;
import org.jeesl.interfaces.bean.sb.bean.SbToggleBean;
import org.jeesl.interfaces.bean.sb.handler.SbToggleSelection;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.controller.web.module.hd.JeeslHdFgaCallback;
import org.jeesl.interfaces.model.io.cms.JeeslIoCms;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsSection;
import org.jeesl.interfaces.model.module.hd.JeeslHdCategory;
import org.jeesl.interfaces.model.module.hd.resolution.JeeslHdFaq;
import org.jeesl.interfaces.model.module.hd.resolution.JeeslHdFga;
import org.jeesl.interfaces.model.module.hd.resolution.JeeslHdScope;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.jsf.handler.PositionListReorderer;
import org.jeesl.jsf.handler.sb.SbMultiHandler;
import org.jeesl.jsf.handler.sb.SbSingleHandler;
import org.jeesl.jsf.helper.TreeHelper;
import org.primefaces.event.DragDropEvent;
import org.primefaces.event.NodeCollapseEvent;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslHdFgaGwc <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
								R extends JeeslTenantRealm<L,D,R,?>, RREF extends EjbWithId,
								CAT extends JeeslHdCategory<L,D,R,CAT,?>,
								SCOPE extends JeeslHdScope<L,D,SCOPE,?>,
								FAQ extends JeeslHdFaq<L,D,R,CAT,SCOPE>,
								FGA extends JeeslHdFga<FAQ,DOC,SEC>,
								DOC extends JeeslIoCms<L,D,LOC,?,SEC>,
								SEC extends JeeslIoCmsSection<L,SEC>
								>
					extends AbstractJeeslLocaleWebController<L,D,LOC>
					implements Serializable,SbToggleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslHdFgaGwc.class);
	
	protected final HdFactoryBuilder<L,D,LOC,R,?,CAT,?,?,?,?,?,?,?,?,FAQ,SCOPE,FGA,DOC,SEC,?,?> fbHd;
	private final IoCmsFactoryBuilder<L,D,LOC,?,DOC,?,SEC,?,?,?,?,?,?,?,?> fbCms;
	
	private JeeslHdFacade<L,D,R,CAT,?,?,?,?,?,?,?,?,?,FAQ,SCOPE,FGA,DOC,SEC,?> fHd;
	private JeeslIoCmsFacade<L,D,LOC,?,DOC,?,SEC,?,?,?,?,?,?,?,?> fCms;
	
	private final JeeslHdFgaCallback<DOC> callback;
	
	protected final SbMultiHandler<CAT> sbhCategory; public SbMultiHandler<CAT> getSbhCategory() {return sbhCategory;}
	protected final SbMultiHandler<SCOPE> sbhScope; public SbMultiHandler<SCOPE> getSbhScope() {return sbhScope;}
	protected final SbSingleHandler<DOC> sbhDocuments; public SbSingleHandler<DOC> getSbhDocuments() {return sbhDocuments;}
	
	private final TreeHelper<SEC> thSection;
	
	private final List<FAQ> faqs; public List<FAQ> getFaqs() {return faqs;}
	private final List<FGA> answers; public List<FGA> getAnswers() {return answers;}
	private final List<SEC> sections; public List<SEC> getSections() {return sections;}
	
	private R realm;
	private RREF rref;
	private FAQ faq; public FAQ getFaq() {return faq;} public void setFaq(FAQ faq) {this.faq = faq;}
	private FGA fga; public FGA getFga() {return fga;} public void setFga(FGA fga) {this.fga = fga;}
	
	public JeeslHdFgaGwc(JeeslHdFgaCallback<DOC> callback,
							HdFactoryBuilder<L,D,LOC,R,?,CAT,?,?,?,?,?,?,?,?,FAQ,SCOPE,FGA,DOC,SEC,?,?> fbHd,
							IoCmsFactoryBuilder<L,D,LOC,?,DOC,?,SEC,?,?,?,?,?,?,?,?> fbCms)
	{
		super(fbHd.getClassL(),fbHd.getClassD());
		this.callback=callback;
		this.fbHd=fbHd;
		this.fbCms=fbCms;
		
		sbhCategory = new SbMultiHandler<>(fbHd.getClassCategory(),this);
		sbhScope = new SbMultiHandler<>(fbHd.getClassScope(),this);
		sbhDocuments = new SbSingleHandler<DOC>(fbHd.getClassDoc(),null);
		
		thSection = TreeHelper.instance();
		
		faqs = new ArrayList<>();
		answers = new ArrayList<>();
		sections = new ArrayList<>();
		documents = new ArrayList<>();
	}

	public void postConstruct(JeeslLocaleProvider<LOC> lp, JeeslFacesMessageBean bMessage,
									JeeslHdFacade<L,D,R,CAT,?,?,?,?,?,?,?,?,?,FAQ,SCOPE,FGA,DOC,SEC,?> fHd,
									JeeslIoCmsFacade<L,D,LOC,?,DOC,?,SEC,?,?,?,?,?,?,?,?> fCms,
									R realm)
	{
		super.postConstructLocaleWebController(lp,bMessage);
		this.fHd=fHd;
		this.fCms=fCms;
		this.realm=realm;
	}
		
	public void updateRealmReference(RREF rref)
	{
		this.rref=rref;
		
		sbhCategory.setList(fHd.all(fbHd.getClassCategory(),realm,rref));
		sbhCategory.selectAll();
		
		sbhScope.setList(fHd.all(fbHd.getClassScope()));
		sbhScope.selectAll();
		
		this.reloadFaqs();
		
		sbhDocuments.clear();
		sbhDocuments.getList().addAll(callback.getDocuments());
	}
	
	@Override public void toggled(SbToggleSelection handler, Class<?> c) throws JeeslLockingException, JeeslConstraintViolationException
	{

	}
	
	private void reset(boolean rFaq, boolean rAnswer, boolean rSections)
	{
		if(rFaq) {faq=null;}
		if(rAnswer) {fga=null;}
		if(rSections) {sections.clear();}
	}
	
	private void reloadFaqs()
	{	
		faqs.clear();
		faqs.addAll(fHd.all(fbHd.getClassFaq(),realm,rref));
	}
	
	public void selectFaq()
	{
		reset(false,true,true);
		logger.info(AbstractLogMessage.selectEntity(faq));
		faq = efLang.persistMissingLangs(fHd,lp.getLocales(),faq);
		faq = efDescription.persistMissingLangs(fHd,lp.getLocales(),faq);
		reloadAnswers();
	}
	
	public void addFaq()
	{
		logger.info(AbstractLogMessage.createEntity(fbHd.getClassFaq()));
		faq = fbHd.ejbFaq().build(realm,rref,sbhCategory.getList().get(0),sbhScope.getList().get(0));
		faq.setName(efLang.buildEmpty(lp.getLocales()));
		faq.setDescription(efDescription.buildEmpty(lp.getLocales()));
	}
	
	public void saveFaq() throws JeeslConstraintViolationException, JeeslLockingException
	{
		fbHd.ejbFaq().converter(fHd,faq);
		faq = fHd.save(faq);
		reloadFaqs();
		reloadAnswers();
	}
	
	public void deleteFaq() throws JeeslConstraintViolationException, JeeslLockingException
	{
		fHd.rm(faq);
		reset(true,true,true);
		reloadFaqs();
	}
	
	private void reloadAnswers()
	{
		answers.clear();
		answers.addAll(fHd.allForParent(fbHd.getClassFga(), faq));
	}
	
	
	public void selectAnswer()
	{
		logger.info(AbstractLogMessage.selectEntity(fga));
		reset(false,false,true);
		reloadSections();
	}
	
	public void addAnswer()
	{
		logger.info(AbstractLogMessage.createEntity(fbHd.getClassFga()));
		reset(false,true,true);
		if(sbhDocuments.getHasSome())
		{
			fga = fbHd.ejbFga().build(faq,sbhDocuments.getList().get(0),answers);
			reloadSections();
		}
	}
	
	public void onDocumentChanged()
	{
		reset(false,false,true);
		fbHd.ejbFga().converter(fHd,fga);
		reloadSections();
	}
	
	public void reloadSections()
	{
		SEC root = fCms.load(fga.getDocument().getRoot(),true);
		sections.addAll(fbCms.ejbSection().toSections(root));
	}
	
	public void saveAnswer() throws JeeslConstraintViolationException, JeeslLockingException
	{
		logger.info(AbstractLogMessage.saveEntity(fga));
		fbHd.ejbFga().converter(fHd,fga);
		fga = fHd.save(fga);
		reloadAnswers();
	}
	
	public void deleteAnswer() throws JeeslConstraintViolationException, JeeslLockingException
	{
		logger.info(AbstractLogMessage.deleteEntity(fga));
		fHd.rm(fga);
		reset(false,true,true);
		reloadAnswers();
	}
	
	public void reorderAnswers() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fHd,answers);}

    // Handler Tree-Select
	
	protected final List<DOC> documents; public List<DOC> getDocuments() {return documents;}
	private DOC document;
    
	private TreeNode helpTree; public TreeNode getHelpTree() {return helpTree;}
	private TreeNode helpNode; public TreeNode getHelpNode() {return helpNode;} public void setHelpNode(TreeNode helpNode) {this.helpNode = helpNode;}
	    
    public void selectDocument(DOC doc)
    {
    	this.document=doc;
    	logger.info(document.toString());
    	
    	SEC root = this.fCms.load(document.getRoot(), true);

		this.helpTree = new DefaultTreeNode(root, null);
		buildTree(this.helpTree, root.getSections());
    }

    private void buildTree(TreeNode parent, List<SEC> sections)
	{
		for(SEC s : sections)
		{
			TreeNode n = new DefaultTreeNode(s, parent);
			if(!s.getSections().isEmpty()) {buildTree(n,s.getSections());}
		}
	}
	
	public void expandHelp(){thSection.setExpansion(this.helpNode!=null ? this.helpNode : this.helpTree, true);}
	public void collapseHelp() {thSection.setExpansion(this.helpTree,  false);}
	public boolean isHelpExpanded() {return this.helpTree != null && this.helpTree.getChildren().stream().filter(node -> node.isExpanded()).count() > 1;}
	
	public void onHelpNodeSelect(NodeSelectEvent event) {if(debugOnInfo) {logger.info("Expanded "+event.getTreeNode().toString());}}
	public void onHelpExpand(NodeExpandEvent event) {if(debugOnInfo) {logger.info("Expanded "+event.getTreeNode().toString());}}
    public void onHelpCollapse(NodeCollapseEvent event) {if(debugOnInfo) {logger.info("Collapsed "+event.getTreeNode().toString());}}
    
    // Handler Tree-Select
	@SuppressWarnings("unchecked")
	public void onHelpDrop(DragDropEvent<SEC> ddEvent) throws JeeslConstraintViolationException, JeeslLockingException
    {
    	if(debugOnInfo) {logger.info("DRAG "+ddEvent.getDragId());}
    	if(debugOnInfo) {logger.info("DROP "+ddEvent.getDropId());}
		Object data = ddEvent.getData();
		if(debugOnInfo) {if(data==null) {logger.info("data = null");} else{logger.info("Data "+data.getClass().getSimpleName());}}
		
		TreeNode n = thSection.getNode(helpTree,ddEvent.getDragId(),3);
		SEC section = (SEC)n.getData();
		logger.info(section.toString());
		
		fga = fbHd.ejbFga().build(faq,document,answers);
		fga.setSection(section);
		fbHd.ejbFga().converter(fHd,fga);
		fga = fHd.save(fga);
		reloadAnswers();
		
    }
}