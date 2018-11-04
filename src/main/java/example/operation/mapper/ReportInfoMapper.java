package example.operation.mapper;

/**
 * Created by Administrator on 2017/2/8.
 */
import example.operation.entity.ReportInfo;
import org.apache.ibatis.annotations.*;
//@CacheNamespace(flushInterval = 3000)

public interface ReportInfoMapper {

    /******************** select ***********************/
    //根据user_id获得该user的全部信息
//    @Select("select * from user where id=#{user_id}")
//    public Object selectExample(int user_id);


    /******************** insert ***********************/
    //插入新的举报消息到数据库
    @Insert("insert into reportinfo(openid, name, contact, content, timestamp) " +
            "values(#{openid}, #{name}, #{contact}, #{content}, #{timestamp})")
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
