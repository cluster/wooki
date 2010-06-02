package com.wooki.services.feeds.impl;

import java.util.Map;

import org.apache.tapestry5.internal.services.LinkSource;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.sun.syndication.feed.atom.Feed;
import com.wooki.domain.model.activity.Activity;
import com.wooki.services.activity.ActivitySourceType;
import com.wooki.services.feeds.ActivityFeedWriter;
import com.wooki.services.feeds.FeedProducer;
import com.wooki.services.feeds.FeedSource;

/**
 * Simple adapter service.
 * 
 * @author ccordenier
 */
public class FeedSourceImpl implements FeedSource
{

    private final Map<ActivitySourceType, FeedProducer> sources;

    public FeedSourceImpl(Map<ActivitySourceType, FeedProducer> sources,
            @Inject ActivityFeedWriter<Activity> activityFeed, @Inject LinkSource lnkSource)
    {
        this.sources = sources;
    }

    public Feed produceFeed(ActivitySourceType type, Long... context)
    {
        if (sources.containsKey(type)) { return sources.get(type).produce(context); }
        return null;
    }

}
