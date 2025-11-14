package com.workline.controller;

import com.workline.model.Booking;
import com.workline.repository.BookingRepository;
import com.workline.repository.UserRepository;
import com.workline.repository.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    
    @Autowired
    private BookingRepository bookingRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private WorkerRepository workerRepository;
    
    @PostMapping
    public Booking createBooking(@RequestBody Booking booking) {
        booking.setStatus(Booking.Status.CONFIRMED);
        return bookingRepository.save(booking);
    }
    
    @GetMapping("/user/{userId}")
    public List<Booking> getUserBookings(@PathVariable Long userId) {
        return bookingRepository.findByUser_Id(userId);
    }
    
    @GetMapping("/worker/{workerId}")
    public List<Booking> getWorkerBookings(@PathVariable Long workerId) {
        return bookingRepository.findByWorker_Id(workerId);
    }
    
    @PutMapping("/{id}/status")
    public Booking updateBookingStatus(@PathVariable Long id, @RequestBody StatusUpdateRequest request) {
        return bookingRepository.findById(id)
                .map(booking -> {
                    Booking.Status newStatus = Booking.Status.valueOf(request.getStatus());
                    Booking.Status oldStatus = booking.getStatus();
                    
                    // Handle refund if worker cancels a confirmed booking
                    if (newStatus == Booking.Status.CANCELLED && 
                        (oldStatus == Booking.Status.CONFIRMED || oldStatus == Booking.Status.PENDING) &&
                        booking.getTotalAmount() != null && booking.getTotalAmount().compareTo(java.math.BigDecimal.ZERO) > 0 &&
                        !"cash".equals(booking.getPaymentMethod())) {
                        
                        // Add refund to user's wallet
                        var user = booking.getUser();
                        if (user.getWalletBalance() == null) {
                            user.setWalletBalance(java.math.BigDecimal.ZERO);
                        }
                        user.setWalletBalance(user.getWalletBalance().add(booking.getTotalAmount()));
                        userRepository.save(user);
                        
                        System.out.println("Refund processed: ₹" + booking.getTotalAmount() + " added to user wallet");
                    }
                    
                    // Handle worker payment when work is completed
                    if (newStatus == Booking.Status.COMPLETED && oldStatus != Booking.Status.COMPLETED &&
                        booking.getTotalAmount() != null && booking.getTotalAmount().compareTo(java.math.BigDecimal.ZERO) > 0) {
                        
                        var worker = booking.getWorker();
                        if (worker.getTotalEarnings() == null) {
                            worker.setTotalEarnings(java.math.BigDecimal.ZERO);
                        }
                        worker.setTotalEarnings(worker.getTotalEarnings().add(booking.getTotalAmount()));
                        workerRepository.save(worker);
                        
                        // Request review from user
                        booking.setReviewRequested(true);
                        
                        System.out.println("Payment processed: ₹" + booking.getTotalAmount() + " added to worker earnings");
                    }
                    
                    booking.setStatus(newStatus);
                    return bookingRepository.save(booking);
                })
                .orElseThrow(() -> new RuntimeException("Booking not found"));
    }
    
    @GetMapping("/user/{userId}/pending-reviews")
    public List<Booking> getPendingReviews(@PathVariable Long userId) {
        List<Booking> result = bookingRepository.findByUser_IdAndStatusAndReviewRequested(userId, Booking.Status.COMPLETED, true);
        System.out.println("Found " + result.size() + " pending reviews for user " + userId);
        return result;
    }
    
    @GetMapping("/user/{userId}/debug")
    public java.util.Map<String, Object> debugUserBookings(@PathVariable Long userId) {
        List<Booking> allBookings = bookingRepository.findByUser_Id(userId);
        List<Booking> completedBookings = allBookings.stream().filter(b -> b.getStatus() == Booking.Status.COMPLETED).toList();
        List<Booking> pendingReviews = bookingRepository.findByUser_IdAndStatusAndReviewRequested(userId, Booking.Status.COMPLETED, true);
        
        return java.util.Map.of(
            "totalBookings", allBookings.size(),
            "completedBookings", completedBookings.size(),
            "pendingReviews", pendingReviews.size(),
            "allBookings", allBookings,
            "completedBookings", completedBookings,
            "pendingReviews", pendingReviews
        );
    }
    
    public static class StatusUpdateRequest {
        private String status;
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
}