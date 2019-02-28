package maksim.iakidovich.rss;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import maksim.iakidovich.rss.feedparameters.FeedParameters;

class RssFeedTest {
    
    private RssFeed getRssFeedWithOneItem() throws URISyntaxException {
        URI coordinatesFile = RssFeedTest.class.getResource("/RssWithOneItem.xml").toURI();
        return new RssFeed(coordinatesFile.toString());
    }
    
    private RssFeed getRssFeedWithoutItems() throws URISyntaxException {
        URI coordinatesFile = RssFeedTest.class.getResource("/RssWithoutItem.xml").toURI();
        return new RssFeed(coordinatesFile.toString());
    }
    
    private RssFeed getRssFeedWhichReadyWriteToFile() throws URISyntaxException {
        RssFeed rssFeedWithOneItem = getRssFeedWithOneItem();
        rssFeedWithOneItem.parseRssFeed();
        rssFeedWithOneItem.defineRssFeedParameters();
        rssFeedWithOneItem.updateActualRssFeedParameters(new String[]{"0", "1", "2"});
        rssFeedWithOneItem.setDefaultCountLimit();
        rssFeedWithOneItem.setInstanceEpochToLastPublishedDate();
        return rssFeedWithOneItem;
    }
    
    @Test
    void readFeed_RssFeedWithItems_MethodReturnTrue() throws URISyntaxException {
        RssFeed rssFeedWithOneItem = getRssFeedWithOneItem();
    
        boolean result = rssFeedWithOneItem.parseRssFeed();
    
        Assertions.assertTrue(result);
    }
    
    @Test
    void readFeed_RssFeedWithoutItems_MethodReturnFalse() throws URISyntaxException {
        RssFeed rssFeedWithoutItems = getRssFeedWithoutItems();
    
        boolean result = rssFeedWithoutItems.parseRssFeed();
        
        Assertions.assertFalse(result);
    }
    
    @Test
    void isPubDatePresent_RssFeedWitPubDateInItem_MethodReturnTrue() throws URISyntaxException {
        RssFeed rssFeedWithOneItem = getRssFeedWithOneItem();
        rssFeedWithOneItem.parseRssFeed();
    
        boolean result = rssFeedWithOneItem.isPubDatePresent();
        
        Assertions.assertTrue(result);
    }
    
    @Test
    void defineRssFeedParameters_InRssItem3Parameters_SizeOfAllRssFeedParametersIs3() throws URISyntaxException {
        RssFeed rssFeedWithOneItem = getRssFeedWithOneItem();
        rssFeedWithOneItem.parseRssFeed();
        
        rssFeedWithOneItem.defineRssFeedParameters();
        List<FeedParameters> allRssFeedParameters = rssFeedWithOneItem.getHideRssFeedParameters();
        int size = allRssFeedParameters.size();
        
        Assertions.assertEquals(3, size);
    }
    
    @Test
    void writeRssFeed_OnlyOneRssFeedWriteToFile_ethalonFileAndFileForWriteIsEqual() throws IOException, URISyntaxException {
        RssFeed rssFeedWhichReadyWriteToFile = getRssFeedWhichReadyWriteToFile();
        File fileForWrite = new File(RssFeedTest.class.getResource("/forOneRssReader.txt").getPath());
        File ethalonFile = new File(RssFeedTest.class.getResource("/ethalonFileForOneRssReader.txt").getPath());
    
        rssFeedWhichReadyWriteToFile.writeRssFeed(fileForWrite, false);
        byte[] writeFromRssFeed = Files.readAllBytes(fileForWrite.toPath());
        byte[] ethalon = Files.readAllBytes(ethalonFile.toPath());
        
        Assertions.assertArrayEquals(writeFromRssFeed, ethalon);
    }
    
    @Test
    void writeRssFeed_TwoInstanceOfRssFeedWriteToOneFile_ethalonFileAndFileForWriteIsEqual() throws URISyntaxException, IOException {
        RssFeed firstInstance = getRssFeedWhichReadyWriteToFile();
        RssFeed secondInstance = getRssFeedWhichReadyWriteToFile();
        File fileForWrite = new File(RssFeedTest.class.getResource("/forTwoRssReader.txt").getPath());
        File ethalonFile = new File(RssFeedTest.class.getResource("/sharedEthalonFile.txt").getPath());
        
        firstInstance.writeRssFeed(fileForWrite, false);
        secondInstance.writeRssFeed(fileForWrite, true);
        byte[] writeFromRssFeeds = Files.readAllBytes(fileForWrite.toPath());
        byte[] ethalon = Files.readAllBytes(ethalonFile.toPath());
        
        Assertions.assertArrayEquals(writeFromRssFeeds, ethalon);
    }
}