package org.jeesl.factory.ejb.io.ssi.data;

import org.jeesl.factory.builder.io.ssi.IoSsiDataFactoryBuilder;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiContext;

public class EjbIoSsiMappingFactory <SYSTEM extends JeeslIoSsiSystem<?,?>,
										MAPPING extends JeeslIoSsiContext<SYSTEM,ENTITY>,
										ENTITY extends JeeslRevisionEntity<?,?,?,?,?,?>>
{
	private final IoSsiDataFactoryBuilder<?,?,SYSTEM,MAPPING,?,?,?,ENTITY,?,?> fbSsi;

	public EjbIoSsiMappingFactory(IoSsiDataFactoryBuilder<?,?,SYSTEM,MAPPING,?,?,?,ENTITY,?,?> fbSsi)
	{
        this.fbSsi = fbSsi;
	}
	
	public MAPPING build(SYSTEM system)
	{
		MAPPING ejb = null;
		try
		{
			ejb = fbSsi.getClassMapping().newInstance();
			ejb.setSystem(system);
	       
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		return ejb;
	}
	
	public void converter(JeeslFacade facade, MAPPING ejb)
	{
		if(ejb.getSystem()!=null) {ejb.setSystem(facade.find(fbSsi.getClassSystem(),ejb.getSystem()));}
		if(ejb.getClassA()!=null) {ejb.setClassA(facade.find(fbSsi.getClassEntity(),ejb.getClassA()));}
		if(ejb.getClassB()!=null) {ejb.setClassB(facade.find(fbSsi.getClassEntity(),ejb.getClassB()));}
		if(ejb.getClassC()!=null) {ejb.setClassC(facade.find(fbSsi.getClassEntity(),ejb.getClassC()));}
	}
}