package maksim.iakidovich.rss;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.jupiter.api.Test;

class ConfigManagerTest {
    private String coordinatesFile = new File(ConfigManager.class.getResource("/start.properties").toURI()).getPath();
    private DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
    
    ConfigManagerTest() throws URISyntaxException {}
    
    private ConfigManager getConfigManager() {
        return new ConfigManager();
    }
    
    @Test
    void loadRssFeedsFromConfig_ConfigWithOneLine_MethodCreateFeedFromConfigWithRightArgumentsWasCalled() throws ParseException {
        ConfigManager configManager = getConfigManager();
        RssFeedManager mockRssFeedManager = mock(RssFeedManager.class);
        configManager.setRssFeedManager(mockRssFeedManager);
        
        configManager.readConfigFile(coordinatesFile);
        String url = "http://www.cnbc.com/id/19789731/device/rss/rss.xml";
        Date date = dateFormat.parse("Wed Feb 27 20:51:06 MSK 2019");
        String[] params = {"AUTHOR", "LINK"};
        int countLimit = 4;
        
        verify(mockRssFeedManager, times(1)).createFeedFromConfig(url, date, params, countLimit);
    }
    
    
}