package mbm.uz.controller;

import mbm.uz.Main;
import mbm.uz.model.CodeMessage;
import mbm.uz.service.FileInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Shakh_test_bot extends TelegramLongPollingBot {

    private static final Logger LOGGER = LoggerFactory.getLogger(Shakh_test_bot.class);
    private MainController mainController;
    private FileInfoService fileInfoService;
    private TodoController todoController;

    public Shakh_test_bot() {
        this.fileInfoService = new FileInfoService();
        this.mainController = new MainController();
        this.todoController = new TodoController();
    }

    @Override
    public void onUpdateReceived(Update update) {

        Message message = update.getMessage();

        try {
            if (update.hasCallbackQuery()) {
                CallbackQuery callbackQuery = update.getCallbackQuery();

                String data = callbackQuery.getData();
                User user = callbackQuery.getFrom();
                message = callbackQuery.getMessage();

                LOGGER.info("MessageId: " + message.getMessageId() + " UserName: " + user.getFirstName() + " UserUserName: " + user.getUserName() + " message " + data);

                if (data.equals("menu")) {
                    this.sendMess(this.mainController.handle(data, message.getChatId(), message.getMessageId()));
                } else if (data.contains("/todo")) {
                    this.sendMess(this.todoController.handle(data, message.getChatId(), message.getMessageId()));
                }
            } else if (message != null) {
                String text = message.getText();
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(message.getChatId());
                Integer messageId = message.getMessageId();
                User user = message.getFrom();
                LOGGER.info("MessageId: " + message.getMessageId() + " UserName: " + user.getFirstName() + " UserUserName: " + user.getUserName() + " message " + text);


                if (text != null) {
                    if (text.equals("/start") || text.equals("/help") || text.equals("/setting")) {
                        this.sendMess(this.mainController.handle(text, message.getChatId(), messageId));
                    } else if (this.todoController.getTodoItemMap().containsKey(message.getChatId()) || text.startsWith("/todo_")) {
                        this.sendMess(this.todoController.handle(text, message.getChatId(), message.getMessageId()));
                    } else {
                        sendMessage.setText("Bunday tugma yoq");
                        this.sendMess(sendMessage);
                    }
                } else {
                    this.sendMess(this.fileInfoService.getFileInfo(message));
                }
            }

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
        }
    }

    public void sendMess(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMess(CodeMessage codeMessage) {
        try {
            switch (codeMessage.getCodeMessageType()) {
                case MESSAGE:
                    execute(codeMessage.getSendMessage());
                    break;
                case EDIT:
                    execute(codeMessage.getEditMessageText());
                    break;
                case MESSAGE_VIDEO_PHOTO:
                    execute(codeMessage.getSendMessage());
                    execute(codeMessage.getSendVideo());
                    execute(codeMessage.getSendPhoto());
                    break;

                default:
                    break;
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "todo_item_bot";
    }

    @Override
    public String getBotToken() {
        return "1759593369:AAHtURePBPQsTcJ6UqwPCp8Ti0_cE_MYHfk";
    }


}
