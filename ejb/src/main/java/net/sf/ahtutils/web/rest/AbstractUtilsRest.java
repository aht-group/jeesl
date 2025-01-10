package net.sf.ahtutils.web.rest;

import java.util.Hashtable;
import java.util.Map;

import org.jeesl.factory.ejb.system.status.EjbDescriptionFactory;
import org.jeesl.factory.ejb.system.status.EjbLangFactory;
import org.jeesl.factory.ejb.system.status.EjbStatusFactory;
import org.jeesl.factory.xml.io.locale.status.XmlStatusFactory;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.xml.io.locale.status.Status;
import org.jeesl.util.db.updater.JeeslDbStatusUpdater;
import org.jeesl.util.query.xml.XmlStatusQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.aht.Aht;
import net.sf.ahtutils.xml.aht.Container;
import org.jeesl.model.xml.io.ssi.sync.DataUpdate;

public class AbstractUtilsRest <L extends JeeslLang, D extends JeeslDescription>
{
	final static Logger logger = LoggerFactory.getLogger(AbstractUtilsRest.class);

	protected JeeslFacade fUtils;
	
	protected final Class<L> cL;
	protected final Class<D> cD;
	
	protected final String[] defaultLangs;
	protected EjbLangFactory<L> efLang;
	protected EjbDescriptionFactory<D> efDescription;
	
	private Map<Class<?>,String> mapGroups;

	public AbstractUtilsRest(JeeslFacade fUtils, final String[] defaultLangs, final Class<L> cL, final Class<D> cD)
	{
		this.fUtils=fUtils;
		this.defaultLangs=defaultLangs;
		this.cL=cL;
		this.cD=cD;
		
        efLang = EjbLangFactory.instance(cL);
        efDescription = EjbDescriptionFactory.factory(cD);
        
        mapGroups = new Hashtable<Class<?>, String>();
	}
	
	public void addGroupCode(Class<?> c, String code)
	{
		mapGroups.put(c,code);
	}
	
	protected <S extends JeeslStatus<L,D,S>> Aht exportStatus(Class<S> c)
	{
		XmlStatusFactory<L,D,S> f = new XmlStatusFactory<>(XmlStatusQuery.get(XmlStatusQuery.Key.StatusExport).getStatus());
		
		Aht xml = new Aht();
		for(S s : fUtils.all(c))
		{
			Status status = f.build(s);
			if(mapGroups.containsKey(c)){status.setGroup(mapGroups.get(c));}
			xml.getStatus().add(status);
		}
		return xml;
	}
	
	protected <S extends JeeslStatus<L,D,S>> Container exportContainer(Class<S> c)
	{
		XmlStatusFactory<L,D,S> f = new XmlStatusFactory<>(XmlStatusQuery.get(XmlStatusQuery.Key.StatusExport).getStatus());
		
		Container xml = new Container();
		for(S s : fUtils.all(c))
		{
			Status status = f.build(s);
			if(mapGroups.containsKey(c)){status.setGroup(mapGroups.get(c));}
			xml.getStatus().add(status);
		}
		return xml;
	}
	
	protected <S extends JeeslStatus<L,D,S>, P extends JeeslStatus<L,D,P>, G extends JeeslGraphic<?,?,?>> DataUpdate importStatus(Class<S> cS, Class<P> cP, Aht container)
	{
		for(Status xml : container.getStatus()){xml.setGroup(cS.getSimpleName());}
		JeeslDbStatusUpdater<L,D,S,G> asdi = new JeeslDbStatusUpdater<L,D,S,G>();
	    asdi.setStatusEjbFactory(EjbStatusFactory.instance(cS,cL,cD));
	    asdi.setFacade(fUtils);
	    DataUpdate dataUpdate = asdi.iuStatus(container.getStatus(),cS,cL,cP);
	    asdi.deleteUnusedStatus(cS,cL,cD);
	    return dataUpdate;
	}
}