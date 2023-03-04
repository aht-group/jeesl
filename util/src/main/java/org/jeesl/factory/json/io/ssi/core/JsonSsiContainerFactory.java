package org.jeesl.factory.json.io.ssi.core;

import java.util.ArrayList;
import java.util.Objects;

import org.jeesl.model.json.io.ssi.core.JsonSsiContainer;
import org.jeesl.model.json.io.ssi.core.JsonSsiCredential;

public class JsonSsiContainerFactory
{
	public static JsonSsiContainer build() {return new JsonSsiContainer();}
	
	public static void add(JsonSsiContainer container, JsonSsiCredential credential)
	{
		if(Objects.isNull(container.getCredentials())) {container.setCredentials(new ArrayList<>());}
		if(Objects.nonNull(credential)) {container.getCredentials().add(credential);}
	}
}