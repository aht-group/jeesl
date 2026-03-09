package org.jeesl.model.json.ssi.openmeteo;

import java.time.Instant;

import org.jeesl.util.time.UnixTimestampSerializer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="meta")
public class OpenMeteoMeta
{
	@JsonProperty("chunk_time_length")
	private Integer chunkTimeLength;
	public Integer getChunkTimeLength() {return chunkTimeLength;}
	public void setChunkTimeLength(Integer chunkTimeLength) {this.chunkTimeLength = chunkTimeLength;}
	
	@JsonProperty("data_end_time")
	@JsonDeserialize(using = UnixTimestampSerializer.Deserializer.class)
	@JsonSerialize(using = UnixTimestampSerializer.Serializer.class)
	private Instant dataEndTime;
	public Instant getDataEndTime() {return dataEndTime;}
	public void setDataEndTime(Instant dataEndTime) {this.dataEndTime = dataEndTime;}
	
	@JsonProperty("last_run_availability_time")
	@JsonDeserialize(using = UnixTimestampSerializer.Deserializer.class)
	@JsonSerialize(using = UnixTimestampSerializer.Serializer.class)
	private Instant lastRunAvailabilityTime;
	public Instant getLastRunAvailabilityTime() {return lastRunAvailabilityTime;}
	public void setLastRunAvailabilityTime(Instant lastRunAvailabilityTime) {this.lastRunAvailabilityTime = lastRunAvailabilityTime;}
	
	@JsonProperty("last_run_initialisation_time")
	@JsonDeserialize(using = UnixTimestampSerializer.Deserializer.class)
	@JsonSerialize(using = UnixTimestampSerializer.Serializer.class)
	private Instant lastRunInitialisationTime;
	public Instant getLastRunInitialisationTime() {return lastRunInitialisationTime;}
	public void setLastRunInitialisationTime(Instant lastRunInitialisationTime) {this.lastRunInitialisationTime = lastRunInitialisationTime;}

	@JsonProperty("last_run_modification_time")
	@JsonDeserialize(using = UnixTimestampSerializer.Deserializer.class)
	@JsonSerialize(using = UnixTimestampSerializer.Serializer.class)
	private Instant lastRunModificationTime;
	public Instant getLastRunModificationTime() {return lastRunModificationTime;}
	public void setLastRunModificationTime(Instant lastRunModificationTime) {this.lastRunModificationTime = lastRunModificationTime;}

	@JsonProperty("temporal_resolution_seconds")
	private Integer temporalResolutionSeconds;
	public Integer getTemporalResolutionSeconds() {return temporalResolutionSeconds;}
	public void setTemporalResolutionSeconds(Integer temporalResolutionSeconds) {this.temporalResolutionSeconds = temporalResolutionSeconds;}

	@JsonProperty("update_interval_seconds")
	private Integer updateIntervalSeconds;
	public Integer getUpdateIntervalSeconds() {return updateIntervalSeconds;}
	public void setUpdateIntervalSeconds(Integer updateIntervalSeconds) {this.updateIntervalSeconds = updateIntervalSeconds;}
	
	
}