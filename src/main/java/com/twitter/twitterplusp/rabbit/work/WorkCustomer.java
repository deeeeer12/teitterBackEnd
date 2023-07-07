package com.twitter.twitterplusp.rabbit.work;

import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class WorkCustomer {

    @RabbitListener(queuesToDeclare = @Queue("work"))
    public void message1(String messasge1){
        System.out.println("message1 = "+messasge1);
    }

    @RabbitListener(queuesToDeclare = @Queue("work"))
    public void message2(String messasge2){
        System.out.println("message2 = "+messasge2);
    }

}
