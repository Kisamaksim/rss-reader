package maksim.iakidovich.rss;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import maksim.iakidovich.rss.feedparameters.FeedParameters;

public class RssFeed {
    private String feedUrl;
    private File feedFile;
    private int itemsLimit;
    private List<SyndEntryImpl> rssItems;
    private List<FeedParameters> hideParameters = new ArrayList<>();
    private List<FeedParameters> actualParameters = new ArrayList<>();
    private Date lastPublishedDate = Date.from(Instant.now());
    
    RssFeed() {
    
    }
    
    RssFeed(String feedUrl) {
        this.feedUrl = feedUrl;
    }
    
    @Override
    public String toString() {
        return "RssFeed from: " + feedUrl +  " is written to file: " + feedFile.getName();
    }
    
    /**
     * Parse RSS Feed into {@code <item></item>} from which consist.
     * @return true if RSS Feed consist one or more {@code <item></item>}
     *         false is RSS Feed is empty
     */
    boolean parseRssFeed() {
        try {
            SyndFeedInput syndFeedInput = new SyndFeedInput();
            SyndFeed build = syndFeedInput.build(new XmlReader(new URL(feedUrl)));
            @SuppressWarnings("unchecked")
            List<SyndEntryImpl> rssItems = build.getEntries();
            if (!rssItems.isEmpty()) {
                this.rssItems = rssItems;
                return true;
            } else {
                System.out.println("RSS Feed from " + feedUrl + " is empty. Can't accept this.");
                return false;
            }
        } catch (FeedException ex) {
            System.out.println("===Can't parse RSS Feed from [" + feedUrl + "] \n" +
                               "[ " + ex.getMessage() + " ]===");
        } catch (IOException ex) {
            System.out.println("===Can't read the stream from URL [" + feedUrl + "] \n" +
                                "[ " + ex.getMessage() + " ] \n" +
                                "Try to check internet connection===");
        }
        return false;
    }
    
    boolean isPubDatePresent() {
        SyndEntryImpl syndEntry = rssItems.get(0);
        if (syndEntry.getPublishedDate() != null) {
            return true;
        } else {
            System.out.println("<Items> from Rss Feed [" + feedUrl + "] doesn't contain <pubDate>");
            return false;
        }
    }
    
    void defineRssFeedParameters() {
        SyndEntryImpl syndEntry = rssItems.get(0);
        for (FeedParameters param : FeedParameters.values()) {
            if (param.isPresent(syndEntry)) {
                hideParameters.add(param);
            }
        }
    }
    
    void updateActualRssFeedParameters(String[] indexes) {
        for (String indexOfParameter : indexes) {
            int i = Integer.parseInt(indexOfParameter);
            actualParameters.add(hideParameters.get(i));
        }
        hideParameters.removeAll(actualParameters);
    }
    
    void setItemsLimit(int itemsLimit) {
        if (itemsLimit > rssItems.size()) {
            System.out.println("===itemsLimit can't be > than count of rssItems in Feed\n" +
                    "count of rssItems: " + rssItems.size() + " ===");
            return;
        }
        this.itemsLimit = itemsLimit;
    }
    
    void setRssFeedFile() {
        setRssFeedFile(feedUrl);
    }
    
    void setRssFeedFile(String filename) {
        File feedFile = new File("feeds/" + filename.replaceAll("\\W+", "-"));
        try {
            feedFile.createNewFile();
        } catch (IOException ex) {
            System.out.println("===[ " + ex.getMessage() + " ]\n " +
                               "Specify only name for file, without path to directory===");
        }
        this.feedFile = feedFile;
    }
    
    void sortRssFeedEntries() {
        rssItems.sort(Comparator.comparing(SyndEntryImpl::getPublishedDate));
        Collections.reverse(rssItems);
    }
    
    synchronized void writeRssFeed() {
        writeRssFeed(feedFile, true);
    }
    
    synchronized void writeRssFeed(File feedFile, boolean append) {
        try (FileWriter fileWriter = new FileWriter(feedFile, append)) {
            for (int i = 0; i < itemsLimit; i++) {
                SyndEntryImpl item = rssItems.get(i);
                if (lastPublishedDate.compareTo(item.getPublishedDate()) < 0) {
                    for (FeedParameters param : actualParameters) {
                        param.write(fileWriter, item);
                    }
                    fileWriter.write("-----" + System.lineSeparator());
                }
            }
        } catch (IOException ex) {
            System.out.println("===Can't open the file [" + feedFile + "]\n[ " + ex.getMessage() + " ]===");
        }
        setLastPublishedDate();
    }
    
    private void setLastPublishedDate() {
        SyndEntryImpl item = rssItems.get(0);
        lastPublishedDate = item.getPublishedDate();
    }
    
    void printHideRssFeedParameters() {
        for (int i = 0; i < hideParameters.size(); i++) {
            System.out.println("[" + i +"] - " + hideParameters.get(i));
        }
    }
    
    void printActualRssFeedParameters() {
        for (int i = 0; i < actualParameters.size(); i++) {
            System.out.println("[" + i +"] - " + actualParameters.get(i));
        }
    }
    
    void updateHideRssFeedParameters(String[] indexes) {
        for (String indexOfParameter : indexes) {
            int i = Integer.parseInt(indexOfParameter);
            hideParameters.add(actualParameters.get(i));
        }
        actualParameters.removeAll(hideParameters);
    }
    
    String getFeedUrl() {
        return feedUrl;
    }
    
    List<FeedParameters> getHideParameters() {
        return hideParameters;
    }
    
    void setDefaultItemsLimit() {
        itemsLimit = rssItems.size();
    }
    
    void setLastPublishedDate(Date lastPublishedDate) {
        this.lastPublishedDate = lastPublishedDate;
    }
    
    int getItemsLimit() {
        return itemsLimit;
    }
    
    Date getLastPublishedDate() {
        return lastPublishedDate;
    }
    
    List<FeedParameters> getActualParameters() {
        return actualParameters;
    }
    
    File getFeedFile() {
        return feedFile;
    }
}