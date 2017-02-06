package com.zhaopeng.eagle.provider;

import com.zhaopeng.eagle.entity.Request;
import com.zhaopeng.eagle.entity.Response;
import com.zhaopeng.eagle.provider.config.ServiceFactory;
import com.zhaopeng.eagle.spring.EagleApplicationBean;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.concurrent.ExecutorService;


/**
 * Created by zhaopeng on 2016/10/29.
 */
public class ProviderServiceHandler extends SimpleChannelInboundHandler<Request> implements ApplicationContextAware {

    private ApplicationContext springContext;

    private ExecutorService executor;

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, final Request request) throws Exception {
        // 根据传递的参数：类名，方法名，参数，实例化对象执行方法，返回结果。
        // 收到消息直接打印输出
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
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
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


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        springContext = applicationContext;
        EagleApplicationBean eagle = (EagleApplicationBean) springContext.getBean("eagle");
        executor = eagle.getExecutor();
        System.out.println(applicationContext.getApplicationName());

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
}
