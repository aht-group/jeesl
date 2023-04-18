package org.jeesl.interfaces.model.io.mail.core;

import java.io.Serializable;
import java.util.Date;

import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslIoMail<L extends JeeslLang,D extends JeeslDescription,
								CATEGORY extends JeeslStatus<L,D,CATEGORY>,
								STATUS extends JeeslIoMailStatus<L,D,STATUS,?>,
								RETENTION extends JeeslStatus<L,D,RETENTION>,
								FRC extends JeeslFileContainer<?,?>
								>
		extends Serializable,EjbWithId,
					EjbRemoveable,EjbPersistable,EjbSaveable
{	
	
	public static enum Attributes{category,status,retention,recordCreation,recordSpool};
	
	Long getVersionLock();
	
	CATEGORY getCategory();
	void setCategory(CATEGORY category);
	
	STATUS getStatus();
	void setStatus(STATUS status);
	
	RETENTION getRetention();
	void setRetention(RETENTION retention);
	
	Date getRecordCreation();
	void setRecordCreation(Date recordCreation);
	
	Date getRecordSpool();
	void setRecordSpool(Date recordSpool);
	
	Date getRecordSent();
	void setRecordSent(Date recordSent);
	
	String getRecipient();
	void setRecipient(String recipient);
	
	int getCounter();
	void setCounter(int counter);
	
	Integer getRetentionDays();
	void setRetentionDays(Integer retentionDays);
	
	String getXml();
	void setXml(String xml);
}