package maksim.iakidovich;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import maksim.iakidovich.rss.ConfigManager;
import maksim.iakidovich.rss.RssFeedManager;

public class Main {
    private static ExecutorService executorService = Executors.newCachedThreadPool();
    private static RssFeedManager rssFeedManager = new RssFeedManager();
    private static ConfigManager configManager = new ConfigManager(rssFeedManager);
    public static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        executorService.submit(() -> configManager.readConfigFile());
        while(true) {
            System.out.println("Enter the:\n" +
                               "[1] - for Add new RSS Feed\n" +
                               "[2] - for Print all RSS Feeds\n" +
                               "[3] - for Remove RSS Feed\n" +
                               "[4] - for Change update period for RSS Feed\n" +
                               "[5] - for Change file for RSS Feed\n" +
                               "[6] - for Print parameters of RSS Feed\n" +
                               "[7] - for ADD parameters to RSS Feed\n" +
                               "[8] - for HIDE parameters from RSS Feed\n" +
                               "[9] - for Change count limit of entities from RSS Feed\n" +
                               "[0] - for Exit");
            int decision = scanner.nextInt();
            scanner.nextLine();
            int index = 0;
            String[] indexesOfParams = null;
            switch (decision) {
                case 1:
                    System.out.println("Enter the URL to RSS Feed: ");
                    String s = scanner.nextLine();
                    rssFeedManager.createFeed(s.trim());
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
                    rssFeedManager.printActualRssFeedParameters(scanner.nextInt());
                    scanner.nextLine();
                    break;
                case 7:
                    System.out.println("Enter the number of RSS Feed that you want to ADD parameters:");
                    rssFeedManager.printFeeds();
                    index = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Enter the numbers of Parameters (like \"1,2,3 etc\") " +
                                       "which you want to ADD to the RSS Feed:");
                    rssFeedManager.printHideRssFeedParameters(index);
                    indexesOfParams = scanner.nextLine().split("\\W+");
                    rssFeedManager.addRssFeedParameters(index, indexesOfParams);
                    break;
                case 8:
                    System.out.println("Enter the number of RSS Feed that you want to EXCLUDE parameters:");
                    rssFeedManager.printFeeds();
                    index = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Enter the numbers of Parameters (like \"1,2,3 etc\") " +
                            "which you want to EXCLUDE from the RSS Feed:");
                    rssFeedManager.printActualRssFeedParameters(index);
                    indexesOfParams = scanner.nextLine().split("\\W+");
                    rssFeedManager.excludeRssFeedParameters(index, indexesOfParams);
                    break;
                case 9:
                    System.out.println("Enter the number of RSS Feed" +
                                       " which you want to change limit of RSS Feeds elements:");
                    rssFeedManager.printFeeds();
                    index = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Enter the count limit: ");
                    rssFeedManager.setCountLimitForFeed(index, scanner.nextInt());
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
