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
    private int countLimit;
    private List<SyndEntryImpl> entries;
    private List<FeedParameters> hideRssFeedParameters = new ArrayList<>();
    private List<FeedParameters> actualRssFeedParameters = new ArrayList<>();
    private Date lastPublishedDate = Date.from(Instant.now());
    
    RssFeed() {
    
    }
    
    RssFeed(String feedUrl) {
        this.feedUrl = feedUrl;
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
            List<SyndEntryImpl> entries = build.getEntries();
            if (!entries.isEmpty()) {
                this.entries = entries;
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
        SyndEntryImpl syndEntry = entries.get(0);
        if (syndEntry.getPublishedDate() != null) {
            return true;
        } else {
            System.out.println("<Items> from Rss Feed [" + feedUrl + "] doesn't contain <pubDate>");
            return false;
        }
    }
    
    void defineRssFeedParameters() {
        SyndEntryImpl syndEntry = entries.get(0);
        for (FeedParameters param : FeedParameters.values()) {
            if (param.isPresent(syndEntry)) {
                hideRssFeedParameters.add(param);
            }
        }
    }
    
    void updateActualRssFeedParameters(String[] indexes) {
        for (String indexOfParameter : indexes) {
            int i = Integer.parseInt(indexOfParameter);
            actualRssFeedParameters.add(hideRssFeedParameters.get(i));
        }
        hideRssFeedParameters.removeAll(actualRssFeedParameters);
    }
    
    void setCountLimit(int countLimit) {
        if (countLimit > entries.size()) {
            System.out.println("===countLimit can't be > than count of entries in Feed\n" +
                    "count of entries: " + entries.size() + " ===");
            return;
        }
        this.countLimit = countLimit;
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
        entries.sort(Comparator.comparing(SyndEntryImpl::getPublishedDate));
        Collections.reverse(entries);
    }
    
    synchronized void writeRssFeed() {
        writeRssFeed(feedFile, true);
    }
    
    synchronized void writeRssFeed(File feedFile, boolean append) {
        try (FileWriter fileWriter = new FileWriter(feedFile, append)) {
            for (int i = 0; i < countLimit; i++) {
                SyndEntryImpl entry = entries.get(i);
                if (lastPublishedDate.compareTo(entry.getPublishedDate()) < 0) {
                    for (FeedParameters param : actualRssFeedParameters) {
                        param.write(fileWriter, entry);
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
        SyndEntryImpl syndEntry = entries.get(0);
        lastPublishedDate = syndEntry.getPublishedDate();
    }
    
    @Override
    public String toString() {
        return "RssFeed from: " + feedUrl +  " is written to file: " + feedFile.getName();
    }
    
    void printHideRssFeedParameters() {
        for (int i = 0; i < hideRssFeedParameters.size(); i++) {
            System.out.println("[" + i +"] - " + hideRssFeedParameters.get(i));
        }
    }
    
    void printActualRssFeedParameters() {
        for (int i = 0; i < actualRssFeedParameters.size(); i++) {
            System.out.println("[" + i +"] - " + actualRssFeedParameters.get(i));
        }
    }
    
    void updateHideRssFeedParameters(String[] indexes) {
        for (String indexOfParameter : indexes) {
            int i = Integer.parseInt(indexOfParameter);
            hideRssFeedParameters.add(actualRssFeedParameters.get(i));
        }
        actualRssFeedParameters.removeAll(hideRssFeedParameters);
    }
    
    String getFeedUrl() {
        return feedUrl;
    }
    
    List<FeedParameters> getHideRssFeedParameters() {
        return hideRssFeedParameters;
    }
    
    void setInstanceEpochToLastPublishedDate() {
        lastPublishedDate = Date.from(Instant.EPOCH);
    }
    
    void setDefaultCountLimit() {
        countLimit = entries.size();
    }
    
    void setLastPublishedDate(Date lastPublishedDate) {
        this.lastPublishedDate = lastPublishedDate;
    }
    
    int getCountLimit() {
        return countLimit;
    }
    
    Date getLastPublishedDate() {
        return lastPublishedDate;
    }
    
    List<FeedParameters> getActualRssFeedParameters() {
        return actualRssFeedParameters;
    }
    
    File getFeedFile() {
        return feedFile;
    }
}