package maksim.iakidovich.rss;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class RssFeedFacadeTest {
    
    private RssFeedFacade getFacadeWithOneRssFeedAndAssociatedRssFeedUpdater() {
        RssFeedFacade rssFeedFacade = new RssFeedFacade();
        List<RssFeed> feeds = rssFeedFacade.getFeeds();
        feeds.add(new RssFeed());
        List<RssFeedUpdater> rssFeedUpdaters = rssFeedFacade.getRssFeedUpdaters();
        rssFeedUpdaters.add(new RssFeedUpdater());
        return rssFeedFacade;
    }
    
    private RssFeedFacade getEmptyFacade() {
        return new RssFeedFacade();
    }
    
    @Test
    void removeFeed_FacadeWithOneRssFeedAndRssFeedUpdaters_FacadeFieldsFeedsAndRssFeedUpdatersIsEmpty() {
        RssFeedFacade manager = getFacadeWithOneRssFeedAndAssociatedRssFeedUpdater();
        
        manager.removeFeed(0);
        List<RssFeed> feeds = manager.getFeeds();
        List<RssFeedUpdater> rssFeedUpdaters = manager.getRssFeedUpdaters();
    
        Assertions.assertTrue(feeds.isEmpty());
        Assertions.assertTrue(rssFeedUpdaters.isEmpty());
    }
    
    @Test
    void changeUpdatePeriod_FacadeWithOneRssFeedUpdater_ChangeUpdatePeriodOfRssFeedUpdaterWasCalled() {
        RssFeedFacade manager = getEmptyFacade();
        RssFeedUpdater mockRssFeedUpdater = mock(RssFeedUpdater.class);
        List<RssFeedUpdater> rssFeedUpdaters = manager.getRssFeedUpdaters();
        rssFeedUpdaters.add(mockRssFeedUpdater);
        
        manager.changeUpdatePeriod(0, 100, TimeUnit.SECONDS);
        
        verify(mockRssFeedUpdater, times(1)).changeUpdatePeriod(100, TimeUnit.SECONDS);
    }
    
    @Test
    void renameFile_FacadeWithOneRssFeed_MethodSetRssFeedFileInRssFeedWasCalled() {
        RssFeedFacade manager = getEmptyFacade();
        RssFeed mockRssFeed = mock(RssFeed.class);
        List<RssFeed> feeds = manager.getFeeds();
        feeds.add(mockRssFeed);
        
        manager.renameFile(0, "test");
        
        verify(mockRssFeed, times(1)).setRssFeedFile("test");
    }
    
    @Test
    void setItemsLimitForFeed_FacadeWithOneRssFeed_MethodSetItemsLimitInRssFeedWasCalled() {
        RssFeedFacade manager = getEmptyFacade();
        RssFeed mockRssFeed = mock(RssFeed.class);
        List<RssFeed> feeds = manager.getFeeds();
        feeds.add(mockRssFeed);
        
        manager.setItemsLimitForFeed(0, 10);
        
        verify(mockRssFeed, times(1)).setItemsLimit(10);
    }
    
}