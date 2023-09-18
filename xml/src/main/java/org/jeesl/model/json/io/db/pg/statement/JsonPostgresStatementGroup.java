package org.jeesl.model.json.io.db.pg.statement;

import java.io.Serializable;
import java.util.List;

import org.jeesl.model.json.io.ssi.core.JsonSsiSystem;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"id","code","name","system","remark"})
public class JsonPostgresStatementGroup implements Serializable
{
	public static final long serialVersionUID=1;
	
	public JsonPostgresStatementGroup()
	{
		
	}
	
	private Long id;
	public Long getId() {return id;}
	public void setId(Long id) {this.id = id;}
	
	private String code;
	public String getCode() {return code;}
	public void setCode(String code) {this.code = code;}

	private String name;
	public String getName() {return name;}
	public void setName(String name) {this.name = name;}
	
	private JsonSsiSystem system;
	public JsonSsiSystem getSystem() {return system;}
	public void setSystem(JsonSsiSystem system) {this.system = system;}
	
	private String remark;
	public String getRemark() {return remark;}
	public void setRemark(String remark) {this.remark = remark;}

	private List<JsonPostgresStatement> statements;
	public List<JsonPostgresStatement> getStatements() {return statements;}
	public void setStatements(List<JsonPostgresStatement> statements) {this.statements = statements;}


}