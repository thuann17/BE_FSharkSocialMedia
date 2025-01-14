package com.system.fsharksocialmedia.services.user;

import com.system.fsharksocialmedia.dtos.*;
import com.system.fsharksocialmedia.entities.*;
import com.system.fsharksocialmedia.models.TripModel;
import com.system.fsharksocialmedia.repositories.*;
import com.system.fsharksocialmedia.services.other.UserInfoService;
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
    private UserInfoService userInfoService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UsertripRepository usertripRepository;
    @Autowired
    private TriproleRepository triproleRepository;
    @Autowired
    private PlacetripRepository placetripRepository;
    @Autowired
    private PlaceimageRepository placetimageRepository;
    @Autowired
    private PlaceRepository placeRepository;

    public List<TripDto> getTripsByUsername(String username) {
        User user = userRepository.findById(username).orElse(null);

        List<Trip> trip = tripRepository.findTripsByUsernameAndPlaceId(username);
        return trip.stream().map(this::convertToTripDto).collect(Collectors.toList());
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
        PlaceimageDto placeimage = new PlaceimageDto();
        placeimage.setId(image.getId());
        placeimage.setImage(image.getImage());
        return placeimage;
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
            List<ImageDto> imageDtos = new ArrayList<>();
            for (Image image : user.getImages()) {
                ImageDto imageDto = convertToImageDto(image);
                imageDtos.add(imageDto);
            }
            userDto.setImages(imageDtos);
        }
        return userDto;
    }

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
            List<UserDto> userDtos = new ArrayList<>();
            for (Usertrip usertrip : trip.getUsertrips()) {
                if (usertrip.getUserid() != null) {
                    User user = usertrip.getUserid();
                    UserDto userDto = new UserDto();
                    userDto.setFirstname(user.getFirstname());
                    userDto.setLastname(user.getLastname());
                    if (user.getImages() != null && !user.getImages().isEmpty()) {
                        List<ImageDto> imageDtos = new ArrayList<>();
                        for (Image image : user.getImages()) {
                            ImageDto imageDto = convertToImageDto(image);
                            imageDtos.add(imageDto);
                        }
                        userDto.setImages(imageDtos);
                    }
                    userDtos.add(userDto);
                }
            }
            tripDto.setUsers(userDtos);
        }
        // Chuyển đổi danh sách Place (PlaceDto)
        if (trip.getPlacetrips() != null && !trip.getPlacetrips().isEmpty()) {
            List<PlaceDto> placeDtos = new ArrayList<>();
            for (Placetrip placetrip : trip.getPlacetrips()) {
                if (placetrip.getPlaceid() != null) {
                    Place place = placetrip.getPlaceid();
                    PlaceDto placeDto = new PlaceDto();
                    placeDto.setId(place.getId());
                    placeDto.setNameplace(place.getNameplace());
                    placeDto.setUrlmap(place.getUrlmap());
                    placeDto.setAddress(place.getAddress());
                    placeDto.setDescription(place.getDescription());
                    placeDtos.add(placeDto);
                    if (place.getPlaceimages() != null && !place.getPlaceimages().isEmpty()) {
                        Set<PlaceimageDto> imageDtos = new LinkedHashSet<>();  // Sử dụng Set thay vì List
                        for (Placeimage image : place.getPlaceimages()) {
                            PlaceimageDto imageDto = convertToPlaceImageDto(image);
                            imageDtos.add(imageDto);
                        }
                        placeDto.setPlaceimages(imageDtos);
                    }
                }
            }

            tripDto.setPlaces(placeDtos);
        }

        // Chuyển đổi danh sách Placetrip (PlacetripDto)
        if (trip.getPlacetrips() != null && !trip.getPlacetrips().isEmpty()) {
            List<PlacetripDto> placetripDtos = new ArrayList<>();
            for (Placetrip placetrip : trip.getPlacetrips()) {
                PlacetripDto placetripDto = new PlacetripDto();
                placetripDto.setId(placetrip.getId());
                placetripDto.setDatetime(placetrip.getDatetime());
                placetripDto.setNote(placetrip.getNote());

                // Thêm thông tin Place vào PlacetripDto
                if (placetrip.getPlaceid() != null) {
                    PlaceDto placeDto = new PlaceDto();
                    placeDto.setId(placetrip.getPlaceid().getId());
                    placeDto.setNameplace(placetrip.getPlaceid().getNameplace());
                    placetripDto.setPlaceid(placeDto);
                }

                placetripDtos.add(placetripDto);
            }
            tripDto.setPlaceTrips(placetripDtos);
        }
        return tripDto;
    }

}
