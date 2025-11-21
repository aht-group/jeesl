package org.jeesl.interfaces.model.io.ssi.data;

import java.time.LocalDateTime;
import java.util.Date;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.job.core.JeeslJobStatus;
import org.jeesl.interfaces.model.system.job.with.EjbWithMigrationJob1;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslIoSsiData <CTX extends JeeslIoSsiContext<?,?>,
									STATUS extends JeeslStatus<?,?,STATUS>,
									ERROR extends JeeslIoSsiError<?,?,CTX,?>,
									JOB extends JeeslJobStatus<?,?,JOB,?>>
		extends EjbWithId,EjbSaveable,EjbRemoveable,EjbWithCode,EjbWithParentAttributeResolver,
						EjbWithMigrationJob1<JOB>
{	
	public enum Attributes{id,mapping,code,link,remark,targetId,localId,refA,refB,refC,job1,error}
	public enum Att{context,jsonCreatedAt,error}
	
//	void x();
	
	public CTX getMapping();
	public void setMapping(CTX mapping);
	
	public STATUS getLink();
	void setLink(STATUS link);
	
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
	
	LocalDateTime getJsonCreatedAt();
	void setJsonCreatedAt(LocalDateTime jsonCreatedAt);
	
	LocalDateTime getJsonUpdatedAt();
	void setJsonUpdatedAt(LocalDateTime jsonUpdatedAt);
	
	Date getEjbCreatedAt();
	void setEjbCreatedAt(Date ejbCreatedAt);
	
	Date getEjbUpdatedAt();
	void setEjbUpdatedAt(Date ejbUpdatedAt);
	

	String getRemark();
	void setRemark(String remark);
	
	ERROR getError();
	void setError(ERROR error);
	
}