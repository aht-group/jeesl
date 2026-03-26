package org.jeesl.model.json.ssi.openmeteo;

import java.time.OffsetDateTime;
import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="meta")
public class JsonMeteoApi
{
	@JsonProperty("lastStart")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ssXXX")
	private ZonedDateTime lastStart;
	public ZonedDateTime getLastStart() {return lastStart;}
	public void setLastStart(ZonedDateTime lastStart) {this.lastStart = lastStart;}
	
	@JsonProperty("lastAvailable")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ssXXX")
	private OffsetDateTime lastAvailable;
	public OffsetDateTime getLastAvailable() {return lastAvailable;}
	public void setLastAvailable(OffsetDateTime lastAvailable) {this.lastAvailable = lastAvailable;}
	
	@JsonProperty("status")
	private String status;
	public String getStatus() {return status;}
	public void setStatus(String status) {this.status = status;}
}