package org.jeesl.model.ejb.io.ai.openai;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.io.ai.JeeslIoAiOpenAiCompletion;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.cms.markup.IoMarkup;
import org.jeesl.model.ejb.system.security.user.SecurityUser;

@Entity
@Table(name="IoAiChatCompletion")
@EjbErNode(name="Content",category="system",subset="cms")
public class IoAiChatCompletion implements JeeslIoAiOpenAiCompletion<IoAiChatThread,IoMarkup,SecurityUser>
{
	public static final long serialVersionUID=1;	


	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}

	@Override public String resolveParentAttribute() {return JeeslIoAiOpenAiCompletion.Attributes.thread.toString();}
	@NotNull @ManyToOne
	@JoinColumn(foreignKey=@ForeignKey(name="fk_IoAiChatCompletion_thread"))
	private IoAiChatThread thread;
	@Override public IoAiChatThread getThread() {return thread;}
	@Override public void setThread(IoAiChatThread thread) {this.thread = thread;}

	@NotNull @ManyToOne
	@JoinColumn(foreignKey=@ForeignKey(name="fk_IoAiChatCompletion_model"))
	private IoOpenAiModel model;
	public IoOpenAiModel getModel() {return model;}
	public void setModel(IoOpenAiModel model) {this.model = model;}

	private LocalDateTime startDate;
	@Override public LocalDateTime getStartDate() {return startDate;}
	@Override public void setStartDate(LocalDateTime startDate) {this.startDate = startDate;}
	
	private LocalDateTime endDate;
	@Override public LocalDateTime getEndDate() {return endDate;}
	@Override public void setEndDate(LocalDateTime endDate) {this.endDate = endDate;}
	
	@NotNull @ManyToOne
	@JoinColumn(foreignKey=@ForeignKey(name="fk_IoAiChatCompletion_author"))
	private SecurityUser author;
	@Override public SecurityUser getAuthor() {return author;}
	@Override public void setAuthor(SecurityUser author) {this.author = author;}
	
	@OneToOne(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinColumn(foreignKey=@ForeignKey(name="fk_IoAiChatCompletion_user"))
	private IoMarkup markupUser;
	@Override public IoMarkup getMarkupUser() {return markupUser;}
	@Override public void setMarkupUser(IoMarkup markupUser) {this.markupUser = markupUser;}
	
	@OneToOne(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinColumn(foreignKey=@ForeignKey(name="fk_IoAiChatCompletion_completion"))
	private IoMarkup markupCompletion;
	public IoMarkup getMarkupCompletion() {return markupCompletion;}
	public void setMarkupCompletion(IoMarkup markupCompletion) {this.markupCompletion = markupCompletion;}

	private int tokensPrompt;
	public int getTokensPrompt() {return tokensPrompt;}
	public void setTokensPrompt(int tokensPrompt) {this.tokensPrompt = tokensPrompt;}
	
	private int tokensCompletion;
	public int getTokensCompletion() {return tokensCompletion;}
	public void setTokensCompletion(int tokensCompletion) {this.tokensCompletion = tokensCompletion;}


	@Override public boolean equals(Object object){return (object instanceof IoAiChatCompletion) ? id == ((IoAiChatCompletion) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,53).append(id).toHashCode();}

	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[").append(id).append("]");
		return sb.toString();
	}
}