package io.travelclub.spring.service.logic;

import io.travelclub.spring.aggregate.club.TravelClub;
import io.travelclub.spring.service.ClubService;
import io.travelclub.spring.service.sdo.TravelClubCdo;
import io.travelclub.spring.shared.NameValueList;
import io.travelclub.spring.store.ClubStore;
import io.travelclub.spring.store.mapstore.ClubMapStore;

import java.util.List;

public class ClubServiceLogic implements ClubService {

    private ClubStore clubStore;

    public ClubServiceLogic(ClubStore clubStore){
        this.clubStore = clubStore;
    }

    @Override
    public String registerClub(TravelClubCdo club) {
//        clubStore
        return null;
    }

    @Override
    public TravelClub findClubById(String id) {
        return null;
    }

    @Override
    public List<TravelClub> findClubsByName(String name) {
        return null;
    }

    @Override
    public void modify(String clubId, NameValueList nameValues) {

    }

    @Override
    public void remove(String clubId) {

    }
}
