package maksim.iakidovich.rss.feedparameters;

import java.io.FileWriter;
import java.io.IOException;

import com.sun.syndication.feed.synd.SyndEntryImpl;

public interface FeedParameter {
    void write(FileWriter writer, SyndEntryImpl feedEntry) throws IOException;
    
    String toString();
}
