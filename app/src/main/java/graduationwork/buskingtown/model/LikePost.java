package graduationwork.buskingtown.model;

public class LikePost {
    int like_post_id, post, busker, likes;

    public int getLike_post_id() {
        return like_post_id;
    }

    public int getPost() {
        return post;
    }

    public void setPost(int post) {
        this.post = post;
    }

    public int getBusker() {
        return busker;
    }

    public void setBusker(int busker) {
        this.busker = busker;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }
}
