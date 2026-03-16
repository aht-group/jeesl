package org.jeesl.model.ejb.io.attribute;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeData;
import org.jeesl.interfaces.qualifier.er.EjbErNode;

@Entity
@Table(name="IoAttributeData")
@EjbErNode(name="Data",category="ioAttribute",level=3,subset="moduleTs")
public class IoAttributeData implements JeeslAttributeData<IoAttributeCriteria,IoAttributeOption,IoAttributeContainer>
{
	public static final long serialVersionUID=1;


	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}

	@Override public String resolveParentAttribute() {return JeeslAttributeData.Attributes.container.toString();}
	@NotNull @ManyToOne
	@JoinColumn(foreignKey=@ForeignKey(name="fk_IoAttributeData_container"))
	private IoAttributeContainer container;
	@Override public IoAttributeContainer getContainer() {return container;}
	@Override public void setContainer(IoAttributeContainer container) {this.container = container;}

	@NotNull @ManyToOne
	@JoinColumn(foreignKey=@ForeignKey(name="fk_IoAttributeData_criteria"))
	private IoAttributeCriteria criteria;
	@Override public IoAttributeCriteria getCriteria() {return criteria;}
	@Override public void setCriteria(IoAttributeCriteria criteria) {this.criteria = criteria;}

	private Boolean valueBoolean;
	@Override public Boolean getValueBoolean() {return valueBoolean;}
	@Override public void setValueBoolean(Boolean valueBoolean) {this.valueBoolean = valueBoolean;}

	private Integer valueInteger;
	@Override public Integer getValueInteger() {return valueInteger;}
	@Override public void setValueInteger(Integer valueInteger) {this.valueInteger = valueInteger;}

	private Double valueDouble;
	@Override public Double getValueDouble() {return valueDouble;}
	@Override public void setValueDouble(Double valueDouble) {this.valueDouble = valueDouble;}

	@Basic @Column(columnDefinition="text")
	private String valueString;
	@Override public String getValueString() {return valueString;}
	@Override public void setValueString(String valueString) {this.valueString = valueString;}

	@Temporal(TemporalType.DATE)
	private Date valueRecord;
	@Override public Date getValueRecord() {return valueRecord;}
	@Override public void setValueRecord(Date valueRecord) {this.valueRecord = valueRecord;}

	@ManyToOne
	private IoAttributeOption valueOption;
	@Override public IoAttributeOption getValueOption() {return valueOption;}
	@Override public void setValueOption(IoAttributeOption valueOption) {this.valueOption = valueOption;}

	@ManyToMany(fetch=FetchType.EAGER)
	private List<IoAttributeOption> valueOptions;
	@Override public List<IoAttributeOption> getValueOptions() {if(valueOptions==null){valueOptions = new ArrayList<>();}return valueOptions;}
	@Override public void setValueOptions(List<IoAttributeOption> valueOptions) {this.valueOptions = valueOptions;}


	@Override public boolean equals(Object object){return (object instanceof IoAttributeData) ? id == ((IoAttributeData) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,53).append(id).toHashCode();}
}