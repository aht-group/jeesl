package org.jeesl.model.json.ssi.pvgis;

import java.util.List;

import org.jeesl.model.json.io.ssi.core.JsonSsiCredential;
import org.jeesl.model.json.ssi.pvgis.input.PvgisInput;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PvgisRequest
{
	@JsonProperty("inputs")
	private List<PvgisInput> inputs;
	public List<PvgisInput> getInputs() {return inputs;}
	public void setInputs(List<PvgisInput> inputs) {this.inputs = inputs;}
	
	@JsonProperty("connection")
	private JsonSsiCredential connection;
	public JsonSsiCredential getConnection() {return connection;}
	public void setConnection(JsonSsiCredential connection) {this.connection = connection;}
}