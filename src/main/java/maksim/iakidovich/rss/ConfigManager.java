package maksim.iakidovich.rss;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Class is intended to load and store Rss Feeds from star.properties file.
 */
public class ConfigManager {
    private static final String COORDINATES_CONFIG_FILE = "scripts/scripts/config/start.properties";
    private RssFeedManager rssFeedManager;
    
    ConfigManager() {
    
    }
    
    public ConfigManager(RssFeedManager currentManager) {
        this.rssFeedManager = currentManager;
    }
    
    /**
     * Load Rss Feeds from star.properties file.
     */
    public void loadRssFeedsFromConfig() {
        loadRssFeedsFromConfig(COORDINATES_CONFIG_FILE);
    }
    
    void loadRssFeedsFromConfig(String coordinatesFile) {
        try(BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(coordinatesFile))) {
            String feedUrl;
            while ((feedUrl = bufferedReader.readLine()) != null) {
                rssFeedManager.createFeed(feedUrl);
            }
        } catch (IOException ex) {
            System.out.println("===Unable to read config file: " + coordinatesFile + "\n" + ex.getMessage() + "===");
        }
    }
    
    /**
     * Save Rss Feeds urls to start.properties file.
     */
    public void storeRssFeedsToConfig() {
        storeRssFeedsToConfig(COORDINATES_CONFIG_FILE);
    }
    
    void storeRssFeedsToConfig(String coordinatesFile) {
        try (FileWriter fileWriter = new FileWriter(coordinatesFile)) {
            for (RssFeed feed : rssFeedManager.getFeeds()) {
                fileWriter.write(feed.getFeedUrl() + System.lineSeparator());
            }
        } catch (IOException ex) {
            System.out.println("===Unable to save divider coordinates to config file: " + coordinatesFile + "\n" +
                    ex.getMessage() + "===");
        }
    }
    
    void setRssFeedManager(RssFeedManager rssFeedManager) {
        this.rssFeedManager = rssFeedManager;
    }
}