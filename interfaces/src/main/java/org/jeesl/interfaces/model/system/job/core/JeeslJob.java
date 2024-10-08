package org.jeesl.interfaces.model.system.job.core;

import java.io.Serializable;
import java.util.Date;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.job.template.JeeslJobTemplate;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithNonUniqueCode;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslJob<TEMPLATE extends JeeslJobTemplate<?,?,?,?,?,?>,
							PRIORITY extends JeeslStatus<?,?,PRIORITY>,
							STATUS extends JeeslJobStatus<?,?,STATUS,?>,
							USER extends JeeslSimpleUser>
		extends Serializable,EjbWithId,EjbSaveable,EjbRemoveable,EjbWithNonUniqueCode,EjbWithParentAttributeResolver
{	
	public static enum Attributes{template,status,priority,recordCreation,recordStart,code};
	
	public static enum Type{reportXml,reportXlsx,reportZip,db,json}
	
	TEMPLATE getTemplate();
	void setTemplate(TEMPLATE template);
	
	STATUS getStatus();
	void setStatus(STATUS status);
	
	PRIORITY getPriority();
	void setPriority(PRIORITY priority);
	
	Date getRecordCreation();
	void setRecordCreation(Date recordCreation);
	
	Date getRecordStart();
	void setRecordStart(Date recordStart);
	
	Date getRecordComplete();
	void setRecordComplete(Date recordComplete);
	
	String getName();
	void setName(String name);
	
	USER getUser();
	void setUser(USER user);
	
	Integer getAttempts();
	void setAttempts(Integer attempts);
	
	Integer getProcessingCounter();
	void setProcessingCounter(Integer processingCounter);
	
//	List<FEEDBACK> getFeedbacks();
//	void setFeedbacks(List<FEEDBACK> feedbacks);
	
	String getJsonFilter();
	void setJsonFilter(String jsonFilter);
	
}