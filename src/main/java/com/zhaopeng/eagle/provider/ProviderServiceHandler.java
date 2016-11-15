package com.zhaopeng.eagle.provider;

import com.zhaopeng.eagle.entity.Request;
import com.zhaopeng.eagle.entity.Response;
import com.zhaopeng.eagle.util.AsyncTaskSubmitUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;



/**
 * Created by zhaopeng on 2016/10/29.
 */
public class ProviderServiceHandler extends SimpleChannelInboundHandler<Request> {
    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, final Request request) throws Exception {
        // 根据传递的参数：类名，方法名，参数，实例化对象执行方法，返回结果。
        // 收到消息直接打印输出
      //  System.out.println(ctx.channel().remoteAddress() + " Say : " + request.getRequestId());

        AsyncTaskSubmitUtil.submit(new Runnable() {
            @Override
            public void run() {
                Response response=new Response();
                response.setRequestId(request.getRequestId());
                try {
                    response.setResult(handle(request));

                } catch (Throwable throwable) {
                    response.setError("出错了");
                    throwable.printStackTrace();
                }finally {
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
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("RamoteAddress : " + ctx.channel().remoteAddress() + " active !");

        //ctx.writeAndFlush( "Welcome to " + InetAddress.getLocalHost().getHostName() + " service! $_");

        super.channelActive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();// 发生异常，关闭链路
    }

    private Object handle(Request request) throws Throwable {
        String className = request.getClassName()+"Impl";
        // 暂时写死
        Class cls = Class.forName("com.zhaopeng.demo.provider.StoreServiceImpl");
        Object serviceBean = cls.newInstance();

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


}
