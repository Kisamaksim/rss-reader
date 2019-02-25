package maksim.iakidovich.rss.feedparameters;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.sun.syndication.feed.synd.SyndCategoryImpl;
import com.sun.syndication.feed.synd.SyndEntryImpl;

public class Categories implements FeedParameter {
    @Override
    public void write(FileWriter writer, SyndEntryImpl feedEntry) throws IOException {
        @SuppressWarnings("unchecked")
        List<SyndCategoryImpl> categories = feedEntry.getCategories();
        writer.write("Categories:" + System.lineSeparator());
        for (SyndCategoryImpl category : categories) {
            writer.write("name: " + category.getName() + System.lineSeparator());
        }
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
