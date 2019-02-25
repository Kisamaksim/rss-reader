package maksim.iakidovich.rss;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

class RssFeedTest {
    
    @Test
    void readFeed() throws URISyntaxException, IOException, FeedException {
        File file = new File(RssFeedTest.class.getResource("/atom.xml").toURI());
    
        SyndFeedInput syndFeedInput = new SyndFeedInput();
        SyndFeed build = syndFeedInput.build(new XmlReader(file));
        System.out.println();
//        RssFeed rssFeed = new RssFeed();
//        List<SyndEntryImpl> syndEntries = rssFeed.get();
//        SyndEntryImpl syndEntry = syndEntries.get(0);
//        List a = (List) syndEntry.getForeignMarkup();
//        System.out.println();
    }
    
    @Test
    void writeFeed() {
    }
}