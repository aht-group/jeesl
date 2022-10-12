package org.jeesl.factory.ejb.system.job.mnt;

import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.system.job.JeeslJobStatus;
import org.jeesl.interfaces.model.system.job.mnt.JeeslJobMaintenance;
import org.jeesl.interfaces.model.system.job.mnt.JeeslJobMaintenanceInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbJobMaintenanceInfoFactory <STATUS extends JeeslJobStatus<?,?,STATUS,?>,
											MNT extends JeeslJobMaintenance<?,?,MNT,?>,
											MNI extends JeeslJobMaintenanceInfo<?,STATUS,MNT>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbJobMaintenanceInfoFactory.class);
	
	private final Class<STATUS> cStatus;
	private final Class<MNT> cMnt;
	private final Class<MNI> cMni;

	public EjbJobMaintenanceInfoFactory(final Class<STATUS> cStatus, final Class<MNT> cMnt, final Class<MNI> cMni)
	{
		this.cStatus=cStatus;
		this.cMnt=cMnt;
        this.cMni = cMni;
	}
 
	public MNI build(MNT maintenance, STATUS status)
	{
		MNI ejb = null;
		try
		{
			ejb = cMni.newInstance();
			ejb.setMaintenance(maintenance);
			ejb.setStatus(status);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
	
	public void converter(JeeslFacade facade, MNI ejb)
	{
		if(ejb.getStatus()!=null) {ejb.setStatus(facade.find(cStatus,ejb.getStatus()));}
		if(ejb.getMaintenance()!=null) {ejb.setMaintenance(facade.find(cMnt,ejb.getMaintenance()));}
	}
}