package maksim.iakidovich.rss;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.net.URISyntaxException;

import org.junit.jupiter.api.Test;

class ConfigManagerTest {
    private String coordinatesFile = new File(ConfigManager.class.getResource("/start.properties").toURI()).getPath();
    
    ConfigManagerTest() throws URISyntaxException {}
    
    private ConfigManager getConfigManager() {
        return new ConfigManager();
    }
    
    @Test
    void loadRssFeedsFromConfig_ConfigWithTwoLines_MethodCreateFeedFromRssFeedManagerWasCalledTwice() {
        ConfigManager configManager = getConfigManager();
        RssFeedManager mockRssFeedManager = mock(RssFeedManager.class);
        configManager.setRssFeedManager(mockRssFeedManager);
        
        configManager.loadRssFeedsFromConfig(coordinatesFile);
        
        verify(mockRssFeedManager, times(2)).createFeed(anyString());
    }
}