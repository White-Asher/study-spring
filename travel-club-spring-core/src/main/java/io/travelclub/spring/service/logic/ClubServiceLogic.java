package io.travelclub.spring.service.logic;

import io.travelclub.spring.aggregate.club.TravelClub;
import io.travelclub.spring.service.ClubService;
import io.travelclub.spring.service.sdo.TravelClubCdo;
import io.travelclub.spring.shared.NameValueList;
import io.travelclub.spring.store.ClubStore;
import io.travelclub.spring.store.mapstore.ClubMapStore;
import io.travelclub.spring.util.exception.NoSuchClubException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("clubService")
public class ClubServiceLogic implements ClubService {

    private ClubStore clubStore;

    public ClubServiceLogic(ClubStore clubStore){
        this.clubStore = clubStore;
    }

    @Override
    public String registerClub(TravelClubCdo club) {
        TravelClub newClub = new TravelClub(club.getName(), club.getIntro());
        newClub.checkValidation();
        return clubStore.create(newClub);
    }

    @Override
    public TravelClub findClubById(String id) {
        return clubStore.retrieve(id);
    }

    @Override
    public List<TravelClub> findClubsByName(String name) {
        return clubStore.retrieveByName(name);
    }

    @Override
    public void modify(String clubId, NameValueList nameValues) {
        TravelClub foundedClub = clubStore.retrieve(clubId);
        if(foundedClub == null){
            throw new NoSuchClubException("No such club with id : "+clubId);
        }
        foundedClub.modifyValues(nameValues);
        clubStore.update(foundedClub);
    }

    @Override
    public void remove(String clubId) {
        if(!clubStore.exists(clubId)){
            throw new NoSuchClubException("No such club with id : " + clubId);
        }
        clubStore.delete(clubId);
    }
}
