package org.jeesl.model.ejb.system.security.context;

import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.system.security.context.JeeslSecurityContext;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;

@Entity
@Table(name="SecurityContext",uniqueConstraints=@UniqueConstraint(columnNames = {"code"}))
@EjbErNode(name="SecurityContext")
public class SecurityContext implements JeeslSecurityContext<IoLang,IoDescription>
{
	public static final long serialVersionUID=1;


	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}

	@NotNull
	private String code;
	@Override public String getCode() {return code;}
	@Override public void setCode(String code) {this.code = code;}

	private int position;
	@Override public int getPosition() {return position;}
	@Override public void setPosition(int position) {this.position = position;}

	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@MapKey(name="lkey")
	@JoinTable(name="SecurityContextJtLang",joinColumns={@JoinColumn(name="context_id")},inverseJoinColumns={@JoinColumn(name="lang_id")})
	private Map<String,IoLang> name;
	@Override public Map<String,IoLang> getName() {return name;}
	@Override public void setName(Map<String,IoLang> name) {this.name = name;}

	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@MapKey(name="lkey")
	@JoinTable(name="SecurityContextJtDescription",joinColumns={@JoinColumn(name="context_id")},inverseJoinColumns={@JoinColumn(name="description_id")})
	private Map<String,IoDescription> description;
	@Override public Map<String,IoDescription> getDescription() {return description;}
	@Override public void setDescription(Map<String,IoDescription> description) {this.description = description;}

	private String serverName;
	@Override public String getServerName() {return serverName;}
	@Override public void setServerName(String serverName) {this.serverName = serverName;}

	private String pageTitle;
	@Override public String getPageTitle() {return pageTitle;}
	@Override public void setPageTitle(String pageTitle) {this.pageTitle = pageTitle;}

	private String pagePrefix;
	@Override public String getPagePrefix() {return pagePrefix;}
	@Override public void setPagePrefix(String pagePrefix) {this.pagePrefix = pagePrefix;}

	private String pageHeadline;
	@Override public String getPageHeadline() {return pageHeadline;}
	@Override public void setPageHeadline(String pageHeadline) {this.pageHeadline = pageHeadline;}

	private String pageCss;
	@Override public String getPageCss() {return pageCss;}
	@Override public void setPageCss(String pageCss) {this.pageCss = pageCss;}

	private String pageFavIcon;
	@Override public String getPageFavIcon() {return pageFavIcon;}
	@Override public void setPageFavIcon(String pageFavIcon) {this.pageFavIcon = pageFavIcon;}
	
	private String pageLogo;
	@Override public String getPageLogo() {return pageLogo;}
	@Override public void setPageLogo(String pageLogo) {this.pageLogo = pageLogo;}

	private String pageBackground;
	@Override public String getPageBackground() {return pageBackground;}
	@Override public void setPageBackground(String pageBackground) {this.pageBackground = pageBackground;}

	private String linkImpressum;
	@Override public String getLinkImpressum() {return linkImpressum;}
	@Override public void setLinkImpressum(String linkImpressum) {this.linkImpressum = linkImpressum;}

	private String emailAddress;
	@Override public String getEmailAddress() {return emailAddress;}
	@Override public void setEmailAddress(String emailAddress) {this.emailAddress = emailAddress;}

	private String emailName;
	@Override public String getEmailName() {return emailName;}
	@Override public void setEmailName(String emailName) {this.emailName = emailName;}

	private Long contextId;
	@Override public Long getContextId() {return contextId;}
	@Override public void setContextId(Long contextId) {this.contextId = contextId;}


	@Override public boolean equals(Object object){return (object instanceof SecurityContext) ? id == ((SecurityContext) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17, 53).append(id).toHashCode();}
	
	@Override public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(id).append("]");
		sb.append(" code="+code);
		return sb.toString();
	}
}