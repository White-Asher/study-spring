package io.travelclub.spring.service;

import io.travelclub.spring.aggregate.club.TravelClub;
import io.travelclub.spring.service.sdo.TravelClubCdo;
import io.travelclub.spring.shared.NameValueList;

import java.util.List;

public interface ClubService {
	//
	String registerClub(TravelClubCdo club);
	TravelClub findClubById(String id);
	List<TravelClub> findClubsByName(String name);
	void modify(String clubId, NameValueList nameValues);
	void remove(String clubId);
}
