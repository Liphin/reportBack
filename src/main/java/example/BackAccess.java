package example;

import com.alibaba.fastjson.JSON;
import example.operation.entity.response.ResponseData;
import example.operation.impl.report.ReportOpt;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import example.tool.common.Common;
import example.tool.parser.form.FormData;
import java.util.Map;
import static com.aliyun.oss.internal.OSSHeaders.ACCESS_CONTROL_ALLOW_ORIGIN;
import static io.netty.handler.codec.http.HttpHeaderNames.*;
import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by Administrator on 2017/2/8.
 */

public class BackAccess {

    private static Logger logger = LoggerFactory.getLogger(BackAccess.class);

    /**
     * 暴露第三方回调的url链接无需cross域验证
     * @param request request请求对象
     * @param msg request请求护具
     * @param ctx 返回通道
     * @return
     */
    public static boolean nonCrossVerify(HttpRequest request, Object msg, ChannelHandlerContext ctx) {
        //获取uri数据
        String uri = request.uri();
        logger.debug("come to nonCrossVerify"+ uri);
        logger.debug("come to nonCrossVerify2"+ uri.equals("/getReportImgAndVoiceToPc"));

//        if(uri.equals("/getReportImgAndVoiceToPc")){
//            logger.debug("come to bingo");
//        }else {
//            logger.debug("come to fail");
//        }

        //记录并返回消息体是否被消费了
        boolean messagePurchase = true;
        //logger.debug("come to nonCrossVerify");

        /* **************************************************************/
        if (uri.equals("/netty1")) {
            Map<String, Object> map = FormData.getParam(msg);
            BackAccess.logger.debug("come to netty test link");
            httpResponse(ctx, msg, JSON.toJSONString("hello world 1"));
        }
        //小程序端 API ---------------------------------------------------------
        //用户提交举报信息
        else if (uri.equals("/submitReportInfo")) {
            ResponseData response = ReportOpt.submitReportInfo(msg);
            httpResponse(ctx, msg, response);
        }
        //获取该用户举报的消息记录
        else if (uri.equals("/getReportItems")) {
            ResponseData response = ReportOpt.getReportItems(msg);
            httpResponse(ctx, msg, response);
        }
        //小程序获取提交报告的图片和音频
        else if (uri.equals("/getReportImgAndVoice")) {
            ResponseData response = ReportOpt.getReportImgAndVoice(msg);
            httpResponse(ctx, msg, response);
        }

        //PC端 API ---------------------------------------------------------
        //登录界面操作
        else if (uri.equals("/managerLogin")) {
            ResponseData response = ReportOpt.managerLogin(msg);
            httpResponse(ctx, msg, response);
        }
        //获取分页的report信息
        else if (uri.equals("/getRangeReport")) {
            ResponseData response = ReportOpt.getRangeReport(msg);
            httpResponse(ctx, msg, response);
        }
        //搜索数据
        else if (uri.equals("/searchReportList")) {
            ResponseData response = ReportOpt.searchReportList(msg);
            httpResponse(ctx, msg, response);
        }
        //删除特定数据
        else if (uri.equals("/deleteReport")) {
            ResponseData response = ReportOpt.deleteReport(msg);
            httpResponse(ctx, msg, response);
        }
        //批量删除新闻数据
        else if (uri.equals("/deleteBranchReport")) {
            ResponseData response = ReportOpt.deleteBranchReport(msg);
            httpResponse(ctx, msg, response);
        }
        //PC端获取提交报告的图片和音频
        else if (uri.equals("/getReportImgAndVoiceToPc")) {
            logger.debug("come to nonCrossOrigin3");
            ResponseData response = ReportOpt.getReportImgAndVoiceToPc(msg);
            httpResponse(ctx, msg, response);
        }

        //若尚未消费该事件，则返回false
        else {
            messagePurchase = false;
        }
        logger.debug("come to nonCrossVerify4"+ uri.equals("/getReportImgAndVoiceToPc"));

        return messagePurchase;
    }


    /**
     * 无需获取登录状态才能访问的链接请求
     *
     * @param request request请求对象
     * @param msg request请求护具
     * @param ctx 返回通道
     */
    public static boolean nonLoginAccess(HttpRequest request, Object msg, ChannelHandlerContext ctx) {
        //获取uri数据
        String uri = request.uri();
        //logger.debug("come to nonLoginAccess");

        //记录并返回消息体是否被消费了
        boolean messagePurchase = true;

        /* **************************************************************/
        if (uri.equals("/netty2")) {
            Map<String, Object> map = FormData.getParam(msg);
            BackAccess.logger.debug("come to netty test link");
            httpResponse(ctx, msg, JSON.toJSONString("hello world 2"));

        }
        //若尚未消费该事件，则返回false
        else {
            messagePurchase = false;
        }
        return messagePurchase;
    }


    /**
     * 需要用户已登录状态才可访问的请求
     *
     * @param request uri请求地址
     * @param msg request请求护具
     * @param ctx 返回通道
     */
    public static void loginAccess(HttpRequest request, Object msg, ChannelHandlerContext ctx) {
        //获取uri数据
        String uri = request.uri();
        //logger.debug("come to loginAccess");

        /* **************************************************************/
        /*测试专区*/
        if (uri.equals("/netty3")) {
            Map<String, Object> map = FormData.getParam(msg);
            BackAccess.logger.debug("come to netty test link");
            httpResponse(ctx, msg, JSON.toJSONString("hello world 3"));

        } else {
            String message = "server do not serve such request: " + uri;
            httpResponse(ctx, msg, message);
            BackAccess.logger.debug(message);
        }
    }


    /**
     * 返回http请求相关消息
     *
     * @param ctx 通信通道
     * @param msg 请求的引用
     */
    public static void httpResponse(ChannelHandlerContext ctx, Object msg, Object dataBack) {

        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(JSON.toJSONString(dataBack).getBytes()));
        response.headers().set(CONTENT_TYPE, Common.RETURN_JSON);
        commonResponse(ctx, msg, response);
        BackAccess.logger.debug("Return Response Data: \n" + dataBack.toString());
    }


    /**
     * 返回http请求相关消息
     *
     * @param ctx 通信通道
     * @param msg 请求的引用
     */
    public static void httpResponsePureHtml(ChannelHandlerContext ctx, Object msg, String htmlData) {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(htmlData.getBytes()));
        response.headers().set(CONTENT_TYPE, Common.RETURN_TEXT_HTML);
        commonResponse(ctx, msg, response);
        BackAccess.logger.debug("Return html pure data response");
    }


    /**
     * 返回json数据和HTML数据相同的消息体
     *
     * @param ctx      通信通道
     * @param msg      请求数据
     * @param response 请求返回消息封装体
     */
    private static void commonResponse(ChannelHandlerContext ctx, Object msg, FullHttpResponse response) {
        if (HttpUtil.is100ContinueExpected((HttpMessage) msg)) {
            ctx.write(new DefaultFullHttpResponse(HTTP_1_1, CONTINUE));
        }
        boolean keepAlive = HttpUtil.isKeepAlive((HttpMessage) msg);
        response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
        response.headers().set(ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        response.headers().set(ACCESS_CONTROL_ALLOW_METHODS, "POST");
        response.headers().set(ACCESS_CONTROL_ALLOW_HEADERS, "user_id, session_id, *");
        response.headers().set(ACCEPT, "*");
        if (!keepAlive) {
            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        } else {
            ctx.writeAndFlush(response);
        }
    }

}