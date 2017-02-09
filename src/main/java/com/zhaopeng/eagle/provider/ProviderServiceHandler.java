package com.zhaopeng.eagle.provider;

import com.zhaopeng.eagle.entity.Request;
import com.zhaopeng.eagle.entity.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.util.concurrent.ExecutorService;


/**
 * Created by zhaopeng on 2016/10/29.
 */
public class ProviderServiceHandler extends SimpleChannelInboundHandler<Request> {

    private final static Logger logger = LoggerFactory.getLogger(ProviderServiceHandler.class);
    //protected final Logger =LOGG

    private ApplicationContext springContext;

    private ExecutorService executor;

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, final Request request) throws Exception {
        executor = (ExecutorService) ServiceFactory.getInstance().getHandlerMap().get("executor");
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

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        // 从map的中取出Channel的个数，大于配置值就报错。

        logger.info("register1 11 ");

        System.out.println("register ");
        super.channelRegistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        // 把Channel 保存到一个map中。
        System.out.println("RamoteAddress : " + ctx.channel().remoteAddress() + " active !");
        super.channelActive(ctx);
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


    public static void main(String args[]){
        logger.debug("12313123");
    }
}
