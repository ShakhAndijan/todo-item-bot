package mbm.uz;

import mbm.uz.controller.Shakh_test_bot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

public class Main {
    
    private static  final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    
    public static void main(String[] args) {
        LOGGER.info(" *start* ");
        ApiContextInitializer.init();

        TelegramBotsApi botsApi = new TelegramBotsApi();

        try {
            botsApi.registerBot(new Shakh_test_bot());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
        System.out.println("bot succesfully");
    }
}
