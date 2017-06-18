package com.wmall;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.alibaba.dubbo.container.Main;

import java.util.Date;

/**
 * Created by asus-pc on 2017/6/14.
 */
public class ServerStart {

    public static ClassPathXmlApplicationContext context;

    private void start(){
        long beginTime = new Date().getTime();
        if(context != null){
            System.out.print("系统已启动，若需要重启请选择reload参数");
        }
        context = new ClassPathXmlApplicationContext("spring_mybatis.xml");
        context.start();
        System.out.println("ServerStart 服务启动完成!耗时:" + (new Date().getTime() - beginTime) + "毫秒");
        Main.main(new String[] {});
    }

    public static void main(String[] args){
        ServerStart pStart = new ServerStart();
        pStart.start();
    }
}
