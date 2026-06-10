package com.Project.SmartHome.controller;

import com.Project.SmartHome.dto.ReviewDto;
import com.Project.SmartHome.entity.Booking;
import com.Project.SmartHome.entity.Property;
import com.Project.SmartHome.entity.Review;
import com.Project.SmartHome.entity.User;
import com.Project.SmartHome.Reposatory.BookingRepository;
import com.Project.SmartHome.Reposatory.PropertyRepository;
import com.Project.SmartHome.Reposatory.ReviewRepository;
import com.Project.SmartHome.Reposatory.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @GetMapping("/review-test")
    public String test() {
        return "Review Controller is working!";
    }

    @GetMapping("/reviews")
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    @GetMapping("/review/{id}")
    public Review getReviewById(@PathVariable Long id) {
        return reviewRepository.findById(id).orElse(null);
    }

    @GetMapping("/reviews/property/{propertyId}")
    public List<Review> getReviewsByProperty(@PathVariable Long propertyId) {
        return reviewRepository.findByPropertyId(propertyId);
    }

    @GetMapping("/reviews/customer/{customerId}")
    public List<Review> getReviewsByCustomer(@PathVariable Long customerId) {
        return reviewRepository.findByCustomerId(customerId);
    }

    @GetMapping("/addReview")
    public Review addReview(
            @RequestParam Long customerId,
            @RequestParam Long propertyId,
            @RequestParam Long bookingId,
            @RequestParam Float rating,
            @RequestParam String comment) {

        User customer = userRepository.findById(customerId).orElse(null);
        Property property = propertyRepository.findById(propertyId).orElse(null);
        Booking booking = bookingRepository.findById(bookingId).orElse(null);
        if (customer == null || property == null || booking == null) {
            return null;
        }

        Review newReview = new Review();
        newReview.setCustomer(customer);
        newReview.setProperty(property);
        newReview.setBooking(booking);
        newReview.setRating(rating);
        newReview.setComment(comment);
        newReview.setIsPublished(true);

        return reviewRepository.save(newReview);
    }

    @PostMapping("/saveReview")
    public Review saveReview(@RequestBody ReviewDto reviewDto) {
        User customer = userRepository.findById(reviewDto.getCustomerId()).orElse(null);
        Property property = propertyRepository.findById(reviewDto.getPropertyId()).orElse(null);
        Booking booking = bookingRepository.findById(reviewDto.getBookingId()).orElse(null);
        if (customer == null || property == null || booking == null) {
            return null;
        }

        Review newReview = new Review();
        newReview.setCustomer(customer);
        newReview.setProperty(property);
        newReview.setBooking(booking);
        newReview.setRating(reviewDto.getRating());
        newReview.setComment(reviewDto.getComment());
        newReview.setIsPublished(reviewDto.getIsPublished());

        return reviewRepository.save(newReview);
    }

    @DeleteMapping("/deleteReview/{id}")
    public String deleteReview(@PathVariable Long id) {
        reviewRepository.deleteById(id);
        return "Review deleted successfully";
    }
}
