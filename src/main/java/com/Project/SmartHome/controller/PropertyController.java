package com.Project.SmartHome.controller;

import com.Project.SmartHome.dto.PropertyDto;
import com.Project.SmartHome.entity.Property;
import com.Project.SmartHome.entity.PropertyImage;
import com.Project.SmartHome.entity.PropertyStatus;
import com.Project.SmartHome.Reposatory.PropertyRepository;
import com.Project.SmartHome.Reposatory.PropertyImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PropertyController {

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private PropertyImageRepository propertyImageRepository;

    @GetMapping("/property-test")
    public String test() {
        return "Property Controller is working!";
    }

    @GetMapping("/properties")
    public List<Property> getAllProperties() {
        return propertyRepository.findAll();
    }

    @GetMapping("/property/{id}")
    public Property getPropertyById(@PathVariable Long id) {
        return propertyRepository.findById(id).orElse(null);
    }

    @GetMapping("/properties/vendor/{vendorId}")
    public List<Property> getPropertiesByVendor(@PathVariable Long vendorId) {
        return propertyRepository.findByVendorId(vendorId);
    }

    @GetMapping("/properties/city")
    public List<Property> getPropertiesByCity(@RequestParam String city) {
        return propertyRepository.findAll().stream()
                .filter(p -> city.equalsIgnoreCase(p.getCity()))
                .collect(Collectors.toList());
    }

    @GetMapping("/addProperty")
    public Property addProperty(
            @RequestParam Long vendorId,
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam String propertyType,
            @RequestParam String address,
            @RequestParam String city,
            @RequestParam String zipCode,
            @RequestParam BigDecimal pricePerNight,
            @RequestParam Integer maxGuests) {

        Property newProperty = new Property();
        newProperty.setVendorId(vendorId);
        newProperty.setTitle(title);
        newProperty.setSlug(title.toLowerCase().replace(" ", "-"));
        newProperty.setDescription(description);
        newProperty.setPropertyType(propertyType);
        newProperty.setAddress(address);
        newProperty.setCity(city);
        newProperty.setZipCode(zipCode);
        newProperty.setPricePerNight(pricePerNight);
        newProperty.setMaxGuests(maxGuests);
        newProperty.setStatus(PropertyStatus.active);

        return propertyRepository.save(newProperty);
    }

    @PostMapping("/saveProperty")
    public Property saveProperty(@RequestBody PropertyDto propertyDto) {
        Property newProperty = new Property();
        newProperty.setVendorId(propertyDto.getVendorId());
        newProperty.setTitle(propertyDto.getTitle());
        newProperty.setSlug(propertyDto.getTitle().toLowerCase().replace(" ", "-"));
        newProperty.setDescription(propertyDto.getDescription());
        newProperty.setPropertyType(propertyDto.getPropertyType());
        newProperty.setAddress(propertyDto.getAddress());
        newProperty.setCity(propertyDto.getCity());
        newProperty.setZipCode(propertyDto.getZipCode());
        newProperty.setLatitude(propertyDto.getLatitude());
        newProperty.setLongitude(propertyDto.getLongitude());
        newProperty.setPricePerNight(propertyDto.getPricePerNight());
        newProperty.setMaxGuests(propertyDto.getMaxGuests());
        newProperty.setStatus(PropertyStatus.active);

        return propertyRepository.save(newProperty);
    }

    @PutMapping("/updateProperty/{id}")
    public Property updateProperty(@PathVariable Long id, @RequestBody PropertyDto propertyDto) {
        Property existingProperty = propertyRepository.findById(id).orElse(null);
        if (existingProperty != null) {
            existingProperty.setTitle(propertyDto.getTitle());
            existingProperty.setDescription(propertyDto.getDescription());
            existingProperty.setPricePerNight(propertyDto.getPricePerNight());
            existingProperty.setMaxGuests(propertyDto.getMaxGuests());
            if (propertyDto.getStatus() != null) {
                existingProperty.setStatus(PropertyStatus.valueOf(propertyDto.getStatus()));
            }
            return propertyRepository.save(existingProperty);
        }
        return null;
    }

    @DeleteMapping("/deleteProperty/{id}")
    public String deleteProperty(@PathVariable Long id) {
        propertyRepository.deleteById(id);
        return "Property deleted successfully";
    }

    @GetMapping("/addPropertyImage")
    public PropertyImage addPropertyImage(
            @RequestParam Long propertyId,
            @RequestParam String imageUrl,
            @RequestParam Boolean isPrimary) {

        Property property = propertyRepository.findById(propertyId).orElse(null);
        if (property == null) {
            return null;
        }

        PropertyImage newImage = new PropertyImage();
        newImage.setProperty(property);
        newImage.setImageUrl(imageUrl);
        newImage.setIsPrimary(isPrimary);

        return propertyImageRepository.save(newImage);
    }
}
