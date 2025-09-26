
package org.jeesl.model.json.module.ts.data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
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
public class JsonTsAggegation implements Serializable
{
    private final static long serialVersionUID = 1L;

    @JsonProperty("seriesId")    
    private Long seriesId;
    public Long getSeriesId() {return seriesId;}
	public void setSeriesId(Long seriesId) {this.seriesId = seriesId;}
	
	private LocalDateTime record;

	public LocalDateTime getRecord() {
		return record;
	}
	public void setRecord(LocalDateTime record) {
		this.record = record;
	}

	@JsonProperty("value")    
	private Double value;
    public Double getValue() {return value;}
    public void setValue(Double value) {this.value = value;}
	
//	public JsonTsAggegation(Long seriesId, Date time, Double value)
//    {
//        this.seriesId = seriesId;
////        this.time = time;
//        this.value = value;
//    }
}