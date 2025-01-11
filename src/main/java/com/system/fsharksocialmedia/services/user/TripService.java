package com.system.fsharksocialmedia.services.user;

import com.system.fsharksocialmedia.dtos.*;
import com.system.fsharksocialmedia.entities.*;
import com.system.fsharksocialmedia.models.TripModel;
import com.system.fsharksocialmedia.repositories.*;
import com.system.fsharksocialmedia.services.other.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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

    // 1. Tạo chuyến đi mới
    public void addTripDetails(TripModel tripModel) {
        // Convert TripDto to Trip entity
        Trip trip = new Trip();
        trip.setTripname(tripModel.getTripDto().getTripname());
        trip.setStartdate(tripModel.getTripDto().getStartdate());
        trip.setEnddate(tripModel.getTripDto().getEnddate());
        trip.setCreatedate(tripModel.getTripDto().getCreatedate());
        trip.setDescription(tripModel.getTripDto().getDescription());

        // Save the Trip entity
        tripRepository.save(trip);

        // Convert PlacetripDto to Placetrip entity
        Placetrip placetrip = new Placetrip();
        PlaceDto placeDto = tripModel.getPlacetripDto().getPlaceid();

        if (placeDto != null) {
            Place place = new Place();
            place.setId(placeDto.getId());
            placetrip.setPlaceid(place);
        } else {
            throw new IllegalArgumentException("PlaceDto cannot be null");
        }

        placetrip.setTripid(trip); // Link the Placetrip to the saved Trip
        placetrip.setDatetime(tripModel.getPlacetripDto().getDatetime());
        placetrip.setNote(tripModel.getPlacetripDto().getNote());

        // Save the Placetrip entity
        placetripRepository.save(placetrip);

        // Convert UsertripDto to Usertrip entity
        Usertrip usertrip = new Usertrip();
        usertrip.setTripid(trip); // Link the Usertrip to the saved Trip

// Mapping UserDto to User entity
        UserDto userDto = tripModel.getUsertripDto().getUserid();
        if (userDto != null) {
            User user = new User();

            // Map other fields of UserDto to User entity if necessary
            usertrip.setUserid(user);
        } else {
            // Handle null userDto, throw exception or log error
            throw new IllegalArgumentException("User cannot be null");
        }
        TriproleDto roleDto = tripModel.getUsertripDto().getRole();
        if (roleDto != null) {
            Triprole role = new Triprole();
            role.setId(roleDto.getId());

            usertrip.setRole(role);
        } else {

            throw new IllegalArgumentException("Role cannot be null");
        }

        usertripRepository.save(usertrip);
    }

    public String requestJoinTrip(Integer tripId, String username) {
        Optional<Trip> tripOptional = tripRepository.findById(tripId);
        if (tripOptional.isEmpty()) {
            return "Chuyến đi không tồn tại!";
        }
        Usertrip userTrip = new Usertrip();
        userTrip.setTripid(tripOptional.get());
        User u = userRepository.findById(username).orElse(null);
        Triprole role = triproleRepository.findById(2).orElse(null);
        userTrip.setUserid(u);
        userTrip.setRole(role);
        usertripRepository.save(userTrip);
        return "Yêu cầu tham gia đã được gửi!";
    }

    public String approveOrRejectRequest(Integer userTripId, boolean isApproved) {
        Optional<Usertrip> userTripOptional = usertripRepository.findById(userTripId);
        if (userTripOptional.isEmpty()) {
            return "Yêu cầu không tồn tại!";
        }
        Usertrip userTrip = userTripOptional.get();
        if (isApproved) {
            usertripRepository.save(userTrip);
            return "Yêu cầu đã được phê duyệt!";
        } else {
            usertripRepository.delete(userTrip);
            return "Yêu cầu đã bị từ chối!";
        }
    }

    // Convert Usertrip entity to UsertripDto
    public UsertripDto toUserTripDto(Usertrip userTrip) {
        UsertripDto dto = new UsertripDto();
        dto.setId(userTrip.getId());
        dto.setTripid(toTripDto(userTrip.getTripid()));
        dto.setUserid(userInfoService.convertToUserDto(userTrip.getUserid()));
        dto.setRole(toTripRoleDto(userTrip.getRole()));
        return dto;
    }

    public PlacetripDto recordPlace(Integer tripId, Integer placeId, Instant dateTime, String note) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found"));
        Place place = new Place(); // Thêm phần tìm kiếm hoặc khởi tạo Place
        place.setId(placeId);

        Placetrip placeTrip = new Placetrip();
        placeTrip.setTripid(trip);
        placeTrip.setPlaceid(place);
        placeTrip.setDatetime(dateTime);
        placeTrip.setNote(note);

        Placetrip savedPlaceTrip = placetripRepository.save(placeTrip);
        return toPlaceTripDto(savedPlaceTrip);
    }

//    // 5. Lấy danh sách chuyến đi của người dùng
//    public TripDetailsDto getTripDetailsByUsername(String username) {
//        // Fetch user by username
//        User user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        // Fetch user trips related to the user
//        List<Usertrip> userTrips = usertripRepository.findByUserid(user);
//
//        if (userTrips.isEmpty()) {
//            throw new RuntimeException("No trips found for user " + username);
//        }
//
//        // Fetch trips associated with the user
//        List<Integer> tripIds = userTrips.stream()
//                .map(usertrip -> usertrip.getTripid().getId())
//                .distinct()
//                .collect(Collectors.toList());
//
//        // Fetch trip details by tripIds
//        List<Trip> trips = tripRepository.findAllById(tripIds);
//
//        // Fetch places related to each trip
//        List<Placetrip> placeTrips = placetripRepository.findByTripid_IdIn(tripIds);
//
//        // Map all the fetched data to the corresponding DTOs
//        List<TripDto> tripDtos = trips.stream()
//                .map(this::toTripDto)
//                .collect(Collectors.toList());
//
//        List<UsertripDto> usertripDtos = userTrips.stream()
//                .map(this::toUserTripDto)
//                .collect(Collectors.toList());
//
//        List<PlacetripDto> placetripDtos = placeTrips.stream()
//                .map(this::toPlaceTripDto)
//                .collect(Collectors.toList());
//
//        // Create and return a combined DTO containing all the necessary information
//        TripDetailsDto tripDetailsDto = new TripDetailsDto();
//        tripDetailsDto.setTrip((TripDto) tripDtos);  // Assuming you modify TripDetailsDto to store multiple trips
//        tripDetailsDto.setUserTrips(usertripDtos);
//        tripDetailsDto.setPlacetrips(placetripDtos);
//        return tripDetailsDto;
//    }

    // 6. Hoàn thành chuyến đi
    public TripDto completeTrip(Integer tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found"));
//        trip.setStatus("COMPLETED");
        Trip savedTrip = tripRepository.save(trip);
        return toTripDto(savedTrip);
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
