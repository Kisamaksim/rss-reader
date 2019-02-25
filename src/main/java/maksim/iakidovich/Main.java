package maksim.iakidovich;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import maksim.iakidovich.rss.ConfigManager;
import maksim.iakidovich.rss.RssFeedManager;

public class Main {
    private static ExecutorService executorService = Executors.newFixedThreadPool(4);
    private static RssFeedManager rssFeedManager = new RssFeedManager();
    private static ConfigManager configManager = new ConfigManager(rssFeedManager);
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        configManager.loadRssFeedsFromConfig();
        while(true) {
            System.out.println("Enter the:\n" +
                               "[1] - for Add new RSS Feed\n" +
                               "[2] - for Print all RSS Feeds\n" +
                               "[3] - for Remove RSS Feed\n" +
                               "[4] - for Change update period for RSS Feed\n" +
                               "[5] - for Change file for RSS Feed\n" +
                               "[6] - for Print parameters of RSS Feed\n" +
                               "[7] - for Change parameters of RSS Feed\n" +
                               "[8] - for Change count limit of entities from RSS Feed\n" +
                               "[9] - for Set RSS Feeds write to shared file\n" +
                               "[10] - for Release RSS Feeds which write to shared file\n" +
                               "[0] - for Exit");
            int decision = scanner.nextInt();
            scanner.nextLine();
            int index = 0;
            String[] indexesOfParams = null;
            switch (decision) {
                case 1:
                    System.out.println("Enter the URL to RSS Feed: ");
                    String s = scanner.nextLine();
                    executorService.submit(() -> rssFeedManager.createFeed(s));
                    break;
                case 2:
                    rssFeedManager.printFeeds();
                    break;
                case 3:
                    System.out.println("Enter the number of RSS Feed that you want to remove:");
                    rssFeedManager.printFeeds();
                    rssFeedManager.removeFeed(scanner.nextInt());
                    scanner.nextLine();
                    break;
                case 4:
                    System.out.println("Enter the number of RSS Feed that you want to change update period:");
                    rssFeedManager.printFeeds();
                    index = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Enter the new period in SECONDS");
                    long period = scanner.nextLong();
                    scanner.nextLine();
                    rssFeedManager.changeUpdatePeriod(index, period, TimeUnit.SECONDS);
                    break;
                case 5:
                    System.out.println("Enter the number of RSS Feed that you want to rename file:");
                    rssFeedManager.printFeeds();
                    index = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Enter the new file name:");
                    String fileName = scanner.nextLine();
                    rssFeedManager.renameFile(index, fileName);
                    break;
                case 6:
                    System.out.println("Enter the number of RSS Feed that you want to print parameters:");
                    rssFeedManager.printFeeds();
                    rssFeedManager.printFeedParameters(scanner.nextInt());
                    scanner.nextLine();
                    break;
                case 7:
                    System.out.println("Enter the number of RSS Feed that you want to change parameters:");
                    rssFeedManager.printFeeds();
                    index = scanner.nextInt();
                    scanner.nextLine();
                    rssFeedManager.printFeedParameters(index);
                    System.out.println("Enter the numbers of Parameters (like \"1,2,3 etc\") " +
                                       "which you want to hide in the RSS Feed:");
                    indexesOfParams = scanner.nextLine().split("\\W+");
                    rssFeedManager.excludeFeedParameters(index, indexesOfParams);
                    break;
                case 8:
                    System.out.println("Enter the number of RSS Feed" +
                                       " which you want to change limit of RSS Feeds elements:");
                    rssFeedManager.printFeeds();
                    index = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Enter the count limit: ");
                    rssFeedManager.setCountLimitForFeed(index, scanner.nextInt());
                    scanner.nextLine();
                    break;
                case 9:
                    System.out.println("Enter the number of RSS Feed which you want to write to share file:");
                    rssFeedManager.printFeeds();
                    indexesOfParams = scanner.nextLine().split("\\W+");
                    rssFeedManager.writeToSharedFile(indexesOfParams);
                    break;
                case 10:
                    System.out.println("Enter the number of WriteToShareFile which you want to release:");
                    rssFeedManager.printWritersToShareFile();
                    rssFeedManager.removeWriterToSharedFile(scanner.nextInt());
                    scanner.nextLine();
                    break;
                case 0:
                    configManager.storeRssFeedsToConfig();
                    System.exit(0);
                    break;
            }
        }
    }
    
}
