package example;

/**
 * Created by Administrator on 2017/2/8.
 */

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * An HTTP server that sends back the content of the received HTTP request
 * in a pretty plaintext form.
 */

public class BackServer {
    static final Logger logger = LoggerFactory.getLogger(BackServer.class);

    static final boolean SSL = true; //配置是否为SSL方式
    static final int PORT = 8082;

    public static void main(String[] args) throws Exception {
        // Configure SSL.
        final SslContext sslCtx;
        if (SSL) {
            //SelfSignedCertificate ssc = new SelfSignedCertificate();
            //sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();

            File cert = new File("/root/ca/https/cert-1541484604580_liphin.com.crt");
            File key = new File("/root/ca/https/cert-1541484604580_liphin.com.key");
            sslCtx = SslContextBuilder.forServer(cert, key).build();
        } else {
            sslCtx = null;
        }

        // Configure the server.
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.option(ChannelOption.SO_BACKLOG, 1024);
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new BackInitializer(sslCtx));

            Channel ch = b.bind(PORT).sync().channel();
            BackServer.logger.debug("Service access through port on:" + PORT + '/');
            ch.closeFuture().sync();

        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
