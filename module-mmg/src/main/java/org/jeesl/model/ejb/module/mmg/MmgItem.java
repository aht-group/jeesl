package org.jeesl.model.ejb.module.mmg;

import java.time.LocalDateTime;
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
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.module.mmg.JeeslMmgItem;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.fr.IoFileContainer;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.system.security.user.SecurityUser;

@Entity
@Table(name="MmgItem")
@EjbErNode(name="Item",category="mMmg",subset="mMmg")
public class MmgItem implements JeeslMmgItem<IoLang,MmgGallery,IoFileContainer,SecurityUser>
{
	public static final long serialVersionUID=1;
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id){this.id = id;}
	
	private boolean visible;
	@Override public boolean isVisible() {return visible;}
	@Override public void setVisible(boolean visible) {this.visible = visible;}
	
	@Override public String resolveParentAttribute() {return JeeslMmgItem.Attributes.gallery.toString();}
	@NotNull @ManyToOne
	private MmgGallery gallery;
	@Override public MmgGallery getGallery() {return gallery;}
	@Override public void setGallery(MmgGallery gallery) {this.gallery = gallery;}
	
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@MapKey(name="lkey")
	@JoinTable(name="MmgItemJtLang",joinColumns={@JoinColumn(name="item_id")},inverseJoinColumns={@JoinColumn(name="lang_id")})
	private Map<String,IoLang> name;
	@Override public Map<String,IoLang> getName() {return name;}
	@Override public void setName(Map<String,IoLang> name) {this.name = name;}
	
	@NotNull
	private LocalDateTime ldtUpload;
	@Override public LocalDateTime getLdtUpload() {return ldtUpload;}
	@Override public void setLdtUpload(LocalDateTime ldtUpload) {this.ldtUpload = ldtUpload;}
	
	private LocalDateTime ldtImage;
	@Override public LocalDateTime getLdtImage() {return ldtImage;}
	@Override public void setLdtImage(LocalDateTime ldtImage) {this.ldtImage = ldtImage;}
	
	@OneToOne
	private IoFileContainer frContainer;
	@Override public IoFileContainer getFrContainer() {return this.frContainer;}
	@Override public void setFrContainer(IoFileContainer frContainer) {this.frContainer = frContainer;}
	
	@NotNull @ManyToOne
	private SecurityUser lastModifiedBy;
	@Override public SecurityUser getLastModifiedBy() {return lastModifiedBy;}
	@Override public void setLastModifiedBy(SecurityUser lastModifiedBy) {this.lastModifiedBy = lastModifiedBy;}
	
	@NotNull
	private LocalDateTime lastModifiedAt;
	@Override public LocalDateTime getLastModifiedAt() {return lastModifiedAt;}
	@Override public void setLastModifiedAt(LocalDateTime lastModifiedAt) {this.lastModifiedAt = lastModifiedAt;}
	
	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[").append(id).append("]");
		return sb.toString();
	}
	
	@Override public boolean equals(Object object) {return (object instanceof MmgItem) ? id == ((MmgItem) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,51).append(id).toHashCode();}
}