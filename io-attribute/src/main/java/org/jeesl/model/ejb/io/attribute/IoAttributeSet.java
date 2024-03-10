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
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeSet;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.system.tenant.TenantRealm;

@Entity
@Table(name="IoAttributeSet",uniqueConstraints=@UniqueConstraint(columnNames={"code"}))
@EjbErNode(name="Set",category="surveyDefinition",subset="moduleTs")
public class IoAttributeSet implements JeeslAttributeSet<IoLang,IoDescription,TenantRealm,IoAttributeCategory,IoAttributeItem>
{
	public static final long serialVersionUID=1;
	


	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}

	@Override public String resolveParentAttribute() {return JeeslAttributeSet.Attributes.category.toString();}
	@NotNull @ManyToOne
	@JoinColumn(foreignKey=@ForeignKey(name="fk_IoAttributeSet_category"))
	private IoAttributeCategory category;
	@Override public IoAttributeCategory getCategory() {return category;}
	@Override public void setCategory(IoAttributeCategory category) {this.category = category;}

	@NotNull @ManyToOne
	@JoinColumn(foreignKey=@ForeignKey(name="fk_IoAttributeSet_realm"))
    private TenantRealm realm;
	@Override public TenantRealm getRealm() {return realm;}
	@Override public void setRealm(TenantRealm realm) {this.realm = realm;}
 
    private long rref;
    @Override public long getRref() {return rref;}
    @Override public void setRref(long rref) {this.rref = rref;}
 
	private String code;
	@Override public String getCode(){return code;}
	@Override public void setCode(String code){this.code = code;}

	private int position;
	@Override public int getPosition() {return position;}
	@Override public void setPosition(int position) {this.position = position;}

	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@MapKey(name="lkey")
	@JoinTable(name="IoAttributeSetJtLang",
		joinColumns={@JoinColumn(foreignKey=@ForeignKey(name="fk_IoAttributeSetJtLang_set"),name="set_id")},
		inverseJoinColumns={@JoinColumn(foreignKey=@ForeignKey(name="fk_IoAttributeSetJtLang_lang"),name="lang_id")},
		uniqueConstraints=@UniqueConstraint(columnNames={"lang_id"},name="uk_IoAttributeSetJtLang_lang"))
	private Map<String,IoLang> name;
	@Override public Map<String,IoLang> getName() {return name;}
	@Override public void setName(Map<String,IoLang> name) {this.name = name;}

	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@MapKey(name="lkey")
	@JoinTable(name="IoAttributeSetJtDescription",
		joinColumns={@JoinColumn(foreignKey=@ForeignKey(name="fk_IoAttributeSetJtDescription_set"),name="set_id")},
		inverseJoinColumns={@JoinColumn(foreignKey=@ForeignKey(name="fk_IoAttributeSetJtDescription_description"),name="description_id")},
		uniqueConstraints=@UniqueConstraint(columnNames={"description_id"},name="uk_IoAttributeSetJtDescription_description"))
	private Map<String,IoDescription> description;
	@Override public Map<String,IoDescription> getDescription() {return description;}
	@Override public void setDescription(Map<String,IoDescription> description) {this.description = description;}

	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="set")
	private List<IoAttributeItem> items;
	@Override public List<IoAttributeItem> getItems() {if(Objects.isNull(items)) {items = new ArrayList<>();} return items;}
	@Override public void setItems(List<IoAttributeItem> items) {this.items = items;}


	@Override public boolean equals(Object object){return (object instanceof IoAttributeSet) ? id == ((IoAttributeSet) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,53).append(id).toHashCode();}

	@Override public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(id).append("]");
		sb.append(" ").append(code);
		return sb.toString();
	}
}