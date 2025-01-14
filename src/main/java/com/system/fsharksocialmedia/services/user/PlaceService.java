package com.system.fsharksocialmedia.services.user;

import com.system.fsharksocialmedia.dtos.PlaceDto;
import com.system.fsharksocialmedia.dtos.PlaceimageDto;
import com.system.fsharksocialmedia.entities.Place;
import com.system.fsharksocialmedia.entities.Placeimage;
import com.system.fsharksocialmedia.repositories.PlaceRepository;
import com.system.fsharksocialmedia.repositories.PlaceimageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PlaceService {
    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private PlaceimageRepository placeimageRepository;

    public PlaceDto convertToDto(Place place) {
        if (place == null) {
            return null; // Xử lý khi đối tượng null
        }

        PlaceDto dto = new PlaceDto();
        dto.setId(place.getId());
        dto.setNameplace(place.getNameplace());
        dto.setUrlmap(place.getUrlmap());
        dto.setAddress(place.getAddress());
        dto.setDescription(place.getDescription());

        return dto;
    }

    @Transactional
    public List<PlaceDto> getPlaceDetailsWithImages(String addressFilter) {
        if (addressFilter == null || addressFilter.trim().isEmpty()) {
            // Handle case where addressFilter is null or empty
            return Collections.emptyList();
        }

        // Fetch results from the repository using a stored procedure or custom query
        List<Object[]> results = placeRepository.getPlaceDetailsWithImages();

        return results.stream()
                .filter(result -> {
                    // Extract address from the result (ensure the correct index for address)
                    String address = (String) result[2];  // Check index based on your query result
                    return address != null && address.contains(addressFilter);
                })
                .map(result -> {
                    // Create a new PlaceDto to hold the place details
                    PlaceDto placeDto = new PlaceDto();
                    Set<PlaceimageDto> placeImageDtos = new HashSet<>();

                    // Map fields from result array to PlaceDto
                    placeDto.setId((Integer) result[0]);
                    placeDto.setNameplace((String) result[1]);
                    placeDto.setAddress((String) result[2]);
                    placeDto.setDescription((String) result[3]);

                    // Fetch images for the place from the repository
                    Place place = new Place();
                    place.setId(placeDto.getId());
                    List<Placeimage> placeImages = placeimageRepository.findByPlaceid(place);


                    for (Placeimage placeImage : placeImages) {
                        PlaceimageDto placeimageDto = new PlaceimageDto();
                        placeimageDto.setId(placeImage.getId());
                        placeimageDto.setPlaceid(convertToDto(placeImage.getPlaceid())); // Convert Place to PlaceDto
                        placeimageDto.setImage(placeImage.getImage());

                        placeImageDtos.add(placeimageDto);
                    }

                    placeDto.setPlaceimages(placeImageDtos);
                    return placeDto;
                })
                .collect(Collectors.toList());
    }


}
