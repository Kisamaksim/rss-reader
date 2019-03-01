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
import java.util.Locale;

/**
 * Class is intended to load and store Rss Feeds from star.properties file.
 */
public class ConfigManager {
    private static final String COORDINATES_CONFIG_FILE = "config/start.properties";
    private RssFeedFacade rssFeedFacade;
    private DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
    
    ConfigManager() {
    }
    
    public ConfigManager(RssFeedFacade currentManager) {
        this.rssFeedFacade = currentManager;
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
        String[] configLines = configLine.split("\\|");
        String rssFeedUrl = configLines[0];
        Date lastPublishedDate;
        try {
            lastPublishedDate = dateFormat.parse(configLines[1]);
        } catch (ParseException ignored) {
            lastPublishedDate = Date.from(Instant.now());
        }
        String[] parameters = configLines[2].split("\\W+");
        int itemsLimit = Integer.parseInt(configLines[3]);
        createRssFeed(rssFeedUrl, lastPublishedDate, parameters, itemsLimit);
    }
    
    private void createRssFeed(String rssFeedUrl, Date lastPublishedDate, String[] parameters, int itemsLimit) {
        rssFeedFacade.createFeedFromConfig(rssFeedUrl, lastPublishedDate, parameters, itemsLimit);
    }
    
    /**
     * Save Rss Feeds urls to start.properties file.
     */
    public void storeRssFeedsToConfig() {
        storeRssFeedsToConfig(COORDINATES_CONFIG_FILE);
    }
    
    private void storeRssFeedsToConfig(String coordinatesFile) {
        try (FileWriter fileWriter = new FileWriter(coordinatesFile)) {
            for (RssFeed feed : rssFeedFacade.getFeeds()) {
                String feedUrl = feed.getFeedUrl();
                Date lastPublishedDate = feed.getLastPublishedDate();
                String parameters = feed.getActualParameters().toString();
                int itemsLimit = feed.getItemsLimit();
                fileWriter.write(feedUrl + "|" + lastPublishedDate + "|" +
                                 parameters.substring(1, parameters.length()-1) + "|" + itemsLimit +
                                 System.lineSeparator());
            }
        } catch (IOException ex) {
            System.out.println("===Unable to save divider coordinates to config file: " + coordinatesFile + "\n" +
                    ex.getMessage() + "===");
        }
    }
    
    void setRssFeedFacade(RssFeedFacade rssFeedFacade) {
        this.rssFeedFacade = rssFeedFacade;
    }
}