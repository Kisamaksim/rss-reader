package maksim.iakidovich.rss.feedparameters;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.sun.syndication.feed.synd.SyndEnclosureImpl;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import maksim.iakidovich.rss.feedparameters.FeedParameter;

public class Enclosures implements FeedParameter {
    @Override
    public void write(FileWriter writer, SyndEntryImpl feedEntry) throws IOException {
        @SuppressWarnings("unchecked")
        List<SyndEnclosureImpl> enclosures = feedEntry.getEnclosures();
        writer.write("Enclosures:\n");
        for (SyndEnclosureImpl enclosure : enclosures) {
            writer.write("url: " + enclosure.getUrl() + " | type:" + enclosure.getType() + System.lineSeparator());
        }
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
