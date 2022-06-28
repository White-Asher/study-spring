package io.travelclub.spring.service.logic;

import io.travelclub.spring.aggregate.club.CommunityMember;
import io.travelclub.spring.service.MemberService;
import io.travelclub.spring.service.sdo.MemberCdo;
import io.travelclub.spring.shared.NameValueList;
import io.travelclub.spring.store.MemberStore;
import io.travelclub.spring.store.mapstore.MemberMapStore;
import io.travelclub.spring.util.exception.MemberDuplicationException;
import io.travelclub.spring.util.exception.NoSuchMemberException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberServiceLogic implements MemberService {


    private MemberStore memberStore;
    public MemberServiceLogic(MemberMapStore memberStore){
        this.memberStore = memberStore;
    }
    /*
    ClubServiceLogic 에서는 단순히 TravelClub 을 저장하는 형태로 진행
    멤버를 저장할 때는 email 을 찾아서 동일한 email 을 갖는 멤버는 저장이 되지 않게 구현
     */
    @Override
    public String registerMember(MemberCdo member) {
        String email = member.getEmail();
        CommunityMember foundedMember = memberStore.retrieveByEmail(email);

        // 해당 이메일을 갖는 member가 이미 존재함.
        if(foundedMember != null){
            throw new MemberDuplicationException("Member already exists with email : " +email);
        }
        foundedMember = new CommunityMember(
                member.getEmail(), member.getName(), member.getPhoneNumber()
        );
        foundedMember.setNickName(member.getNickName());
        foundedMember.setBirthDay(member.getBirthDay());
        foundedMember.checkValidation();

        memberStore.create(foundedMember);

        return foundedMember.getId();
    }

    @Override
    public CommunityMember findMemberById(String memberId) {
        return memberStore.retrieve(memberId);
    }

    @Override
    public CommunityMember findMemberByEmail(String memberEmail) {
        return memberStore.retrieveByEmail(memberEmail);
    }

    @Override
    public List<CommunityMember> findMembersByName(String name) {
        return memberStore.retrieveByName(name);
    }

    @Override
    public void modifyMember(String memberId, NameValueList nameValueList) {
        CommunityMember targetMember = memberStore.retrieve(memberId);
        if(targetMember == null ){
            throw new NoSuchMemberException("No such member with id : " + memberId);
        }
        targetMember.modifyValues(nameValueList);

        memberStore.update(targetMember);
    }

    @Override
    public void removeMember(String memberId) {
        if(memberStore.exists(memberId)){
            throw new NoSuchMemberException("No such member with id : "+ memberId);
        }
        memberStore.delete(memberId);
    }
}
