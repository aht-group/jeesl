package org.jeesl.model.ejb.module.news;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.module.news.JeeslNewsFeed;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.system.tenant.TenantRealm;
import org.jeesl.model.ejb.io.locale.IoDescription;

@Entity
@Table(name="NewsFeed")
@EjbErNode(name="Feed",category="moduleLog",subset="news")
public class NewsFeed implements JeeslNewsFeed<IoLang,IoDescription,TenantRealm>

{
	public static final long serialVersionUID=1;
	public enum Attributes {log,record,scopes}
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id){this.id = id;}
	
	@NotNull @ManyToOne
	private TenantRealm realm;
	@Override public TenantRealm getRealm() {return realm;}
	@Override public void setRealm(TenantRealm realm) {this.realm = realm;}
	
	private long rref;
	@Override public long getRref() {return rref;}
	@Override public void setRref(long rref) {this.rref = rref;}
	
	private boolean visible;
	@Override public boolean isVisible() {return visible;}
	@Override public void setVisible(boolean visible) {this.visible = visible;}
	
	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[").append(id).append("]");
		return sb.toString();
	}
	
	@Override public boolean equals(Object object) {return (object instanceof NewsFeed) ? id == ((NewsFeed) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,51).append(id).toHashCode();}
	
}