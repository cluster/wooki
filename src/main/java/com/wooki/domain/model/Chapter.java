package com.wooki.domain.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Represents a chapter of the book.
 * 
 * @author ccordenier
 * 
 */
@Entity
@NamedQueries( {
		@NamedQuery(name = "com.wooki.domain.model.chapter.listChapterForBook", query = "from Chapter c where c.book.id=:book"),
		@NamedQuery(name = "com.wooki.domain.model.chapter.getContent", query = "select c.content from Chapter c where c.id=:id") })
public class Chapter {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Long id;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_book", nullable = false)
	private Book book;

	/** User friendly title */
	private String title;

	/** Identifier title */
	private String slugTitle;

	/** Last editing date */
	private Timestamp lastModifed;

	/** Creation date */
	@Temporal(TemporalType.DATE)
	private Date creationDate;

	/** Content of the book */
	@Lob
	private byte[] content;

	/** The list of comment associated to the chapter */
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "chapter", fetch = FetchType.LAZY)
	private List<Comment> comments;

	/**
	 * Constructor used to retrieve only required information for chapters
	 * display.
	 * 
	 * @param id
	 * @param title
	 * @param lastModified
	 */
	public Chapter(long id, String title) {
		this.id = id;
		this.title = title;
	}

	public Chapter() {

	}

	public void addComment(Comment com) {
		if (this.comments == null) {
			this.comments = new ArrayList<Comment>();
		}
		this.comments.add(com);
	}

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

	public String getSlugTitle() {
		return slugTitle;
	}

	public void setSlugTitle(String titleSlug) {
		this.slugTitle = titleSlug;
	}

	public Timestamp getLastModifed() {
		return lastModifed;
	}

	public void setLastModifed(Timestamp lastModifed) {
		this.lastModifed = lastModifed;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

}
