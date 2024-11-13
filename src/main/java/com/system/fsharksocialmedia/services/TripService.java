package com.system.fsharksocialmedia.services;

import com.system.fsharksocialmedia.dtos.*;
import com.system.fsharksocialmedia.entities.*;
import com.system.fsharksocialmedia.models.TripModel;
import com.system.fsharksocialmedia.repositories.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class TripService {

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private UserInfoService userInfoService;

    public TripDto createTrip(TripModel model) {
        Trip trip = new Trip();
        trip.setTripname(model.getTripName());
        trip.setStartdate(model.getStartDate());
        trip.setEnddate(model.getEndDate());
        trip.setCreatedate(Instant.now());
        trip.setDescription(model.getDescription());
        trip = tripRepository.save(trip);
        return toTripDto(trip);
    }

//    public PlacetripDto addPlaceToTrip(Integer tripId, PlaceDto placeDto, Instant datetime, String note) {
//        // Tìm Trip theo ID
//        Optional<Trip> tripOptional = tripRepository.findById(tripId);
//        if (!tripOptional.isPresent()) {
//            throw new IllegalArgumentException("Trip not found");
//        }
//        // Tạo đối tượng Place từ PlaceDto
//        Place place = new Place();
//        place.setNamePlace(placeDto.getNameplace());
//        place.setUrlMap(placeDto.getUrlmap());
//        place.setAddress(placeDto.getAddress());
//        place.setDescription(placeDto.getDescription());
//        Placetrip placeTrip = new Placetrip();
//        placeTrip.setTrip(tripOptional.get());
//        placeTrip.setPlace(place);
//        placeTrip.setDateTime(datetime);
//        placeTrip.setNote(note);
//        placeTrip = placeTripRepository.save(placeTrip);
//        return toPlaceTripDto(placeTrip);
//    }


    // Convert Usertrip entity to UsertripDto
    public UsertripDto toUserTripDto(Usertrip userTrip) {
        UsertripDto dto = new UsertripDto();
        dto.setId(userTrip.getId());
        dto.setTripid(toTripDto(userTrip.getTripid()));
        dto.setUserid(userInfoService.convertToUserDto(userTrip.getUserid()));
        dto.setRole(toTripRoleDto(userTrip.getRole()));
        return dto;
    }


    // Convert Trip entity to TripDto
    public TripDto toTripDto(Trip trip) {
        TripDto dto = new TripDto();
        dto.setId(trip.getId());
        dto.setTripname(trip.getTripname());
        dto.setStartdate(trip.getStartdate());
        dto.setEnddate(trip.getEnddate());
        dto.setCreatedate(trip.getCreatedate());
        dto.setDescription(trip.getDescription());
        return dto;
    }

    // Convert TripDto to Trip entity
    public Trip toTrip(TripDto dto) {
        Trip trip = new Trip();
        trip.setId(dto.getId());
        trip.setTripname(dto.getTripname());
        trip.setStartdate(dto.getStartdate());
        trip.setEnddate(dto.getEnddate());
        trip.setCreatedate(dto.getCreatedate());
        trip.setDescription(dto.getDescription());
        return trip;
    }

    // Convert Placetrip entity to PlacetripDto
    public PlacetripDto toPlaceTripDto(Placetrip placeTrip) {
        PlacetripDto dto = new PlacetripDto();
        dto.setId(placeTrip.getId());
        dto.setPlaceid(toPlaceDto(placeTrip.getPlaceid()));
        dto.setTripid(placeTrip.getTripid());
        dto.setDatetime(placeTrip.getDatetime());
        dto.setNote(placeTrip.getNote());
        return dto;
    }


    // Convert Place entity to PlaceDto
    public PlaceDto toPlaceDto(Place place) {
        PlaceDto dto = new PlaceDto();
        dto.setId(place.getId());
        dto.setNameplace(place.getNameplace());
        dto.setUrlmap(place.getUrlmap());
        dto.setAddress(place.getAddress());
        dto.setDescription(place.getDescription());
        return dto;
    }

    // Convert Triprole entity to TriproleDto
    public TriproleDto toTripRoleDto(Triprole tripRole) {
        TriproleDto dto = new TriproleDto();
        dto.setId(tripRole.getId());
        dto.setRole(tripRole.getRole());
        return dto;
    }

}
