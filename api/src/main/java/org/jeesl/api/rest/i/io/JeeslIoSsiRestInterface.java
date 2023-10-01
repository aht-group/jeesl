package org.jeesl.api.rest.i.io;

import org.jeesl.model.json.io.ssi.core.JsonSsiSystem;

public interface JeeslIoSsiRestInterface
{
	JsonSsiSystem getCredentials(String system);
}