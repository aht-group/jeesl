package org.jeesl.interfaces.model.system.locale.status;

import java.io.Serializable;

import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslData;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslData
public interface JeeslAbstractStatus extends Serializable

// By activating this import it can be checked if all classes implement the interface, by default it needs to be commented out
//							,EjbWithLocked
{	
//	public void x();
}