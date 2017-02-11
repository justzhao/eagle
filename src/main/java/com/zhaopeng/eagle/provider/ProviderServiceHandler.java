package com.zhaopeng.eagle.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zhaopeng.eagle.entity.Request;
import com.zhaopeng.eagle.entity.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;
import org.springframework.context.ApplicationContext;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Created by zhaopeng on 2016/10/29.
 */
public class ProviderServiceHandler extends SimpleChannelInboundHandler<Request> {

    private final static Logger logger = LoggerFactory.getLogger(ProviderServiceHandler.class);

    private ApplicationContext springContext;

    private ExecutorService executor;

    private AtomicInteger accepts = new AtomicInteger();

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, final Request request) throws Exception {
        executor = (ExecutorService) ServiceFactory.getInstance().getSetsMap().get("executor");
        executor.submit(new Runnable() {
            @Override
            public void run() {
                Response response = new Response();
                response.setRequestId(request.getRequestId());
                try {
                    response.setResult(handle(request));
                } catch (Throwable throwable) {
                    response.setError("出错了");
                    throwable.printStackTrace();
                } finally {
                    ctx.writeAndFlush(response);
                }
            }
        });
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
        // ctx.close();
    }

    /**
     * 当创建Channel注册带eventLoop 中调用
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        // 从map的中取出Channel的个数，大于配置值就报错。
        int accepts = (int) ServiceFactory.getInstance().getSetsMap().get("accepts");

        if (this.accepts.get() > accepts) {

            logger.error("channels 的个数超过配置");
            throw new Exception("Channels 太多了");
            //return;
        }
        this.accepts.getAndIncrement();
        logger.info("register accepts is " + this.accepts.get() + " and sets accepts is " + accepts);
        super.channelRegistered(ctx);
    }

    /**
     * Channel处于激活状态
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        // 把Channel 保存到一个map中。
        System.out.println("RamoteAddress : " + ctx.channel().remoteAddress() + " active !" + ctx.channel().toString());
        super.channelActive(ctx);
    }


    /**
     * 当Channel非激活状态
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {


        super.channelInactive(ctx);
    }


    /**
     * 当Channel已经创建但是没有注册到eventloop
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {

        this.accepts.getAndDecrement();
        logger.info("unregister accepts is " + this.accepts.get());
        super.channelUnregistered(ctx);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();// 发生异常，关闭链路
    }


    private Object handle(Request request) throws Throwable {
        String className = request.getClassName();
        Object serviceBean = ServiceFactory.getInstance().getHandlerMap().get(className);
        Class<?> serviceClass = serviceBean.getClass();
        String methodName = request.getMethodName();
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParameters();

        // JDK reflect
        /*Method method = serviceClass.getMethod(methodName, parameterTypes);
        method.setAccessible(true);
        return method.invoke(serviceBean, parameters);*/
        // Cglib reflect
        FastClass serviceFastClass = FastClass.create(serviceClass);
        FastMethod serviceFastMethod = serviceFastClass.getMethod(methodName, parameterTypes);
        return serviceFastMethod.invoke(serviceBean, parameters);
    }


    public ApplicationContext getSpringContext() {
        return springContext;
    }

    public void setSpringContext(ApplicationContext springContext) {
        this.springContext = springContext;
    }

    public ExecutorService getExecutor() {
        return executor;
    }

    public void setExecutor(ExecutorService executor) {
        this.executor = executor;
    }


    public static void main(String args[]) {
        logger.info("12313123");
    }
}
