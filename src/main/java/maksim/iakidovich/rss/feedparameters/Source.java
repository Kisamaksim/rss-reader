package maksim.iakidovich.rss.feedparameters;

import java.io.FileWriter;
import java.io.IOException;

import com.sun.syndication.feed.synd.SyndEntryImpl;

public class Source implements FeedParameter {
    @Override
    public void write(FileWriter writer, SyndEntryImpl feedEntry) throws IOException {
        writer.write("Source: " + feedEntry.getSource() + System.lineSeparator());
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
