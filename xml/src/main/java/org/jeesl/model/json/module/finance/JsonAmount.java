
package org.jeesl.model.json.module.finance;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="collection")
public class JsonAmount implements Serializable
{
    private final static long serialVersionUID = 1L;
 
    public JsonAmount() {}
	
	@JsonProperty("value")
	private Double value;
	public Double getValue() {return value;}
	public void setValue(Double value) {this.value = value;}
	
	@JsonProperty("currency")
	private JsonCurrency currency;
	public JsonCurrency getCurrency() {return currency;}
	public void setCurrency(JsonCurrency currency) {this.currency = currency;}
}