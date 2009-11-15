package com.wooki.domain.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 * User added comment on existing book.
 * 
 * @author ccordenier
 * 
 */
@Entity
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Long id;

	private String title;

	@ManyToOne
	private Chapter chapter;

	@OneToOne
	private CommentLabel label;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn
	private Author author;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private CommentState state;

	@Column(nullable = false)
	private String domId;

	private String content;

	private Date creationDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Chapter getChapter() {
		return chapter;
	}

	public void setChapter(Chapter chapter) {
		this.chapter = chapter;
	}

	public CommentLabel getLabel() {
		return label;
	}

	public void setLabel(CommentLabel state) {
		this.label = state;
	}

	public String getDomId() {
		return domId;
	}

	public void setDomId(String domId) {
		this.domId = domId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

	public CommentState getState() {
		return state;
	}

	public void setState(CommentState state) {
		this.state = state;
	}

}
