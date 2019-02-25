package maksim.iakidovich.rss;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RssFeedUpdater implements Runnable {
    private RssFeed rssFeed;
    private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    
    RssFeedUpdater() {
    
    }
    
    RssFeedUpdater(RssFeed rssFeed) {
        this.rssFeed = rssFeed;
        scheduledExecutorService.scheduleAtFixedRate(this, 30, 30, TimeUnit.SECONDS);
    }
    
    @Override
    public void run() {
        rssFeed.readFeed();
        rssFeed.writeFeed();
    }
    
    void shutdown() {
        scheduledExecutorService.shutdown();
    }
    
    void changeUpdatePeriod(long period, TimeUnit timeUnit) {
        shutdown();
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(this, period, period, timeUnit);
    }
}