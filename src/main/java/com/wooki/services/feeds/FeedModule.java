package com.wooki.services.feeds;

import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Autobuild;

import com.wooki.ActivityType;
import com.wooki.services.feeds.impl.FeedSourceImpl;
import com.wooki.services.feeds.impl.SingleBookFeedProducer;

/**
 * Define Feed Sources.
 * 
 * @author ccordenier
 * 
 */
public class FeedModule {

	public static void bind(ServiceBinder binder) {
		binder.bind(FeedSource.class, FeedSourceImpl.class);
	}

	/**
	 * Contribute sources elements
	 * 
	 * @param configuration
	 */
	public void contributeFeedSource(MappedConfiguration<ActivityType, FeedProducer> configuration, @Autobuild SingleBookFeedProducer singleBook) {
		configuration.add(ActivityType.BOOK, singleBook);
	}

}
