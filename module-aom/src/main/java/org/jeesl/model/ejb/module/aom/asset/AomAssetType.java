package org.jeesl.model.ejb.module.aom.asset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomAssetType;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.system.tenant.TenantRealm;

@Entity
@Table(name="AomAssetType",uniqueConstraints=@UniqueConstraint(name="UC_AomAssetType_realm_view_code",columnNames={"realm_id","rref","view_id","code"}))
public class AomAssetType implements JeeslAomAssetType<IoLang,IoDescription,TenantRealm,AomAssetType,AomView,IoGraphic>
{
	public static final long serialVersionUID=1;

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}

	@NotNull @ManyToOne
	private TenantRealm realm;
	@Override public TenantRealm getRealm() {return realm;}
	@Override public void setRealm(TenantRealm realm) {this.realm = realm;}

	@Column(name="rref")
	private long realmIdentifier;
	@Override public long getRealmIdentifier() {return realmIdentifier;}
	@Override public void setRealmIdentifier(long realmIdentifier) {this.realmIdentifier = realmIdentifier;}

	@NotNull @ManyToOne
	private AomView view;
	@Override public AomView getView() {return view;}
	@Override public void setView(AomView view) {this.view = view;}

	@Override public String resolveParentAttribute() {return JeeslAomAssetType.Attributes.parent.toString();}
	@ManyToOne
	private AomAssetType parent;
	@Override public AomAssetType getParent() {return parent;}
	@Override public void setParent(AomAssetType parent) {this.parent = parent;}

	private String code;
	@Override public String getCode() {return code;}
	@Override public void setCode(String code) {this.code = code;}

	private int position;
	@Override public int getPosition() {return position;}
	@Override public void setPosition(int position) {this.position = position;}

	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinTable(name="AomAssetTypeJtLang",joinColumns={@JoinColumn(name="type_id")},inverseJoinColumns={@JoinColumn(name="lang_id")})
	@MapKey(name="lkey")
	private Map<String,IoLang> name;
	@Override public Map<String,IoLang> getName() {if(Objects.isNull(name)) {name=new HashMap<>();} return name;}
	@Override public void setName(Map<String,IoLang> name) {this.name = name;}

	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinTable(name="AomAssetTypeJtDescription",joinColumns={@JoinColumn(name="type_id")},inverseJoinColumns={@JoinColumn(name="description_id")})
	@MapKey(name="lkey")
	private Map<String,IoDescription> description;
	@Override public Map<String,IoDescription> getDescription() {if(Objects.isNull(description)) {description = new HashMap<>();} return description;}
	@Override public void setDescription(Map<String,IoDescription> description) {this.description = description;}

	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY,mappedBy="parent")
	@OrderBy("position ASC")
	private List<AomAssetType> types;
	@Override public List<AomAssetType> getTypes() {if(Objects.isNull(types)) {types = new ArrayList<AomAssetType>();} return types;}
	@Override public void setTypes(List<AomAssetType> types) {this.types = types;}

	@OneToOne(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	private IoGraphic graphic;
	@Override public IoGraphic getGraphic() {return graphic;}
	@Override public void setGraphic(IoGraphic graphic) {this.graphic = graphic;}


	@Override public boolean equals(Object object) {return (object instanceof AomAssetType) ? id == ((AomAssetType) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,51).append(id).toHashCode();}

	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[").append(id).append("]");
		return sb.toString();
	}
}