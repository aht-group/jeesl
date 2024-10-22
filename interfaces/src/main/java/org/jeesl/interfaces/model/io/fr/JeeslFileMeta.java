package org.jeesl.interfaces.model.io.fr;

import java.io.Serializable;
import java.util.Date;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.with.date.jt.JeeslWithRecordDateTime;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithSize;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPosition;
import org.jeesl.interfaces.model.with.system.locale.EjbWithDescription;
import org.jeesl.interfaces.model.with.system.status.JeeslWithStatus;
import org.jeesl.interfaces.model.with.system.status.JeeslWithType;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslAttributes
@DownloadJeeslDescription
public interface JeeslFileMeta<D extends JeeslDescription,
								CONTAINER extends JeeslFileContainer<?,?>,
								TYPE extends JeeslFileType<?,D,TYPE,?>,
								STATUS extends JeeslFileStatus<?,D,STATUS,?>
>
			extends Serializable,EjbWithId,EjbSaveable,EjbRemoveable,
					EjbWithParentAttributeResolver,
					EjbWithCode,EjbWithPosition,
					JeeslWithType<TYPE>,JeeslWithStatus<STATUS>,
					EjbWithSize,EjbWithDescription<D>,
				//	EjbWithRecord
					JeeslWithRecordDateTime
{
	public enum Attributes{container,type,size,record}
	
	CONTAINER getContainer();
	void setContainer(CONTAINER container);
	
	String getMd5Hash();
	void setMd5Hash(String md5Hash);
	
	String getFileName();
	void setFileName(String fileName);
	
	Date getStatusCheck();
	void setStatusCheck(Date statusCheck);
	
	String getCategory();
	void setCategory(String category);
}