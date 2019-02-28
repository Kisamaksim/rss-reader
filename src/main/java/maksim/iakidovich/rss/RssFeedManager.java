package maksim.iakidovich.rss;

import static maksim.iakidovich.Main.scanner;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import maksim.iakidovich.rss.feedparameters.FeedParameters;

/**
 * This class is intended to manage Rss Feeds.
 * @author Maksim Iakidovich
 */
public class RssFeedManager {
    private List<RssFeed> feeds = new CopyOnWriteArrayList<>();
    private List<RssFeedUpdater> rssFeedUpdaters = new CopyOnWriteArrayList<>();
    
    /**
     * Created {@link RssFeed} instance and added to {@link #feeds}, created associated {@link RssFeedUpdater} instance
     * and added to {@link #rssFeedUpdaters}.
     * @param feedUrl URL to RSS Feed (e.g https://lenta.ru/rss/top7)
     */
    public void createFeed(String feedUrl) {
        RssFeed rssFeed = new RssFeed(feedUrl);
        if (!rssFeed.parseRssFeed()) {
            return;
        }
        if (!rssFeed.isPubDatePresent()) {
            return;
        }
        rssFeed.defineRssFeedParameters();
        System.out.println("Enter the numbers of Parameters which you want to see (eg: 1,2,3):\n" +
                           "Parameters:");
        rssFeed.printHideRssFeedParameters();
        String indexesOfParams = scanner.nextLine();
        rssFeed.updateActualRssFeedParameters(indexesOfParams.split("\\W+"));
        System.out.println("Enter the number of <items> tou want to read from RSS Feed:");
        rssFeed.setCountLimit(scanner.nextInt());
        scanner.nextLine();
        rssFeed.setRssFeedFile();
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
        rssFeed.setRssFeedFile(fileName);
    }
    
    /**
     * Print hide feed parameters.
     * @param index of RssFeed
     */
    public void printHideRssFeedParameters(int index) {
        RssFeed rssFeed = feeds.get(index);
        rssFeed.printHideRssFeedParameters();
    }
    
    /**
     * Print actual feed parameters.
     * @param index of RssFeed
     */
    public void printActualRssFeedParameters(int index) {
        RssFeed rssFeed = feeds.get(index);
        rssFeed.printActualRssFeedParameters();
    }
    
    /**
     * Add parameters to feed.
     * @param index of RssFeed
     * @param paramIndexes indexes of parameters that will be added to RssFeed
     */
    public void addRssFeedParameters(int index, String[] paramIndexes) {
        RssFeed rssFeed = feeds.get(index);
        rssFeed.updateActualRssFeedParameters(paramIndexes);
    }
    
    /**
     * Exclude parameters from feed.
     * @param index of RssFeed
     * @param paramIndexes indexes of parameters that will be excluded from RssFeed
     */
    public void excludeRssFeedParameters(int index, String[] paramIndexes) {
        RssFeed rssFeed = feeds.get(index);
        rssFeed.updateHideRssFeedParameters(paramIndexes);
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
    
    List<RssFeed> getFeeds() {
        return feeds;
    }
    
    List<RssFeedUpdater> getRssFeedUpdaters() {
        return rssFeedUpdaters;
    }
    
    List<File> getFeedsFiles() {
        List<File> feedsFiles = new ArrayList<>();
        for (RssFeed feed : feeds) {
            feedsFiles.add(feed.getFeedFile());
        }
        return feedsFiles;
    }
    
    void createFeedFromConfig(String rssFeedUrl, Date lastPublishedDate, String[] parameters, int countLimit) {
        RssFeed rssFeed = new RssFeed(rssFeedUrl);
        rssFeed.setLastPublishedDate(lastPublishedDate);
        rssFeed.parseRssFeed();
        rssFeed.defineRssFeedParameters();
        List<FeedParameters> actualRssFeedParameters = rssFeed.getActualRssFeedParameters();
        for (String param : parameters) {
            actualRssFeedParameters.add(FeedParameters.valueOf(param));
        }
        List<FeedParameters> hideRssFeedParameters = rssFeed.getHideRssFeedParameters();
        hideRssFeedParameters.removeAll(actualRssFeedParameters);
        rssFeed.setRssFeedFile();
        rssFeed.setCountLimit(countLimit);
        feeds.add(rssFeed);
        createFeedUpdater(rssFeed);
    }
}