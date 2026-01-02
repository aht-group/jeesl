package org.jeesl.web.mbean.prototype.io.cms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.exlp.util.io.StringUtil;
import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.cache.JeeslCmsCacheBean;
import org.jeesl.api.bean.callback.JeeslFileRepositoryCallback;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoCmsFacade;
import org.jeesl.controller.web.util.AbstractLogMessage;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.io.IoCmsFactoryBuilder;
import org.jeesl.factory.ejb.io.cms.EjbIoCmsContentFactory;
import org.jeesl.factory.ejb.io.cms.EjbIoCmsElementFactory;
import org.jeesl.factory.ejb.io.cms.EjbIoCmsFactory;
import org.jeesl.factory.ejb.io.cms.EjbIoCmsSectionFactory;
import org.jeesl.factory.ejb.util.EjbIdFactory;
import org.jeesl.factory.json.system.translation.JsonTranslationFactory;
import org.jeesl.interfaces.bean.op.OpEntityBean;
import org.jeesl.interfaces.bean.sb.bean.SbSingleBean;
import org.jeesl.interfaces.bean.sb.bean.SbToggleBean;
import org.jeesl.interfaces.bean.sb.handler.SbSingleSelection;
import org.jeesl.interfaces.bean.sb.handler.SbToggleSelection;
import org.jeesl.interfaces.controller.handler.OutputXpathPattern;
import org.jeesl.interfaces.controller.handler.op.OpSelectionHandler;
import org.jeesl.interfaces.controller.handler.system.io.JeeslFileRepositoryHandler;
import org.jeesl.interfaces.model.io.cms.JeeslIoCms;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsCategory;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsContent;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsElement;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsElementType;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsSection;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsVisiblity;
import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkupType;
import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.io.fr.JeeslFileMeta;
import org.jeesl.interfaces.model.io.fr.JeeslFileStorage;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.jsf.handler.PositionListReorderer;
import org.jeesl.jsf.handler.op.OpEntitySelectionHandler;
import org.jeesl.jsf.handler.sb.SbSingleHandler;
import org.jeesl.jsf.helper.TreeHelper;
import org.jeesl.web.mbean.prototype.system.AbstractAdminBean;
import org.openfuxml.model.xml.core.ofx.Section;
import org.primefaces.event.NodeCollapseEvent;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.TreeDragDropEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractCmsEditorBean <L extends JeeslLang,D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
										CAT extends JeeslIoCmsCategory<L,D,CAT,?>,
										CMS extends JeeslIoCms<L,D,LOC,CAT,S>,
										V extends JeeslIoCmsVisiblity,
										S extends JeeslIoCmsSection<L,S>,
										E extends JeeslIoCmsElement<V,S,EC,ET,C,FC>,
										EC extends JeeslStatus<L,D,EC>,
										ET extends JeeslIoCmsElementType<L,D,ET,?>,
										C extends JeeslIoCmsContent<V,E,MT>,
										MT extends JeeslIoMarkupType<L,D,MT,?>,
										FS extends JeeslFileStorage<L,D,?,?,?>,
										FC extends JeeslFileContainer<FS,FM>,
										FM extends JeeslFileMeta<D,FC,?,?>
										>
					extends AbstractAdminBean<L,D,LOC>
					implements Serializable,SbToggleBean,SbSingleBean,OpEntityBean,JeeslFileRepositoryCallback
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractCmsEditorBean.class);
	
	private final IoCmsFactoryBuilder<L,D,LOC,CAT,CMS,V,S,E,EC,ET,C,?,MT,FC,FM> fbCms;
	
	protected JeeslIoCmsFacade<L,D,LOC,CAT,CMS,V,S,E,EC,ET,C,?,MT,FC,FM> fCms;
	
	private JeeslCmsCacheBean<S,E> bCache;

	private String currentLocaleCode;
	protected String[] cmsLocales; public String[] getCmsLocales() {return cmsLocales;}
	
	private final TreeHelper<S> thSection;
	
	protected final EjbIoCmsFactory<L,D,CAT,CMS,V,S,C,MT,LOC> efCms;
	private final EjbIoCmsSectionFactory<L,S,FM> efS;
	private final EjbIoCmsElementFactory<L,S,E> efElement;
	private final EjbIoCmsContentFactory<LOC,E,C,MT> efContent;
	
	protected final SbSingleHandler<CMS> sbhCms; public SbSingleHandler<CMS> getSbhCms() {return sbhCms;}
	protected final SbSingleHandler<CAT> sbhCategory; public SbSingleHandler<CAT> getSbhCategory() {return sbhCategory;}
	private final SbSingleHandler<LOC> sbhLocale; public SbSingleHandler<LOC> getSbhLocale() {return sbhLocale;}
	private final OpEntitySelectionHandler<LOC> opLocale; public OpEntitySelectionHandler<LOC> getOpLocale() {return opLocale;}
	private JeeslFileRepositoryHandler<LOC,FS,FC,FM> hFileRepository; public JeeslFileRepositoryHandler<LOC,FS,FC,FM> gethFileRepository() {return hFileRepository;}

	private final Map<E,Section> mapOfx; public Map<E, Section> getMapOfx() {return mapOfx;}

	private List<E> elements; public List<E> getElements() {return elements;}
	private List<EC> elementCategories; public List<EC> getElementCategories() {return elementCategories;}
	private List<ET> types; public List<ET> getTypes() {return types;}

	protected CMS cms; public CMS getCms() {return cms;} public void setCms(CMS cms) {this.cms = cms;}
	private C content; public C getContent() {return content;} public void setContent(C content) {this.content = content;}

	protected EC elementCategory; public EC getElementCategory() {return elementCategory;} public void setElementCategory(EC elementCategory) {this.elementCategory = elementCategory;}
	
	private S section; public S getSection() {return section;} public void setSection(S section) {this.section = section;}
	protected E element; public E getElement() {return element;} public void setElement(E element) {this.element = element;}
	
	private MT markupHtml;
	
	private TreeNode tree; public TreeNode getTree() {return tree;}
    private TreeNode node; public TreeNode getNode() {return node;} public void setNode(TreeNode node) {this.node = node;}

	public AbstractCmsEditorBean(IoCmsFactoryBuilder<L,D,LOC,CAT,CMS,V,S,E,EC,ET,C,?,MT,FC,FM> fbCms)
	{
		super(fbCms.getClassL(),fbCms.getClassD());
		this.fbCms=fbCms;
		
		thSection = TreeHelper.instance();
		
		efCms = fbCms.ejbCms();
		efS = fbCms.ejbSection();
		efElement = fbCms.ejbElement();
		efContent = fbCms.ejbContent();
		
		sbhCategory = new SbSingleHandler<>(fbCms.getClassCategory(),this);
		sbhCms = new SbSingleHandler<>(fbCms.getClassCms(),this);
		sbhLocale = new SbSingleHandler<>(fbCms.getClassLocale(),this);
		
		opLocale = new OpEntitySelectionHandler<>(this);
		opLocale.addColumn(JsonTranslationFactory.build(fbCms.getClassLocale(),JeeslLocale.Attributes.name,OutputXpathPattern.multiLang));
		
		mapOfx = new HashMap<>();
		types = new ArrayList<ET>();
	}
	
	protected void postConstructCms(JeeslTranslationBean<L,D,LOC> bTranslation, String currentLocaleCode,
									List<LOC> locales, JeeslFacesMessageBean bMessage, JeeslCmsCacheBean<S,E> bCache,
									JeeslIoCmsFacade<L,D,LOC,CAT,CMS,V,S,E,EC,ET,C,?,MT,FC,FM> fCms,
									JeeslFileRepositoryHandler<LOC,FS,FC,FM> hFileRepository)
	{
		super.initJeeslAdmin(bTranslation,bMessage);
		this.bCache=bCache;
		this.currentLocaleCode=currentLocaleCode;
		this.fCms=fCms;
		this.hFileRepository=hFileRepository;
		
		this.hFileRepository.setDebugOnInfo(debugOnInfo);
		opLocale.setLazy(locales);
		
		elementCategories=fCms.allOrderedPositionVisible(fbCms.getClassElementCategory());
		types.addAll(fCms.allOrderedPositionVisible(fbCms.getClassElementType()));
		
		try {markupHtml = fCms.fByCode(fbCms.getClassMarkupType(), JeeslIoMarkupType.Code.xhtml);}
		catch (JeeslNotFoundException e) {e.printStackTrace();}
	}
	
	protected abstract void reloadCmsDocuments();
	protected void reloadCmsDocumentsForCategory()
	{
		sbhCms.setList(fCms.allForCategory(fbCms.getClassCms(),sbhCategory.getSelection()));
		logger.info(AbstractLogMessage.reloaded(fbCms.getClassCms(), sbhCms.getList()));
	}
	
	@Override public void toggled(SbToggleSelection handler, Class<?> c) throws JeeslLockingException, JeeslConstraintViolationException
	{
		logger.info(AbstractLogMessage.toggled(c));
	}
	
	@SuppressWarnings("unchecked")
	@Override public void selectSbSingle(SbSingleSelection handler, EjbWithId ejb)
	{
		if(ejb==null) {cms=null;}
		else if(JeeslIoCms.class.isAssignableFrom(ejb.getClass()))
		{
			cms = (CMS)ejb;
			if(EjbIdFactory.isSaved(cms))
			{
				cmsSelected();
				cms = efLang.persistMissingLangs(fCms,localeCodes,cms);
				reloadCms();
				reloadTree();
			}
			logger.info("Twice:"+sbhCms.getTwiceSelected()+" for "+cms.toString());
		}
		else
		{
			logger.info("NOT Assignable");
		}
		reset(true);
	}
	protected void cmsSelected(){}
	
	private void reset(boolean rElement)
	{
		if(rElement) {element=null;}
	}
	
	protected abstract void addDocument();
	public void addDocumentForCategory()
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.createEntity(fbCms.getClassCms()));}
		cms = efCms.build(sbhCategory.getSelection(),efS.build());
		cms.setName(efLang.createEmpty(localeCodes));
	}
	
	protected abstract void saveDocument() throws JeeslConstraintViolationException, JeeslLockingException;
	public void saveCms() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.saveEntity(cms));}
		cms = fCms.save(cms);
		savedCms();
		reloadCms();
	}
	protected void savedCms() throws JeeslLockingException, JeeslConstraintViolationException
	{
		sbhCms.selectSbSingle(cms);
		reloadCmsDocuments();
		reloadTree();
	}
	
	public void selectDocument()
	{
		reset(true);
		if(debugOnInfo){logger.info(AbstractLogMessage.selectEntity(cms));}
	
		reloadCms();
	}
	
	private void reloadCms()
	{
		cms = fCms.find(fbCms.getClassCms(),cms);
		opLocale.setTbList(cms.getLocales());
		
		cmsLocales = new String[cms.getLocales().size()];
		for(int i=0;i<cms.getLocales().size();i++) {cmsLocales[i]=cms.getLocales().get(i).getCode();}
		
		sbhLocale.setList(cms.getLocales());
		sbhLocale.setSelection(null);
		for(LOC l : cms.getLocales())
		{
			if(l.getCode().equals(currentLocaleCode)) {sbhLocale.setSelection(l);break;}
		}
		if(!sbhLocale.isSelected() && !cms.getLocales().isEmpty()) {sbhLocale.setSelection(cms.getLocales().get(0));}
		logger.info("SBHLocale.selection==null:"+(sbhLocale.getSelection()==null));
	}
	
	private void reloadTree()
	{
		S root = fCms.load(cms.getRoot(),true);
		
		if(debugOnInfo)
		{
			logger.info(StringUtil.stars());
			if(sbhLocale.getSelection()!=null)
			{
				for(S s : root.getSections())
				{
					logger.info(s.toString()+" "+s.getName().get(sbhLocale.getSelection().getCode()).getLang());
				}
			}
			else {logger.info("No Sections, because sbhLocale is null");}
		}
		
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
    		S db = fCms.load(child,false);
    		efS.update(db,child);
    		child.setSection(parent);
    		child.setPosition(index);
    		fCms.save(child);
    		index++;
        }  
    }

    @SuppressWarnings("unchecked")
	public void onSectionSelect(NodeSelectEvent event)
    {
		logger.info("Selected "+event.getTreeNode().toString());
		section = (S)event.getTreeNode().getData();
		section = efLang.persistMissingLangs(fCms, cmsLocales, section);
		S db = fCms.load(section,false);
		efS.update(db,section);
		reloadSection();
		reset(true);		
    }
    
	@SuppressWarnings("unchecked")
	@Override public void addOpEntity(OpSelectionHandler handler, EjbWithId item) throws JeeslLockingException, JeeslConstraintViolationException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.addOpEntity(item));}
		if(fbCms.getClassLocale().isAssignableFrom(item.getClass()))
		{
			LOC locale = (LOC)item;
			if(!cms.getLocales().contains(locale))
			{
				cms.getLocales().add(locale);
				cms = fCms.save(cms);
				reloadCms();
			}
		}
	}
	@SuppressWarnings("unchecked")
	@Override public void rmOpEntity(OpSelectionHandler handler, EjbWithId item) throws JeeslLockingException, JeeslConstraintViolationException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.rmOpEntity(item));}
		if(fbCms.getClassLocale().isAssignableFrom(item.getClass()))
		{
			LOC locale = (LOC)item;
			if(cms.getLocales().contains(locale))
			{
				cms.getLocales().remove(locale);
				cms = fCms.save(cms);
				reloadCms();
			}
		}
	}
    
	public void addSection() 
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.createEntity(fbCms.getClassSection()));}
		section = efS.build(cms.getRoot());
		section.setName(efLang.createEmpty(cmsLocales));
		for(String k : section.getName().keySet()){section.getName().get(k).setLang("XXX");}
	}
	
	public void saveSection() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.saveEntity(section));}
		boolean appendToTree = EjbIdFactory.isUnSaved(section);
		
		section = fCms.save(section);
		if(appendToTree) {new DefaultTreeNode(section,tree);}
		reloadSection();
	}
	
	protected void reloadSection()
	{
		elements = fCms.allForParent(fbCms.getClassElement(),section);
		elements = fCms.fCmsElements(section);
		
		mapOfx.clear();
		for(E e : elements)
		{
			logger.info("Rendering deactivated "+e.toString());
//			Section s = bCache.buildByElement(sbhLocale.getSelection().getCode(),e);
//			JaxbUtil.info(s);
//			mapOfx.put(e,s);
		}
	}
	
	public void clearSectionCache()
	{
		bCache.clearCache(section);
	}
	
//	****************************************************************************************
	
	public void addElement() 
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.createEntity(fbCms.getClassElement()));}
		element = efElement.build(section,elements);
	}
	
	public void selectElement() 
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.selectEntity(element));}
		elementWasSelected();
	}
	
	public void changeElementCategory()
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.selectOneMenuChange(elementCategory));}
	}
	
	//For testing only
	private String jsonString;
	public String getJsonString() {return jsonString;}
	public void setJsonString(String jsonString) {this.jsonString = jsonString;}
	
	public void saveElement() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.saveEntity(element));}
		logger.warn("element: "+element.getJson());
		logger.warn("jsonString: "+jsonString);
		element.setType(fCms.find(fbCms.getClassElementType(),element.getType()));
		element = fCms.save(element);
		reloadSection();
		elementWasSelected();
	}
	
	public void deleteElement() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.deleteEntity(element));}
		fCms.deleteCmsElement(element);
		reset(true);
		reloadSection();
	}
	
	
	public void expandTree()
	{
		thSection.setExpansion(this.node != null ? this.node : this.tree, true);
	}
	
	public void collapseTree()
	{
		thSection.setExpansion(this.tree,  false);
	}
	
	public boolean isExpanded()
	{
		return this.tree != null && this.tree.getChildren().stream().filter(node -> node.isExpanded()).count() > 1;
	}
	
	protected void elementWasSelected()
	{
		boolean isParagraph = element.getType().getCode().equals(JeeslIoCmsElement.Type.paragraph.toString());
		boolean isImage = element.getType().getCode().equals(JeeslIoCmsElement.Type.image.toString());
		
		if(isParagraph || isImage)
		{
			if(!element.getContent().containsKey(sbhLocale.getSelection().getCode()))
			{
				element.getContent().put(sbhLocale.getSelection().getCode(), efContent.build(element,sbhLocale.getSelection(), "", markupHtml));
			}
		}
		if(isImage)
		{
			if(hFileRepository!=null)
			{
				try {hFileRepository.init(element);}
				catch (JeeslConstraintViolationException e) {e.printStackTrace();}
				catch (JeeslLockingException e) {e.printStackTrace();}
			}
			else {logger.warn("hFileRepository==null");}
		}
	}
	@Override public void callbackFrContainerSaved(EjbWithId id) throws JeeslConstraintViolationException, JeeslLockingException
	{
		element.setFrContainer(hFileRepository.getContainer());
		element = fCms.save(element);
	}
	@Override public void callbackFrMetaSelected() {}
	
	public void saveParagraph() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.saveEntity(element)+" "+element.getContent().size());}
		element = fCms.save(element);
	}
	
	public void reorderDocuments() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fCms, sbhCms.getList());}
	public void reorderElements() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fCms, elements);}
}