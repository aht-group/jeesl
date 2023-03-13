
package org.jeesl.model.json.module.ts;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import org.jeesl.model.json.system.security.JsonSecurityUser;
import org.jeesl.model.json.system.status.JsonSource;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="transaction")
public class JsonTransaction implements Serializable
{
    private final static long serialVersionUID = 1L;

    @JsonProperty("id")
    private Long id;
    public Long getId() {return id;}
    public void setId(Long value) {this.id = value;}
    
    @JsonProperty("localDateTime")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss.SSS")
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime localDateTime;
	public LocalDateTime getLocalDateTime() {return localDateTime;}
	public void setLocalDateTime(LocalDateTime localDateTime) {this.localDateTime = localDateTime;}
    
    @JsonProperty("user")
    private JsonSecurityUser user;
    public JsonSecurityUser getUser() {return user;}
    public void setUser(JsonSecurityUser value) {this.user = value;}

    @JsonProperty("source")
    private JsonSource source;
    public JsonSource getSource() {return source;}
    public void setSource(JsonSource source) {this.source = source;}
    
    private List<JsonTsSeries> series;
	public List<JsonTsSeries> getSeries() {return series;}
	public void setSeries(List<JsonTsSeries> series) {this.series = series;}
}