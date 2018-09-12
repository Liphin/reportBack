package example.operation.impl.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import example.operation.entity.response.ResponseData;
import example.operation.entity.response.StatusCode;
import org.apache.ibatis.session.SqlSession;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2018/2/20.
 */
public class CommonService {

    private static Logger logger = LoggerFactory.getLogger(CommonService.class);

    /**
     * 获取唯一序列号字符串
     *
     * @return
     */
    public static String getUnionId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 返回时间戳公用方法
     *
     * @return
     */
    public static String getTimeStamp() {
        return String.valueOf(System.currentTimeMillis());
    }

    /**
     * 返回时间点公用方法
     *
     * @return
     */
    public static String getDateTime() {
        Date day = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(day);
    }


    /**
     * 进入http调用后打印request的方法名
     *
     * @param functionName http调用的方法名
     */
    public static void printHttpInvokeFunction(String functionName) {
        CommonService.logger.debug("Come into http request function: " + functionName);
    }


    /**
     * 对数据库进行后续提交和关闭操作
     *
     * @param sqlSession   sql数据库操作句柄
     * @param responseData api调用返回的数据
     * @param toCommit     是否做了需要数据库提交的操作
     */
    public static void databaseCommitClose(SqlSession sqlSession, ResponseData responseData, boolean toCommit) {
        if (sqlSession != null) {
            //传入参数定义是否需要提交操作，纯粹数据库查询则不需提交操作
            if (toCommit) {
                //如果整个流程准确无误地实现则对数据库操作进行提交，否则不提交
                if (responseData != null && responseData.getStatus_code() == StatusCode.OK.getValue()) {
                    sqlSession.commit();
                }
            }
            sqlSession.close(); //数据库操作完毕，关闭连接，释放资源
        }
    }


    /**
     * 查看该对象是否为空，返回不为空的Boolean值
     *
     * @param object
     * @return
     */
    public static boolean checkNotNull(Object object) {
        boolean status = false;
        if (object != null && object != "undefined" && object != "null") {
            if (object instanceof String) {
                if (!((String) object).isEmpty()) {
                    //CommonService.logger.debug("checkNotNull: " + object + " come to String check, result is true");
                    status = true;
                }
            } else {
                //CommonService.logger.debug("checkNotNull: " + object + " come to Object check, result is true");
                status = true;
            }
        }
        return status;
    }

}

















