package com.system.fsharksocialmedia.services.admin;

import com.system.fsharksocialmedia.dtos.PlaceDto;
import com.system.fsharksocialmedia.dtos.PlaceimageDto;
import com.system.fsharksocialmedia.entities.Place;
import com.system.fsharksocialmedia.entities.Placeimage;
import com.system.fsharksocialmedia.entities.Post;
import com.system.fsharksocialmedia.models.AdminPlaceModel;
import com.system.fsharksocialmedia.repositories.PlaceRepository;
import com.system.fsharksocialmedia.repositories.PlaceimageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AdminPlaceService {
    @Autowired
    private PlaceRepository placeRepository;
    @Autowired
    private PlaceimageRepository placeimageRepository;

    public List<PlaceDto> getAllPlaces() {
        List<Place> places = placeRepository.findAll();
        return places.stream().map(this::converToDto).collect(Collectors.toList());
    }

    public PlaceDto addPlace(AdminPlaceModel model) {
        try {
            Place place = new Place();
            place.setAddress(model.getAddress());
            place.setDescription(model.getDescription());
            place.setUrlmap(model.getUrlMap());
            place.setNameplace(model.getNamePlace());
            Place savedPlace = placeRepository.save(place);
            for (String image : model.getPlaceImages()) {
                Placeimage placeimage = new Placeimage();
                placeimage.setPlaceid(savedPlace);
                placeimage.setImage(image);
                placeimageRepository.save(placeimage);
            }
            return converToDto(savedPlace);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }


    public PlaceDto converToDto(Place place) {
        PlaceDto p = new PlaceDto();
        p.setId(place.getId());
        p.setNameplace(place.getNameplace());
        p.setAddress(place.getAddress());
        p.setDescription(place.getDescription());
        p.setUrlmap(place.getUrlmap());
        if (place.getPlaceimages() != null) {
            Set<PlaceimageDto> imageDtos = place.getPlaceimages().stream()
                    .map(this::converPlaceImageToDto)
                    .collect(Collectors.toSet());
            p.setPlaceimages(imageDtos);
        }
        return p;
    }

    public PlaceimageDto converPlaceImageToDto(Placeimage placeimage) {
        PlaceimageDto p = new PlaceimageDto();
        p.setId(placeimage.getId());
        p.setImage(placeimage.getImage());
        p.setPlaceid(p.getPlaceid());
        return p;
    }

}
