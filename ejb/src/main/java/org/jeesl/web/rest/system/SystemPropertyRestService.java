package org.jeesl.web.rest.system;

import org.jeesl.api.facade.system.JeeslSystemPropertyFacade;
import org.jeesl.api.rest.rs.system.property.JeeslSystemPropertyRestExport;
import org.jeesl.api.rest.rs.system.property.JeeslSystemPropertyRestImport;
import org.jeesl.controller.monitoring.counter.DataUpdateTracker;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.system.PropertyFactoryBuilder;
import org.jeesl.factory.ejb.system.status.EjbStatusFactory;
import org.jeesl.factory.ejb.system.util.EjbPropertyFactory;
import org.jeesl.factory.xml.io.locale.status.XmlTypeFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.property.JeeslProperty;
import org.jeesl.model.xml.io.locale.status.Status;
import org.jeesl.util.db.updater.JeeslDbStatusUpdater;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.rest.AbstractUtilsRest;
import net.sf.ahtutils.xml.aht.Container;
import org.jeesl.model.xml.io.ssi.sync.DataUpdate;
import org.jeesl.model.xml.system.util.Property;
import org.jeesl.model.xml.system.util.Utils;

public class SystemPropertyRestService <L extends JeeslLang,D extends JeeslDescription,
										C extends JeeslStatus<L,D,C>,
										P extends JeeslProperty<L,D,C,P>>
					extends AbstractUtilsRest<L,D>
					implements JeeslSystemPropertyRestExport,JeeslSystemPropertyRestImport
{
	final static Logger logger = LoggerFactory.getLogger(SystemPropertyRestService.class);
	
	private JeeslSystemPropertyFacade<L,D,C,P> fProperty;
	private final PropertyFactoryBuilder<L,D,C,P> fbProperty;
	
	private EjbPropertyFactory<L,D,C,P> efProperty;
	
	private SystemPropertyRestService(JeeslSystemPropertyFacade<L,D,C,P> fProperty,final String[] localeCodes, final PropertyFactoryBuilder<L,D,C,P> fbProperty)
	{
		super(fProperty,localeCodes,fbProperty.getClassL(),fbProperty.getClassD());
		this.fProperty=fProperty;
		this.fbProperty=fbProperty;
		
		efProperty = EjbPropertyFactory.factory(fbProperty.getClassProperty());
	}
	
	public static <L extends JeeslLang,D extends JeeslDescription,
					C extends JeeslStatus<L,D,C>,
					P extends JeeslProperty<L,D,C,P>>
		SystemPropertyRestService<L,D,C,P>
			factory(JeeslSystemPropertyFacade<L,D,C,P> fNews, final String[] localeCodes, final PropertyFactoryBuilder<L,D,C,P> fbProperty)
	{
		return new SystemPropertyRestService<L,D,C,P>(fNews,localeCodes,fbProperty);
	}
	
	@Override public Container exportSystemPropertyCategories() {return super.exportContainer(fbProperty.getClassCategory());}
	@Override public DataUpdate importSystemPropertyCategories(Container categories){return importStatus2(fbProperty.getClassCategory(),null,categories);}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
    public <S extends JeeslStatus<L,D,S>> DataUpdate importStatus2(Class<S> clStatus, Class<P> clParent, Container container)
    {
    	for(Status xml : container.getStatus()){xml.setGroup(clStatus.getSimpleName());}
		JeeslDbStatusUpdater asdi = new JeeslDbStatusUpdater();
        asdi.setStatusEjbFactory(EjbStatusFactory.instance(clStatus, cL, cD));
        asdi.setFacade(fProperty);
        DataUpdate dataUpdate = asdi.iuStatus(container.getStatus(), clStatus, cL, clParent);
        asdi.deleteUnusedStatus(clStatus, cL, cD);
        return dataUpdate;
    }

	@Override
	public DataUpdate importSystemProperties(Utils utils)
	{
		DataUpdateTracker dut = new DataUpdateTracker(true);
		dut.setType(XmlTypeFactory.build(fbProperty.getClassD().getName(),JeeslProperty.class.getSimpleName()+"-DB Import"));
		
		for(Property property : utils.getProperty())
		{			
			P ejb;			
			try
			{
				fProperty.valueStringForKey(property.getKey(),null);
			}
			catch (JeeslNotFoundException e1)
			{
				ejb = efProperty.build(property);
				dut.success();
				try
				{
					ejb = (P)fUtils.persist(ejb);
				}
				catch (JeeslConstraintViolationException e) {dut.fail(e, true);}
			}
		}

		return dut.toDataUpdate();
	}

	@Override
	public Container exportSystemProperties() {
		// TODO Auto-generated method stub
		return null;
	}
}