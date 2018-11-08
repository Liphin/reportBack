package example.operation.mapper;


import example.tool.common.Common;

import java.util.Map;

/**
 * Created by Administrator on 2018/3/12.
 */
public class SqlProvider {


    /**
     * 根据项目传递过来参数进行更新项目公开程度状态
     *
     * @param map
     * @return
     */
//    public String updateProjectOpenness(Map<String, Object> map) {
//        StringBuilder stringBuilder = new StringBuilder();
//        int is_public = Integer.parseInt((map.get(Common.IS_PUBLIC)).toString());
//
//        //需检查user_id和project_id同时不为空才继续进行update操作
//        if (CommonService.checkNotNull(map.get(Common.USER_ID)) && CommonService.checkNotNull(map.get(Common.PROJECT_ID))) {
//            stringBuilder.append("update project set is_public=" + is_public);
//
//            //如果提交公开待审核状态则进行添加对应的industry类型
//            if (is_public == 1) {
//                stringBuilder.append(" , industry_code='" + map.get(Common.INDUSTRY_CODE) + "'");
//                stringBuilder.append(" , industry_sub_code='" + map.get(Common.INDUSTRY_SUB_CODE) + "'");
//
//            } else if (is_public == 0) {
//                //若设置不公开则设置industry类型等为空
//                stringBuilder.append(" , industry_code=''");
//                stringBuilder.append(" , industry_sub_code=''");
//            }
//            stringBuilder.append(" where id=" + map.get(Common.PROJECT_ID) + " and user_id=" + map.get(Common.USER_ID));
//        }
//        return stringBuilder.toString();
//    }
    /**
     * 获取待审核的朋友圈数据
     *
     * @param map
     * @return
     */
    public String searchReportList(Map<String, Object> map) {
        StringBuilder stringBuilder = new StringBuilder();
        String  startDate = String.valueOf(map.get("startDate"));
        String  endDate = String.valueOf(map.get("endDate"));
        Integer reportType = Integer.parseInt(String.valueOf(map.get("type")));

        //根据搜索条件进行不同的搜索
        if (reportType == 5) {
            stringBuilder.append("select * from reportinfo where (create_time >= '" + startDate + "' and create_time <= '" + endDate + "') order by create_time desc");
        } else {
            stringBuilder.append("select * from reportinfo where realm=" + reportType + " and (create_time >= '" + startDate + "' and create_time <= '" + endDate + "') order by create_time desc");
        }

        return stringBuilder.toString();
    }

}





