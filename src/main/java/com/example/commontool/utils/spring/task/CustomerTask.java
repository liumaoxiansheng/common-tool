package com.example.commontool.utils.spring.task;

import com.example.commontool.utils.spring.GetBeanUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;

/**
 * @ClassName: CustomerTask
 * @Description:  // 自定义任务，当调用此方法时定时开启
 * @Author: th_legend
 **/
public class CustomerTask {


    private static boolean init=false;

    @Scheduled(cron = "0/3 * * * * ? ")
    public  void  task1(){
        if (!init){
            String baneName="customerTask";
            CustomerTask customerTask = new CustomerTask();
            GetBeanUtil.registerBean("customerTask", customerTask);
            ScheduledAnnotationBeanPostProcessor schedule = GetBeanUtil.getBean(ScheduledAnnotationBeanPostProcessor.class);
            schedule.postProcessAfterInitialization(customerTask,baneName);
            init=true;
        }
        System.out.println("CustomerTask.task1()");
    }
}
