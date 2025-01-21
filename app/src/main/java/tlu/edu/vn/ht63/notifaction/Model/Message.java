package tlu.edu.vn.ht63.notifaction.Model;

public class Message {
    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
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

    private String packageName;
    private String title;
    private String content;

    public Message(String packageName, String title, String content) {
        this.packageName = packageName;
        this.title = title;
        this.content = content;
    }
}
