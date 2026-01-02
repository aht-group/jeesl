package org.jeesl.web.mbean.prototype.io.dms;

import java.io.Serializable;
import java.util.List;

import org.jeesl.api.bean.JeeslAttributeBean;
import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.callback.JeeslFileRepositoryCallback;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoAttributeFacade;
import org.jeesl.api.facade.io.JeeslIoDmsFacade;
import org.jeesl.api.facade.io.JeeslIoFrFacade;
import org.jeesl.controller.handler.system.io.AttributeHandler;
import org.jeesl.controller.web.util.AbstractLogMessage;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.io.IoAttributeFactoryBuilder;
import org.jeesl.factory.builder.io.IoDmsFactoryBuilder;
import org.jeesl.factory.builder.io.IoFileRepositoryFactoryBuilder;
import org.jeesl.interfaces.bean.AttributeBean;
import org.jeesl.interfaces.bean.sb.handler.SbSingleSelection;
import org.jeesl.interfaces.controller.handler.JeeslAttributeHandler;
import org.jeesl.interfaces.controller.handler.system.io.JeeslFileRepositoryHandler;
import org.jeesl.interfaces.model.io.dms.JeeslIoDms;
import org.jeesl.interfaces.model.io.dms.JeeslIoDmsDocument;
import org.jeesl.interfaces.model.io.dms.JeeslIoDmsLayer;
import org.jeesl.interfaces.model.io.dms.JeeslIoDmsSection;
import org.jeesl.interfaces.model.io.dms.JeeslIoDmsView;
import org.jeesl.interfaces.model.io.domain.JeeslDomainSet;
import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.io.fr.JeeslFileMeta;
import org.jeesl.interfaces.model.io.fr.JeeslFileStorage;
import org.jeesl.interfaces.model.io.fr.JeeslFileStorageEngine;
import org.jeesl.interfaces.model.io.fr.JeeslFileType;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeCategory;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeContainer;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeCriteria;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeData;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeItem;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeOption;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeSet;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.jsf.handler.PositionListReorderer;
import org.jeesl.jsf.handler.sb.SbSingleHandler;
import org.primefaces.event.NodeCollapseEvent;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractDmsUploadBean <L extends JeeslLang,D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
R extends JeeslTenantRealm<L,D,R,?>,
											DMS extends JeeslIoDms<L,D,FSTORAGE,ASET,DS,S>,
											S extends JeeslIoDmsSection<L,D,S>,
											FILE extends JeeslIoDmsDocument<L,S,FCONTAINER,ACONTAINER>,
											VIEW extends JeeslIoDmsView<L,D,DMS>,
											LAYER extends JeeslIoDmsLayer<VIEW,AITEM>,
											
											FSTORAGE extends JeeslFileStorage<L,D,?,?,FENGINE>,
											FENGINE extends JeeslFileStorageEngine<L,D,FENGINE,?>,
											FCONTAINER extends JeeslFileContainer<FSTORAGE,FMETA>,
											FMETA extends JeeslFileMeta<D,FCONTAINER,FTYPE,?>,
											FTYPE extends JeeslFileType<L,D,FTYPE,?>,
											
											DS extends JeeslDomainSet<L,D,?>,
											
											ACAT extends JeeslAttributeCategory<L,D,R,ACAT,?>,
											ACATEGORY extends JeeslStatus<L,D,ACATEGORY>,
											ACRITERIA extends JeeslAttributeCriteria<L,D,R,ACAT,ATYPE,AOPTION,ASET>,
											ATYPE extends JeeslAttributeType<L,D,ATYPE,?>,
											AOPTION extends JeeslAttributeOption<L,D,ACRITERIA>,
											ASET extends JeeslAttributeSet<L,D,R,ACAT,AITEM>,
											AITEM extends JeeslAttributeItem<ACRITERIA,ASET>,
											ACONTAINER extends JeeslAttributeContainer<ASET,ADATA>,
											ADATA extends JeeslAttributeData<ACRITERIA,AOPTION,ACONTAINER>
>
					extends AbstractDmsBean<L,D,LOC,DMS,FSTORAGE,ASET,DS,S,FILE,VIEW,LAYER,FCONTAINER,AITEM,ACONTAINER>
					implements Serializable,AttributeBean<ACONTAINER>,JeeslFileRepositoryCallback
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractDmsUploadBean.class);	

	private JeeslIoAttributeFacade<R,ACAT,ACRITERIA,ATYPE,AOPTION,ASET,AITEM,ACONTAINER,ADATA> fAttribute; 
	private JeeslIoFrFacade<L,D,?,FSTORAGE,?,FENGINE,FCONTAINER,FMETA,FTYPE,?,?,?> fFr;
	
	private final IoAttributeFactoryBuilder<L,D,R,ACAT,ACRITERIA,ATYPE,AOPTION,ASET,AITEM,ACONTAINER,ADATA> fbAttribute;
//	private final IoFileRepositoryFactoryBuilder<L,D,FSTORAGE,FENGINE,FCONTAINER,FMETA,FTYPE> fbFr;
	
	protected final SbSingleHandler<DMS> sbhDms; public SbSingleHandler<DMS> getSbhDms() {return sbhDms;}

	private AttributeHandler<L,D,R,ACAT,ACRITERIA,ATYPE,AOPTION,ASET,AITEM,ACONTAINER,ADATA> attributeHandler; public AttributeHandler<L,D,R,ACAT,ACRITERIA,ATYPE,AOPTION,ASET,AITEM,ACONTAINER,ADATA> getAttributeHandler() {return attributeHandler;}
	private JeeslFileRepositoryHandler<LOC,FSTORAGE,FCONTAINER,FMETA> fileHandler; public JeeslFileRepositoryHandler<LOC,FSTORAGE,FCONTAINER,FMETA> getFileHandler() {return fileHandler;}

	private List<FILE> files; public List<FILE> getFiles() {return files;} public void setFiles(List<FILE> files) {this.files = files;}

	private TreeNode tree; public TreeNode getTree() {return tree;}
	private TreeNode node; public TreeNode getNode() {return node;} public void setNode(TreeNode node) {this.node = node;}
	private S section; public S getSection() {return section;} public void setSection(S section) {this.section = section;}
	private FILE file; public FILE getFile() {return file;} public void setFile(FILE file) {this.file = file;}

	public AbstractDmsUploadBean(final IoDmsFactoryBuilder<L,D,LOC,DMS,FSTORAGE,S,FILE,VIEW,LAYER> fbDms,
								final IoAttributeFactoryBuilder<L,D,R,ACAT,ACRITERIA,ATYPE,AOPTION,ASET,AITEM,ACONTAINER,ADATA> fbAttribute,
								final IoFileRepositoryFactoryBuilder<L,D,LOC,?,FSTORAGE,?,FENGINE,FCONTAINER,FMETA,FTYPE,?,?,?> fbFr)
	{
		super(fbDms);
		this.fbAttribute=fbAttribute;
//		this.fbFr=fbFr;
		sbhDms = new SbSingleHandler<DMS>(fbDms.getClassDms(),this);
	}
	
	protected void postConstructDmsUpload(JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage,
								JeeslIoDmsFacade<L,D,LOC,DMS,FSTORAGE,ASET,DS,S,FILE,VIEW,FCONTAINER,ACONTAINER> fDms,
								JeeslIoFrFacade<L,D,?,FSTORAGE,?,FENGINE,FCONTAINER,FMETA,FTYPE,?,?,?> fFr,
								JeeslIoAttributeFacade<R,ACAT,ACRITERIA,ATYPE,AOPTION,ASET,AITEM,ACONTAINER,ADATA> fAttribute,
								JeeslAttributeBean<R,ACAT,ACRITERIA,ATYPE,AOPTION,ASET,AITEM,ACONTAINER,ADATA> bAttribute,
								JeeslFileRepositoryHandler<LOC,FSTORAGE,FCONTAINER,FMETA> fileHandler
								)
	{
		super.initDms(bTranslation,bMessage,fDms);
		this.fFr=fFr;
		this.fAttribute=fAttribute;
		this.fileHandler=fileHandler;
		
		initPageConfiguration();
		super.initLocales();
		attributeHandler = fbAttribute.handler(bMessage,fAttribute,bAttribute,this);
//		fileHandler = fbFr.handler(fFr,this);
		sbhDms.silentCallback();
	}
	protected abstract void initPageConfiguration();
	
	
	@Override @SuppressWarnings("unchecked")
	public void selectSbSingle(SbSingleSelection handler, EjbWithId item) throws JeeslLockingException, JeeslConstraintViolationException
	{
		logger.info(AbstractLogMessage.selectEntity(item));
		this.dm = (DMS)item;
		attributeHandler.init(dm.getSet());
		fileHandler.setStorage(dm.getStorage());
		reloadTree();
		reset(true,true);
	}
	
	private void reset(boolean rSection, boolean rFile)
	{
		if(rSection) {section=null;}
		if(rFile) {file=null;}
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
	public void onSectionSelect(NodeSelectEvent event)
    {
		logger.info("Selected "+event.getTreeNode().toString());
		section = (S)event.getTreeNode().getData();
		section = efLang.persistMissingLangs(fDms, localeCodes, section);
		S db = fDms.load(section,false);
		efSection.update(db,section);
		reloadFiles();
		reset(false,true);
    }
    
    private void reloadFiles()
    {
    	files = fDms.allForParent(fbDms.getClassFile(),section);
    }

    public void addFile() throws JeeslConstraintViolationException, JeeslLockingException
    {
		if(debugOnInfo) {logger.info(AbstractLogMessage.createEntity(fbDms.getClassFile()));}
		file = efFile.build(section, files);
		file.setName(efLang.buildEmpty(sbhLocale.getList()));
		attributeHandler.prepare(file);
		fileHandler.init(file);
    }
    	
    public void saveFile() throws JeeslConstraintViolationException, JeeslLockingException
    {
		if(debugOnInfo) {logger.info(AbstractLogMessage.saveEntity(file));}
		file = fDms.save(file);
		reloadFiles();
		fileHandler.init(file);
		attributeHandler.prepare(file);
    }
    
    public void selectFile() throws JeeslConstraintViolationException, JeeslLockingException
    {
		if(debugOnInfo) {logger.info(AbstractLogMessage.selectEntity((file)));}
		file = efLang.persistMissingLangs(fDms, sbhLocale.getList(), file);
		attributeHandler.prepare(file);
		fileHandler.init(file);
    }
    
    public void deleteFile() throws JeeslConstraintViolationException, JeeslLockingException
    {
		if(debugOnInfo) {logger.info(AbstractLogMessage.deleteEntity(file));}
		
		if(fileHandler.getMetas()!=null && fileHandler.getMetas().size()>0)
		{
			bMessage.constraintInUse(null);
			return;
		}
		
		if(file.getAttributeContainer()!=null)	
		{
			ACONTAINER container = file.getAttributeContainer();
			
			file.setAttributeContainer(null);
			fFr.save(file);
			
			fFr.rm(container);
		}
		fFr.rm(file);
		reset(false,true);
		reloadFiles();
    }
    
	@Override
	public void save(JeeslAttributeHandler<ACONTAINER> handler) throws JeeslConstraintViolationException, JeeslLockingException
	{
		file.setAttributeContainer(handler.saveContainer());
		file = fAttribute.save(file);
	}
	
	@Override public void callbackFrContainerSaved(EjbWithId id) throws JeeslConstraintViolationException, JeeslLockingException
	{
		file.setFrContainer(fileHandler.getContainer());
		file = fFr.save(file);
	}
	@Override public void callbackFrMetaSelected() {}
	
	public void reorderFiles() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fDms, files);}

	public void resetFile()
	{
		if(debugOnInfo){logger.info("Action canceled.");}
	}
}