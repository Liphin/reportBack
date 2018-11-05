package example.operation.mapper;

/**
 * Created by Administrator on 2017/2/8.
 */
import example.operation.entity.ReportInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;
//@CacheNamespace(flushInterval = 3000)

public interface ReportInfoMapper {

    /******************** select ***********************/
    //根据openid获取该用户的全部举报数据
    @Select("select * from reportinfo where openid=#{openid} order by timestamp desc ")
    public List<ReportInfo> getReportItems(String openid);


    /******************** insert ***********************/
    //插入新的举报消息到数据库
    @Insert("insert into reportinfo(openid, name, contact, content, timestamp, realm) " +
            "values(#{openid}, #{name}, #{contact}, #{content}, #{timestamp}, #{realm})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    public int insertNewReportInfo(ReportInfo reportInfo);


    /******************** delete ***********************/
    //测试Junit时删除新添加的user数据
//    @Delete("delete from user where email=#{email}")
//    public int deleteUserInDb(int user_id);


    /******************** update ***********************/
    //更新用户个人信息
//    @Update("update user set user_name=#{user_name},role=#{role},phone=#{phone},nation=#{nation},portrait=#{portrait} where id=#{id}")
//    public int updateUserInfo(int user_id);

}
