package com.system.fsharksocialmedia.services.user;

import com.system.fsharksocialmedia.dtos.*;
import com.system.fsharksocialmedia.entities.*;
import com.system.fsharksocialmedia.exceptions.TripOverlapException;
import com.system.fsharksocialmedia.models.PlaceTripModel;
import com.system.fsharksocialmedia.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TripService {

    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PlacetripRepository tripPlaceRepository;
    @Autowired
    private PlaceRepository placeRepository;
    @Autowired
    private TriproleRepository triproleRepository;
    @Autowired
    private UsertripRepository userTripRepository;
    @Autowired
    private PlacetripRepository placeTripRepository;
    @Autowired
    private UsertripRepository usertripRepository;

    public PlacetripDto createTrip(String username, int placeId, PlaceTripModel placeTripModel) {
        try {
            Place place = placeRepository.findById(placeId).orElse(null);
            User user = userRepository.findById(username).orElse(null);
            Triprole triprole = triproleRepository.findById(1).orElse(null);

            if (place == null || user == null || triprole == null) {
                throw new IllegalArgumentException("Địa điểm, người dùng hoặc vai trò không hợp lệ.");
            }
            Trip newTrip = new Trip();
            List<Trip> existingTrips = tripRepository.findTripsByDateRange(
                    placeTripModel.getStartDate(),
                    placeTripModel.getEndDate()
            );
            if (!existingTrips.isEmpty()) {
                String errorMessage = "Đã có một chuyến đi trong khoảng thời gian này.";
                System.err.println(errorMessage);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
            }
            newTrip.setTripname(placeTripModel.getTripName());
            newTrip.setStartdate(placeTripModel.getStartDate());
            newTrip.setEnddate(placeTripModel.getEndDate());
            newTrip.setCreatedate(Instant.now());
            newTrip.setDescription(placeTripModel.getDescription());
            Trip savedTrip = tripRepository.save(newTrip);

            Usertrip userTrip = new Usertrip();
            userTrip.setRole(triprole);
            userTrip.setUserid(user);
            userTrip.setTripid(savedTrip);
            usertripRepository.save(userTrip);

            Placetrip tripPlace = new Placetrip();
            tripPlace.setDatetime(Instant.now());
            tripPlace.setNote(placeTripModel.getNote());
            tripPlace.setPlaceid(place);
            tripPlace.setTripid(savedTrip);
            Placetrip savedTripPlace = tripPlaceRepository.save(tripPlace);
            return convertToPlaceTripDto(savedTripPlace);
        } catch (RuntimeException e) {
            throw new RuntimeException("Lỗi khi tạo chuyến đi", e);
        }
    }

    public TripDto updateTrip(Integer tripId, PlaceTripModel placeTripModel) {
        try {
            Trip trip = tripRepository.findById(tripId).orElse(null);
            if(placeTripModel.getStartDate() == null){

            }
            if (placeTripModel.getStartDate() != null) {
                trip.setStartdate(placeTripModel.getStartDate());
            }

            if (placeTripModel.getEndDate() != null) {
                trip.setEnddate(placeTripModel.getEndDate());
            }
            trip.setDescription(placeTripModel.getDescription());
            Trip savedTrip = tripRepository.save(trip);
            return convertToTripDto(savedTrip);
        } catch (RuntimeException e) {
            throw new RuntimeException("Error updating trip.", e);
        }
    }


    public List<TripDto> getTripsByUsername(String username) {
        User user = userRepository.findById(username).orElse(null);
        if (user == null) {
            throw new IllegalArgumentException("User not found.");
        }
        List<Trip> trips = tripRepository.findTripsByUsernameAndPlaceId(username);
        return trips.stream().map(this::convertToTripDto).collect(Collectors.toList());
    }

    @Transactional
    public String deleteTrip(Integer tripID) {
        try {
            System.out.println("Deleting trip " + tripID);
            Trip trip = tripRepository.findById(tripID)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy chuyến đi với ID: " + tripID));
            Usertrip usertrip = userTripRepository.findUsertripByTripid(trip);
            Placetrip tripPlace = tripPlaceRepository.findPlacetripByTripid(trip);
            userTripRepository.delete(usertrip);
            placeTripRepository.delete(tripPlace);
            tripRepository.delete(trip);
            return "success";
        } catch (RuntimeException e) {
            System.err.println("Error while deleting trip: " + e.getMessage());
            throw new RuntimeException("Lỗi khi xóa chuyến đi: " + tripID, e);
        }
    }

    @Transactional
    public List<TripDto> getTripStartDates() {
        List<Object[]> results = tripRepository.getTripStartDates();
        List<TripDto> tripDtos = new ArrayList<>();

        for (Object[] result : results) {
            TripDto tripDto = new TripDto();
            tripDto.setTripid((Integer) result[0]);
            tripDto.setTripname((String) result[1]);

            // Convert Timestamp to Instant
            tripDto.setStartdate(((Timestamp) result[2]).toInstant());
//            tripDto.setEnddate(((Timestamp) result[3]).toInstant());
            tripDtos.add(tripDto);
        }

        return tripDtos;
    }


    public ImageDto convertToImageDto(Image image) {
        ImageDto imageDto = new ImageDto();
        imageDto.setId(image.getId());
        imageDto.setImage(image.getImage());
        imageDto.setCreatedate(image.getCreatedate());
        imageDto.setAvatarrurl(image.getAvatarrurl());
        imageDto.setCoverurl(image.getCoverurl());
        imageDto.setStatus(image.getStatus());
        return imageDto;
    }

    public UserDto convertToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setLastname(user.getLastname());
        userDto.setFirstname(user.getFirstname());
        userDto.setActive(user.getActive());
        userDto.setBio(user.getBio());
        userDto.setHometown(user.getHometown());
        userDto.setCurrency(user.getCurrency());
        userDto.setGender(user.getGender());
        userDto.setBirthday(user.getBirthday());
        if (user.getRoles() != null) {
            UserroleDto userroleDto = new UserroleDto();
            userroleDto.setId(user.getRoles().getId());
            userroleDto.setRole(user.getRoles().getRole());
            userDto.setRoles(userroleDto);
        }
        if (user.getImages() != null && !user.getImages().isEmpty()) {
            List<ImageDto> imageDtos = user.getImages().stream()
                    .map(this::convertToImageDto)
                    .collect(Collectors.toList());
            userDto.setImages(imageDtos);
        }
        return userDto;
    }

    public TripDto convertToTripDto(Trip trip) {
        TripDto tripDto = new TripDto();
        tripDto.setTripid(trip.getId());
        tripDto.setTripname(trip.getTripname());
        tripDto.setStartdate(trip.getStartdate());
        tripDto.setEnddate(trip.getEnddate());
        tripDto.setCreatedate(trip.getCreatedate());
        tripDto.setDescription(trip.getDescription());
        Set<UsertripDto> usertripDtos = new HashSet<>();
        for (Usertrip usertrip : trip.getUsertrips()) {
            UsertripDto usertripDto = new UsertripDto();
            usertripDto.setId(usertrip.getId());
            usertripDto.setUserid(convertToUserDto(usertrip.getUserid()));
            usertripDtos.add(usertripDto);
        }
        tripDto.setUsertrips(usertripDtos);
        Set<PlacetripDto> placetripDtos = new HashSet<>();
        for (Placetrip placetrip : trip.getPlacetrips()) {
            PlacetripDto placetripDto = new PlacetripDto();
            placetripDto.setId(placetrip.getId());
            placetripDto.setPlaceid(convertToPlaceDto(placetrip.getPlaceid()));
            TripDto associatedTripDto = new TripDto();
            associatedTripDto.setTripname(placetrip.getTripid().getTripname());
            associatedTripDto.setStartdate(placetrip.getTripid().getStartdate());
            associatedTripDto.setEnddate(placetrip.getTripid().getEnddate());
            placetripDto.setTripid(associatedTripDto);
            placetripDto.setDatetime(placetrip.getDatetime());
            placetripDto.setNote(placetrip.getNote());
            placetripDtos.add(placetripDto);
        }
        tripDto.setPlacetrips(placetripDtos);

        return tripDto;
    }

    public PlacetripDto convertToPlaceTripDto(Placetrip tripPlace) {
        PlacetripDto tripPlaceDto = new PlacetripDto();
        tripPlaceDto.setId(tripPlace.getId());
        tripPlaceDto.setDatetime(tripPlace.getDatetime());
        tripPlaceDto.setNote(tripPlace.getNote());
        return tripPlaceDto;
    }

    public PlaceimageDto convertToPlaceImageDto(Placeimage image) {
        PlaceimageDto placeimageDto = new PlaceimageDto();
        placeimageDto.setId(image.getId());
        placeimageDto.setImage(image.getImage());

        return placeimageDto;
    }

    public PlaceDto convertToPlaceDto(Place place) {
        PlaceDto placeDto = new PlaceDto();
        placeDto.setId(place.getId());
        placeDto.setNameplace(place.getNameplace());
        placeDto.setUrlmap(place.getUrlmap());
        placeDto.setAddress(place.getAddress());
        placeDto.setDescription(place.getDescription());
        Set<PlaceimageDto> placeImages = new HashSet<>();
        for (Placeimage placeImage : place.getPlaceimages()) {
            placeImages.add(convertToPlaceImageDto(placeImage));
        }
        placeDto.setPlaceimages(placeImages);

        return placeDto;
    }

}
