package org.jeesl.model.json.module.ts;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.jeesl.model.json.system.status.JsonCategory;
import org.jeesl.model.json.system.status.JsonInterval;
import org.jeesl.model.json.system.status.JsonWorkspace;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="series")
public class JsonTsSeries implements Serializable
{
	public static final long serialVersionUID=1;

	@JsonProperty("category")
	private JsonCategory category;
	public JsonCategory getCategory() {return category;}
	public void setCategory(JsonCategory category) {this.category = category;}

	@JsonProperty("scope")
	private JsonTsScope scope;
	public JsonTsScope getScope() {return scope;}
	public void setScope(JsonTsScope scope) {this.scope = scope;}
	
	@JsonProperty("bridge")
	private JsonTsBridge bridge;
	public JsonTsBridge getBridge() {return bridge;}
	public void setBridge(JsonTsBridge bridge) {this.bridge = bridge;}

	@JsonProperty("stat")
	private JsonTsStat stat;
	public JsonTsStat getStat() {return stat;}
	public void setStat(JsonTsStat stat) {this.stat = stat;}

	@JsonProperty("interval")
	private JsonInterval interval;
	public JsonInterval getInterval() {return interval;}
	public void setInterval(JsonInterval interval) {this.interval = interval;}

	@JsonProperty("workspace")
	private JsonWorkspace workspace;
	public JsonWorkspace getWorkspace() {return workspace;}
	public void setWorkspace(JsonWorkspace workspace) {this.workspace = workspace;}

	@JsonProperty("size")
	private Integer size;
	public Integer getSize() {return size;}
	public void setSize(Integer size) {this.size = size;}

	@JsonProperty("dateStart")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
	private Date dateStart;
	public Date getDateStart() {return dateStart;}
	public void setDateStart(Date dateStart) {this.dateStart = dateStart;}

	@JsonProperty("dateEnd")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
	private Date dateEnd;
	public Date getDateEnd() {return dateEnd;}
	public void setDateEnd(Date dateEnd) {this.dateEnd = dateEnd;}
	
	@JsonProperty("ldStart")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	private LocalDate ldStart;
	public LocalDate getLdStart() {return ldStart;}
	public void setLdStart(LocalDate ldStart) {this.ldStart = ldStart;}
	
	@JsonProperty("ldEnd")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	private LocalDate ldEnd;
	public LocalDate getLdEnd() {return ldEnd;}
	public void setLdEnd(LocalDate ldEnd) {this.ldEnd = ldEnd;}

	@JsonProperty("datas")
	private List<JsonTsData> datas;
	public List<JsonTsData> getDatas() {return datas;}
	public void setDatas(List<JsonTsData> datas) {this.datas = datas;}

	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();
		return sb.toString();
	}
}