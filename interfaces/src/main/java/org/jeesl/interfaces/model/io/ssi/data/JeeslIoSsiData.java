package org.jeesl.interfaces.model.io.ssi.data;

import java.util.Date;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.job.JeeslJobStatus;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslIoSsiData <MAPPING extends JeeslIoSsiMapping<?,?>,
									LINK extends JeeslStatus<?,?,LINK>,
									JOB extends JeeslJobStatus<?,?,JOB,?>>
		extends EjbWithId,EjbSaveable,EjbRemoveable,EjbWithCode,EjbWithParentAttributeResolver
{	
	public enum Attributes{mapping,code,link,targetId,localId,refA,refB,refC}
	
	public MAPPING getMapping();
	public void setMapping(MAPPING mapping);
	
	public LINK getLink();
	void setLink(LINK link);
	
	String getJson();
	void setJson(String json);
	
	Long getTargetId();
	void setTargetId(Long targetId);
	
	Long getLocalId();
	void setLocalId(Long localId);

	Long getRefA();
	void setRefA(Long refA);

	Long getRefB();
	void setRefB(Long refB);
	
	Long getRefC();
	void setRefC(Long refC);
	
	Date getJsonCreatedAt();
	void setJsonCreatedAt(Date jsonCreatedAt);
	
	Date getJsonUpdatedAt();
	void setJsonUpdatedAt(Date jsonUpdatedAt);
	
	Date getEjbCreatedAt();
	void setEjbCreatedAt(Date ejbCreatedAt);
	
	Date getEjbUpdatedAt();
	void setEjbUpdatedAt(Date ejbUpdatedAt);
}