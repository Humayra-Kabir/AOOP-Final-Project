package com.Project.SmartHome.controller;

import com.Project.SmartHome.dto.PaymentDto;
import com.Project.SmartHome.entity.Booking;
import com.Project.SmartHome.entity.Payment;
import com.Project.SmartHome.entity.PaymentStatus;
import com.Project.SmartHome.Reposatory.BookingRepository;
import com.Project.SmartHome.Reposatory.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@RestController
public class PaymentController {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @GetMapping("/payment-test")
    public String test() {
        return "Payment Controller is working!";
    }

    @GetMapping("/payments")
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    @GetMapping("/payment/{id}")
    public Payment getPaymentById(@PathVariable Long id) {
        return paymentRepository.findById(id).orElse(null);
    }

    @GetMapping("/payments/booking/{bookingId}")
    public List<Payment> getPaymentsByBooking(@PathVariable Long bookingId) {
        return paymentRepository.findByBookingId(bookingId)
                .map(Collections::singletonList)
                .orElse(Collections.emptyList());
    }

    @GetMapping("/addPayment")
    public Payment addPayment(
            @RequestParam Long bookingId,
            @RequestParam BigDecimal amount,
            @RequestParam String currency,
            @RequestParam String paymentGateway,
            @RequestParam String transactionId) {

        Booking booking = bookingRepository.findById(bookingId).orElse(null);
        if (booking == null) {
            return null;
        }

        Payment newPayment = new Payment();
        newPayment.setBooking(booking);
        newPayment.setAmount(amount);
        newPayment.setCurrency(currency);
        newPayment.setPaymentGateway(paymentGateway);
        newPayment.setTransactionId(transactionId);
        newPayment.setStatus(PaymentStatus.succeeded);

        return paymentRepository.save(newPayment);
    }

    @PostMapping("/savePayment")
    public Payment savePayment(@RequestBody PaymentDto paymentDto) {
        Booking booking = bookingRepository.findById(paymentDto.getBookingId()).orElse(null);
        if (booking == null) {
            return null;
        }

        Payment newPayment = new Payment();
        newPayment.setBooking(booking);
        newPayment.setAmount(paymentDto.getAmount());
        newPayment.setCurrency(paymentDto.getCurrency());
        newPayment.setPaymentGateway(paymentDto.getPaymentGateway());
        newPayment.setTransactionId(paymentDto.getTransactionId());
        if (paymentDto.getStatus() != null) {
            newPayment.setStatus(PaymentStatus.valueOf(paymentDto.getStatus()));
        }

        return paymentRepository.save(newPayment);
    }

    @PutMapping("/updatePaymentStatus/{id}")
    public Payment updatePaymentStatus(@PathVariable Long id, @RequestParam String status) {
        Payment existingPayment = paymentRepository.findById(id).orElse(null);
        if (existingPayment != null) {
            existingPayment.setStatus(PaymentStatus.valueOf(status));
            return paymentRepository.save(existingPayment);
        }
        return null;
    }
}
