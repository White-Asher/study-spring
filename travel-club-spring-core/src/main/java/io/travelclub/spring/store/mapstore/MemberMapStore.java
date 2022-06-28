package io.travelclub.spring.store.mapstore;

import io.travelclub.spring.aggregate.club.CommunityMember;
import io.travelclub.spring.store.MemberStore;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class MemberMapStore implements MemberStore {

    private Map<String, CommunityMember> memberMap;

    public MemberMapStore(){
        this.memberMap = new LinkedHashMap<>();
    }

    @Override
    public String create(CommunityMember member) {
        memberMap.put(member.getId(), member);
        return member.getId();
    }

    @Override
    public CommunityMember retrieve(String memberId) {
        return memberMap.get(memberId);
    }

    /*  이 email 을 가지고 있는 멤버가 있다면 그 멤버를 리턴, 없다면 null 값을 리턴 */
    @Override
    public CommunityMember retrieveByEmail(String email) {
        CommunityMember targetMember = null;
        for (CommunityMember member:
             memberMap.values()) {
            if(member.getEmail().equals(email)){
                targetMember = member;
                break;
            }
        }
        return targetMember;
    }

    /* 동일한 이름의 CommunityMember 가 있을 수 있다.
    파라미터로 이름을 받았을 때 동일한 이름을 갖는 CommunityMember 를 List 타입으로 반환
     */
    @Override
    public List<CommunityMember> retrieveByName(String name) {
        return memberMap.values().stream()
                .filter(member -> member.getName().equals(name))
                .collect(Collectors.toList());
    }
    /*stream이 어렵다면 for문을 사용해보자 (여러 방법을 사용해보자) */

    @Override
    public void update(CommunityMember member) {
        memberMap.put(member.getId(), member);
    }

    @Override
    public void delete(String memberId) {
        memberMap.remove(memberId);
    }

    @Override
    public boolean exists(String memberId) {
        return Optional.ofNullable(memberMap.get(memberId)).isPresent();
    }
}
