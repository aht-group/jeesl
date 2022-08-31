package org.jeesl.web.rest;

import org.jeesl.factory.ejb.system.status.EjbStatusFactory;
import org.jeesl.factory.xml.jeesl.XmlContainerFactory;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.xml.jeesl.Container;
import org.jeesl.util.db.updater.JeeslDbStatusUpdater;
import org.jeesl.util.query.xml.XmlStatusQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.status.Status;
import net.sf.ahtutils.xml.sync.DataUpdate;

public abstract class AbstractJeeslRestHandler <L extends JeeslLang,D extends JeeslDescription>
{
	final static Logger logger = LoggerFactory.getLogger(AbstractJeeslRestHandler.class);
	
	private final JeeslFacade fUtils;
	
	protected final Class<L> cL;
	protected final Class<D> cD;
	
	protected final XmlContainerFactory xfContainer;

	public AbstractJeeslRestHandler(final JeeslFacade fUtils,final Class<L> cL, final Class<D> cD)
	{
		this.fUtils=fUtils;
		this.cL=cL;
		this.cD=cD;
		
		xfContainer = new XmlContainerFactory(XmlStatusQuery.get(XmlStatusQuery.Key.StatusExport).getStatus());
	}
		
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected <S extends JeeslStatus<L,D,S>, P extends JeeslStatus<L,D,P>> DataUpdate importStatus(Class<S> clStatus, Container container, Class<P> clParent)
    {
    	for(Status xml : container.getStatus()){xml.setGroup(clStatus.getSimpleName());}
		JeeslDbStatusUpdater asdi = new JeeslDbStatusUpdater();
        asdi.setStatusEjbFactory(EjbStatusFactory.createFactory(clStatus,cL,cD));
        asdi.setFacade(fUtils);
        DataUpdate dataUpdate = asdi.iuStatus(container.getStatus(), clStatus, cL, clParent);
        asdi.deleteUnusedStatus(clStatus, cL, cD);
        return dataUpdate;
    }
}