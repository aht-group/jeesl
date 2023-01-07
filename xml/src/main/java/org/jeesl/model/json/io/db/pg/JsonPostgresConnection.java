package org.jeesl.model.json.io.db.pg;

import java.io.Serializable;
import java.util.Date;

public class JsonPostgresConnection implements Serializable
{
	public static final long serialVersionUID=1;
	
	private long id;
	public long getId() {return id;}
	public void setId(long id) {this.id = id;}
	
	private Date transaction;
	public Date getTransaction() {return transaction;}
	public void setTransaction(Date transaction) {this.transaction = transaction;}

	private Date query;
	public Date getQuery() {return query;}
	public void setQuery(Date query) {this.query = query;}
	
	private Date change;
	public Date getChange() {return change;}
	public void setChange(Date change) {this.change = change;}

	private String state;
	public String getState() {return state;}
	public void setState(String state) {this.state = state;}
	
	private String sql;
	public String getSql() {return sql;}
	public void setSql(String sql) {this.sql = sql;}
}