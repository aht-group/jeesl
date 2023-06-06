package org.jeesl.api.rest.rs.module.mdc;

import org.jeesl.model.json.module.mdc.JsonMdcContainer;
import org.jeesl.model.json.module.mdc.JsonMdcData;

public interface JeeslMdcRestInterface
{	
	JsonMdcContainer enrolment(String token);
	JsonMdcContainer download(Long id);
	JsonMdcData upload(JsonMdcData data);
}