package example.operation.mapper;

/**
 * Created by Administrator on 2017/2/8.
 */

import example.operation.entity.Manager;
import example.operation.entity.Resource;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
//@CacheNamespace(flushInterval = 3000)

public interface ManagerMapper {

    /******************** select ***********************/
    //根据timestamp获取该举报消息的图片和音频资源
    @Select("select * from manager where account=#{account} and password=#{password}")
    public Manager checkManagerInfo(@Param("account")String account, @Param("password")String password);


    /******************** insert ***********************/
    //插入新的resource数据到数据库
//    @Insert("insert into resource(filename, type, report_timestamp, create_time) " +
//            "values(#{filename}, #{type}, #{report_timestamp}, #{create_time})")
//    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    public int insertNewResource(Resource resource);


    /******************** delete ***********************/
//    //测试Junit时删除新添加的user数据
//    @Delete("delete from user where email=#{email}")
//    public int deleteUserInDb(int user_id);


    /******************** update ***********************/
//    //更新用户个人信息
//    @Update("update user set user_name=#{user_name},role=#{role},phone=#{phone},nation=#{nation},portrait=#{portrait} where id=#{id}")
//    public int updateUserInfo(int user_id);

}
