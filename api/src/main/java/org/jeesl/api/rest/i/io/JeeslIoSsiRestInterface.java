package org.jeesl.api.rest.i.io;

import org.jeesl.model.json.io.ssi.core.JsonSsiContainer;
import org.jeesl.model.json.io.ssi.core.JsonSsiSystem;

public interface JeeslIoSsiRestInterface
{
//	void x();
	JsonSsiSystem getUrlCredentials(String system);
	JsonSsiContainer getNat(String system, String host);
}