package com.workline.controller;

import com.workline.model.Worker;
import com.workline.repository.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/workers")
public class WorkerController {
    
    @Autowired
    private WorkerRepository workerRepository;
    
    @GetMapping("/{id}")
    public ResponseEntity<Worker> getWorkerById(@PathVariable Long id) {
        return workerRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    public ResponseEntity<?> getAllWorkers(@RequestParam(required = false) String profession, 
                                          @RequestParam(required = false) String district) {
        try {
            System.out.println("Getting workers - profession: " + profession + ", district: " + district);
            
            java.util.List<Worker> result;
            if (profession != null && district != null) {
                result = workerRepository.findByDistrictAndProfessionAndStatus(district, profession, Worker.Status.ACTIVE);
            } else if (profession != null) {
                result = workerRepository.findByProfessionAndStatus(profession, Worker.Status.ACTIVE);
            } else if (district != null) {
                result = workerRepository.findByDistrictAndStatus(district, Worker.Status.ACTIVE);
            } else {
                result = workerRepository.findByStatus(Worker.Status.ACTIVE);
            }
            
            System.out.println("Found " + result.size() + " workers");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.out.println("Error loading workers: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Unable to load workers at the moment.");
        }
    }
    
    @GetMapping("/debug")
    public ResponseEntity<?> debugWorkers() {
        try {
            long totalWorkers = workerRepository.count();
            java.util.List<Worker> allWorkers = workerRepository.findAll();
            java.util.List<Worker> activeWorkers = workerRepository.findByStatus(Worker.Status.ACTIVE);
            
            return ResponseEntity.ok(java.util.Map.of(
                "totalWorkers", totalWorkers,
                "allWorkers", allWorkers,
                "activeWorkers", activeWorkers
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Debug error: " + e.getMessage());
        }
    }
    
    @PutMapping("/{id}/setup")
    public ResponseEntity<Worker> setupWorkerProfile(@PathVariable Long id, @RequestBody SetupRequest request) {
        return workerRepository.findById(id)
                .map(worker -> {
                    worker.setProfession(request.getProfession());
                    worker.setDistrict(request.getDistrict());
                    worker.setHourlyRate(java.math.BigDecimal.valueOf(request.getHourlyRate()));
                    worker.setStatus(Worker.Status.ACTIVE);
                    return ResponseEntity.ok(workerRepository.save(worker));
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}/profile")
    public ResponseEntity<Worker> updateWorkerProfile(@PathVariable Long id, @RequestBody ProfileUpdateRequest request) {
        return workerRepository.findById(id)
                .map(worker -> {
                    if (request.getPhone() != null) worker.setPhone(request.getPhone());
                    if (request.getProfession() != null) worker.setProfession(request.getProfession());
                    if (request.getDistrict() != null) worker.setDistrict(request.getDistrict());
                    if (request.getHourlyRate() != null) worker.setHourlyRate(java.math.BigDecimal.valueOf(request.getHourlyRate()));
                    return ResponseEntity.ok(workerRepository.save(worker));
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    public static class SetupRequest {
        private String profession;
        private String district;
        private Double hourlyRate;
        
        public String getProfession() { return profession; }
        public void setProfession(String profession) { this.profession = profession; }
        public String getDistrict() { return district; }
        public void setDistrict(String district) { this.district = district; }
        public Double getHourlyRate() { return hourlyRate; }
        public void setHourlyRate(Double hourlyRate) { this.hourlyRate = hourlyRate; }
    }
    
    public static class ProfileUpdateRequest {
        private String phone;
        private String profession;
        private String district;
        private Double hourlyRate;
        
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
        public String getProfession() { return profession; }
        public void setProfession(String profession) { this.profession = profession; }
        public String getDistrict() { return district; }
        public void setDistrict(String district) { this.district = district; }
        public Double getHourlyRate() { return hourlyRate; }
        public void setHourlyRate(Double hourlyRate) { this.hourlyRate = hourlyRate; }
    }
}