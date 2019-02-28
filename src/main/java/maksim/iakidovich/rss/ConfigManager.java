package maksim.iakidovich.rss;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import maksim.iakidovich.rss.feedparameters.FeedParameters;

/**
 * Class is intended to load and store Rss Feeds from star.properties file.
 */
public class ConfigManager {
    private static final String COORDINATES_CONFIG_FILE = "config/start.properties";
    private RssFeedManager rssFeedManager;
    private DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
    
    ConfigManager() {
    }
    
    public ConfigManager(RssFeedManager currentManager) {
        this.rssFeedManager = currentManager;
    }
    
    /**
     * Load Rss Feeds from star.properties file.
     */
    public void readConfigFile() {
        readConfigFile(COORDINATES_CONFIG_FILE);
    }
    
    void readConfigFile(String coordinatesFile) {
        try(BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(coordinatesFile))) {
            String configLine;
            while ((configLine = bufferedReader.readLine()) != null) {
                parseConfigLine(configLine);
            }
        } catch (IOException ex) {
            System.out.println("===Unable to read config file: " + coordinatesFile + "\n" + ex.getMessage() + "===");
        }
    }
    
    private void parseConfigLine(String configLine) {
        String[] splitConfigLine = configLine.split("\\|");
        String rssFeedUrl = splitConfigLine[0];
        Date lastPublishedDate = null;
        try {
            lastPublishedDate = dateFormat.parse(splitConfigLine[1]);
        } catch (ParseException ignored) {
            lastPublishedDate = Date.from(Instant.now());
        }
        String[] parameters = splitConfigLine[2].split("\\W+");
        int countLimit = Integer.parseInt(splitConfigLine[3]);
        createRssFeed(rssFeedUrl, lastPublishedDate, parameters, countLimit);
    }
    
    private void createRssFeed(String rssFeedUrl, Date lastPublishedDate, String[] parameters, int countLimit) {
        rssFeedManager.createFeedFromConfig(rssFeedUrl, lastPublishedDate, parameters, countLimit);
    }
    
    /**
     * Save Rss Feeds urls to start.properties file.
     */
    public void storeRssFeedsToConfig() {
        storeRssFeedsToConfig(COORDINATES_CONFIG_FILE);
    }
    
    private void storeRssFeedsToConfig(String coordinatesFile) {
        try (FileWriter fileWriter = new FileWriter(coordinatesFile)) {
            for (RssFeed feed : rssFeedManager.getFeeds()) {
                String feedUrl = feed.getFeedUrl();
                String lastPublishedDate = feed.getLastPublishedDate().toString();
                String parameters = feed.getActualRssFeedParameters().toString();
                int countLimit = feed.getCountLimit();
                fileWriter.write(feedUrl + "|" + lastPublishedDate + "|" +
                                 parameters.substring(1, parameters.length()-1) + "|" + countLimit +
                                 System.lineSeparator());
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