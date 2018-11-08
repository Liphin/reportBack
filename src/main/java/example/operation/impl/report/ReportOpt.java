package example.operation.impl.report;

import com.alibaba.fastjson.JSON;
import example.operation.entity.Manager;
import example.operation.entity.ReportInfo;
import example.operation.entity.Resource;
import example.operation.entity.response.ResponseData;
import example.operation.impl.common.CommonImpl;
import example.operation.impl.common.CommonService;
import example.tool.common.Assemble;
import example.tool.common.Common;
import example.tool.common.Mapper;
import example.tool.parser.form.FormData;
import example.tool.parser.text.TextData;
import org.apache.ibatis.session.SqlSession;
import sun.text.resources.FormatData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Administrator on 2018/11/4.
 */
public class ReportOpt {


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
            String text = TextData.getText(msg);
            Map<String, Object> map = JSON.parseObject(text);
            String timestamp = (String) map.get(Common.TIMESTAMP);

            //根据消息体的timestamp获取图片和音频资源
            List<Resource> list = sqlSession.selectList(Mapper.GET_RESOURCE_IMG_AND_VOICE,timestamp);
            Assemble.responseSuccessSetting(responseData, list);
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


}
