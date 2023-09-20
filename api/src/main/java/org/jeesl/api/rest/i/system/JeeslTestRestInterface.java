package org.jeesl.api.rest.i.system;

import org.jeesl.model.json.io.ssi.update.JsonSsiUpdate;
import org.jeesl.model.json.system.job.JsonSystemJob;

public interface JeeslTestRestInterface
{
	String dateTimePublic();
	String dateTimeRestricted();
	
	JsonSsiUpdate jsonUpdate();
	JsonSystemJob jsonJob();
	
	String jsonMirror(String content);
}