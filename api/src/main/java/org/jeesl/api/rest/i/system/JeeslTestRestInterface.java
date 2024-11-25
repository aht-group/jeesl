package org.jeesl.api.rest.i.system;

import org.jeesl.model.json.io.ssi.update.JsonSsiUpdate;
import org.jeesl.model.json.system.job.JsonSystemJob;
import org.jeesl.model.json.util.JsonTime;
import org.jeesl.model.xml.system.test.Test;

public interface JeeslTestRestInterface
{
	String dateTimePublic();
	String dateTimeRestricted();
	
	String timeout(int seconds);
	
	JsonSsiUpdate jsonUpdate();
	JsonSystemJob jsonJob();
	
	JsonTime jsonTimeDownload();
	JsonTime jsonTimeUpload(JsonTime jsonTime);
	
	Test jaxb();
}