package org.jeesl.interfaces.model.io.db.flyway;

import java.time.LocalDateTime;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithIntegerId;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslIoDbFlyway extends EjbWithIntegerId
{	
	String getVersion();
	void setVersion(String version);
	
	String getDescription();
	void setDescription(String description);
	
	LocalDateTime getRecord();
	void setRecord(LocalDateTime record);
	
	String getType();
	void setType(String type);
}