package com.wooki.services.feeds.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.internal.services.LinkSource;

import com.sun.syndication.feed.atom.Feed;
import com.sun.syndication.feed.atom.Link;
import com.wooki.domain.biz.BookManager;
import com.wooki.domain.model.Book;
import com.wooki.domain.model.activity.Activity;
import com.wooki.services.EnumServiceLocator;
import com.wooki.services.ServicesMessages;
import com.wooki.services.activity.ActivitySourceType;
import com.wooki.services.feeds.AbstractFeedProducer;
import com.wooki.services.feeds.ActivityFeedWriter;

/**
 * Produces the feed for a single book.
 * 
 * @author ccordenier
 */
public class SingleBookFeedProducer extends AbstractFeedProducer
{

    private final BookManager bookManager;

    public SingleBookFeedProducer(ServicesMessages messages, LinkSource lnkSource,
            ActivityFeedWriter<Activity> activityFeed, BookManager bookManager,
            EnumServiceLocator locator)
    {
        super(messages, lnkSource, activityFeed, locator.getService(ActivitySourceType.BOOK));
        this.bookManager = bookManager;
    }

    /**
     * Read book definition and generate corresponding feed.
     */
    public Feed produce(Long... context)
    {

        if (context == null || !(context.length > 0)) { throw new IllegalArgumentException(
                "SingleBookFeedProducer requires the book id as a parameter"); }

        Long bookId = context[0];
        Book book = this.bookManager.findById(bookId);

        String title = this.messages.getMessages().format("recent-activity", book.getTitle());
        String id = book.getSlugTitle();

        List<Link> alternateLinks = new ArrayList<Link>();

        Link linkToSelf = new Link();
        linkToSelf.setHref(lnkSource.createPageRenderLink("book/index", false, book.getId())
                .toAbsoluteURI());
        linkToSelf.setTitle(book.getTitle());

        alternateLinks.add(linkToSelf);

        return super.fillFeed(id, title, alternateLinks, this.source.listActivitiesForFeed(bookId));
    }

}
