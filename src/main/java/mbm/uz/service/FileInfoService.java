package mbm.uz.service;

import mbm.uz.model.CodeMessage;
import mbm.uz.model.enums.CodeMessageType;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Video;

import java.util.List;

public class FileInfoService {
    public CodeMessage getFileInfo(Message message){
        Long cio = message.getChatId();
        CodeMessage codeMessage = new CodeMessage();
        codeMessage.setCodeMessageType(CodeMessageType.MESSAGE);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(cio);

        if (message.getPhoto() != null){
            String s = this.show_photo_details(message.getPhoto());
            sendMessage.setText(s);
        }else if (message.getVideo() != null){
            String s = this.show_video_details(message.getVideo());
            sendMessage.setText(s);
        }else {
            sendMessage.setText("Page not found");
        }

        codeMessage.setSendMessage(sendMessage);
        return codeMessage;
    }

    private String show_photo_details(List<PhotoSize> photoSizeList){
        String s = "----------------------Photo Info------------------\n";
        for (PhotoSize photoSize: photoSizeList){
            s += "Size = " + photoSize.getFileSize() + ", ID = " + photoSize.getFileId() + "\n";
        }
        return s;
    }

    private String show_video_details(Video video){
        String s = "----------------------Video Info------------------\n";
            s += video.toString();
        return s;
    }

}
