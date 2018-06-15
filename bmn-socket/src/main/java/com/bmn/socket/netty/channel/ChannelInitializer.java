package com.bmn.socket.netty.channel;


/**
 * Created by Administrator on 2017/1/14.
 */
@ChannelHandler.Sharable
public abstract class ChannelInitializer<C extends Channel> extends ChannelInboundHandlerAdapter {

    protected abstract void initChannel(C ch) throws Exception;

    public final void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        initChannel((C)ctx.channel());
        ctx.pipeline().remove(this);
        ctx.pipeline().fireChannelRegistered();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        try {
            ChannelPipeline pipeline = ctx.pipeline();
            if(pipeline.context(this) != null) {
                pipeline.remove(this);
            }
        } finally {
            ctx.close();
        }
    }


}
