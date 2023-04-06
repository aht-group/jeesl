package org.jeesl.model.ejb.system.tenant;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.status.JeeslAbstractStatus;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantStatus;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.system.graphic.EjbWithGraphic;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLangDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;
import org.jeesl.model.ejb.io.locale.IoDescription;

@Entity
@Table(name="TenantStatus",uniqueConstraints=@UniqueConstraint(columnNames={"type","realm_id","rref","code"}))
@Inheritance(strategy= InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="type")
@DiscriminatorValue("generic")
@SequenceGenerator(name="SequenceTenantStatus", sequenceName="TenantStatus_id_seq", allocationSize=1)
public class TenantStatus implements JeeslAbstractStatus,EjbPersistable,EjbRemoveable,EjbSaveable,
									JeeslTenantStatus<TenantRealm>,
									EjbWithGraphic<IoGraphic>,
									EjbWithLangDescription<IoLang,IoDescription>
{
	private static final long serialVersionUID = 1;


	@Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SequenceTenantStatus")
	protected long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}

	@NotNull @ManyToOne
    private TenantRealm realm;
	@Override public TenantRealm getRealm() {return realm;}
	@Override public void setRealm(TenantRealm realm) {this.realm = realm;}

    private long rref;
    @Override public long getRref() {return rref;}
    @Override public void setRref(long rref) {this.rref = rref;}

	protected String code;
	@Override public String getCode() {return code;}
	@Override public void setCode(String code) {this.code = code;}

	protected String symbol;
	@Override public String getSymbol(){return symbol;}
	@Override public void setSymbol(String symbol){this.symbol = symbol;}

	protected Boolean locked;
	public Boolean getLocked() {return locked;}
	public void setLocked(Boolean locked) {this.locked = locked;}

	protected boolean visible;
	@Override public boolean isVisible() {return visible;}
	@Override public void setVisible(boolean visible) {this.visible = visible;}

	protected int position;
	@Override public int getPosition() {return position;}
	@Override public void setPosition(int position) {this.position = position;}

	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinTable(name="TenantStatusJtLang",joinColumns={@JoinColumn(name="status_id")},inverseJoinColumns={@JoinColumn(name="lang_id")})
	@MapKey(name="lkey")
	protected Map<String,IoLang> name;
	@Override public Map<String,IoLang> getName() {if(name==null){name=new HashMap<String,IoLang>();}return name;}
	@Override public void setName(Map<String,IoLang> name) {this.name = name;}

	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinTable(name="TenantStatusJtDescription",joinColumns={@JoinColumn(name="status_id")},inverseJoinColumns={@JoinColumn(name="description_id")})
	@MapKey(name="lkey")
	protected Map<String,IoDescription> description;
	@Override public Map<String,IoDescription> getDescription() {return description;}
	@Override public void setDescription(Map<String,IoDescription> description) {this.description = description;}

	@OneToOne(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	private IoGraphic graphic;
	@Override public IoGraphic getGraphic() {return graphic;}
	@Override public void setGraphic(IoGraphic graphic) {this.graphic = graphic;}

	protected String style;
	@Override public String getStyle() {return style;}
	@Override public void setStyle(String style) {this.style=style;}

	@ManyToOne
	protected TenantStatus parent;
	@SuppressWarnings("unchecked")
	public <P extends EjbWithCode> P getParent() {return (P)parent;}
	public <P extends EjbWithCode> void setParent(P parent) {this.parent=(TenantStatus)parent;}


	public String getImage() {return null;}
	public void setImage(String image) {}

	public String getImageAlt() {return null;}
	public void setImageAlt(String image) {}


	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(id);
		sb.append(" code="+code);
		return sb.toString();
	}
}