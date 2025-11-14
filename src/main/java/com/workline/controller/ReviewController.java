package com.workline.controller;

import com.workline.model.Review;
import com.workline.model.Booking;
import com.workline.repository.ReviewRepository;
import com.workline.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    
    @Autowired
    private ReviewRepository reviewRepository;
    
    @Autowired
    private BookingRepository bookingRepository;
    
    @PostMapping
    public Review createReview(@RequestBody ReviewRequest request) {
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        
        Review review = new Review();
        review.setBooking(booking);
        review.setUser(booking.getUser());
        review.setWorker(booking.getWorker());
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        
        // Mark booking as reviewed
        booking.setReviewRequested(false);
        bookingRepository.save(booking);
        
        return reviewRepository.save(review);
    }
    
    @GetMapping("/worker/{workerId}")
    public List<Review> getWorkerReviews(@PathVariable Long workerId) {
        return reviewRepository.findByWorker_Id(workerId);
    }
    
    @GetMapping("/user/{userId}")
    public List<Review> getUserReviews(@PathVariable Long userId) {
        return reviewRepository.findByUser_Id(userId);
    }
    
    public static class ReviewRequest {
        private Long bookingId;
        private Integer rating;
        private String comment;
        
        public Long getBookingId() { return bookingId; }
        public void setBookingId(Long bookingId) { this.bookingId = bookingId; }
        
        public Integer getRating() { return rating; }
        public void setRating(Integer rating) { this.rating = rating; }
        
        public String getComment() { return comment; }
        public void setComment(String comment) { this.comment = comment; }
    }
}