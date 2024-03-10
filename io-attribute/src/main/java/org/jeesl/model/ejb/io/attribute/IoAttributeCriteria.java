package org.jeesl.model.ejb.io.attribute;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeCriteria;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.system.tenant.TenantRealm;

@Entity
@Table(name="IoAttributeCriteria")
@EjbErNode(name="Criteria",category="ioAttribute",level=3,subset="ioAttribute")
public class IoAttributeCriteria implements JeeslAttributeCriteria<IoLang,IoDescription,TenantRealm,IoAttributeCategory,IoAttributeType,IoAttributeOption,IoAttributeSet>
{
	public static final long serialVersionUID=1;


	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}

	@NotNull @ManyToOne
	@JoinColumn(foreignKey=@ForeignKey(name="fk_IoAttributeCriteria_realm"))
    private TenantRealm realm;
	@Override public TenantRealm getRealm() {return realm;}
	@Override public void setRealm(TenantRealm realm) {this.realm = realm;}

    private long rref;
	@Override public long getRref() {return rref;}
	@Override public void setRref(long rref) {this.rref = rref;}

	@Override public String resolveParentAttribute() {return JeeslAttributeCriteria.Attributes.category.toString();}
	@NotNull @ManyToOne
	@JoinColumn(foreignKey=@ForeignKey(name="fk_IoAttributeCriteria_category"))
	private IoAttributeCategory category;
	@Override public IoAttributeCategory getCategory() {return category;}
	@Override public void setCategory(IoAttributeCategory category) {this.category = category;}
    
	@NotNull @ManyToOne
	@JoinColumn(foreignKey=@ForeignKey(name="fk_IoAttributeCriteria_type"))
	private IoAttributeType type;
	@Override public IoAttributeType getType() {return type;}
	@Override public void setType(IoAttributeType type) {this.type = type;}

	private String code;
	@Override public String getCode(){return code;}
	@Override public void setCode(String code){this.code = code;}

	private int position;
	@Override public int getPosition() {return position;}
	@Override public void setPosition(int position) {this.position = position;}

	private boolean visible;
	@Override public boolean isVisible() {return visible;}
	@Override public void setVisible(boolean visible) {this.visible = visible;}

	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@MapKey(name="lkey")
	@JoinTable(name="IoAttributeCriteriaJtLang",
		joinColumns={@JoinColumn(foreignKey=@ForeignKey(name="fk_IoAttributeCriteriaJtLang_criteria"),name="criteria_id")},
		inverseJoinColumns={@JoinColumn(foreignKey=@ForeignKey(name="fk_IoAttributeCriteriaJtLang_lang"),name="lang_id")},
		uniqueConstraints=@UniqueConstraint(columnNames={"lang_id"},name="uk_IoAttributeCriteriaJtLang_lang"))
	private Map<String,IoLang> name;
	@Override public Map<String,IoLang> getName() {return name;}
	@Override public void setName(Map<String,IoLang> name) {this.name = name;}

	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@MapKey(name="lkey")
	@JoinTable(name="IoAttributeCriteriaJtDescription",
		joinColumns={@JoinColumn(foreignKey=@ForeignKey(name="fk_IoAttributeCriteriaJtDescription_criteria"),name="criteria_id")},
		inverseJoinColumns={@JoinColumn(foreignKey=@ForeignKey(name="fk_IoAttributeCriteriaJtDescription_description"),name="description_id")},
		uniqueConstraints=@UniqueConstraint(columnNames={"description_id"},name="uk_IoAttributeCriteriaJtDescription_description"))
	private Map<String,IoDescription> description;
	@Override public Map<String,IoDescription> getDescription() {return description;}
	@Override public void setDescription(Map<String,IoDescription> description) {this.description = description;}

	private Boolean allowEmpty;
	@Override public Boolean getAllowEmpty() {return allowEmpty;}
	@Override public void setAllowEmpty(Boolean allowEmpty) {this.allowEmpty = allowEmpty;}

	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="criteria")
	private List<IoAttributeOption> options;
	@Override public List<IoAttributeOption> getOptions() {if(options==null){options = new ArrayList<IoAttributeOption>();}return options;}
	@Override public void setOptions(List<IoAttributeOption> options) {this.options = options;}

	@ManyToOne
	@JoinColumn(foreignKey=@ForeignKey(name="fk_IoAttributeCriteria_nested"))
	private IoAttributeSet nested;
	@Override public IoAttributeSet getNested() {return nested;}
	@Override public void setNested(IoAttributeSet nested) {this.nested = nested;}


	@Override public boolean equals(Object object){return (object instanceof IoAttributeCriteria) ? id == ((IoAttributeCriteria) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,53).append(id).toHashCode();}
	
	@Override public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(id).append("]");
		sb.append(" realm:"); if(realm!=null) {sb.append(realm.getCode());} else {sb.append("--");}
		sb.append(" nested:"); if(Objects.nonNull(nested)) {sb.append(nested.toString());} else {sb.append("--");}
		return sb.toString();
	}
}