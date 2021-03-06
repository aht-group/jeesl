package net.sf.ahtutils.interfaces.model.system.mail;

import java.util.Date;

import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface UtilsMailTracker<S extends JeeslStatus<L,D,S>,
									L extends JeeslLang,
									U extends EjbWithId,
									D extends JeeslDescription>
						extends EjbWithId
{
	long getRefId();
	void setRefId(long refId);
	
	S getType();
	void setType(S type);
	
	Date getRecordCreated();
	void setRecordCreated(Date recordGenerated);
	
	Date getRecordSent();
	void setRecordSent(Date recordSent);
	
	int getRetryCounter();
	void setRetryCounter(int retryCounter);
	
	U getUser();
	void setUser(U user);
}