package com.workline.repository;

import com.workline.model.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface WorkerRepository extends JpaRepository<Worker, Long> {
    List<Worker> findByDistrict(String district);
    List<Worker> findByStatus(Worker.Status status);
    List<Worker> findByDistrictAndStatus(String district, Worker.Status status);
    List<Worker> findByProfessionAndStatus(String profession, Worker.Status status);
    List<Worker> findByDistrictAndProfessionAndStatus(String district, String profession, Worker.Status status);
    Optional<Worker> findByPhone(String phone);
    Optional<Worker> findByEmail(String email);
    Optional<Worker> findByEmailAndPassword(String email, String password);
}