package mbm.uz.controller;

import mbm.uz.model.CodeMessage;
import mbm.uz.model.enums.CodeMessageType;
import mbm.uz.util.InlineButtonUtil;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.LinkedList;
import java.util.List;

public class MainController {
    public CodeMessage handle(String text, Long chatId, Integer messageId) {

        CodeMessage codeMessage = new CodeMessage();

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        codeMessage.setSendMessage(sendMessage);

        if (text.equals("/start")) {

            sendMessage.setText("Assalamu alaykum \n" +
                    "ToDo Item bot sizning xizmatingizga tayyor!!!");
            sendMessage.setParseMode("Markdown");

            InlineKeyboardButton menuButton = InlineButtonUtil.button("Goto Menu", "menu");

            List<InlineKeyboardButton> row = InlineButtonUtil.row(menuButton);

            List<List<InlineKeyboardButton>> rowCOllection = InlineButtonUtil.collections(row);

            sendMessage.setReplyMarkup(InlineButtonUtil.markup(rowCOllection));

            codeMessage.setCodeMessageType(CodeMessageType.MESSAGE);

        } else if (text.equals("/help")) {

            String messages = "Yordam Bo'limi.\n Botning vazifasini tushuntirib beruvchi video...";
            sendMessage.setText(messages);
            sendMessage.setParseMode("Markdown");
            sendMessage = sendMessage.disableWebPagePreview();
//
//            SendPhoto sendPhoto = new SendPhoto();
//            sendPhoto.setPhoto("AgACAgIAAxkBAAMjYIAAAZlQ6dLzmgcDaMz8P0stSrcSAAKNszEbzusBSENJ0jtpXcBSN9gMpC4AAwEAAwIAA20AA5MxAAIfBA");
//            sendPhoto.setChatId(chatId);
//            sendPhoto.setCaption("Loyiha asoschisi");
//            sendPhoto.setParseMode("MARKDOWN");
//
//            codeMessage.setSendPhoto(sendPhoto);

            SendVideo sendVideo = new SendVideo();
            sendVideo.setVideo("BAACAgIAAxkBAAICNWCFR8DvDn2fLYdP-bqvONrazaXdAAJhCgACXQUpSJHHpcfnDHHnHwQ");
            sendVideo.setChatId(chatId);
            sendVideo.setCaption("Botning vazifasini...");
            sendVideo.setParseMode("HTML");

            codeMessage.setSendVideo(sendVideo);

            codeMessage.setSendMessage(sendMessage);
            codeMessage.setCodeMessageType(CodeMessageType.MESSAGE_VIDEO_PHOTO);
        } else if (text.equals("/setting")) {
            sendMessage.setText("ERROR \n Page not found 404");
            sendMessage.setParseMode("Markdown");
            codeMessage.setCodeMessageType(CodeMessageType.MESSAGE);
        } else if (text.equals("menu")) {
            EditMessageText editMessageText = new EditMessageText();

            editMessageText.setText("Menu bo'lomiga xush kelibsiz");
            editMessageText.setChatId(chatId);
            editMessageText.setMessageId(messageId);
            editMessageText.setParseMode("Markdown");


            InlineKeyboardButton toDoList = InlineButtonUtil.button("To do List", "/todo/list", ":clipboard:");
            InlineKeyboardButton add = InlineButtonUtil.button("Create New", "/todo/create", ":clipboard:");

            List<InlineKeyboardButton> row = InlineButtonUtil.row(toDoList, add);

            List<List<InlineKeyboardButton>> rowCOllection = InlineButtonUtil.collections(row);

            editMessageText.setReplyMarkup(InlineButtonUtil.markup(rowCOllection));
//            editMessageText.setReplyMarkup(InlineButtonUtil.markup(
//                    InlineButtonUtil.collections(
//                            InlineButtonUtil.row(InlineButtonUtil.button("To do List", "data/list")),
//                            InlineButtonUtil.row(InlineButtonUtil.button("Create New", "data/add")))));

            codeMessage.setCodeMessageType(CodeMessageType.EDIT);
            codeMessage.setEditMessageText(editMessageText);
        }

        return codeMessage;
    }
}
