package org.jeesl.interfaces.model.io.db;

import java.io.Serializable;

import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiHost;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
public interface JeeslDbDumpFile<DUMP extends JeeslDbDump<?,?>,
								HOST extends JeeslIoSsiHost<?,?,?>,
								STATUS extends JeeslDbDumpStatus<?,?,STATUS,?>>
					extends Serializable,EjbSaveable,EjbRemoveable,EjbWithId
{
	public static enum Attributes{dump,host,status}
	public static enum Status{stored,flagged,deleted};
	
	DUMP getDump();
	void setDump(DUMP dump);
	
	HOST getHost();
	void setHost(HOST host);
	
	STATUS getStatus();
	void setStatus(STATUS status);
}