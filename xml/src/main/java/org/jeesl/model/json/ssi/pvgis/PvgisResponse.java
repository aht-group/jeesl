package org.jeesl.model.json.ssi.pvgis;

import org.jeesl.model.json.ssi.pvgis.input.Inputs;
import org.jeesl.model.json.ssi.pvgis.meta.Meta;
import org.jeesl.model.json.ssi.pvgis.output.Outputs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PvgisResponse
{
	@JsonProperty("inputs")
	private Inputs inputs;
	public Inputs getInputs() {return inputs;}
	public void setInputs(Inputs inputs) {this.inputs = inputs;}

	@JsonProperty("outputs")
	private Outputs outputs;
	public Outputs getOutputs() { return outputs; }
	public void setOutputs(Outputs outputs) { this.outputs = outputs; }

	@JsonProperty("meta")
	private Meta meta;
	public Meta getMeta() {return meta;}
	public void setMeta(Meta meta) {this.meta = meta;}
}