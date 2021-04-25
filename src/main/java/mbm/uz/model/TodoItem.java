package mbm.uz.model;

import mbm.uz.model.enums.TodoItemType;

import java.util.Date;

public class TodoItem {
    private String id;
    private String title;
    private String content;
    private Date createDate;
    private Long userId;

    private TodoItemType type;

    public TodoItem() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public TodoItemType getType() {
        return type;
    }

    public void setType(TodoItemType type) {
        this.type = type;
    }
}
