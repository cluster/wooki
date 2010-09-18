package com.wooki.services.feeds.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.tapestry5.internal.services.LinkSource;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.sun.syndication.feed.atom.Content;
import com.sun.syndication.feed.atom.Entry;
import com.sun.syndication.feed.atom.Feed;
import com.sun.syndication.feed.atom.Link;
import com.sun.syndication.feed.atom.Person;
import com.wooki.domain.model.User;
import com.wooki.domain.model.activity.Activity;
import com.wooki.services.ServicesMessages;
import com.wooki.services.feeds.ActivityFeedWriter;
import com.wooki.services.feeds.FeedProducer;

/**
 * Define a base utility for feed producing.
 * 
 * @author ccordenier
 */
public abstract class AbstractFeedProducer implements FeedProducer
{

    @Inject
    protected ServicesMessages messages;

    @Inject
    protected LinkSource lnkSource;

    @Inject
    protected ActivityFeedWriter<Activity> activityFeed;

    protected Feed fillFeed(String id, String title, List<Link> altLinks, List<Activity> activities)
    {
        Feed result = new Feed("atom_1.0");

        result.setTitle(title);
        result.setId(id);
        result.setAlternateLinks(altLinks);

        if (activities != null && activities.size() > 0)
        {
            if (activities.size() > 0)
            {
                result.setUpdated(activities.get(0).getCreationDate());
            }

            List<Entry> entries = new ArrayList<Entry>();

            for (final Activity activity : activities)
            {
                Entry e = new Entry();

                // title, date
                e.setTitle(activityFeed.getTitle(activity));
                e.setPublished(activity.getCreationDate());

                // adding author
                List<Person> author = new ArrayList<Person>();
                author.add(toPerson(activity.getUser()));
                e.setAuthors(author);

                // setting content
                Content c = new Content();
                c.setType(Content.TEXT);
                c.setValue(activityFeed.getSummary(activity));

                List<Content> content = new ArrayList<Content>();
                content.add(c);

                e.setContents(content);
                Link altLink = activityFeed.getLink(activity);
                if (altLink != null)
                {
                    e.setAlternateLinks(Arrays.asList(altLink));
                }
                else
                {
                    e.setAlternateLinks(altLinks);
                }

                // add this entry
                entries.add(e);
            }
            result.setEntries(entries);
        }

        return result;

    }

    private Person toPerson(User user)
    {
        Person person = new Person();
        person.setName(user.getFullname());
        person.setUri(this.lnkSource.createPageRenderLink("index", true, user.getUsername())
                .toURI());
        person.setEmail(user.getEmail());
        return person;
    }

}
