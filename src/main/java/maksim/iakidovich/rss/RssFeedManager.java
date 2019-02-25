package maksim.iakidovich.rss;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import maksim.iakidovich.rss.feedparameters.FeedParameter;

/**
 * This class is intended to manage Rss Feeds.
 * @author Maksim Iakidovich
 */
public class RssFeedManager {
    private List<RssFeed> feeds = new CopyOnWriteArrayList<>();
    private List<RssFeedUpdater> rssFeedUpdaters = new CopyOnWriteArrayList<>();
    private List<WriterToShareFile> writerToShareFiles = new ArrayList<>();
    
    /**
     * Created {@link RssFeed} instance and added to {@link #feeds}, created associated {@link RssFeedUpdater} instance
     * and added to {@link #rssFeedUpdaters}.
     * @param feedUrl URL to RSS Feed (e.g https://lenta.ru/rss/top7)
     */
    public void createFeed(String feedUrl) {
        RssFeed rssFeed = new RssFeed(feedUrl.trim());
        feeds.add(rssFeed);
        createFeedUpdater(rssFeed);
    }
    
    private void createFeedUpdater(RssFeed feed) {
        RssFeedUpdater rssFeedUpdater = new RssFeedUpdater(feed);
        rssFeedUpdaters.add(rssFeedUpdater);
    }
    
    /**
     * Remove specified {@link RssFeed} from {@link #feeds} and associated {@link RssFeedUpdater}
     * from {@link #rssFeedUpdaters}.
     * @param index of RssFeed
     */
    public void removeFeed(int index) {
        removeFeedUpdater(index);
        feeds.remove(index);
    }
    
    private void removeFeedUpdater(int index) {
        RssFeedUpdater rssFeedUpdater = rssFeedUpdaters.get(index);
        rssFeedUpdater.shutdown();
        rssFeedUpdaters.remove(index);
    }
    
    /**
     * Change UpdatePeriod for {@link RssFeedUpdater#scheduledExecutorService} task.
     * @param index of RssFeed
     * @param period the period between update
     * @param timeUnit which {@link TimeUnit#values()} will be used
     */
    public void changeUpdatePeriod(int index, long period, TimeUnit timeUnit) {
        RssFeedUpdater rssFeedUpdater = rssFeedUpdaters.get(index);
        rssFeedUpdater.changeUpdatePeriod(period, timeUnit);
    }
    
    /**
     * Change the file where the feeds is written.
     * @param index of RssFeed
     * @param fileName new name for file
     */
    public void renameFile(int index, String fileName) {
        RssFeed rssFeed = feeds.get(index);
        rssFeed.renameFile(fileName);
    }
    
    /**
     * Print feed parameters.
     * @param index of RssFeed
     */
    public void printFeedParameters(int index) {
        RssFeed rssFeed = feeds.get(index);
        List<FeedParameter> feedParameters = rssFeed.getFeedParameters();
        for (int i = 0; i < feedParameters.size(); i++) {
            System.out.println("[" + i + "] " + feedParameters.get(i));
        }
    }
    
    /**
     * Exclude parameters from feed.
     * @param index of RssFeed
     * @param paramIndexes indexes of parameters that will be excluded from RssFeed
     */
    public void excludeFeedParameters(int index, String... paramIndexes) {
        RssFeed rssFeed = feeds.get(index);
        List<FeedParameter> feedParameters = rssFeed.getFeedParameters();
        List<FeedParameter> feedParametersForRemove = new ArrayList<>();
        for (String paramIndex : paramIndexes) {
            feedParametersForRemove.add(feedParameters.get(Integer.parseInt(paramIndex)));
        }
        feedParameters.removeAll(feedParametersForRemove);
    }
    
    /**
     * Set the count of entity from RssFeed that will be recorded to {@link RssFeed#feedFile}.
     * @param index of RssFeed
     * @param count of RssFeed entity that will be written to file
     */
    public void setCountLimitForFeed(int index, int count) {
        RssFeed rssFeed = feeds.get(index);
        rssFeed.setCountLimit(count);
    }
    
    /**
     * Print all RssFeeds from {@link #feeds}
     */
    public void printFeeds() {
        for (int i = 0; i < feeds.size(); i++) {
            System.out.println("[" + i + "] " + feeds.get(i));
        }
    }
    
    /**
     * Creates {@link WriterToShareFile} and adds Rss Feeds.
     * @param feedIndexes indexes of Rss Feeds
     */
    public void writeToSharedFile(String... feedIndexes) {
        List<RssFeed> feedsForWriter = new ArrayList<>();
        for (String index : feedIndexes) {
            feedsForWriter.add(feeds.get(Integer.parseInt(index)));
        }
        writerToShareFiles.add(new WriterToShareFile(feedsForWriter));
    }
    
    /**
     * Remove instance of {@link WriterToShareFile} from {@link #writerToShareFiles}.
     * @param index of WriterToShareFile
     */
    public void removeWriterToSharedFile(int index) {
        WriterToShareFile writerToShareFile = writerToShareFiles.get(index);
        writerToShareFile.shutdown();
        writerToShareFiles.remove(index);
    }
    
    /**
     * Print {@link WriterToShareFile} from {@link #writerToShareFiles}.
     */
    public void printWritersToShareFile() {
        for (WriterToShareFile write : writerToShareFiles) {
            System.out.println(write);
        }
    }
    
    List<RssFeed> getFeeds() {
        return feeds;
    }
    
    List<RssFeedUpdater> getRssFeedUpdaters() {
        return rssFeedUpdaters;
    }
}