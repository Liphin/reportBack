package example.operation.impl.report;

import com.alibaba.fastjson.JSON;
import example.operation.entity.Manager;
import example.operation.entity.ReportInfo;
import example.operation.entity.Resource;
import example.operation.entity.response.ResponseData;
import example.operation.entity.response.StatusCode;
import example.operation.impl.common.CommonImpl;
import example.operation.impl.common.CommonService;
import example.tool.common.Assemble;
import example.tool.common.Common;
import example.tool.common.Mapper;
import example.tool.config.GlobalConfig;
import example.tool.parser.form.FormData;
import example.tool.parser.text.TextData;
import example.tool.util.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.text.resources.FormatData;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Administrator on 2018/11/4.
 */
public class ReportOpt {
    private static Logger logger = LoggerFactory.getLogger(ReportOpt.class);


    //********************************** 手机端的API操作 *************************************************************
    /**
     * 提交举报信息，并插入数据库等操作
     * @return
     */
    public static ResponseData submitReportInfo(Object msg){
        return CommonService.simpleImplOpt(true, new CommonImpl() {
            @Override
            public void run(ResponseData responseData, SqlSession sqlSession) throws Exception {
                //解析传递的参数
                String text = TextData.getText(msg);
                ReportInfo reportInfo = JSON.parseObject(text, ReportInfo.class);
                reportInfo.setTimestamp(CommonService.getTimeStamp());
                reportInfo.setCreate_time(CommonService.getDateTime());

                //插入消息体数据
                sqlSession.insert(Mapper.INSERT_NEW_REPORTINFO, reportInfo);

                //循环插入pictures数据
                for(int i =0; i<reportInfo.getPictures().size(); i++){
                    Resource resource = new Resource(reportInfo.getPictures().get(i), Common.IMG_TYPE, reportInfo.getTimestamp(), CommonService.getDateTime());
                    sqlSession.insert(Mapper.INSERT_NEW_RESOURCE, resource);
                }

                //循环插入recorder数据
                for(int i =0; i<reportInfo.getRecorder().size(); i++){
                    Resource resource = new Resource(reportInfo.getRecorder().get(i), Common.VOICE_TYPE, reportInfo.getTimestamp(), CommonService.getDateTime());
                    sqlSession.insert(Mapper.INSERT_NEW_RESOURCE, resource);
                }

                //封装返回数据
                Assemble.responseSuccessSetting(responseData, true);
            }
        });
    }


    /**
     * 获取该用户举报的消息记录
     * @param msg
     * @return
     */
    public static ResponseData getReportItems(Object msg){
        return CommonService.simpleImplOpt(false, (responseData, sqlSession) -> {
            //获取数据
            String text = TextData.getText(msg);
            Map<String, Object> map = JSON.parseObject(text);
            String openid = (String) map.get(Common.OPENID);

            //查询并返回数据
            List<ReportInfo> reportInfos = sqlSession.selectList(Mapper.GET_REPORT_ITEMS, openid);
            Assemble.responseSuccessSetting(responseData, reportInfos);
        });
    }


    /**
     * 获取该消息体的图片和声音信息
     * @param msg
     * @return
     */
    public static ResponseData getReportImgAndVoice(Object msg){
        return CommonService.simpleImplOpt(false, (responseData, sqlSession) -> {
            //获取报告timestamp消息体

            String timestamp = FormData.getParam(msg, Common.TIMESTAMP);

            //根据消息体的timestamp获取图片和音频资源
            List<Resource> list = sqlSession.selectList(Mapper.GET_RESOURCE_IMG_AND_VOICE,timestamp);
            //装载数据到map中
            Map<String, Object> map = new HashMap<>();
            map.put("resource", list);

            Assemble.responseSuccessSetting(responseData, map);
        });
    }



    //************************************ PC端操作 *******************************************************
    /**
     * 获取分页的report信息
     * @param msg
     * @return
     */
    public static ResponseData getRangeReport(Object msg){
        return CommonService.simpleImplOpt(false, (responseData, sqlSession) -> {
            //获取分页create_time的值
            String createTime = FormData.getParam(msg, Common.CREATE_TIME);

            //根据消息体的createTime获取一定范围内的消息数据和数据总条目
            List<ReportInfo> list = sqlSession.selectList(Mapper.GET_RANGE_REPORT,createTime);
            int totalNum = sqlSession.selectOne(Mapper.GET_REPORT_INFO_NUM);

            //装载数据到map中
            Map<String, Object> map = new HashMap<>();
            map.put("totalNum", totalNum);
            map.put("reportList", list);
            Assemble.responseSuccessSetting(responseData, map);
        });
    }

    /**
     * 获取特定搜索数据
     * @param msg
     * @return
     */
    public static ResponseData searchReportList(Object msg){
        return CommonService.simpleImplOpt(false, (responseData, sqlSession) -> {
            Map<String, Object> map = FormData.getParam(msg);
            List<ReportInfo> list = sqlSession.selectList(Mapper.SEARCH_REPORT_LIST, map);
            Assemble.responseSuccessSetting(responseData, list);
        });
    }


    /**
     * 登录界面操作
     * @param msg
     * @return
     */
    public static ResponseData managerLogin(Object msg){
        return CommonService.simpleImplOpt(false, (responseData, sqlSession) -> {
            //获取登录的信息
            Map<String, Object> map = FormData.getParam(msg);
            ReportOpt.logger.warn("88888888");
            //根据传递过来的account和password查询数据库是否有该值
            Manager manager = sqlSession.selectOne(Mapper.CHECK_MANAGER_INFO, map);

            //根据查询出来的manager信息是否为空包装不同的返回值
            if(CommonService.checkNotNull(manager)){
                Assemble.responseSuccessSetting(responseData, manager);

            }else{
                Assemble.responseErrorSetting(responseData, 400, "Manager info not found");
            }

        });
    }

    /**
     * 同时删除多条朋友圈
     *
     * @param msg
     */
    public static ResponseData deleteReport(Object msg) {
        return CommonService.simpleImplOpt(true, new CommonImpl() {
            @Override
            public void run(ResponseData responseData, SqlSession sqlSession) throws Exception {

                Map<String, Object> map = FormData.getParam(msg);
                String timestamp = (String) map.get(Common.TIMESTAMP);

                deleteReportOpt(sqlSession, timestamp);
                //返回正确数据
                Assemble.responseSuccessSetting(responseData, null);
            }
        });
    }

    /**
     * 删除朋友圈数据操作
     */
    public static void deleteReportOpt(SqlSession sqlSession, String timestamp) throws Exception{

        //获取删除的文件列表
        //根据消息体的timestamp获取图片和音频资源
        List<String> resourcesList = sqlSession.selectList(Mapper.GET_RESOURCE_IMG_AND_VOICE_NAME,timestamp);

        //删除数据库数据
        int deleteInfoNum = sqlSession.delete(Mapper.DELETE_REPORT, timestamp);
        int deleteImgNum = sqlSession.delete(Mapper.DELETE_RESOURCE, timestamp);

        if (deleteInfoNum > 0 && deleteImgNum > 0) {
            //删除朋友圈封面、图片、内容数据
//            String dynamicInfoCoverImg = GlobalConfig.getProperties(Common.DYNAMICINFOS_SYS_PATH_COVERIMG);
//            String dynamicInfoHtmlPath = GlobalConfig.getProperties(Common.DYNAMICINFOS_SYS_PATH_HTML);
//
//            //删除朋友圈封面文件数据
//            String coverImg = dynamicInfoCoverImg + timestamp + Common.SUFFIX_PNG;
//            deleteFile(coverImg);
//
//            //删除朋友圈内容文件数据
//            String htmlFile = dynamicInfoHtmlPath + timestamp + Common.SUFFIX_INDEX_HTML;
//            deleteFile(htmlFile);

            for (int index = 0; index < resourcesList.size(); index++) {
                System.out.println(resourcesList.get(index));  //.get(index)
//                String resourceFile=resourcesList.get(index);
//                deleteFile(resourceFile);
            }


        } else {
            String message = "delete file from database error, timestamp: " + timestamp;
            ReportOpt.logger.warn(message);
        }
    }

    /**
     * 批量删除数据
     *
     * @param msg
     */
    public static ResponseData deleteBatchReport(Object msg) {
        return CommonService.simpleImplOpt(true, new CommonImpl() {
            @Override
            public void run(ResponseData responseData, SqlSession sqlSession) throws Exception {
                String listStr = (String) FormData.getParam(msg, Common.DELETE_LIST);
                //解析Json数组数据
                List<String> list = JSON.parseArray(listStr, String.class);

                //循环传递过来的list中每个数据，并删除每条朋友圈数据
                for (String timestamp : list) {
                    deleteReportOpt(sqlSession, timestamp);
                }
                //返回正确数据
                Assemble.responseSuccessSetting(responseData, null);
            }
        });
    }

    /**
     * 删除单文件操作
     *
     * @param path
     */
    public static void deleteFile(String path) {
        File file = new File(path);
        if (file.exists() && file.isFile()) {
            file.delete();
        }
    }

}
