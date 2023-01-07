package org.jeesl.model.json.io.db.pg;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class JsonPostgres implements Serializable
{
	public static final long serialVersionUID=1;
	
	private List<JsonPostgresConnection> connections;
	public List<JsonPostgresConnection> getConnections() {if(connections==null) {connections = new ArrayList<>();}return connections;}
	public void setConnections(List<JsonPostgresConnection> connections) {this.connections = connections;}
	
	private List<JsonPostgresReplication> replications;
	public List<JsonPostgresReplication> getReplications() {if(replications==null) {replications = new ArrayList<>();} return replications;}
	public void setReplications(List<JsonPostgresReplication> replications) {this.replications = replications;}
	
	private List<JsonPostgresStatement> statements;
	public List<JsonPostgresStatement> getStatements() {if(statements==null) {statements = new ArrayList<>();} return statements;}
	public void setStatements(List<JsonPostgresStatement> statements) {this.statements = statements;}
}