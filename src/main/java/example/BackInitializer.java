package example;

/**
 * Created by Administrator on 2017/2/8.
 */
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.ssl.SslContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BackInitializer extends ChannelInitializer<SocketChannel> {
    static final Logger logger = LoggerFactory.getLogger(BackInitializer.class);

    private final SslContext sslCtx;
    private static final String WEBSOCKET_PATH = "/websocket";

    public BackInitializer(SslContext sslCtx) {
        this.sslCtx = sslCtx;
    }

    @Override
    public void initChannel(SocketChannel ch) {
        //System.out.println("b");
        ChannelPipeline p = ch.pipeline();
        if (sslCtx != null) {
            logger.debug("--------------use ssl model -------------");
            p.addLast(sslCtx.newHandler(ch.alloc()));

        } else {
            logger.debug("--------------use simple model -------------");
        }
        p.addLast(new HttpServerCodec());

        // Deals with fragmentation in http traffic. important without it could not withdraw posted data
        //p.addLast(new HttpObjectAggregator(Short.MAX_VALUE));
        p.addLast(new HttpObjectAggregator(2048000000));//max value could contain 2G

        /*
        * deal with data transmit in the procedure of shake hands，(ping,pong,close)，
        * text and binary data will pass to the next handler in pipeline
        * 在websocket握手阶段进行验证等操作（ping,pong,close事件），
        * 对于文本和二进制数据会传递到管道流中其他handler中
        */
        //p.addLast(new WebSocketServerProtocolHandler(WEBSOCKET_PATH, null, true));

        /*
        *  deal with data transfer after websocket channel build
        *  当建立websocket后，下面channel进行数据发送
        */
        //System.out.println("b");
        p.addLast(new BackServerHandler());

    }
}
