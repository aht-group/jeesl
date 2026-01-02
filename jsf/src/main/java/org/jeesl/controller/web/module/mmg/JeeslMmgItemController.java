package org.jeesl.controller.web.module.mmg;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.jeesl.api.bean.callback.JeeslFileRepositoryCallback;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.module.JeeslMmgFacade;
import org.jeesl.controller.web.AbstractJeeslLocaleWebController;
import org.jeesl.controller.web.util.AbstractLogMessage;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.io.IoFileRepositoryFactoryBuilder;
import org.jeesl.factory.builder.module.MmgFactoryBuilder;
import org.jeesl.factory.ejb.io.label.revision.data.EjbLastModifiedFactory;
import org.jeesl.interfaces.bean.sb.bean.SbSingleBean;
import org.jeesl.interfaces.bean.sb.handler.SbSingleSelection;
import org.jeesl.interfaces.controller.handler.system.io.JeeslFileRepositoryHandler;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.controller.web.module.mmg.JeeslMmgItemCallback;
import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.io.fr.JeeslFileMeta;
import org.jeesl.interfaces.model.io.fr.JeeslFileStorage;
import org.jeesl.interfaces.model.module.mmg.JeeslMmgClassification;
import org.jeesl.interfaces.model.module.mmg.JeeslMmgGallery;
import org.jeesl.interfaces.model.module.mmg.JeeslMmgItem;
import org.jeesl.interfaces.model.module.mmg.JeeslMmgQuality;
import org.jeesl.interfaces.model.module.mmg.JeeslWithMmgGallery;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.jsf.handler.sb.SbSingleHandler;
import org.primefaces.event.FileUploadEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslMmgItemController <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
										R extends JeeslTenantRealm<L,D,R,?>, RREF extends EjbWithId,
										MG extends JeeslMmgGallery<L>,
										MI extends JeeslMmgItem<L,MG,FRC,USER>,
										MC extends JeeslMmgClassification<L,R,MC,?>,
										MQ extends JeeslMmgQuality<L,D,MQ,?>,
										FRS extends JeeslFileStorage<L,D,?,?,?>,
										FRC extends JeeslFileContainer<FRS,FRM>,
										FRM extends JeeslFileMeta<D,FRC,?,?>,
										USER extends JeeslSimpleUser>
		extends AbstractJeeslLocaleWebController<L,D,LOC>
		implements SbSingleBean,JeeslFileRepositoryCallback
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslMmgItemController.class);
	
	@SuppressWarnings("unused")
	private final JeeslMmgItemCallback callback;
	private JeeslMmgFacade<L,D,R,MG,MI,MC,MQ,USER> fMmg;
	
	private final MmgFactoryBuilder<L,D,LOC,R,MG,MI,MC,MQ,USER> fbMmg;
	private final IoFileRepositoryFactoryBuilder<L,D,LOC,?,FRS,?,?,FRC,?,?,?,?,?> fbFile;
	
	protected final SbSingleHandler<LOC> sbhLocale; public SbSingleHandler<LOC> getSbhLocale() {return sbhLocale;}
	private JeeslFileRepositoryHandler<LOC,FRS,FRC,FRM> frh; public JeeslFileRepositoryHandler<LOC, FRS, FRC, FRM> getFrh() {return frh;}
	public long getFileSizeLimit() {return frh.getStorage().getFileSizeLimit();}
	
	private final List<MI> items; public List<MI> getItems() {return items;}
	private final List<MC> classifications; public List<MC> getClassifications() {return classifications;}

	private R realm;
	@SuppressWarnings("unused")
	private RREF rref;
	private MG gallery;
	private USER currentUser;
	private MI item; public MI getItem() {return item;} public void setItem(MI item) {this.item = item;}

	public JeeslMmgItemController(final JeeslMmgItemCallback callback,
									final MmgFactoryBuilder<L,D,LOC,R,MG,MI,MC,MQ,USER> fbRmmv,
									final IoFileRepositoryFactoryBuilder<L,D,LOC,?,FRS,?,?,FRC,?,?,?,?,?> fbFile)
	{
		super(fbRmmv.getClassL(),fbRmmv.getClassD());
		this.callback=callback;
		this.fbMmg=fbRmmv;
		this.fbFile=fbFile;
		
		sbhLocale = new SbSingleHandler<>(fbRmmv.getClassLocale(),this);
		
		items = new ArrayList<>();
		classifications = new ArrayList<>();
	}
	
	public void postConstructMmgItem(JeeslMmgFacade<L,D,R,MG,MI,MC,MQ,USER> fRmmv,
									JeeslLocaleProvider<LOC> lp, JeeslFacesMessageBean bMessage,
									R realm)
	{
		super.postConstructLocaleWebController(lp,bMessage);
		this.fMmg=fRmmv;
		this.realm=realm;
		
		sbhLocale.setList(lp.getLocales());
		sbhLocale.setDefault();
	}
	
	public void updateRealmReference(RREF rref, JeeslFileRepositoryHandler<LOC,FRS,FRC,FRM> frh)
	{
		this.rref=rref;
		this.frh=frh;
		
		classifications.addAll(fMmg.all(fbMmg.getClassClassification(),realm,rref));
	}
	
	public <OWNER extends JeeslWithMmgGallery<MG>> void updateGallery(Class<OWNER> cOwner, OWNER owner, USER user) throws JeeslConstraintViolationException, JeeslLockingException
	{
		this.currentUser=user;
		try {gallery = fMmg.fMmgGallery(cOwner,owner);}
		catch (JeeslNotFoundException e)
		{
			gallery = fbMmg.ejbGallery().build();
			gallery = fMmg.save(gallery);
			owner.setMmgGallery(gallery);
			fMmg.save(owner);
		}
		this.reset(true,true);
		reloadItems();
	}
	
	@Override
	public void selectSbSingle(SbSingleSelection handler, EjbWithId item) throws JeeslLockingException, JeeslConstraintViolationException
	{
		// TODO Auto-generated method stub
	}
	
	private void reset(boolean rItmes, boolean rItem)
	{
		if(rItmes) {items.clear();}
		if(rItem) {item=null;}
	}
	
	protected void reloadItems()
	{
		this.reset(true,false);
		items.addAll(fMmg.allForParent(fbMmg.getClassItem(),gallery));
	}
	
//	public void addItem()
//	{
//		this.reset(false,true);
//		item = fbMmg.ejbItem().build(gallery);
//		item.setName(efLang.createEmpty(lp.getLocales()));
//	}
//	
//	public void saveItem() throws JeeslConstraintViolationException, JeeslLockingException
//	{
//		fbMmg.ejbItem().converter(fMmg,item);
//		EjbLastModifiedFactory.modified(currentUser,item);
//		item = fMmg.save(item);
//	}
	
	public void selectItem(MI item) throws JeeslNotFoundException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.selectEntity(item));}
		this.item = item;
	}
	
	public InputStream is(MI item) throws JeeslNotFoundException
	{
		frh.initSilent(item);
		for(FRM m : frh.getMetas())
		{
			if(m.getCategory().equals(JeeslMmgQuality.Code.original.toString()))
			{
//				byte[] test = frh.download(m);
				return frh.download(m);
			}
		}
		return null;
	}
	
	protected void simulateFileUpload(Path path) throws JeeslNotFoundException, JeeslConstraintViolationException, JeeslLockingException, IOException
	{
		if(debugOnInfo) {logger.info("Handling FileUpload: "+path.getFileName().toString());}
		
		MI i = fbMmg.ejbItem().build(gallery);
		i.setName(efLang.buildEmpty(lp.getLocales()));
		for(String k : i.getName().keySet()) {i.getName().get(k).setLang(path.getFileName().toString());}
		fbMmg.ejbItem().converter(fMmg,i);
		EjbLastModifiedFactory.modified(currentUser,i);
		
		FRC container = fMmg.save(fbFile.ejbContainer().build(frh.getStorage()));
		i.setFrContainer(container);
		i = fMmg.save(i);
		
		frh.saveThreadsafe(container,path.getFileName().toString(),Files.readAllBytes(path),JeeslMmgQuality.Code.original.toString());
    }
	public void handleFileUpload(FileUploadEvent event) throws JeeslNotFoundException, JeeslConstraintViolationException, JeeslLockingException
	{
		MI i = fbMmg.ejbItem().build(gallery);
		i.setName(efLang.buildEmpty(lp.getLocales()));
		for(String k : i.getName().keySet()) {i.getName().get(k).setLang(event.getFile().getFileName());}
		fbMmg.ejbItem().converter(fMmg,i);
		EjbLastModifiedFactory.modified(currentUser,i);
		
		FRC container = fMmg.save(fbFile.ejbContainer().build(frh.getStorage()));
		i.setFrContainer(container);
		i = fMmg.save(i);
		
		frh.saveThreadsafe(container,event.getFile().getFileName(),event.getFile().getContent(),JeeslMmgQuality.Code.original.toString());
    }

	@Override public void callbackFrMetaSelected() {}
	@Override public void callbackFrContainerSaved(EjbWithId id) throws JeeslConstraintViolationException, JeeslLockingException
	{
		logger.info("callbackFrContainerSaved "+id);
		item.setFrContainer(frh.getContainer());
		item = fMmg.save(item);
	}
}