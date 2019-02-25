package maksim.iakidovich.rss;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import maksim.iakidovich.rss.feedparameters.Author;
import maksim.iakidovich.rss.feedparameters.Categories;
import maksim.iakidovich.rss.feedparameters.Description;
import maksim.iakidovich.rss.feedparameters.Enclosures;
import maksim.iakidovich.rss.feedparameters.FeedParameter;
import maksim.iakidovich.rss.feedparameters.Guid;
import maksim.iakidovich.rss.feedparameters.Link;
import maksim.iakidovich.rss.feedparameters.PublishedDate;
import maksim.iakidovich.rss.feedparameters.Source;
import maksim.iakidovich.rss.feedparameters.Title;

public class RssFeed {
    private String feedUrl;
    private File feedFile;
    private int countLimit;
    private List<SyndEntryImpl> entries;
    private List<FeedParameter> feedParameters = new ArrayList<>();
    
    RssFeed() {
    
    }
    
    RssFeed(String feedUrl) {
        this.feedUrl = feedUrl;
        this.feedFile = createFeedFile(feedUrl.replaceAll("\\W+","-"));
        readFeed();
        this.countLimit = entries.size();
        defineFeedParameters();
        writeFeed();
    }
    
    private File createFeedFile(String filename) {
        File feedFile = new File("feeds/" + filename);
        try {
            feedFile.createNewFile();
        } catch (IOException ex) {
            System.out.println("===[ " + ex.getMessage() + " ]\n " +
                               "Specify only name for file, without directory path===");
        }
        return feedFile;
    }
    
    void readFeed() {
        try {
            SyndFeedInput syndFeedInput = new SyndFeedInput();
            SyndFeed build = syndFeedInput.build(new XmlReader(new URL(feedUrl)));
            entries = build.getEntries();
        } catch (FeedException ex) {
            System.out.println("===Can't parse RSS Feed from [" + feedUrl + "] \n" +
                               "[ " + ex.getMessage() + " ]===");
        } catch (IOException ex) {
            System.out.println("===Can't read the stream from URL [" + feedUrl + "] \n" +
                                "[ " + ex.getMessage() + " ] \n" +
                                "Try to check internet connection===");
        }
    }
    
    private void defineFeedParameters() {
        if (entries.isEmpty()) {
            return;
        }
        SyndEntryImpl syndEntry = entries.get(0);
        if (syndEntry.getTitle() != null) {
            feedParameters.add(new Title());
        }
        if (!syndEntry.getAuthor().isEmpty()) {
            feedParameters.add(new Author());
        }
        if (syndEntry.getLink() != null) {
            feedParameters.add(new Link());
        }
        if (syndEntry.getDescription() != null) {
            feedParameters.add(new Description());
        }
        if (!syndEntry.getCategories().isEmpty()) {
            feedParameters.add(new Categories());
        }
        if (!syndEntry.getEnclosures().isEmpty()) {
            feedParameters.add(new Enclosures());
        }
        if (syndEntry.getUri() != null) {
            feedParameters.add(new Guid());
        }
        if (syndEntry.getPublishedDate() != null) {
            feedParameters.add(new PublishedDate());
        }
        if (syndEntry.getSource() != null) {
            feedParameters.add(new Source());
        }
    }
    
    void writeFeed() {
        writeFeed(feedFile, false);
    }
    
    void writeFeed(File feedFile, boolean appendToFile) {
        try (FileWriter fileWriter = new FileWriter(feedFile, appendToFile)) {
            for (int i = 0; i < countLimit; i++) {
                SyndEntryImpl entry = entries.get(i);
                for (FeedParameter param : feedParameters) {
                    param.write(fileWriter, entry);
                }
                fileWriter.write("-----" + System.lineSeparator());
            }
        } catch (IOException ex) {
            System.out.println("===Can't open the file [" + feedFile + "]\n[ " + ex.getMessage() + " ]===");
        }
    }
    
    void renameFile(String fileName) {
        if (Files.exists(Paths.get("feeds/", fileName))) {
            System.out.println("===File with name: [" + fileName + "] is already exist. Try another name for file===");
            return;
        }
        try {
            Files.move(feedFile.toPath(), Paths.get("feeds/" + fileName));
            feedFile = createFeedFile(fileName);
        } catch (IOException ex) {
            System.out.println("===[ " + ex.getMessage() + " ]\n" +
                    "Specify only name for file, without directory path===");
        }
    }
    
    void setCountLimit(int countLimit) {
        if (countLimit > entries.size()) {
            System.out.println("===countLimit can't be > than count of entries in Feed\n" +
                               "count of entries: " + entries.size() + " ===");
            return;
        }
        this.countLimit = countLimit;
    }
    
    @Override
    public String toString() {
        return "RssFeed from: " + feedUrl +  " in file: " + feedFile.getName();
    }
    
    List<SyndEntryImpl> get() {
        return entries;
    }
    
    String getFeedUrl() {
        return feedUrl;
    }
    
    List<FeedParameter> getFeedParameters() {
        return feedParameters;
    }
}