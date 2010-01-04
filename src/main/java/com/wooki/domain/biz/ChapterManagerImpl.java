//
// Copyright 2009 Robin Komiwes, Bruno Verachten, Christophe Cordenier
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// 	http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

package com.wooki.domain.biz;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ibm.icu.util.Calendar;
import com.wooki.domain.dao.ActivityDAO;
import com.wooki.domain.dao.ChapterDAO;
import com.wooki.domain.dao.CommentDAO;
import com.wooki.domain.dao.PublicationDAO;
import com.wooki.domain.exception.AuthorizationException;
import com.wooki.domain.model.Chapter;
import com.wooki.domain.model.Comment;
import com.wooki.domain.model.CommentState;
import com.wooki.domain.model.Publication;
import com.wooki.domain.model.User;
import com.wooki.domain.model.activity.ChapterActivity;
import com.wooki.domain.model.activity.ChapterEventType;
import com.wooki.domain.model.activity.CommentActivity;
import com.wooki.domain.model.activity.CommentEventType;
import com.wooki.services.parsers.DOMManager;
import com.wooki.services.security.WookiSecurityContext;

@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
@Component("chapterManager")
public class ChapterManagerImpl extends AbstractManager implements ChapterManager {

	@Autowired
	private ChapterDAO chapterDao;

	@Autowired
	private ActivityDAO activityDao;

	@Autowired
	private CommentDAO commentDao;

	@Autowired
	private PublicationDAO publicationDao;

	@Autowired
	// TODO : to delete?
	private DOMManager domManager;

	@Autowired
	private WookiSecurityContext securityCtx;

	@Transactional(readOnly = false)
	public Comment addComment(Long publicationId, String content, String domId) {

		// Check security
		if (!securityCtx.isLoggedIn()) {
			throw new AuthorizationException("You must be logged in to add a comment.");
		}
		User author = securityCtx.getAuthor();

		if (publicationId == null || content == null) {
			throw new IllegalArgumentException("Chapter and comment cannot be null for addition.");
		}

		Publication toUpdate = publicationDao.findById(publicationId);
		Comment comment = new Comment();
		comment.setState(CommentState.OPEN);
		comment.setCreationDate(new Date());
		comment.setDomId(domId);
		comment.setUser(author);
		comment.setContent(content);
		comment.setPublication(toUpdate);
		toUpdate.addComment(comment);
		this.commentDao.create(comment);

		// Log activity
		CommentActivity activity = new CommentActivity();
		activity.setCreationDate(Calendar.getInstance().getTime());
		activity.setComment(comment);
		activity.setType(CommentEventType.POST);
		activity.setUser(author);
		this.activityDao.create(activity);

		return comment;
	}

	public Chapter findById(Long chapterId) {
		return this.chapterDao.findById(chapterId);
	}

	public Publication getLastPublication(Long chapterId) {
		protectionNotNull(chapterId);

		return publicationDao.findLastRevision(chapterId);
	}

	public String getLastContent(Long chapterId) {
		Publication publication = getLastPublication(chapterId);
		if (publication != null) {
			return publication.getContent();
		}
		return null;
	}

	public Publication getLastPublishedPublication(Long chapterId) {
		protectionNotNull(chapterId);

		return publicationDao.findLastPublishedRevision(chapterId);
	}

	public String getLastPublishedContent(Long chapterId) {
		Publication published = getLastPublishedPublication(chapterId);
		if (published != null) {
			return published.getContent();
		}
		return null;
	}

	@Transactional(readOnly = false)
	public void publishChapter(Long chapterId) {
		protectionNotNull(chapterId);

		// Check security
		if (!securityCtx.isLoggedIn()) {
			throw new AuthorizationException("You must be logged in to publish chapter.");
		}

		Publication published = publicationDao.findLastRevision(chapterId);

		// Check that the logged user is an author of the book
		User author = securityCtx.getAuthor();
		if (!securityCtx.isAuthorOfBook(published.getChapter().getBook().getId())) {
			throw new AuthorizationException("You must be author to publish this chapter");
		}

		published.setLastModified(Calendar.getInstance().getTime());
		String content = domManager.adaptContent(published.getContent(), published.getId());
		published.setContent(content);
		published.setPublished(true);

		publicationDao.update(published);

		ChapterActivity ca = new ChapterActivity();
		ca.setChapter(published.getChapter());
		ca.setType(ChapterEventType.PUBLISHED);
		ca.setCreationDate(Calendar.getInstance().getTime());
		ca.setUser(author);
		activityDao.create(ca);

	}

	@Transactional(readOnly = false)
	public void updateContent(Long chapterId, String content) {
		protectionNotNull(chapterId);

		Publication publication = publicationDao.findLastRevision(chapterId);

		// we check the published flag. If set, then this Publication must
		// be considered as "locked" and we must create a new publication as
		// the new working copy
		if (publication == null || (publication != null && publication.isPublished())) {
			publication = new Publication();

			Chapter chapter = chapterDao.findById(chapterId);
			publication.setChapter(chapter);

			publication.setCreationDate(Calendar.getInstance().getTime());
			publicationDao.create(publication);
		}

		publication.setContent(content);
		publication.setLastModified(Calendar.getInstance().getTime());

		publicationDao.update(publication);

	}

	@Transactional(readOnly = false)
	public void updateAndPublishContent(Long chapterId, String content) {
		updateContent(chapterId, content);
		publishChapter(chapterId);
	}

	@Transactional(readOnly = false)
	public void remove(Long chapterId) {
		protectionNotNull(chapterId);

		if (!this.securityCtx.isLoggedIn()) {
			throw new AuthorizationException("User not logged in.");
		}

		Chapter toDelete = chapterDao.findById(chapterId);
		if (toDelete != null) {
			if (!this.securityCtx.isAuthorOfBook(toDelete.getBook().getId())) {
				throw new AuthorizationException("You are not owner of the book");
			}
			chapterDao.delete(toDelete);

			ChapterActivity activity = new ChapterActivity();
			activity.setCreationDate(Calendar.getInstance().getTime());
			activity.setChapter(toDelete);
			activity.setUser(this.securityCtx.getAuthor());
			activity.setType(ChapterEventType.DELETE);
			this.activityDao.create(activity);
		}

	}

	public Object[] findPrevious(Long bookId, Long chapterId) {
		List<Object[]> result = this.chapterDao.findPrevious(bookId, chapterId);
		if (result != null && result.size() > 0) {
			return result.get(0);
		}
		return null;
	}

	public Object[] findNext(Long bookId, Long chapterId) {
		List<Object[]> result = this.chapterDao.findNext(bookId, chapterId);
		if (result != null && result.size() > 0) {
			return result.get(0);
		}
		return null;
	}

	public List<Chapter> listChapters(Long bookId) {
		return chapterDao.listChapters(bookId);
	}

	public List<Chapter> listChaptersInfo(Long bookId) {
		return chapterDao.listChapterInfo(bookId);
	}

}
