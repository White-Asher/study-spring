package io.travelclub.spring.service.logic;

import io.travelclub.spring.aggregate.club.CommunityMember;
import io.travelclub.spring.service.MemberService;
import io.travelclub.spring.service.sdo.MemberCdo;
import io.travelclub.spring.shared.NameValueList;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberServiceLogic implements MemberService {
    @Override
    public String registerMember(MemberCdo member) {
        return null;
    }

    @Override
    public CommunityMember findMemberById(String memberId) {
        return null;
    }

    @Override
    public CommunityMember findMemberByEmail(String memberEmail) {
        return null;
    }

    @Override
    public List<CommunityMember> findMembersByName(String name) {
        return null;
    }

    @Override
    public void modifyMember(String memberId, NameValueList member) {

    }

    @Override
    public void removeMember(String memberId) {

    }
}
