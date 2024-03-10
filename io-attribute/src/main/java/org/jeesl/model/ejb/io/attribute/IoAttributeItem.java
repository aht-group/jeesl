package org.jeesl.model.ejb.io.attribute;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeItem;
import org.jeesl.interfaces.qualifier.er.EjbErNode;

@Entity
@Table(name="IoAttributeItem")
@EjbErNode(name="Item",category="ioAttribute",level=3,subset="moduleTs")
public class IoAttributeItem implements JeeslAttributeItem<IoAttributeCriteria,IoAttributeSet>
{
	public static final long serialVersionUID=1;


	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}

	@Override public String resolveParentAttribute() {return JeeslAttributeItem.Attributes.set.toString();}
	@NotNull @ManyToOne
	@JoinColumn(foreignKey=@ForeignKey(name="fk_IoAttributeItem_set"))
	private IoAttributeSet set;
	@Override public IoAttributeSet getSet() {return set;}
	@Override public void setSet(IoAttributeSet set) {this.set = set;}
	
	@ManyToOne
	@JoinColumn(foreignKey=@ForeignKey(name="fk_IoAttributeItem_section"))
	private IoAttributeSection section;
	public IoAttributeSection getSection() {return section;}
	public void setSection(IoAttributeSection section) {this.section = section;}
	
	private String code;
	@Override public String getCode() {return code;}
	@Override public void setCode(String code) {this.code = code;}
	
	@NotNull @ManyToOne
	@JoinColumn(foreignKey=@ForeignKey(name="fk_IoAttributeItem_criteria"))
	private IoAttributeCriteria criteria;
	@Override public IoAttributeCriteria getCriteria() {return criteria;}
	@Override public void setCriteria(IoAttributeCriteria criteria) {this.criteria = criteria;}

	private int position;
	@Override public int getPosition() {return position;}
	@Override public void setPosition(int position) {this.position = position;}
	
	private boolean visible;
	@Override public boolean isVisible() {return visible;}
	@Override public void setVisible(boolean visible) {this.visible = visible;}
	
	private Boolean tableHeader;
	@Override public Boolean getTableHeader() {return tableHeader;}
	@Override public void setTableHeader(Boolean tableHeader) {this.tableHeader = tableHeader;}

	@Basic @Column(columnDefinition = "text")
	private String remark;
	@Override public String getRemark() {return remark;}
	@Override public void setRemark(String remark) {this.remark = remark;}


	@Override public boolean equals(Object object){return (object instanceof IoAttributeItem) ? id == ((IoAttributeItem) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,53).append(id).toHashCode();}
	
	@Override public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(id).append("]");
		return sb.toString();
	}
}