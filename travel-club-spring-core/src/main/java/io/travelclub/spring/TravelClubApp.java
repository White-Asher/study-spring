package io.travelclub.spring;

import io.travelclub.spring.aggregate.club.CommunityMember;
import io.travelclub.spring.aggregate.club.TravelClub;
import io.travelclub.spring.service.ClubService;
import io.travelclub.spring.service.MemberService;
import io.travelclub.spring.service.sdo.MemberCdo;
import io.travelclub.spring.service.sdo.TravelClubCdo;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


import java.util.Arrays;
import java.util.Date;

public class TravelClubApp {
    public static void main(String[] args) {
        // ClassPathXmlApplicationContext정보를 읽어온다. (applicationContext.xml로 설정정보를 읽어오겠다)
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        // 실제로 bean이 등록되었는지 확인
        String[] beanNames = context.getBeanDefinitionNames();
        // bean의 id들이 출력될 것이다.
//        System.out.println(Arrays.toString(beanNames));
        // 실행하면 [clubStore, clubService] 두개의 bean이 이상없이 등록됨을 확인할 수 있다.

        // bean으로 등록되어 있는 MemberServiceLogic 의 인스턴스를 참조
        MemberService memberService = context.getBean("memberServiceLogic", MemberService.class);

        String memberId = memberService.registerMember(
                new MemberCdo(
                        "test@nextree.io",
                        "kim",
                        "Test Member",
                        "000-0000-0000",
                        "2020.10"));

        CommunityMember foundedMember = memberService.findMemberById(memberId);
        System.out.println(foundedMember.toString());


//        TravelClubCdo clubCdo = new TravelClubCdo("TravelClub", "Test TravelClub");
//        ClubService clubService = context.getBean("clubService", ClubService.class);
//
//        String clubId = clubService.registerClub(clubCdo);
//
//        TravelClub foundecClub = clubService.findClubById(clubId);
//
//        System.out.println("Club name " + foundecClub.getName());
//        System.out.println("Club intro " + foundecClub.getIntro());
//        System.out.println("Club foundationTime " + new Date(foundecClub.getFoundationTime()));
    }
}
