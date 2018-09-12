package example.tool.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Administrator on 2018/2/12.
 */
public class Common {

    private static Logger logger = LoggerFactory.getLogger(Common.class.getName());

    //global环境设置配置
    public static String DEV_ENVIRONMENT = "dev"; //测试环境
    public static String PROD_ENVIRONMENT = "prod"; //生产环境
    public static String UTF8 = "UTF-8";
    public static String SETTING_FILES = "com.viewcoder.setting.files"; //设定目标环境下的文件

    //数据返回设置
    public static String RETURN_TEXT_HTML = "text/html;charset=UTF-8";
    public static String RETURN_JSON = "application/json";

}
