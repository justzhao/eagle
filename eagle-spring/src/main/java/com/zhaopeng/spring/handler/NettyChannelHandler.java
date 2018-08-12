package com.zhaopeng.spring.handler;


import com.zhaopeng.remote.dispacher.DefaultFuture;
import com.zhaopeng.remote.entity.Request;
import com.zhaopeng.remote.entity.Response;
import com.zhaopeng.remote.hanlder.ChannelHandler;

import com.zhaopeng.spring.holder.ServiceHolder;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyChannelHandler implements ChannelHandler {


    @Override
    public void connected(Channel channel) {

    }

    @Override
    public void disconnected(Channel channel) {

    }

    @Override
    public void sent(Channel channel, Object message) {

        if (message instanceof Request) {
            new DefaultFuture(channel, (Request) message, 0);
            channel.writeAndFlush(message);

        } else {
            channel.writeAndFlush(message);
            log.error("not request {}", message);
        }

    }

    @Override
    public void received(Channel channel, Object message) {


        if (message instanceof Request) {
            Request request = (Request) message;
            if (request.isHeartEvent()) {
                Response response = new Response(request.getRequestId());
                channel.writeAndFlush(response);
            } else {
                if (request.isTwoWay()) {
                    Response response = new Response(request.getRequestId());
                    try {
                        response.setResult(handle(request));
                    } catch (Throwable throwable) {

                    } finally {
                        channel.writeAndFlush(response);
                    }
                }
            }
        } else if (message instanceof Response) {
            handleResponse(channel, (Response) message);
        }
    }

    @Override
    public void caught(Channel channel, Throwable ex) {

    }


    public void handleResponse(Channel channel, Response response) {

    }

    public Object handle(Request request){

        ServiceHolder.getService(request.getClassName());

        return null;

    }

    /**
     * ChannelState
     */
    public enum ChannelState {

        /**
         * CONNECTED
         */
        CONNECTED,

        /**
         * DISCONNECTED
         */
        DISCONNECTED,

        /**
         * SENT
         */
        SENT,

        /**
         * RECEIVED
         */
        RECEIVED,

        /**
         * CAUGHT
         */
        CAUGHT
    }

    class ChannelEventRunnable implements Runnable {

        private final Channel channel;
        private final ChannelState state;
        private final Throwable exception;
        private final Object message;

        public ChannelEventRunnable(Channel channel, ChannelState state) {
            this(channel, state, null);
        }

        public ChannelEventRunnable(Channel channel, ChannelState state, Object message) {
            this(channel, state, message, null);
        }

        public ChannelEventRunnable(Channel channel, ChannelState state, Throwable t) {
            this(channel, state, null, t);
        }

        public ChannelEventRunnable(Channel channel, ChannelState state, Object message, Throwable exception) {
            this.channel = channel;

            this.state = state;
            this.message = message;
            this.exception = exception;
        }

        @Override
        public void run() {
            switch (state) {
                case CONNECTED:
                    try {
                        connected(channel);
                    } catch (Exception e) {
                        log.warn("ChannelEventRunnable handle " + state + " operation error, channel is " + channel, e);
                    }
                    break;
                case DISCONNECTED:
                    try {
                        disconnected(channel);
                    } catch (Exception e) {
                        log.warn("ChannelEventRunnable handle " + state + " operation error, channel is " + channel, e);
                    }
                    break;
                case SENT:
                    try {
                     sent(channel, message);
                    } catch (Exception e) {
                        log.warn("ChannelEventRunnable handle " + state + " operation error, channel is " + channel
                            + ", message is " + message, e);
                    }
                    break;
                case RECEIVED:
                    try {
                       received(channel, message);
                    } catch (Exception e) {
                        log.warn("ChannelEventRunnable handle " + state + " operation error, channel is " + channel
                            + ", message is " + message, e);
                    }
                    break;
                case CAUGHT:
                    try {
                       caught(channel, exception);
                    } catch (Exception e) {
                        log.warn("ChannelEventRunnable handle " + state + " operation error, channel is " + channel
                            + ", message is: " + message + ", exception is " + exception, e);
                    }
                    break;
                default:
                    log.warn("unknown state: " + state + ", message is " + message);
            }
        }

    }

}