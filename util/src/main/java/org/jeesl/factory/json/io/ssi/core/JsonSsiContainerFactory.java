package org.jeesl.factory.json.io.ssi.core;

import java.util.ArrayList;
import java.util.Objects;

import org.jeesl.model.json.io.ssi.core.JsonSsiContainer;
import org.jeesl.model.json.io.ssi.core.JsonSsiCredential;
import org.jeesl.model.json.io.ssi.core.JsonSsiNat;
import org.jeesl.model.json.io.ssi.svn.JsonSvnRepository;

public class JsonSsiContainerFactory
{
	private JsonSsiContainer json;
	
	public static JsonSsiContainerFactory instance() {return new JsonSsiContainerFactory();}
	private JsonSsiContainerFactory()
	{
		json = JsonSsiContainerFactory.create();
	}
	
	public JsonSsiContainer assemble() {return json;}
	public static JsonSsiContainer create() {return new JsonSsiContainer();}
	
	public static void add(JsonSsiContainer container, JsonSsiCredential credential)
	{
		if(Objects.isNull(container.getCredentials())) {container.setCredentials(new ArrayList<>());}
		if(Objects.nonNull(credential)) {container.getCredentials().add(credential);}
	}
	
	public void add(JsonSsiNat nat)
	{
		if(Objects.isNull(json.getNats())) {json.setNats(new ArrayList<>());}
		if(Objects.nonNull(nat)) {json.getNats().add(nat);}
	}
	
	public void add(JsonSvnRepository repository)
	{
		if(Objects.isNull(json.getSvns())) {json.setSvns(new ArrayList<>());}
		if(Objects.nonNull(repository)) {json.getSvns().add(repository);}
	}
}