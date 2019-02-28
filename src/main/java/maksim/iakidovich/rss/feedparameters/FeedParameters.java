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
        public void write(FileWriter fileWriter, SyndEntryImpl syndEntry) throws IOException {
            fileWriter.write("Title: " + syndEntry.getTitle() + System.lineSeparator());
        }
    
        @Override
        public boolean isPresent(SyndEntryImpl syndEntry) {
            return syndEntry.getTitle() != null;
        }
    },
    AUTHOR {
        @Override
        public void write(FileWriter fileWriter, SyndEntryImpl syndEntry) throws IOException {
            fileWriter.write("Author: " + syndEntry.getAuthor() + System.lineSeparator());
        }
    
        @Override
        public boolean isPresent(SyndEntryImpl syndEntry) {
            return !syndEntry.getAuthor().isEmpty();
        }
    },
    DESCRIPTION {
        @Override
        public void write(FileWriter fileWriter, SyndEntryImpl syndEntry) throws IOException {
            fileWriter.write("Description: " + syndEntry.getDescription().getValue().trim() + System.lineSeparator());
        }
    
        @Override
        public boolean isPresent(SyndEntryImpl syndEntry) {
            return syndEntry.getDescription() != null;
        }
    },
    PUBLISHED_DATE {
        @Override
        public void write(FileWriter fileWriter, SyndEntryImpl syndEntry) throws IOException {
            fileWriter.write("Published Date: " + syndEntry.getPublishedDate() + System.lineSeparator());
        }
    
        @Override
        public boolean isPresent(SyndEntryImpl syndEntry) {
            return syndEntry.getPublishedDate() != null;
        }
    },
    LINK {
        @Override
        public void write(FileWriter fileWriter, SyndEntryImpl syndEntry) throws IOException {
            fileWriter.write("Link: " + syndEntry.getLink() + System.lineSeparator());
        }
    
        @Override
        public boolean isPresent(SyndEntryImpl syndEntry) {
            return syndEntry.getLink() != null;
        }
    },
    SOURCE {
        @Override
        public void write(FileWriter fileWriter, SyndEntryImpl syndEntry) throws IOException {
            fileWriter.write("Source: " + syndEntry.getSource() + System.lineSeparator());
        }
    
        @Override
        public boolean isPresent(SyndEntryImpl syndEntry) {
            return syndEntry.getSource() != null;
        }
    },
    GUID {
        @Override
        public void write(FileWriter fileWriter, SyndEntryImpl syndEntry) throws IOException {
            fileWriter.write("GUID: " + syndEntry.getUri() + System.lineSeparator());
        }
    
        @Override
        public boolean isPresent(SyndEntryImpl syndEntry) {
            return syndEntry.getUri() != null;
        }
    },
    ENCLOSURES {
        @Override
        public void write(FileWriter fileWriter, SyndEntryImpl syndEntry) throws IOException {
            @SuppressWarnings("unchecked") List<SyndEnclosureImpl> enclosures = syndEntry.getEnclosures();
            fileWriter.write("Enclosures:\n");
            for (SyndEnclosureImpl enclosure : enclosures) {
                fileWriter.write("url: " + enclosure.getUrl() + " | type:" + enclosure.getType() + System.lineSeparator());
            }
        }
    
        @Override
        public boolean isPresent(SyndEntryImpl syndEntry) {
            return !syndEntry.getEnclosures().isEmpty();
        }
    },
    CATEGORIES {
        @Override
        public void write(FileWriter fileWriter, SyndEntryImpl syndEntry) throws IOException {
            @SuppressWarnings("unchecked") List<SyndCategoryImpl> categories = syndEntry.getCategories();
            fileWriter.write("Categories:" + System.lineSeparator());
            for (SyndCategoryImpl category : categories) {
                fileWriter.write("name: " + category.getName() + System.lineSeparator());
            }
        }
    
        @Override
        public boolean isPresent(SyndEntryImpl syndEntry) {
            return !syndEntry.getCategories().isEmpty();
        }
    };
    
    public abstract void write(FileWriter fileWriter, SyndEntryImpl syndEntry) throws IOException;
    public abstract boolean isPresent(SyndEntryImpl syndEntry);
}
