package com.workline.controller;

import com.workline.dto.LoginRequest;
import com.workline.dto.LoginResponse;
import com.workline.model.User;
import com.workline.model.Worker;
import com.workline.repository.UserRepository;
import com.workline.repository.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private WorkerRepository workerRepository;
    
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        try {
            System.out.println("Login attempt: " + request.getEmail());
            
            // Check user login
            var user = userRepository.findByEmailAndPassword(request.getEmail(), request.getPassword());
            if (user.isPresent()) {
                System.out.println("User found: " + user.get().getFullName());
                return ResponseEntity.ok(new LoginResponse("USER", user.get().getId(), user.get().getFullName()));
            }
            
            // Check worker login by email and password
            var worker = workerRepository.findByEmailAndPassword(request.getEmail(), request.getPassword());
            if (worker.isPresent()) {
                System.out.println("Worker found: " + worker.get().getFullName());
                return ResponseEntity.ok(new LoginResponse("WORKER", worker.get().getId(), worker.get().getFullName()));
            }
            
            System.out.println("No user or worker found");
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            System.out.println("Login error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @PostMapping("/register/user")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        try {
            System.out.println("User registration: " + user.getEmail());
            if (userRepository.findByEmail(user.getEmail()).isPresent()) {
                return ResponseEntity.badRequest().build();
            }
            User savedUser = userRepository.save(user);
            System.out.println("User registered: " + savedUser.getId());
            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            System.out.println("Registration error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        try {
            long count = workerRepository.count();
            return ResponseEntity.ok("Database connected. Workers count: " + count);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Database error: " + e.getMessage());
        }
    }
    
    @PostMapping("/register/worker")
    public ResponseEntity<LoginResponse> registerWorker(@RequestBody Worker worker) {
        try {
            System.out.println("=== WORKER REGISTRATION START ===");
            System.out.println("Received worker data:");
            System.out.println("  Full Name: " + worker.getFullName());
            System.out.println("  Email: " + worker.getEmail());
            System.out.println("  Phone: " + worker.getPhone());
            System.out.println("  Password: " + (worker.getPassword() != null ? "[SET]" : "[NULL]"));
            
            // Validate required fields
            if (worker.getFullName() == null || worker.getFullName().trim().isEmpty()) {
                System.out.println("ERROR: Full name is required");
                return ResponseEntity.badRequest().build();
            }
            if (worker.getEmail() == null || worker.getEmail().trim().isEmpty()) {
                System.out.println("ERROR: Email is required");
                return ResponseEntity.badRequest().build();
            }
            if (worker.getPassword() == null || worker.getPassword().trim().isEmpty()) {
                System.out.println("ERROR: Password is required");
                return ResponseEntity.badRequest().build();
            }
            if (worker.getPhone() == null || worker.getPhone().trim().isEmpty()) {
                System.out.println("ERROR: Phone is required");
                return ResponseEntity.badRequest().build();
            }
            
            System.out.println("Checking if email already exists...");
            if (workerRepository.findByEmail(worker.getEmail()).isPresent()) {
                System.out.println("ERROR: Email already exists");
                return ResponseEntity.badRequest().build();
            }
            
            System.out.println("Setting default values...");
            worker.setStatus(Worker.Status.INACTIVE);
            worker.setRating(0.0);
            worker.setHourlyRate(java.math.BigDecimal.ZERO);
            worker.setTotalEarnings(java.math.BigDecimal.ZERO);
            worker.setProfession("Not Set");
            worker.setDistrict("Not Set");
            
            System.out.println("Saving worker to database...");
            Worker savedWorker = workerRepository.save(worker);
            System.out.println("Worker saved successfully with ID: " + savedWorker.getId());
            
            LoginResponse response = new LoginResponse("WORKER", savedWorker.getId(), savedWorker.getFullName());
            System.out.println("=== WORKER REGISTRATION SUCCESS ===");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.out.println("=== WORKER REGISTRATION ERROR ===");
            System.out.println("Error type: " + e.getClass().getSimpleName());
            System.out.println("Error message: " + e.getMessage());
            if (worker != null) {
                System.out.println("Worker data: fullName=" + worker.getFullName() + ", email=" + worker.getEmail() + ", phone=" + worker.getPhone());
            }
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
}