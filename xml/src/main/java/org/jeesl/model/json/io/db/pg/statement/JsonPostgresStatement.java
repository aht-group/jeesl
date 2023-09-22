package org.jeesl.model.json.io.db.pg.statement;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.jeesl.model.json.io.ssi.core.JsonSsiHost;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

@JsonRootName(value="statement")
//@JsonPropertyOrder({"id", "code", "record", "rows"})
public class JsonPostgresStatement implements Serializable
{
	public static final long serialVersionUID=1;
	
	public JsonPostgresStatement()
	{
		
	}
	
	@JsonProperty("id")
	private Long id;
	public Long getId() {return id;}
	public void setId(Long id) {this.id = id;}
	
	@JsonProperty("code")
	private String code;
	public String getCode() {return code;}
	public void setCode(String code) {this.code = code;}
	
	@JsonProperty("host")
	private JsonSsiHost host;
	public JsonSsiHost getHost() {return host;}
	public void setHost(JsonSsiHost host) {this.host = host;}

	@JsonProperty("record")
	@javax.json.bind.annotation.JsonbDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
	@jakarta.json.bind.annotation.JsonbDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSS")
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime record;
	public LocalDateTime getRecord() {return record;}
	public void setRecord(LocalDateTime record) {this.record = record;}

	@JsonProperty("rows")
	private Long rows;
	public Long getRows() {return rows;}
	public void setRows(Long rows) {this.rows = rows;}
	
	@JsonProperty("calls")
	private Long calls;
	public Long getCalls() {return calls;}
	public void setCalls(Long calls) {this.calls = calls;}
	
	@JsonProperty("average")
	private Double average;
	public Double getAverage() {return average;}
	public void setAverage(Double average) {this.average = average;}

	@JsonProperty("total")
	private Double total;
	public Double getTotal() {return total;}
	public void setTotal(Double total) {this.total = total;}

	@JsonProperty("sql")
	private String sql;
	public String getSql() {return sql;}
	public void setSql(String sql) {this.sql = sql;}
	
	@JsonProperty("xhtml")
	private String xhtml;
	public String getXhtml() {return xhtml;}
	public void setXhtml(String xhtml) {this.xhtml = xhtml;}
	
	@JsonProperty("remark")
	private String remark;
	public String getRemark() {return remark;}
	public void setRemark(String remark) {this.remark = remark;}
}