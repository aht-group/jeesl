package org.jeesl.model.json.ssi.pvgis.meta;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MetaSection
{
    private String description;
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    private String choices;
    public String getChoices() { return choices; }
    public void setChoices(String choices) { this.choices = choices; }

    @JsonProperty("variables")
    private Map<String, MetaVariable> variables;
    public Map<String, MetaVariable> getVariables() { return variables; }
    public void setVariables(Map<String, MetaVariable> variables) { this.variables = variables; }

    @JsonProperty("fields")
    private Map<String, MetaVariable> fields;
    public Map<String, MetaVariable> getFields() { return fields; }
    public void setFields(Map<String, MetaVariable> fields) { this.fields = fields; }
}