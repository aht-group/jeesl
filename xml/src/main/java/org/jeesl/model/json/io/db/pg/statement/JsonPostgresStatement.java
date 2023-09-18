package org.jeesl.model.json.io.db.pg.statement;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

@JsonRootName(value="statement")
@JsonPropertyOrder({"id", "code", "record", "rows"})
public class JsonPostgresStatement implements Serializable
{
	public static final long serialVersionUID=1;
	
	public JsonPostgresStatement()
	{
		
	}
	
	private Long id;
	public Long getId() {return id;}
	public void setId(Long id) {this.id = id;}
	
	@JsonProperty("code")
	private String code;
	public String getCode() {return code;}
	public void setCode(String code) {this.code = code;}
	
	@JsonProperty("record")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss.SSS")
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime record;
	public LocalDateTime getRecord() {return record;}
	public void setRecord(LocalDateTime record) {this.record = record;}

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
	
	private String xhtml;
	public String getXhtml() {return xhtml;}
	public void setXhtml(String xhtml) {this.xhtml = xhtml;}
	
	private String remark;
	public String getRemark() {return remark;}
	public void setRemark(String remark) {this.remark = remark;}
}