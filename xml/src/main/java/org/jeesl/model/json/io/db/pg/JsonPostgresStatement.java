package org.jeesl.model.json.io.db.pg;

import java.io.Serializable;

public class JsonPostgresStatement implements Serializable
{
	public static final long serialVersionUID=1;
	
	public JsonPostgresStatement()
	{
		
	}
	
	private long id;
	public long getId() {return id;}
	public void setId(long id) {this.id = id;}
	
	private Long rows;
	public Long getRows() {return rows;}
	public void setRows(Long rows) {this.rows = rows;}
	
	private Long calls;
	public Long getCalls() {return calls;}
	public void setCalls(Long calls) {this.calls = calls;}
	
	private Double average;
	public Double getAverage() {return average;}
	public void setAverage(Double average) {this.average = average;}

	private Double total;
	public Double getTotal() {return total;}
	public void setTotal(Double total) {this.total = total;}

	private String sql;
	public String getSql() {return sql;}
	public void setSql(String sql) {this.sql = sql;}
}