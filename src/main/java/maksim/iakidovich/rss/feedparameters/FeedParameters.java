package maksim.iakidovich.rss.feedparameters;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.sun.syndication.feed.synd.SyndCategoryImpl;
import com.sun.syndication.feed.synd.SyndEnclosureImpl;
import com.sun.syndication.feed.synd.SyndEntryImpl;

public enum FeedParameters {
    TITLE {
        @Override
        public void write(FileWriter fileWriter, SyndEntryImpl item) throws IOException {
            fileWriter.write("Title: " + item.getTitle() + System.lineSeparator());
        }
    
        @Override
        public boolean isPresent(SyndEntryImpl item) {
            return item.getTitle() != null;
        }
    },
    AUTHOR {
        @Override
        public void write(FileWriter fileWriter, SyndEntryImpl item) throws IOException {
            fileWriter.write("Author: " + item.getAuthor() + System.lineSeparator());
        }
    
        @Override
        public boolean isPresent(SyndEntryImpl item) {
            return !item.getAuthor().isEmpty();
        }
    },
    DESCRIPTION {
        @Override
        public void write(FileWriter fileWriter, SyndEntryImpl item) throws IOException {
            fileWriter.write("Description: " + item.getDescription().getValue().trim() + System.lineSeparator());
        }
    
        @Override
        public boolean isPresent(SyndEntryImpl item) {
            return item.getDescription() != null;
        }
    },
    PUBLISHED_DATE {
        @Override
        public void write(FileWriter fileWriter, SyndEntryImpl item) throws IOException {
            fileWriter.write("Published Date: " + item.getPublishedDate() + System.lineSeparator());
        }
    
        @Override
        public boolean isPresent(SyndEntryImpl item) {
            return item.getPublishedDate() != null;
        }
    },
    LINK {
        @Override
        public void write(FileWriter fileWriter, SyndEntryImpl item) throws IOException {
            fileWriter.write("Link: " + item.getLink() + System.lineSeparator());
        }
    
        @Override
        public boolean isPresent(SyndEntryImpl item) {
            return item.getLink() != null;
        }
    },
    SOURCE {
        @Override
        public void write(FileWriter fileWriter, SyndEntryImpl item) throws IOException {
            fileWriter.write("Source: " + item.getSource() + System.lineSeparator());
        }
    
        @Override
        public boolean isPresent(SyndEntryImpl item) {
            return item.getSource() != null;
        }
    },
    GUID {
        @Override
        public void write(FileWriter fileWriter, SyndEntryImpl item) throws IOException {
            fileWriter.write("GUID: " + item.getUri() + System.lineSeparator());
        }
    
        @Override
        public boolean isPresent(SyndEntryImpl item) {
            return item.getUri() != null;
        }
    },
    ENCLOSURES {
        @Override
        public void write(FileWriter fileWriter, SyndEntryImpl item) throws IOException {
            @SuppressWarnings("unchecked") List<SyndEnclosureImpl> enclosures = item.getEnclosures();
            fileWriter.write("Enclosures:\n");
            for (SyndEnclosureImpl enclosure : enclosures) {
                fileWriter.write("url: " + enclosure.getUrl() + " | type:" + enclosure.getType() + System.lineSeparator());
            }
        }
    
        @Override
        public boolean isPresent(SyndEntryImpl item) {
            return !item.getEnclosures().isEmpty();
        }
    },
    CATEGORIES {
        @Override
        public void write(FileWriter fileWriter, SyndEntryImpl item) throws IOException {
            @SuppressWarnings("unchecked") List<SyndCategoryImpl> categories = item.getCategories();
            fileWriter.write("Categories:" + System.lineSeparator());
            for (SyndCategoryImpl category : categories) {
                fileWriter.write("name: " + category.getName() + System.lineSeparator());
            }
        }
    
        @Override
        public boolean isPresent(SyndEntryImpl item) {
            return !item.getCategories().isEmpty();
        }
    };
    
    public abstract void write(FileWriter fileWriter, SyndEntryImpl item) throws IOException;
    public abstract boolean isPresent(SyndEntryImpl item);
}
