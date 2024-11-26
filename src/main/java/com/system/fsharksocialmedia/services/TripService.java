package com.system.fsharksocialmedia.services;

import com.system.fsharksocialmedia.dtos.*;
import com.system.fsharksocialmedia.entities.*;
import com.system.fsharksocialmedia.models.TripModel;
import com.system.fsharksocialmedia.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
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
    public TripDto createTrip(TripModel tripModel, String username) {
        Trip trip = new Trip();
        User u = userRepository.findByUsername(username).orElse(null);
        Triprole role = triproleRepository.findById(1).orElse(null);
        if (tripModel.getTripName() == null || tripModel.getTripName().isEmpty()) {
            trip.setTripname("Chuyến đi mới");
        } else {
            trip.setTripname(tripModel.getTripName());
        }
        trip.setStartdate(tripModel.getStartDate());
        trip.setEnddate(tripModel.getEndDate());
        trip.setCreatedate(Instant.now());
        trip.setDescription(tripModel.getDescription());
        Trip savedTrip = tripRepository.save(trip);
        Usertrip userTrip = new Usertrip();
        userTrip.setTripid(savedTrip);
        userTrip.setUserid(u);
        userTrip.setRole(role);
        usertripRepository.save(userTrip);
        return toTripDto(savedTrip);
    }

    public String requestJoinTrip(Integer tripId, String username) {
        Optional<Trip> tripOptional = tripRepository.findById(tripId);
        if (tripOptional.isEmpty()) {
            return "Chuyến đi không tồn tại!";
        }
        Usertrip userTrip = new Usertrip();
        userTrip.setTripid(tripOptional.get());
        User u = userRepository.findByUsername(username).orElse(null);
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
//            userTrip.setStatus("APPROVED");
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

    // 5. Lấy danh sách chuyến đi của người dùng
    public List<TripDto> getTripsByUser(String username) {
        List<Usertrip> userTrips = usertripRepository.findByUserid_Username(username);
        return userTrips.stream()
                .map(userTrip -> toTripDto(userTrip.getTripid()))
                .collect(Collectors.toList());
    }

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
