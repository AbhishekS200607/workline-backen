package com.workline.repository;

import com.workline.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<Booking> findByWorker_IdAndBookingDate(Long workerId, LocalDateTime bookingDate);
    List<Booking> findByUser_Id(Long userId);
    List<Booking> findByWorker_Id(Long workerId);
    List<Booking> findByUser_IdAndStatusAndReviewRequested(Long userId, Booking.Status status, Boolean reviewRequested);
}