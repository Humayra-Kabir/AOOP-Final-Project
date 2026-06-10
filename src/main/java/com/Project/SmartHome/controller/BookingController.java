package com.Project.SmartHome.controller;

import com.Project.SmartHome.dto.BookingDto;
import com.Project.SmartHome.entity.Booking;
import com.Project.SmartHome.entity.BookingStatus;
import com.Project.SmartHome.entity.Property;
import com.Project.SmartHome.entity.User;
import com.Project.SmartHome.Reposatory.BookingRepository;
import com.Project.SmartHome.Reposatory.PropertyRepository;
import com.Project.SmartHome.Reposatory.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
public class BookingController {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @GetMapping("/booking-test")
    public String test() {
        return "Booking Controller is working!";
    }

    @GetMapping("/bookings")
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @GetMapping("/booking/{id}")
    public Booking getBookingById(@PathVariable Long id) {
        return bookingRepository.findById(id).orElse(null);
    }

    @GetMapping("/booking/reference")
    public Booking getBookingByReference(@RequestParam String reference) {
        return bookingRepository.findAll().stream()
                .filter(b -> reference.equals(b.getBookingReference()))
                .findFirst()
                .orElse(null);
    }

    @GetMapping("/bookings/customer/{customerId}")
    public List<Booking> getBookingsByCustomer(@PathVariable Long customerId) {
        return bookingRepository.findByCustomerId(customerId);
    }

    @GetMapping("/bookings/property/{propertyId}")
    public List<Booking> getBookingsByProperty(@PathVariable Long propertyId) {
        return bookingRepository.findByPropertyId(propertyId);
    }

    @GetMapping("/addBooking")
    public Booking addBooking(
            @RequestParam Long customerId,
            @RequestParam Long propertyId,
            @RequestParam String checkInDate,
            @RequestParam String checkOutDate,
            @RequestParam Integer guestCount,
            @RequestParam BigDecimal totalPrice) {

        User customer = userRepository.findById(customerId).orElse(null);
        Property property = propertyRepository.findById(propertyId).orElse(null);
        if (customer == null || property == null) {
            return null;
        }

        Booking newBooking = new Booking();
        newBooking.setCustomer(customer);
        newBooking.setProperty(property);
        newBooking.setCheckInDate(LocalDate.parse(checkInDate));
        newBooking.setCheckOutDate(LocalDate.parse(checkOutDate));
        newBooking.setGuestCount(guestCount);
        newBooking.setTotalPrice(totalPrice);
        newBooking.setStatus(BookingStatus.pending);
        newBooking.setBookingReference("BK" + System.currentTimeMillis());

        return bookingRepository.save(newBooking);
    }

    @PostMapping("/saveBooking")
    public Booking saveBooking(@RequestBody BookingDto bookingDto) {
        User customer = userRepository.findById(bookingDto.getCustomerId()).orElse(null);
        Property property = propertyRepository.findById(bookingDto.getPropertyId()).orElse(null);
        if (customer == null || property == null) {
            return null;
        }

        Booking newBooking = new Booking();
        newBooking.setCustomer(customer);
        newBooking.setProperty(property);
        newBooking.setCheckInDate(bookingDto.getCheckInDate());
        newBooking.setCheckOutDate(bookingDto.getCheckOutDate());
        newBooking.setGuestCount(bookingDto.getGuestCount());
        newBooking.setBasePrice(bookingDto.getBasePrice());
        newBooking.setPlatformFee(bookingDto.getPlatformFee());
        newBooking.setTotalPrice(bookingDto.getTotalPrice());
        newBooking.setStatus(BookingStatus.pending);
        newBooking.setBookingReference("BK" + System.currentTimeMillis());

        return bookingRepository.save(newBooking);
    }

    @PutMapping("/updateBookingStatus/{id}")
    public Booking updateBookingStatus(@PathVariable Long id, @RequestParam String status) {
        Booking existingBooking = bookingRepository.findById(id).orElse(null);
        if (existingBooking != null) {
            existingBooking.setStatus(BookingStatus.valueOf(status));
            return bookingRepository.save(existingBooking);
        }
        return null;
    }

    @DeleteMapping("/cancelBooking/{id}")
    public String cancelBooking(@PathVariable Long id) {
        Booking booking = bookingRepository.findById(id).orElse(null);
        if (booking != null) {
            booking.setStatus(BookingStatus.cancelled);
            bookingRepository.save(booking);
            return "Booking cancelled successfully";
        }
        return "Booking not found";
    }
}
