package org.jeesl.model.json.io.label;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value="numberPrecision")
public class JsonLabelNumberPrecision implements Serializable
{
	public static final long serialVersionUID=1;

	@JsonProperty("minFractionDigits")
	private Integer minFractionDigits;
	public Integer getMinFractionDigits() {return minFractionDigits;}
	public void setMinFractionDigits(Integer minFractionDigits) {this.minFractionDigits = minFractionDigits;}

	@JsonProperty("maxFractionDigits")
	private Integer maxFractionDigits;
	public Integer getMaxFractionDigits() {return maxFractionDigits;}
	public void setMaxFractionDigits(Integer maxFractionDigits) {this.maxFractionDigits = maxFractionDigits;}

	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("minFractionDigits: ").append(minFractionDigits);
		sb.append(" maxFractionDigits: ").append(maxFractionDigits);
		return sb.toString();
	}
}