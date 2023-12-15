package org.jeesl.web.rest.module;

import org.jeesl.api.facade.system.JeeslSystemNewsFacade;
import org.jeesl.api.rest.rs.module.news.JeeslNewsRestExport;
import org.jeesl.api.rest.rs.module.news.JeeslNewsRestImport;
import org.jeesl.factory.ejb.system.status.EjbStatusFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.news.JeeslSystemNews;
import org.jeesl.interfaces.model.system.news.JeeslSystemNewsCategory;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.xml.io.locale.status.Status;
import org.jeesl.util.db.updater.JeeslDbStatusUpdater;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.rest.AbstractUtilsRest;
import net.sf.ahtutils.xml.aht.Aht;
import net.sf.ahtutils.xml.sync.DataUpdate;

public class SystemNewsRestService <L extends JeeslLang,D extends JeeslDescription,
									CATEGORY extends JeeslSystemNewsCategory<L,D,CATEGORY,?>,
									NEWS extends JeeslSystemNews<L,D,CATEGORY,NEWS,USER>,
									USER extends EjbWithId>
					extends AbstractUtilsRest<L,D>
					implements JeeslNewsRestExport,JeeslNewsRestImport
{
	final static Logger logger = LoggerFactory.getLogger(SystemNewsRestService.class);
	
	private JeeslSystemNewsFacade<L,D,CATEGORY,NEWS,USER> fNews;
	
	private final Class<CATEGORY> cCategory;
	
	@SuppressWarnings("unused")
	private final Class<NEWS> cNews;
	
	private SystemNewsRestService(JeeslSystemNewsFacade<L,D,CATEGORY,NEWS,USER> fNews,final String[] localeCodes, final Class<L> cL, final Class<D> cD, Class<CATEGORY> cCategory, final Class<NEWS> cNews)
	{
		super(fNews,localeCodes,cL,cD);
		this.fNews=fNews;
		
		this.cCategory=cCategory;
		this.cNews=cNews;
	}
	
	public static <L extends JeeslLang,D extends JeeslDescription,
					CATEGORY extends JeeslSystemNewsCategory<L,D,CATEGORY,?>,
					NEWS extends JeeslSystemNews<L,D,CATEGORY,NEWS,USER>,
					USER extends EjbWithId>
		SystemNewsRestService<L,D,CATEGORY,NEWS,USER>
		factory(JeeslSystemNewsFacade<L,D,CATEGORY,NEWS,USER> fNews, final String[] localeCodes, final Class<L> cL, final Class<D> cD, Class<CATEGORY> cCategory, final Class<NEWS> cNews)
	{
		return new SystemNewsRestService<L,D,CATEGORY,NEWS,USER>(fNews,localeCodes,cL,cD,cCategory,cNews);
	}
	
	@Override public Aht exportSystemNewsCategories() {return super.exportStatus(cCategory);}
	@Override public DataUpdate importSystemNewsCategories(Aht categories){return importStatus2(cCategory,null,categories);}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
    public <S extends JeeslStatus<L,D,S>, P extends JeeslStatus<L,D,P>> DataUpdate importStatus2(Class<S> clStatus, Class<P> clParent, Aht container)
    {
    	for(Status xml : container.getStatus()){xml.setGroup(clStatus.getSimpleName());}
		JeeslDbStatusUpdater asdi = new JeeslDbStatusUpdater();
        asdi.setStatusEjbFactory(EjbStatusFactory.instance(clStatus, cL, cD));
        asdi.setFacade(fNews);
        DataUpdate dataUpdate = asdi.iuStatus(container.getStatus(), clStatus, cL, clParent);
        asdi.deleteUnusedStatus(clStatus, cL, cD);
        return dataUpdate;
    }
}