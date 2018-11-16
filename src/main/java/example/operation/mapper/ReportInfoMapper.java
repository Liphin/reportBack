package example.operation.mapper;

/**
 * Created by Administrator on 2017/2/8.
 */
import example.operation.entity.ReportInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;
//@CacheNamespace(flushInterval = 3000)

public interface ReportInfoMapper {

    /******************** select ***********************/
    //根据openid获取该用户的全部举报数据
    @Select("select * from reportinfo where openid=#{openid} order by create_time desc ")
    public List<ReportInfo> getReportItems(String openid);

    //根据openid获取该用户的全部举报数据
    @Select("select * from reportinfo where create_time<#{create_time} order by create_time desc limit 120")
    public List<ReportInfo> getRangeReport(String create_time);

    //获取全部的动态消息条目的数目
    @Select("select count(*) from reportinfo")
    public int getReportInfoNum();

    //获取搜索数据
    @SelectProvider(type = SqlProvider.class, method = "searchReportList")
    public List<ReportInfo> searchReportList(Map<String, Object> map);

    /******************** insert ***********************/
    //插入新的举报消息到数据库
    @Insert("insert into reportinfo(openid, name, contact, content, timestamp, realm, create_time) " +
            "values(#{openid}, #{name}, #{contact}, #{content}, #{timestamp}, #{realm}, #{create_time})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    public int insertNewReportInfo(ReportInfo reportInfo);


    /******************** delete ***********************/
    //测试Junit时删除新添加的user数据
//    @Delete("delete from user where email=#{email}")
//    public int deleteUserInDb(int user_id);
    @Delete("delete from reportinfo where timestamp=#{timestamp}")
    public int deleteReport(String timestamp);


    /******************** update ***********************/
    //更新阅读状态信息
    @Update("update reportinfo set view=2 where timestamp=#{timestamp} ")
    public int updateViewStatus(String timestamp);

}
