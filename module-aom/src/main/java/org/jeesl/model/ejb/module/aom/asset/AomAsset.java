package org.jeesl.model.ejb.module.aom.asset;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomAsset;
import org.jeesl.model.ejb.module.aom.company.AomCompany;
import org.jeesl.model.ejb.system.tenant.TenantRealm;

@Entity
@Table(name="AomAsset",uniqueConstraints=@UniqueConstraint(name="UC_AomAsset_realm_code",columnNames={"realm_id","rref","code"}))
public class AomAsset implements JeeslAomAsset<TenantRealm,AomAsset,AomCompany,AomAssetStatus,AomAssetType>
{
	public static final long serialVersionUID = 1;

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

	@Override public String resolveParentAttribute() {return JeeslAomAsset.Attributes.parent.toString();}
	@ManyToOne
	private AomAsset parent;
	@Override public AomAsset getParent() {return parent;}
	@Override public void setParent(AomAsset parent) {this.parent = parent;}

	private String code;
	@Override public String getCode() {return code;}
	@Override public void setCode(String code) {this.code = code;}

	private int position;
	@Override public int getPosition() {return position;}
	@Override public void setPosition(int position) {this.position = position;}

	private String name;
	@Override public String getName() {return name;}
	@Override public void setName(String name) {this.name = name;}

	@NotNull @ManyToOne
	private AomAssetStatus status;
	@Override public AomAssetStatus getStatus() {return status;}
	@Override public void setStatus(AomAssetStatus status) {this.status = status;}

	@NotNull @ManyToOne
	private AomAssetType type1;
	@Override public AomAssetType getType1() {return type1;}
	@Override public void setType1(AomAssetType type1) {this.type1 = type1;}

//	@ManyToOne
//	private AomAssetType type2;
//	@Override public AomAssetType getType2() {return type2;}
//	@Override public void setType2(AomAssetType type2) {this.type2 = type2;}

	@ManyToOne
	private AomCompany manufacturer;
	@Override public AomCompany getManufacturer() {return manufacturer;}
	@Override public void setManufacturer(AomCompany manufacturer) {this.manufacturer = manufacturer;}

	@NotNull @Basic @Column(columnDefinition="text")
	private String remark;
	@Override public String getRemark() {return remark;}
	@Override public void setRemark(String remark) {this.remark = remark;}

	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="parent")
	@OrderBy("position ASC")
	private List<AomAsset> assets;
	@Override public List<AomAsset> getAssets() {if (Objects.isNull(assets)) {this.assets = new ArrayList<>(); } return this.assets;}
	@Override public void setAssets(List<AomAsset> assets) {this.assets = assets;}


	@Override public boolean equals(Object object) {return (object instanceof AomAsset) ? id == ((AomAsset) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,51).append(id).toHashCode();}

	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[").append(id).append("]");
		sb.append(" ").append(code);
		return sb.toString();
	}
}