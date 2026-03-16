package org.jeesl.model.ejb.io.attribute;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeContainer;
import org.jeesl.interfaces.qualifier.er.EjbErNode;

@Entity
@Table(name="IoAttributeContainer")
@EjbErNode(name="Container",category="ioAttribute",level=3,subset="ioAttribute")
public class IoAttributeContainer implements JeeslAttributeContainer<IoAttributeSet,IoAttributeData>
{
	public static final long serialVersionUID=1;


	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}

	@NotNull @ManyToOne
	@JoinColumn(foreignKey=@ForeignKey(name="fk_IoAttributeContainer_set"))
	private IoAttributeSet set;
	@Override public IoAttributeSet getSet() {return set;}
	@Override public void setSet(IoAttributeSet set) {this.set = set;}

	@ManyToOne
	private IoAttributeData parent;
	@Override public IoAttributeData getParent() {return parent;}
	@Override public void setParent(IoAttributeData parent) {this.parent = parent;}

	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY, mappedBy="container")
	private List<IoAttributeData> datas;
	public List<IoAttributeData> getDatas() {return datas;}
	public void setDatas(List<IoAttributeData> datas) {this.datas = datas;}
	
	
	@Override public boolean equals(Object object){return (object instanceof IoAttributeContainer) ? id == ((IoAttributeContainer) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,53).append(id).toHashCode();}
}