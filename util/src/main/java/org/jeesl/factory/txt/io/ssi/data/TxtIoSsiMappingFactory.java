package org.jeesl.factory.txt.io.ssi.data;

import org.jeesl.interfaces.model.io.revision.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiMapping;

public class TxtIoSsiMappingFactory <SYSTEM extends JeeslIoSsiSystem<?,?>,
										MAPPING extends JeeslIoSsiMapping<SYSTEM,ENTITY>,
										ENTITY extends JeeslRevisionEntity<?,?,?,?,?,?>>
{
	private final String localeCode;
	
	public TxtIoSsiMappingFactory(String localeCode)
	{
        this.localeCode=localeCode;
	}
	
	public String debug(MAPPING system)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(system.getId());
		
		sb.append(" ").append(system.getJson().getName().get(localeCode).getLang());
		sb.append(" -> ").append(system.getEntity().getName().get(localeCode).getLang());
		
		return sb.toString();
	}

}