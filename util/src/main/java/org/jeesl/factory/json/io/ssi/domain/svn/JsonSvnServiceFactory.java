package org.jeesl.factory.json.io.ssi.domain.svn;

import java.util.ArrayList;
import java.util.Objects;

import org.jeesl.factory.json.system.status.JsonStatusFactory;
import org.jeesl.model.json.io.ssi.core.JsonSsiCredential;
import org.jeesl.model.json.io.ssi.svn.JsonSvnRepository;
import org.jeesl.model.json.io.ssi.svn.JsonSvnRevision;
import org.jeesl.model.json.io.ssi.svn.JsonSvnService;

public class JsonSvnServiceFactory
{
	private JsonSvnService json;
	
	public static JsonSvnServiceFactory instance() {return new JsonSvnServiceFactory();}
	private JsonSvnServiceFactory()
	{
		json = JsonSvnServiceFactory.build();
	}
	
	// Fluent
	public JsonSvnService assemble() {return json;}
	public JsonSvnServiceFactory repository(JsonSvnRepository repository) {json.setRepository(repository); return this;}
	public JsonSvnServiceFactory connection(JsonSsiCredential connection) {json.setConnection(connection); return this;}
	public JsonSvnServiceFactory revision(long revision) {json.setRevision(revision); return this;}
	
	public <E extends Enum<E>> JsonSvnServiceFactory status(E status) {json.setStatus(JsonStatusFactory.build(status)); return this;}
	
	public JsonSvnServiceFactory revision(JsonSvnRevision revision) {if(Objects.isNull(json.getRevisions())) {json.setRevisions(new ArrayList<>());} json.getRevisions().add(revision); return this;}
	
	
	public static JsonSvnService build() {return new JsonSvnService();}
}