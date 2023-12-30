package com.twitter.twitterplusp.rabbit.fanout;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class FanoutCustomer {

    @RabbitListener(bindings = {
            @QueueBinding(
                   value = @Queue(),//不写名字就是临时队列
                    exchange = @Exchange(value = "logs",type = "fanout")
            )
    })
    public void receiver1(String message1){
        System.out.println(message1);
    }

    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue(),//不写名字就是临时队列
                    exchange = @Exchange(value = "logs",type = "fanout")
            )
    })
    public void receiver2(String message1){
        System.out.println(message1);
    }

    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue(),//不写名字就是临时队列
                    exchange = @Exchange(value = "logs",type = "fanout")
            )
    })
    public void receiver3(String message1){
        System.out.println(message1);
    }

}
