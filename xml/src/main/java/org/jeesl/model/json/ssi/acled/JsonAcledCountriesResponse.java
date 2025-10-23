package org.jeesl.model.json.ssi.acled;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="response")
public class JsonAcledCountriesResponse implements Serializable
{
	public static final long serialVersionUID=1;

	@JsonProperty("status")
	private Integer status;
	public Integer getStatus() {return status;}
	public void setStatus(Integer status) {this.status = status;}

	@JsonProperty("success")
	private Boolean success;
	public Boolean getSuccess() {return success;}
	public void setSuccess(Boolean success) {this.success = success;}

	@JsonProperty("count")
	private Integer count;
	public Integer getCount() {return count;}
	public void setCount(Integer count) {this.count = count;}

	@JsonProperty("data")
	private List<JsonAcledCountry> datas;
	public List<JsonAcledCountry> getData() {return datas;}
	public void setData(List<JsonAcledCountry> datas) {this.datas = datas;}

	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();
		return sb.toString();
	}
}