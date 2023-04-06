package org.jeesl.model.ejb.module.news;

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
import org.jeesl.interfaces.model.module.news.JeeslNewsItem;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.cms.markup.IoMarkup;
import org.jeesl.model.ejb.io.fr.IoFileContainer;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.system.security.user.SecurityUser;

@Entity
@Table(name="NewsItem")
@EjbErNode(name="Item",category="moduleLog",subset="news")
public class NewsItem implements JeeslNewsItem<IoLang,NewsFeed,NewsCategory,SecurityUser,IoMarkup,IoFileContainer>
{
	public static final long serialVersionUID=1;


	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id){this.id = id;}

	@Override public String resolveParentAttribute() {return JeeslNewsItem.Attributes.feed.toString();}	
	@NotNull @ManyToOne
	private NewsFeed feed;
	@Override public NewsFeed getFeed() {return feed;}
	@Override public void setFeed(NewsFeed feed) {this.feed = feed;}

	@NotNull @ManyToOne
	private NewsCategory category;
	@Override public NewsCategory getCategory() {return category;}
	@Override public void setCategory(NewsCategory category) {this.category = category;}

	private boolean visible;
	@Override public boolean isVisible() {return visible;}
	@Override public void setVisible(boolean visible) {this.visible = visible;}

	@NotNull
	private LocalDateTime record;
	@Override public LocalDateTime getRecord() {return record;}
	@Override public void setRecord(LocalDateTime record) {this.record = record;}

	private LocalDateTime validFrom;
	@Override public LocalDateTime getValidFrom() {return validFrom;}
	@Override public void setValidFrom(LocalDateTime validFrom) {this.validFrom = validFrom;}

	private LocalDateTime validUntil;
	@Override public LocalDateTime getValidUntil() {return validUntil;}
	@Override public void setValidUntil(LocalDateTime validUntil) {this.validUntil = validUntil;}

	@NotNull @ManyToOne
	private SecurityUser author;
	@Override public SecurityUser getAuthor() {return author;}
	@Override public void setAuthor(SecurityUser author) {this.author = author;}

	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinTable(name="NewsItemJtLang",joinColumns={@JoinColumn(name="item_id")},inverseJoinColumns={@JoinColumn(name="lang_id")})
	@MapKey(name="lkey")
	private Map<String,IoLang> name;
	@Override public Map<String,IoLang> getName() {return name;}
	@Override public void setName(Map<String,IoLang> name) {this.name = name;}

	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinTable(name="NewsItemJtMarkup",joinColumns={@JoinColumn(name="item_id")},inverseJoinColumns={@JoinColumn(name="markup_id")})
	@MapKey(name="lkey")
	private Map<String,IoMarkup> markup;
	@Override public Map<String,IoMarkup> getMarkup() {return markup;}
	@Override public void setMarkup(Map<String,IoMarkup> markup) {this.markup = markup;}
	
	@OneToOne
	private IoFileContainer frContainer;
	@Override public IoFileContainer getFrContainer() {return frContainer;}
	@Override public void setFrContainer(IoFileContainer frContainer) {this.frContainer = frContainer;}


	@Override public boolean equals(Object object) {return (object instanceof NewsItem) ? id == ((NewsItem) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,51).append(id).toHashCode();}

	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[").append(id).append("]");
		sb.append(" ").append(name).append(":");
		return sb.toString();
	}
}