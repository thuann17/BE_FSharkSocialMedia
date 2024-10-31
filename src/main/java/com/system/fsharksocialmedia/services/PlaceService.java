package com.system.fsharksocialmedia.services;


import com.system.fsharksocialmedia.dtos.PlaceDto;
import com.system.fsharksocialmedia.entities.Place;
import com.system.fsharksocialmedia.repositories.PlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class PlaceService {

    @Autowired
    private PlaceRepository placeRepository;

    // Lấy danh sách các địa điểm, phân trang
    public Page<PlaceDto> getPlaces(int page, int size, String search) {
        PageRequest pageRequest = PageRequest.of(page, size);
        if (search != null && !search.isEmpty()) {
            // Tìm kiếm theo tên hoặc các thuộc tính khác
            return placeRepository.findByNameplaceContaining(search, pageRequest)
                    .map(this::convertToDto);
        } else {
            // Nếu không có từ khóa tìm kiếm thì trả về tất cả
            return placeRepository.findAll(pageRequest)
                    .map(this::convertToDto);
        }
    }

    // Tìm địa điểm theo ID
//    public Optional<PlaceDto> getPlaceById(Integer id) {
//        return placeRepository.findById(id).map(this::convertToDto);
//    }
//
    // Chuyển đổi từ Entity sang DTO
    private PlaceDto convertToDto(Place place) {
        PlaceDto dto = new PlaceDto();
        dto.setId(place.getId());
        dto.setNameplace(place.getNameplace());
        dto.setUrlmap(place.getUrlmap());
        dto.setAddress(place.getAddress());
        dto.setDescription(place.getDescription());
        return dto;
    }
//
//    // Thêm mới một địa điểm
//    public PlaceDto createPlace(PlaceDto placesDto) {
//        Place place = new Place();
//        place.setNameplace(placesDto.getNameplace());
//        place.setLongitude(placesDto.getLongitude());
//        place.setLatitude(placesDto.getLatitude());
//        place.setUrlmap(placesDto.getUrlmap());
//        place.setAddress(placesDto.getAddress());
//        place.setDescription(placesDto.getDescription());
//
//        place = placeRepository.save(place);
//        return convertToDto(place);
//    }
//
//    // Cập nhật thông tin địa điểm
//    public Optional<PlaceDto> updatePlace(Integer id, PlaceDto placesDto) {
//        return placeRepository.findById(id).map(place -> {
//            place.setNameplace(placesDto.getNameplace());
//            place.setLongitude(placesDto.getLongitude());
//            place.setLatitude(placesDto.getLatitude());
//            place.setUrlmap(placesDto.getUrlmap());
//            place.setAddress(placesDto.getAddress());
//            place.setDescription(placesDto.getDescription());
//
//            Place updatedPlace = placeRepository.save(place);
//            return convertToDto(updatedPlace);
//        });
//    }
//
//    // Xóa địa điểm
//    public boolean deletePlace(Integer id) {
//        return placeRepository.findById(id).map(place -> {
//            placeRepository.delete(place);
//            return true;
//        }).orElse(false);
//    }
}
