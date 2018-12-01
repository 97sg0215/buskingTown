package graduationwork.buskingtown.model;

public class Post {
    int post_id;
    String content;
    String busker;
    String post_image;
    String created_at;

    public int getPost_id() {
        return post_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBusker() {
        return busker;
    }

    public void setBusker(String busker) {
        this.busker = busker;
    }

    public String getPost_image() {
        return post_image;
    }

    public String getCreated_at() {
        return created_at;
    }

}
