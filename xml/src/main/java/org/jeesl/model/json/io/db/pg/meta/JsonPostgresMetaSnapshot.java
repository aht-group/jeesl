package org.jeesl.model.json.io.db.pg.meta;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import org.jeesl.model.json.io.ssi.core.JsonSsiSystem;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="snapshot")
public class JsonPostgresMetaSnapshot implements Serializable
{
	public static final long serialVersionUID=1;
	
	@JsonProperty("record")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss.SSS")
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime record;
	public LocalDateTime getRecord() {return record;}
	public void setRecord(LocalDateTime record) {this.record = record;}
	
	@JsonProperty("system")
	private JsonSsiSystem system;
	public JsonSsiSystem getSystem() {return system;}
	public void setSystem(JsonSsiSystem system) {this.system = system;}

	@JsonProperty("tables")
	private List<JsonPostgresMetaTable> tables;
	public List<JsonPostgresMetaTable> getTables() {return tables;}
	public void setTables(List<JsonPostgresMetaTable> tables) {this.tables = tables;}
	
	
}