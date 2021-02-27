package org.jeesl.util.db.updater;

import java.util.ArrayList;
import java.util.List;

import org.jeesl.api.facade.system.graphic.JeeslGraphicFacade;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.system.LocaleFactoryBuilder;
import org.jeesl.factory.builder.system.SvgFactoryBuilder;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphicType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.locale.JeeslLocaleProvider;
import org.jeesl.interfaces.model.system.locale.status.JeeslMcsStatus;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.xml.jeesl.Container;
import org.jeesl.util.db.cache.EjbCodeCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.status.Status;

public class JeeslDbMcsStatusUpdater <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,G>,
										R extends JeeslTenantRealm<L,D,R,G>, RREF extends EjbWithId,
//										S extends JeeslMcsStatus<L,D,R,S,G>,
										G extends JeeslGraphic<L,D,GT,?,?>, GT extends JeeslGraphicType<L,D,GT,G>>
{
	final static Logger logger = LoggerFactory.getLogger(JeeslDbMcsStatusUpdater.class);
	
	private final JeeslGraphicFacade<?,?,?,G,GT,?,?> fGraphic;
	private final LocaleFactoryBuilder<L,D,?> fbLocale;
	private final JeeslLocaleProvider<LOC> lp;
	private JeeslDbGraphicUpdater<G,GT> uGraphic;

	private R realm;
	private RREF rref;
	
	public JeeslDbMcsStatusUpdater(LocaleFactoryBuilder<L,D,LOC> fbLocale, SvgFactoryBuilder<L,D,G,GT,?,?> fbSvg,
										JeeslGraphicFacade<L,D,?,G,GT,?,?> fGraphic, JeeslLocaleProvider<LOC> lp)
	{
		this.fbLocale=fbLocale;
		this.fGraphic=fGraphic;
		this.lp=lp;
		uGraphic = new JeeslDbGraphicUpdater<>(fbSvg);
		uGraphic.setFacade(fGraphic);
	}
	
	public void initMcs(R realm, RREF rref)
	{
		this.realm = realm;
		this.rref = rref;
	}
	
//	public List<Status> getStatus(String xmlFile) throws FileNotFoundException
//	{
//		Aht aht = (Aht)JaxbUtil.loadJAXB(xmlFile, Aht.class);
//		logger.debug("Loaded "+aht.getStatus().size()+" Elements from "+xmlFile);
//		return aht.getStatus();
//	}
	
//	private void savePreviousDbEntries(String key, List<JeeslMcsStatus<L,D,R,S,G>> availableStatus)
//	{
//		Set<Long> dbStatus = new HashSet<Long>();
//		for(JeeslStatus<S,L,D> ejbStatus : availableStatus)
//		{
//			dbStatus.add(ejbStatus.getId());
//		}
//		logger.debug("Saved existing DB entries for "+key+": "+dbStatus.size());
//		mDbAvailableStatus.put(key, dbStatus);
//	}
	
//	public JeeslStatus<S,L,D> addVisible(JeeslStatus<S,L,D> ejbStatus, Status status)
//	{
//		boolean visible=true;
//		if(status.isSetVisible()){visible=status.isVisible();}
//		ejbStatus.setVisible(visible);
//		return ejbStatus;
//	}
//	
//	private JeeslStatus<S,L,D> addLangsAndDescriptions(JeeslStatus<S,L,D> ejbStatus, Status status) throws InstantiationException, IllegalAccessException, JeeslConstraintViolationException
//	{
//		JeeslStatus<S,L,D> ejbUpdateInfo = statusEjbFactory.create(status);		
//		ejbStatus.setName(ejbUpdateInfo.getName());
//		ejbStatus.setDescription(ejbUpdateInfo.getDescription());
//		return ejbStatus;
//	}
//	
//	public void removeStatusFromDelete(String key, long id)
//	{
//		mDbAvailableStatus.get(key).remove(id);
//	}
//	
//	public List<Long> getDeleteStatusIds()
//	{
//		List<Long> result = new ArrayList<Long>();
//		for(String group : mDbAvailableStatus.keySet())
//		{
//			Set<Long> delIds = mDbAvailableStatus.get(group);
//			Iterator<Long> iterator = delIds.iterator();
//			while(iterator.hasNext())
//			{
//				result.add(iterator.next());
//			}
//		}
//		return result;
//	}
//	
//	public void deleteUnusedStatus(Class<S> cStatus, Class<L> cLang, Class<D> cDescription)
//	{
//		logger.debug("Deleting unused childs of Status: "+cLang.getName()+":"+sDeleteLangs.size());
//		for(long id : sDeleteLangs)
//		{
//			try
//			{
//				logger.trace("Deleting "+cLang.getName()+": "+id);
//				L lang = fStatus.find(cLang, id);
//				logger.trace("\t"+lang);
//				fStatus.rm(lang);
//			}
//			catch (JeeslNotFoundException e) {logger.error("",e);}
//			catch (JeeslConstraintViolationException e) {logger.error("",e);}
//		}
//		for(long id : sDeleteDescriptions)
//		{
//			try
//			{
//				logger.debug("Deleting "+cDescription.getName()+": "+id);
//				D d = fStatus.find(cDescription, id);
//				fStatus.rm(d);
//			}
//			catch (JeeslNotFoundException e) {logger.error("",e);}
//			catch (JeeslConstraintViolationException e) {logger.error("",e);}
//		}
//		
//		 for(String group : mDbAvailableStatus.keySet())
//		 {
//			 Set<Long> sIds = mDbAvailableStatus.get(group);
//			 logger.trace("Deleting Group "+group+": "+sIds.size());
//			 for(long id : sIds)
//			 {
//				 try
//				 {
//					 logger.trace("Deleting status: "+id);
//					 S status = fStatus.find(cStatus, id);
//					 fStatus.rm(status);
//				 }
//				 catch (JeeslConstraintViolationException e) {logger.error("Error with following ID:"+id,e);}
//				 catch (JeeslNotFoundException e)  {logger.error("Error with following ID:"+id,e);}
//			 }
//		 }
//	}
//	
	public <S extends JeeslMcsStatus<L,D,R,S,G>> List<S> iStatus(Class<S> cStatus, Container xContainer)
	{
		List<S> added = new ArrayList<>();
		EjbCodeCache<S> cache = new EjbCodeCache<>(cStatus,fGraphic.all(cStatus,realm,rref));
		logger.debug("Updating "+cStatus.getSimpleName()+" with "+xContainer.getStatus().size()+" entries, "+cache.size()+" already in DB");
//		iuStatusEJB(list, cStatus, cLang);
		for(Status xml : xContainer.getStatus())
		{
			if(!cache.contains(xml.getCode()))
			{
				try {added.add(iStatus(cStatus,xml));}
				catch (JeeslConstraintViolationException e) {e.printStackTrace();}
			}
		}
		return added;
	}
	
	public <S extends JeeslMcsStatus<L,D,R,S,G>> S iStatus(Class<S> cStatus, Status xml) throws JeeslConstraintViolationException
	{
		try
		{
			S ejb = cStatus.newInstance();
			ejb.setRealm(realm);
			ejb.setRref(rref.getId());
			ejb.setCode(xml.getCode());
			ejb.setVisible(xml.isVisible());
			ejb.setPosition(xml.getPosition());
			ejb.setName(fbLocale.ejbLang().build(lp,xml.getLangs()));
			ejb.setDescription(fbLocale.ejbDescription().build(lp,xml.getDescriptions()));
			ejb = fGraphic.persist(ejb);
			
			try {uGraphic.updateSvg(cStatus,ejb,xml);}
			catch (JeeslConstraintViolationException | JeeslLockingException e) {e.printStackTrace();}
			
			logger.info("Added: "+ejb);
			return ejb;
		}
		catch (InstantiationException | IllegalAccessException e) {e.printStackTrace(); throw new JeeslConstraintViolationException(e.getMessage());}
	}
	
//	
//	public <P extends JeeslStatus<P,L,D>> DataUpdate iuStatus(List<Status> list, Class<S> cStatus, Class<L> cLang, Class<P> cParent)
//	{
//		DataUpdateTracker dut = new DataUpdateTracker(true);
//		dut.setType(XmlTypeFactory.build(cStatus.getName(),"Status-DB Import"));
//		
//		if(fStatus==null){dut.fail(new UtilsDeveloperException("No Facade available for "+cStatus.getName()), true);}
//		else {logger.debug("Updating "+cStatus.getSimpleName()+" with "+list.size()+" entries");}
//		iuStatusEJB(list, cStatus, cLang);
//		
//		for(Status xml : list)
//		{
//			try
//			{
//				if(xml.isSetParent() && cParent!=null)
//				{
//					logger.trace("Parent: "+xml.getParent().getCode());
//					S ejbStatus = fStatus.fByCode(cStatus,xml.getCode());
//					ejbStatus.setParent(fStatus.fByCode(cParent, xml.getParent().getCode()));
//					ejbStatus = fStatus.update(ejbStatus);
//					dut.success();
//				}
//			}
//			catch (JeeslConstraintViolationException e){dut.fail(e,true);}
//			catch (JeeslLockingException e) {dut.fail(e,true);}
//			catch (JeeslNotFoundException e) {dut.fail(e,true);}
//		}
//		return dut.toDataUpdate();
//	}
//	
//	private void iuStatusEJB(List<Status> list, Class<S> cStatus, Class<L> cLang)
//	{
//		for(Status xml : list)
//		{
//			if(!xml.isSetGroup()) {xml.setGroup(cStatus.getName());}
//			try
//			{
//				logger.debug("Processing "+xml.getGroup()+" with "+xml.getCode());
//				S ejb;
//				if(!isGroupInMap(xml.getGroup()))
//				{	// If a new group occurs, all entities are saved in a (delete) pool where
//					// they will be deleted if in the current list no entity with this key exists.
//					List<JeeslStatus<S,L,D>> l = new ArrayList<JeeslStatus<S,L,D>>();
//					for(S s : fStatus.all(cStatus)){l.add(s);}
//					savePreviousDbEntries(xml.getGroup(), l);
//					logger.debug("Delete Pool: "+mDbAvailableStatus.get(xml.getGroup()).size());
//				}
//				
//				try
//				{
//					ejb = fStatus.fByCode(cStatus,xml.getCode());
//					
//					//UTILS-145 Don't do unnecessary entity updates in AhtStatusDbInit
//					ejb = removeData(ejb);
//					ejb = fStatus.update(ejb);
//					ejb = fStatus.find(cStatus, ejb.getId());
//					removeStatusFromDelete(xml.getGroup(), ejb.getId());
//					logger.trace("Now in Pool: "+mDbAvailableStatus.get(xml.getGroup()).size());
//					logger.trace("Found: "+ejb);
//				}
//				catch (JeeslNotFoundException e)
//				{
//					ejb = cStatus.newInstance();
//					ejb.setCode(xml.getCode());
//					ejb = fStatus.persist(ejb);
//					logger.trace("Added: "+ejb);
//				}
//				
//				try
//				{
//					addLangsAndDescriptions(ejb,xml);
//					ejb.setSymbol(xml.getSymbol());
//					if(xml.isSetImage()){ejb.setImage(xml.getImage());}
//					if(xml.isSetStyle()){ejb.setStyle(xml.getStyle());}
//				}
//				catch (InstantiationException e) {logger.error("",e);}
//				catch (IllegalAccessException e) {logger.error("",e);}
//				catch (JeeslConstraintViolationException e) {logger.error("",e);}
//		        
//				if(xml.isSetPosition()){ejb.setPosition(xml.getPosition());}
//		        else{ejb.setPosition(0);}
//				
//				if(xml.isSetVisible()){ejb.setVisible(xml.isVisible());}
//				else{ejb.setVisible(false);}
//				
//				ejb = fStatus.update(ejb);
//				
//			}
//			catch (JeeslConstraintViolationException e){logger.error("",e);}
//			catch (InstantiationException e) {logger.error("",e);}
//			catch (IllegalAccessException e) {logger.error("",e);}
//			catch (JeeslLockingException e) {logger.error("",e);}
//		}
//	}
	
//	private S removeData(S ejbStatus)
//	{
//		Map<String,L> dbLangMap = ejbStatus.getName();
//		ejbStatus.setName(null);
//		for(JeeslLang lang : dbLangMap.values()){sDeleteLangs.add(lang.getId());}
//		
//		if(ejbStatus.getDescription()!=null)
//		{
//			Map<String,D> dbDescrMap = ejbStatus.getDescription();
//			ejbStatus.setDescription(null);
//			for(JeeslDescription d : dbDescrMap.values()){sDeleteDescriptions.add(d.getId());}
//		}
//		
//		return ejbStatus;
//	}
}