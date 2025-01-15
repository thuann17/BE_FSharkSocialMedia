package com.system.fsharksocialmedia.services.user;

import com.system.fsharksocialmedia.dtos.*;
import com.system.fsharksocialmedia.entities.*;
import com.system.fsharksocialmedia.models.PlaceTripModel;
import com.system.fsharksocialmedia.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
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
    private UsertripRepository usertripRepository;

    public PlacetripDto createTrip(String username, int placeId, PlaceTripModel placeTripModel) {
        try {
            Place place = placeRepository.findById(placeId).orElse(null);
            User user = userRepository.findById(username).orElse(null);
            Triprole triprole = triproleRepository.findById(1).orElse(null);

            if (place == null || user == null || triprole == null) {
                throw new IllegalArgumentException("Invalid place, user, or role.");
            }
            Trip newTrip = new Trip();
            newTrip.setTripname(placeTripModel.getTripName());
            newTrip.setStartdate(placeTripModel.getStartDate());
            newTrip.setEnddate(placeTripModel.getEndDate());
            newTrip.setCreatedate(Instant.now());
            newTrip.setDescription(placeTripModel.getDescription());
            Trip savedTrip = tripRepository.save(newTrip);

            Usertrip userTrip = new Usertrip();
            userTrip.setRole(triprole);
            userTrip.setUserid(user);
            userTrip.setId(userTrip.getId());
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
            throw new RuntimeException();
        }
    }

//    public PlacetripDto updateTrip(Integer tripId, String username, int placeId, PlaceTripModel placeTripModel) {
//        try {
//            Trip trip = tripRepository.findById(tripId).orElse(null);
//            Place place = placeRepository.findById(placeId).orElse(null);
//            User user = userRepository.findById(username).orElse(null);
//            Triprole triprole = triproleRepository.findById(1).orElse(null);
//            if (place == null || user == null || triprole == null || trip == null) {
//                throw new IllegalArgumentException("Invalid place, user, role, or trip.");
//            }
//
//            trip.setTripname(placeTripModel.getTripName());
//            trip.setStartdate(placeTripModel.getStartDate());
//            trip.setEnddate(placeTripModel.getEndDate());
//            trip.setDescription(placeTripModel.getDescription());
//            Trip updatedTrip = tripRepository.save(trip);
//
//            Usertrip userTrip = usertripRepository.findByUseridAndTripid(user.getId(), updatedTrip.getId());
//            if (userTrip != null) {
//                userTrip.setRole(triprole);
//                usertripRepository.save(userTrip);
//            }
//
//            Placetrip tripPlace = tripPlaceRepository.findByTripidAndPlaceid(updatedTrip.getId(), place.getId());
//            if (tripPlace != null) {
//                tripPlace.setNote(placeTripModel.getNote()); // Update note if needed
//                tripPlace.setDatetime(Instant.now()); // Update timestamp for the trip-place relation
//                Placetrip updatedTripPlace = tripPlaceRepository.save(tripPlace);
//                return convertToPlaceTripDto(updatedTripPlace);
//            } else {
//                throw new IllegalArgumentException("Trip-place relation not found.");
//            }
//
//        } catch (RuntimeException e) {
//            throw new RuntimeException("Error updating trip.", e);
//        }
//    }


    public List<TripDto> getTripsByUsername(String username) {
        User user = userRepository.findById(username).orElse(null);
        if (user == null) {
            throw new IllegalArgumentException("User not found.");
        }
        List<Trip> trips = tripRepository.findTripsByUsernameAndPlaceId(username);
        return trips.stream().map(this::convertToTripDto).collect(Collectors.toList());
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
