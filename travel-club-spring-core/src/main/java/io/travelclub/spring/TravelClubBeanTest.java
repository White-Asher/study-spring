package io.travelclub.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Arrays;

public class TravelClubBeanTest {
    public static void main(String[] args) {
        // ClassPathXmlApplicationContext정보를 읽어온다. (applicationContext.xml로 설정정보를 읽어오겠다)
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        // 실제로 bean이 등록되었는지 확인
        String[] beanNames = context.getBeanDefinitionNames();
        // bean의 id들이 출력될 것이다.
        System.out.println(Arrays.toString(beanNames));
        // 실행하면 [clubStore, clubService] 두개의 bean이 이상없이 등록됨을 확인할 수 있다.
    }
}
