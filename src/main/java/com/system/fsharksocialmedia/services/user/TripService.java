package com.system.fsharksocialmedia.services.user;

import com.system.fsharksocialmedia.dtos.*;
import com.system.fsharksocialmedia.entities.*;
import com.system.fsharksocialmedia.models.PlaceTripModel;
import com.system.fsharksocialmedia.models.TripModel;
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

    public PlacetripDto convertToPlaceTripDto(Placetrip tripPlace) {
        PlacetripDto tripPlaceDto = new PlacetripDto();
        tripPlaceDto.setId(tripPlace.getId());
        tripPlaceDto.setTripid(tripPlace.getTripid());
        tripPlaceDto.setDatetime(tripPlace.getDatetime());
        tripPlaceDto.setNote(tripPlace.getNote());
        return tripPlaceDto;
    }

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

    public PlaceimageDto convertToPlaceImageDto(Placeimage image) {
        PlaceimageDto placeimageDto = new PlaceimageDto();
        placeimageDto.setId(image.getId());
        placeimageDto.setImage(image.getImage());
        return placeimageDto;
    }

    public UserDto convertToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setPassword(user.getPassword());
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

    // Convert Usertrip entity to UsertripDto
    public UsertripDto convertToUsertripDto(Usertrip usertrip) {
        UsertripDto usertripDto = new UsertripDto();
        usertripDto.setId(usertrip.getId());
        usertripDto.setTripid(usertrip.getTripid() != null ? convertToTripDto(usertrip.getTripid()) : null);
        usertripDto.setUserid(usertrip.getUserid() != null ? convertToUserDto(usertrip.getUserid()) : null);
        return usertripDto;
    }

    public TripDto convertToTripDto(Trip trip) {
        TripDto tripDto = new TripDto();
        tripDto.setId(trip.getId());
        tripDto.setTripname(trip.getTripname());
        tripDto.setStartdate(trip.getStartdate());
        tripDto.setEnddate(trip.getEnddate());
        tripDto.setCreatedate(trip.getCreatedate());
        tripDto.setDescription(trip.getDescription());

        if (trip.getUsertrips() != null && !trip.getUsertrips().isEmpty()) {
            List<UserDto> userDtos = trip.getUsertrips().stream()
                    .map(usertrip -> convertToUserDto(usertrip.getUserid()))
                    .collect(Collectors.toList());
            tripDto.setUsers(userDtos);
        }

        if (trip.getPlacetrips() != null && !trip.getPlacetrips().isEmpty()) {
            List<PlaceDto> placeDtos = trip.getPlacetrips().stream()
                    .map(placetrip -> {
                        PlaceDto placeDto = new PlaceDto();
                        placeDto.setId(placetrip.getPlaceid().getId());
                        placeDto.setNameplace(placetrip.getPlaceid().getNameplace());
                        placeDto.setUrlmap(placetrip.getPlaceid().getUrlmap());
                        placeDto.setAddress(placetrip.getPlaceid().getAddress());
                        placeDto.setDescription(placetrip.getPlaceid().getDescription());

                        // Map place images
                        Set<PlaceimageDto> placeImages = placetrip.getPlaceid().getPlaceimages().stream()
                                .map(this::convertToPlaceImageDto)
                                .collect(Collectors.toSet());
                        placeDto.setPlaceimages(placeImages);

                        return placeDto;
                    })
                    .collect(Collectors.toList());
            tripDto.setPlaces(placeDtos);
        }
        if (trip.getPlacetrips() != null && !trip.getPlacetrips().isEmpty()) {
            List<PlacetripDto> placetripDtos = trip.getPlacetrips().stream()
                    .map(this::convertToPlaceTripDto)
                    .collect(Collectors.toList());
            tripDto.setPlaceTrips(placetripDtos);
        }

        return tripDto;
    }
}
