package maksim.iakidovich.rss.feedparameters;

import java.io.FileWriter;
import java.io.IOException;

import com.sun.syndication.feed.synd.SyndEntryImpl;
import maksim.iakidovich.rss.feedparameters.FeedParameter;

public class Link implements FeedParameter {
    @Override
    public void write(FileWriter writer, SyndEntryImpl feedEntry) throws IOException {
        writer.write("Link: " + feedEntry.getLink() + System.lineSeparator());
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
