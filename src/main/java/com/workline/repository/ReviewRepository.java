package com.workline.repository;

import com.workline.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByWorker_Id(Long workerId);
    List<Review> findByUser_Id(Long userId);
    List<Review> findByBooking_Id(Long bookingId);
}