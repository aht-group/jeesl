package org.jeesl.model.ejb.module.mmg;

import java.util.Map;

import javax.persistence.CascadeType;
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
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.module.mmg.JeeslMmgClassification;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.system.tenant.TenantRealm;

@Table(name="MmgClassification",uniqueConstraints=@UniqueConstraint(name="uk_mmgclassification_code",columnNames={"realm_id","rref","code"}))
@EjbErNode(name="Classification",category="eh",subset="mMmg")
@Entity
public class MmgClassification implements JeeslMmgClassification<IoLang,TenantRealm,MmgClassification,IoGraphic>
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
	
	private long rref;
	@Override public long getRref() {return rref;}
	@Override public void setRref(long rref) {this.rref = rref;}

	private String code;
	@Override public String getCode() {return code;}
	@Override public void setCode(String code) {this.code = code;}
	
	@ManyToOne
	private MmgClassification parent;
	@Override public MmgClassification getParent() {return parent;}
	@Override public void setParent(MmgClassification parent) {this.parent = parent;}
	
	private boolean visible;
	@Override public boolean isVisible() {return visible;}
	@Override public void setVisible(boolean visible) {this.visible = visible;}
	
	private int position;
	@Override public int getPosition() {return position;}
	@Override public void setPosition(int position) {this.position = position;}
	
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@MapKey(name="lkey")
	@JoinTable(name="MmgClassificationJtLang",joinColumns={@JoinColumn(name="classification_id")},inverseJoinColumns={@JoinColumn(name="lang_id")})
	private Map<String,IoLang> name;
	@Override public Map<String,IoLang> getName() {return name;}
	@Override public void setName(Map<String,IoLang> name) {this.name = name;}
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private IoGraphic graphic;
	@Override public IoGraphic getGraphic() { return graphic; }
	@Override public void setGraphic(IoGraphic graphic) { this.graphic = graphic;}
	
	@Override public boolean equals(Object object) {return (object instanceof MmgClassification) ? id == ((MmgClassification) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(23,7).append(id).toHashCode();}
	
}