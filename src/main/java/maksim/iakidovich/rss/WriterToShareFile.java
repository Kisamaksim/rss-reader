package maksim.iakidovich.rss;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class WriterToShareFile implements Runnable {
    private static ThreadLocalRandom random = ThreadLocalRandom.current();
    private List<RssFeed> feeds = new ArrayList<>();
    private ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private File sharedFile;
    
    public WriterToShareFile(List<RssFeed> feeds) {
        sharedFile = createSharedFile();
        this.feeds = feeds;
        executorService.scheduleAtFixedRate(this, 0, 70, TimeUnit.SECONDS);
    }
    
    private File createSharedFile() {
        File feedFile = new File("feeds/" + "SharedFile" + random.nextInt(1000));
        try {
            feedFile.createNewFile();
        } catch (IOException ex) {
            System.out.println("===[ " + ex.getMessage() + " ]\n " +
                               "Specify only name for file, without directory path===");
        }
        return feedFile;
    }
    
    @Override
    public void run() {
        for (int i = 0; i < feeds.size(); i++) {
            RssFeed rssFeed = feeds.get(i);
            rssFeed.readFeed();
            if (i == 0) {
                rssFeed.writeFeed(sharedFile, false);
            } else {
                rssFeed.writeFeed(sharedFile, true);
            }
        }
    }
    
    void shutdown() {
        executorService.shutdown();
    }
    
    @Override
    public String toString() {
        return feeds + " writes to file: " + sharedFile.getName();
    }
}
