package com.wooki.components;

import java.util.List;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.beaneditor.Validate;
import org.apache.tapestry5.beaneditor.Width;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.wooki.WookiEventConstants;
import com.wooki.domain.biz.ChapterManager;
import com.wooki.domain.biz.CommentManager;
import com.wooki.domain.model.Comment;

/**
 * Display the list of comments for a block.
 * 
 * @author ccordenier
 * 
 */
public class CommentDialogContent {

	@Parameter
	private Long publicationId;

	@Parameter
	private Long bookId;

	@Property
	@Parameter(defaultPrefix = BindingConstants.PROP)
	private String domId;

	@Inject
	private ComponentResources resources;

	@Inject
	private RenderSupport support;

	@Inject
	private ChapterManager chapterManager;

	@Inject
	private CommentManager commentManager;

	@Inject
	private Block commentDetails;

	@InjectComponent
	private Form createCommentForm;

	@InjectComponent
	@Property
	private Zone updateStateZone;

	@Property
	private List<Comment> comments;

	@Property
	private Comment currentComment;

	@Property
	@Validate("required")
	@Width(80)
	private String content;

	@SetupRender
	public Object searchComments() {
		if (!resources.isBound("publicationId") || publicationId == null) {
			return false;
		}
		this.comments = commentManager.listForPublicationAndDomId(
				publicationId, domId);
		return true;
	}

	@OnEvent(value = EventConstants.PREPARE_FOR_SUBMIT, component = "updateStateForm")
	public void prepareUpdateComment(Long commId) {
		this.currentComment = commentManager.findById(commId);
	}

	@OnEvent(value = EventConstants.SUCCESS, component = "updateStateForm")
	public void updateState() {
		commentManager.update(currentComment);
	}

	@OnEvent(value = EventConstants.SUCCESS, component = "createCommentForm")
	public Object addComment() {
		this.currentComment = chapterManager.addComment(publicationId, content,
				this.domId);
		return this.commentDetails;
	}

	@OnEvent(value = WookiEventConstants.REMOVE)
	public void removeComment(Long comId) {
		this.commentManager.removeComment(comId);
	}

	/**
	 * Initialize the reminder in the comment dialog.
	 * 
	 */
	@AfterRender
	public void initReminder() {
		support.addInit("initBlockReminder", domId);
	}

}