package org.jeesl.model.ejb.io.ai.openai;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.io.ai.openai.JeeslIoAiOpenAiModel;
import org.jeesl.interfaces.qualifier.er.EjbErNode;

@Entity
//jeesl.highlight:unique
@Table(name="IoAiOpenAiModel",uniqueConstraints={
		@UniqueConstraint(name="uk_IoAiOpenAiModel_code",columnNames={"code"}),
		@UniqueConstraint(name="uk_IoAiOpenAiModel_generation",columnNames={"generation_id", "fallback"})
	})
//jeesl.highlight:unique
@EjbErNode(name="Model",category="system",subset="cms")
public class IoAiOpenAiModel implements JeeslIoAiOpenAiModel<IoOpenAiGeneration>
{
	public static final long serialVersionUID=1;	


	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}

	private String code;
	@Override public String getCode() {return code;}
	@Override public void setCode(String code) {this.code = code;}

	@Override
	public String resolveParentAttribute() {return JeeslIoAiOpenAiModel.At.generation.toString();}
	@ManyToOne @NotNull
	@JoinColumn(foreignKey=@ForeignKey(name="fk_IoAiOpenAiModel_generation"))
	private IoOpenAiGeneration generation;
	@Override public IoOpenAiGeneration getGeneration() {return generation;}
	@Override public void setGeneration(IoOpenAiGeneration generation) {this.generation = generation;}
	
	private Boolean fallback;
	@Override public Boolean getFallback() {return fallback;}
	@Override public void setFallback(Boolean fallback) {this.fallback = fallback;}
	
	private int contextWindow;
	@Override public int getContextWindow() {return contextWindow;}
	@Override public void setContextWindow(int contextWindow) {this.contextWindow = contextWindow;}
	
	private int responseToken;
	@Override public int getResponseToken() {return responseToken;}
	@Override public void setResponseToken(int responseToken) {this.responseToken = responseToken;}

	private LocalDate record;
	@Override public LocalDate getRecord() {return record;}
	@Override public void setRecord(LocalDate record) {this.record = record;}


	@Override public boolean equals(Object object){return (object instanceof IoAiOpenAiModel) ? id == ((IoAiOpenAiModel) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,53).append(id).toHashCode();}

	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[").append(id).append("]");
		return sb.toString();
	}
}