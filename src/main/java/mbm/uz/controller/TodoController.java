package mbm.uz.controller;

import mbm.uz.model.CodeMessage;
import mbm.uz.model.TodoItem;
import mbm.uz.model.enums.CodeMessageType;
import mbm.uz.model.enums.TodoItemType;
import mbm.uz.repository.TodoRepository;
import mbm.uz.util.InlineButtonUtil;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TodoController {

    private Map<Long, TodoItem> todoItemMap = new HashMap<>();
    private TodoRepository todoRepository = new TodoRepository();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    public CodeMessage handle(String text, Long chatId, Integer messageId) {
        CodeMessage codeMessage = new CodeMessage();

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        if (text.startsWith("/todo/")) {
            String[] commanList = text.split("/");
            String comman = commanList[2];
            if (comman.equals("list")) {
                EditMessageText editMessageText = new EditMessageText();
                editMessageText.setMessageId(messageId);
                editMessageText.setChatId(chatId);
                editMessageText.setParseMode("HTML");


                List<TodoItem> todoItemList = this.todoRepository.getTodoList(chatId);
                StringBuilder stringBuilder = new StringBuilder();
                if (todoItemList == null || todoItemList.isEmpty()) {
                    stringBuilder.append("You have any todo");
                } else {
                    int count = 1;
                    for (TodoItem dto : todoItemList) {
                        stringBuilder.append("<b>" + "**********   " + count + "   *************" + "</b>");
                        stringBuilder.append("\n");
                        stringBuilder.append(dto.getTitle());
                        stringBuilder.append("\n");
                        stringBuilder.append(dto.getContent());
                        stringBuilder.append("\n");
                        stringBuilder.append(simpleDateFormat.format(dto.getCreateDate()));
                        stringBuilder.append("\n\n");
                        stringBuilder.append(" /todo_edit_" + dto.getId());
                        stringBuilder.append("\n\n");

                        count++;

                    }
                }
                editMessageText.setText(stringBuilder.toString());
                codeMessage.setEditMessageText(editMessageText);
                codeMessage.setCodeMessageType(CodeMessageType.EDIT);

                InlineKeyboardButton add = InlineButtonUtil.button("Goto Menu", "menu");

                List<InlineKeyboardButton> row = InlineButtonUtil.row(add);

                List<List<InlineKeyboardButton>> rowCOllection = InlineButtonUtil.collections(row);

                editMessageText.setReplyMarkup(InlineButtonUtil.markup(rowCOllection));


            } else if (comman.equals("create")) {
                EditMessageText editMessageText = new EditMessageText();
                editMessageText.setChatId(chatId);
                editMessageText.setText("Send a title");
                editMessageText.setParseMode("MARKDOWN");
                editMessageText.setMessageId(messageId);

                TodoItem todoItem = new TodoItem();
                todoItem.setId(String.valueOf(messageId));
                todoItem.setUserId(chatId);
                todoItem.setType(TodoItemType.TITLE);

                this.todoItemMap.put(chatId, todoItem);


                codeMessage.setEditMessageText(editMessageText);
                codeMessage.setCodeMessageType(CodeMessageType.EDIT);
            } else if (comman.equals("update")) {
                comman = commanList[3];
                String id = commanList[4];

                TodoItem todoItem = this.todoRepository.getItem(chatId, id);
                EditMessageText editMessageText = new EditMessageText();
                editMessageText.setMessageId(messageId);
                editMessageText.setChatId(chatId);
                editMessageText.setParseMode("MARKDOWN");

                if (comman.equals("title")) {
                    editMessageText.setText("*Current Title*:  " + todoItem.getTitle() + "\nPlease sen new Title...");
                    codeMessage.setEditMessageText(editMessageText);
                    codeMessage.setCodeMessageType(CodeMessageType.EDIT);
                    todoItem.setType(TodoItemType.UPDATE_TITLE);
                    todoItemMap.put(chatId, todoItem);

                } else if (comman.equals("content")) {
                    editMessageText.setText("*Current Content*:  " + todoItem.getContent() + "\nPlease sen new Content...");
                    codeMessage.setEditMessageText(editMessageText);
                    codeMessage.setCodeMessageType(CodeMessageType.EDIT);
                    todoItem.setType(TodoItemType.UPDATE_CONTENT);
                    todoItemMap.put(chatId, todoItem);
                }
                editMessageText.setReplyMarkup(InlineButtonUtil.markup(InlineButtonUtil.collections(
                        InlineButtonUtil.row(InlineButtonUtil.button("Cancel", "/todo/cancel"))
                )));
            } else if (comman.equals("cancel")) {
                this.todoItemMap.remove(chatId);

                EditMessageText editMessageText = new EditMessageText();

                editMessageText.setMessageId(messageId);
                editMessageText.setChatId(chatId);
                editMessageText.setText("Update was canceled");

                InlineKeyboardButton list = InlineButtonUtil.button("ToDo List", "/todo/list", ":clipboard:");
                InlineKeyboardButton menu = InlineButtonUtil.button("Goto Menu", "menu");

                List<InlineKeyboardButton> row = InlineButtonUtil.row(list, menu);

                List<List<InlineKeyboardButton>> rowCOllection = InlineButtonUtil.collections(row);

                editMessageText.setReplyMarkup(InlineButtonUtil.markup(rowCOllection));
                codeMessage.setEditMessageText(editMessageText);
                codeMessage.setCodeMessageType(CodeMessageType.EDIT);
            } else if (comman.equals("delete")) {
                String id = commanList[3];
                boolean result = this.todoRepository.delete(chatId, id);

                EditMessageText editMessageText = new EditMessageText();

                editMessageText.setMessageId(messageId);
                editMessageText.setChatId(chatId);

                if (result) {
                    editMessageText.setText("Todo was deleted!!!");
                } else {
                    editMessageText.setText("ERROR");
                }

                InlineKeyboardButton list = InlineButtonUtil.button("ToDo List", "/todo/list", ":clipboard:");
                InlineKeyboardButton menu = InlineButtonUtil.button("Goto Menu", "menu");

                List<InlineKeyboardButton> row = InlineButtonUtil.row(list, menu);

                List<List<InlineKeyboardButton>> rowCOllection = InlineButtonUtil.collections(row);

                editMessageText.setReplyMarkup(InlineButtonUtil.markup(rowCOllection));
                codeMessage.setEditMessageText(editMessageText);
                codeMessage.setCodeMessageType(CodeMessageType.EDIT);

            }
            return codeMessage;

        }

        if (text.startsWith("/todo_")) {
            String todoID = text.split("/todo_edit_")[1];
            TodoItem todoItem = this.todoRepository.getItem(chatId, todoID);
            if (todoItem == null) {
                sendMessage.setText("ID Not FOUND");
            } else {
                sendMessage.setText("Title " + todoItem.getTitle() + "\n" + "Content " + todoItem.getContent() + "\n" +
                        "_" + simpleDateFormat.format(todoItem.getCreateDate()) + "_");
                sendMessage.setParseMode("MARKDOWN");
                sendMessage.setReplyMarkup(getTodoItemKeyBoard(todoItem.getId()));

            }
            codeMessage.setSendMessage(sendMessage);
            codeMessage.setCodeMessageType(CodeMessageType.MESSAGE);
        }

        if (this.todoItemMap.containsKey(chatId)) {
            TodoItem todoItem = this.todoItemMap.get(chatId);

            sendMessage.setParseMode("MARKDOWN");
            codeMessage.setSendMessage(sendMessage);
            codeMessage.setCodeMessageType(CodeMessageType.MESSAGE);


            if (todoItem.getType().equals(TodoItemType.TITLE)) {
                todoItem.setTitle(text);
                sendMessage.setText("*Item Title*: " + todoItem.getTitle() + "\nSend your content: ");
                todoItem.setType(TodoItemType.CONTENT);
            } else if (todoItem.getType().equals(TodoItemType.CONTENT)) {
                todoItem.setContent(text);

                todoItem.setCreateDate(new Date());
                todoItem.setType(TodoItemType.FINISH);
                int n = this.todoRepository.add(chatId, todoItem);
                todoItemMap.remove(chatId);

                sendMessage.setText("*Item Count*: " + n + "\n*Title*: " + todoItem.getTitle() + "\n*Content*: " + todoItem.getContent() + "\n" +
                        "Create Succesfull!!!");

                InlineKeyboardButton toDoList = InlineButtonUtil.button("To do List", "/todo/list", ":clipboard:");
                InlineKeyboardButton add = InlineButtonUtil.button("Goto Menu", "menu");

                List<InlineKeyboardButton> row = InlineButtonUtil.row(toDoList, add);

                List<List<InlineKeyboardButton>> rowCOllection = InlineButtonUtil.collections(row);

                sendMessage.setReplyMarkup(InlineButtonUtil.markup(rowCOllection));

            } else if (todoItem.getType().equals(TodoItemType.UPDATE_TITLE)) {
                todoItem.setTitle(text);
                this.todoItemMap.remove(chatId);
                sendMessage.setText("*Title*: " + todoItem.getTitle() + "\n*Content*: " + todoItem.getContent());

                sendMessage.setReplyMarkup(getTodoItemKeyBoard(todoItem.getId()));
            } else if (todoItem.getType().equals(TodoItemType.UPDATE_CONTENT)) {
                todoItem.setContent(text);
                this.todoItemMap.remove(chatId);
                sendMessage.setText("*Title*: " + todoItem.getTitle() + "\n*Content*: " + todoItem.getContent());

                sendMessage.setReplyMarkup(getTodoItemKeyBoard(todoItem.getId()));
            }
        }

        return codeMessage;
    }

    public InlineKeyboardMarkup getTodoItemKeyBoard(String id) {

        InlineKeyboardButton updateTitle = InlineButtonUtil.button("Update Title", "/todo/update/title/" + id);
        InlineKeyboardButton updateContent = InlineButtonUtil.button("Update Content", "/todo/update/content/" + id);
        InlineKeyboardButton delete = InlineButtonUtil.button("Delete", "/todo/delete/" + id, ":x:");

        List<InlineKeyboardButton> row = InlineButtonUtil.row(updateTitle, updateContent, delete);

        InlineKeyboardButton todoList = InlineButtonUtil.button("ToDO List", "/todo/list", ":clipboard:");
        List<InlineKeyboardButton> row2 = InlineButtonUtil.row(todoList);

        List<List<InlineKeyboardButton>> rowCOllection = InlineButtonUtil.collections(row, row2);

        return InlineButtonUtil.markup(rowCOllection);
    }

    public Map<Long, TodoItem> getTodoItemMap() {
        return todoItemMap;
    }

    public void setTodoItemMap(Map<Long, TodoItem> todoItemMap) {
        this.todoItemMap = todoItemMap;
    }
}
