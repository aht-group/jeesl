package org.jeesl.model.ejb.io.locale;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
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

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.status.JeeslAbstractStatus;
import org.jeesl.interfaces.model.with.parent.EjbWithParent;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPositionVisible;
import org.jeesl.interfaces.model.with.system.graphic.EjbWithImage;
import org.jeesl.interfaces.model.with.system.graphic.EjbWithImageAlt;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLangDescription;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;

@Entity
@Table(name="IoStatus",uniqueConstraints=@UniqueConstraint(columnNames={"type","code"}))
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="type", discriminatorType=DiscriminatorType.STRING, length=32)
@DiscriminatorValue("generic")
@SequenceGenerator(name="SequenceStatus", sequenceName="iostatus_id_seq", allocationSize=1)
public class IoStatus implements JeeslAbstractStatus,EjbPersistable,EjbRemoveable,EjbSaveable,
									EjbWithParent,EjbWithCode,EjbWithImage,EjbWithImageAlt,EjbWithPositionVisible,
									EjbWithLangDescription<IoLang,IoDescription>
{
	private static final long serialVersionUID = 1l;


	@Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SequenceStatus")
	protected long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}

	protected String code;
	@Override public String getCode() {return code;}
	@Override public void setCode(String code) {this.code = code;}

	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@MapKey(name="lkey")
	@JoinTable(name="IoStatusJtLang",joinColumns={@JoinColumn(name="status_id")},inverseJoinColumns={@JoinColumn(name="lang_id")})
	protected Map<String,IoLang> name;
	@Override public Map<String,IoLang> getName() {if(name==null) {name=new HashMap<>();} return name;}
	@Override public void setName(Map<String,IoLang> name) {this.name = name;}

	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@MapKey(name="lkey")
	@JoinTable(name="IoStatusJtDescription",joinColumns={@JoinColumn(name="status_id")},inverseJoinColumns={@JoinColumn(name="description_id")})
	protected Map<String,IoDescription> description;
	@Override public Map<String,IoDescription> getDescription() {return description;}
	@Override public void setDescription(Map<String,IoDescription> description) {this.description = description;}

	protected boolean visible;
	@Override public boolean isVisible() {return visible;}
	@Override public void setVisible(boolean visible) {this.visible = visible;}

	protected String image;
	@Override public String getImage() {return image;}
	@Override public void setImage(String image) {this.image = image;}

	protected String imageAlt;
	@Override public String getImageAlt() {return imageAlt;}
	@Override public void setImageAlt(String imageAlt) {this.imageAlt=imageAlt;}

	protected String style;
	public String getStyle() {return style;}
	public void setStyle(String style) {this.style = style;}

	protected int position;
	@Override public int getPosition() {return position;}
	@Override public void setPosition(int position) {this.position = position;}

	@ManyToOne
	protected IoStatus parent;
	@SuppressWarnings("unchecked")
	@Override public <P extends EjbWithCode> P getParent() {return (P)parent;}
	@Override public <P extends EjbWithCode> void setParent(P parent) {this.parent=(IoStatus)parent;}

	protected String symbol;
	public String getSymbol() {return symbol;}
	public void setSymbol(String symbol) {this.symbol = symbol;}

	protected Boolean locked;
	public Boolean getLocked() {return locked;}
	public void setLocked(Boolean locked) {this.locked = locked;}

	@OneToOne(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	private IoGraphic graphic;
	public IoGraphic getGraphic() {return graphic;}
	public void setGraphic(IoGraphic graphic) {this.graphic = graphic;}


	@Override public boolean equals(Object object){return (object instanceof IoStatus) ? id == ((IoStatus) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17, 53).append(id).toHashCode();}

	@Override public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(id).append("]");
		sb.append(" (").append(code).append(")");
		return sb.toString();
	}
}

// Discriminator
//   SELECT type, length(type) as l from IoStatus where length(type) = (select max(length(type)) from IoStatus);
//   ALTER TABLE public.iostatus ALTER COLUMN type TYPE character varying(32) COLLATE pg_catalog."default";