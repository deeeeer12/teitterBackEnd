package com.twitter.twitterplusp.rabbit.route;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RouterCustomer {


    @RabbitListener(
            bindings = {
                    @QueueBinding(
                            value = @Queue(),
                            exchange = @Exchange(value = "directs",type = "direct"),
                            key = {"error"}
                    )
            }
    )
    public void receiver1(String message1){
        System.out.println(message1);
    }

}
