package org.jeesl.model.json.io.db.pg;

import java.io.Serializable;

public class JsonPostgresReplication implements Serializable
{
	public static final long serialVersionUID=1;
	
	private long id;
	public long getId() {return id;}
	public void setId(long id) {this.id = id;}
	
	private String state;
	public String getState() {return state;}
	public void setState(String state) {this.state = state;}
	
	private String clientAddr;
	public String getClientAddr() {return clientAddr;}
	public void setClientAddr(String clientAddr)  {this.clientAddr = clientAddr;}
	
	private double replayLag;
	public double getReplayLag() {return replayLag;}	
	public void setReplayLag(double replayLag) {this.replayLag = replayLag;}
	
	private double flushLag;
	public double getFlushLag() {return flushLag;}	
	public void setFlushLag(double flushLag) {this.flushLag = flushLag;}
	
	private double writeLag;
	public double getWriteLag() {return writeLag;}	
	public void setWriteLag(double writeLag) {this.writeLag = writeLag;}
	
}