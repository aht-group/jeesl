package org.jeesl.model.json.io.fr;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value="FileRepositoryAmazonS3")
public class JsonFrAmazonS3 implements Serializable
{
	public static final long serialVersionUID=1;

	@JsonProperty("bucket")
	private String bucket;
	public String getBucket() {return bucket;}
	public void setBucket(String bucket) {this.bucket = bucket;}
	
	@JsonProperty("id")
	private String id;
	public String getId() {return id;}
	public void setId(String id) {this.id = id;}

	@JsonProperty("key")
	private String key;
	public String getKey() {return key;}
	public void setKey(String key) {this.key = key;}
}