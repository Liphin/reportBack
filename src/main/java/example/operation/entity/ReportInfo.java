package example.operation.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/11/4.
 * 举报消息实体类
 */
public class ReportInfo {

    private int id;
    private String openid;
    private String name;
    private String contact;
    private String content;
    private int realm;
    private List<String> pictures;
    private List<String> recorder;
    private String timestamp;
    private int status;
    private String comment;
    private String update_time;

    public ReportInfo() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }

    public List<String> getRecorder() {
        return recorder;
    }

    public void setRecorder(List<String> recorder) {
        this.recorder = recorder;
    }

    public int getStatus() {
        return status;
    }

    public int getRealm() {
        return realm;
    }

    public void setRealm(int realm) {
        this.realm = realm;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    @Override
    public String toString() {
        return "ReportInfo{" +
                "id=" + id +
                ", openid='" + openid + '\'' +
                ", name='" + name + '\'' +
                ", contact='" + contact + '\'' +
                ", content='" + content + '\'' +
                ", realm='" + realm + '\'' +
                ", pictures=" + pictures +
                ", recorder=" + recorder +
                ", timestamp='" + timestamp + '\'' +
                ", status=" + status +
                ", comment='" + comment + '\'' +
                ", update_time='" + update_time + '\'' +
                '}';
    }
}
