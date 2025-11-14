package com.workline.service;

import com.workline.model.Booking;
import com.workline.model.User;
import com.workline.model.Worker;
import com.workline.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class BookingService {
    
    @Autowired
    private BookingRepository bookingRepository;
    
    @Transactional
    public Booking createBooking(User user, Worker worker, LocalDateTime bookingDate) {
        // Check if slot is already booked
        if (bookingRepository.findByWorker_IdAndBookingDate(worker.getId(), bookingDate).isPresent()) {
            throw new RuntimeException("Time slot already booked");
        }
        
        Booking booking = new Booking(user, worker, bookingDate);
        return bookingRepository.save(booking);
    }
}