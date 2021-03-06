package mbm.uz.util;
import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class InlineButtonUtil {
    public static InlineKeyboardButton button(String text, String callbackData) {
        return new InlineKeyboardButton().setText(text).setCallbackData(callbackData);
    }
    public static InlineKeyboardButton button(String text, String callbackData, String emoji) {
        String emojiText = EmojiParser.parseToUnicode(emoji + " " + text);
        return new InlineKeyboardButton().setText(emojiText).setCallbackData(callbackData);
    }

    public static List<InlineKeyboardButton> row(InlineKeyboardButton... inlineKeyboardButtons) {
        List<InlineKeyboardButton> row = new LinkedList<>();
        row.addAll(Arrays.asList(inlineKeyboardButtons));
        return row;
    }

    public static List<List<InlineKeyboardButton>> collections(List<InlineKeyboardButton>... rows) {
        List<List<InlineKeyboardButton>> collection = new LinkedList<>();
        collection.addAll(Arrays.asList(rows));

        return collection;
    }

    public static InlineKeyboardMarkup markup(List<List<InlineKeyboardButton>> markuprows) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(markuprows);
        return markup;
    }
}
