package example.operation.entity;

/**
 * Created by Administrator on 2018/11/3.
 * 用户上传的资源文件实体类
 */
public class Resource {

    private String filename;
    private Integer type;
    private String report_timestamp;
    private String create_time;
    private String update_time;

    public Resource() {
    }

    public Resource(String filename, Integer type, String report_timestamp, String create_time) {
        this.filename = filename;
        this.type = type;
        this.report_timestamp = report_timestamp;
        this.create_time = create_time;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getReport_timestamp() {
        return report_timestamp;
    }

    public void setReport_timestamp(String report_timestamp) {
        this.report_timestamp = report_timestamp;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    @Override
    public String toString() {
        return "Resource{" +
                "filename='" + filename + '\'' +
                ", type=" + type +
                ", report_timestamp='" + report_timestamp + '\'' +
                ", create_time='" + create_time + '\'' +
                ", update_time='" + update_time + '\'' +
                '}';
    }
}
