package maksim.iakidovich.rss;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import maksim.iakidovich.rss.feedparameters.FeedParameters;

class RssFeedManagerTest {
    
    private RssFeedManager getManagerWithOneRssFeedAndAssociatedRssFeedUpdater() {
        RssFeedManager rssFeedManager = new RssFeedManager();
        List<RssFeed> feeds = rssFeedManager.getFeeds();
        feeds.add(new RssFeed());
        List<RssFeedUpdater> rssFeedUpdaters = rssFeedManager.getRssFeedUpdaters();
        rssFeedUpdaters.add(new RssFeedUpdater());
        return rssFeedManager;
    }
    
    private RssFeedManager getEmptyManager() {
        return new RssFeedManager();
    }
    
    @Test
    void removeFeed_ManagerWithOneRssFeedAndRssFeedUpdaters_ManagerFieldsFeedsAndRssFeedUpdatersIsEmpty() {
        RssFeedManager manager = getManagerWithOneRssFeedAndAssociatedRssFeedUpdater();
        
        manager.removeFeed(0);
        List<RssFeed> feeds = manager.getFeeds();
        List<RssFeedUpdater> rssFeedUpdaters = manager.getRssFeedUpdaters();
    
        Assertions.assertTrue(feeds.isEmpty());
        Assertions.assertTrue(rssFeedUpdaters.isEmpty());
    }
    
    @Test
    void changeUpdatePeriod_ManagerWithOneRssFeedUpdater_ChangeUpdatePeriodOfRssFeedUpdaterWasCalled() {
        RssFeedManager manager = getEmptyManager();
        RssFeedUpdater mockRssFeedUpdater = mock(RssFeedUpdater.class);
        List<RssFeedUpdater> rssFeedUpdaters = manager.getRssFeedUpdaters();
        rssFeedUpdaters.add(mockRssFeedUpdater);
        
        manager.changeUpdatePeriod(0, 100, TimeUnit.SECONDS);
        
        verify(mockRssFeedUpdater, times(1)).changeUpdatePeriod(100, TimeUnit.SECONDS);
    }
    
    @Test
    void renameFile_ManagerWithOneMockRssFeed_MethodSetRssFeedFileInRssFeedWasCalled() {
        RssFeedManager manager = getEmptyManager();
        RssFeed mockRssFeed = mock(RssFeed.class);
        List<RssFeed> feeds = manager.getFeeds();
        feeds.add(mockRssFeed);
        
        manager.renameFile(0, "test");
        
        verify(mockRssFeed, times(1)).setRssFeedFile("test");
    }
    
    @Test
    void setCountLimitForFeed_ManagerWithOneMockRssFeed_MethodSetCountLimitInRssFeedWasCalled() {
        RssFeedManager manager = getEmptyManager();
        RssFeed mockRssFeed = mock(RssFeed.class);
        List<RssFeed> feeds = manager.getFeeds();
        feeds.add(mockRssFeed);
        
        manager.setCountLimitForFeed(0, 10);
        
        verify(mockRssFeed, times(1)).setCountLimit(10);
    }
    
}