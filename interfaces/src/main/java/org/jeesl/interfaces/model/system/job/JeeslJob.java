package org.jeesl.interfaces.model.system.job;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.code.EjbWithNonUniqueCode;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;

import net.sf.ahtutils.interfaces.model.behaviour.EjbSaveable;
import net.sf.ahtutils.interfaces.model.crud.EjbRemoveable;
import net.sf.ahtutils.interfaces.model.with.EjbWithEmail;
import net.sf.ahtutils.model.interfaces.with.EjbWithId;

public interface JeeslJob<TEMPLATE extends JeeslJobTemplate<?,?,?,?,?,?>,
							PRIORITY extends JeeslStatus<PRIORITY,?,?>,
							FEEDBACK extends JeeslJobFeedback<?,?,USER>,
							STATUS extends JeeslStatus<STATUS,?,?>,
							USER extends EjbWithEmail>
		extends Serializable,EjbWithId,EjbSaveable,EjbRemoveable,EjbWithNonUniqueCode,EjbWithParentAttributeResolver
{	
	public static enum Attributes{template,status,priority,recordCreation,recordStart,code};
	
	public static enum Type{reportXml,reportXlsx,reportZip,db}
	
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
	
	List<FEEDBACK> getFeedbacks();
	void setFeedbacks(List<FEEDBACK> feedbacks);
	
	String getJsonFilter();
	void setJsonFilter(String jsonFilter);
}