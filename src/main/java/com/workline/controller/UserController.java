package com.workline.controller;

import com.workline.model.User;
import com.workline.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserRepository userRepository;
    
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}/address")
    public ResponseEntity<User> updateUserAddress(@PathVariable Long id, @RequestBody AddressRequest request) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setAddress(request.getAddress());
                    return ResponseEntity.ok(userRepository.save(user));
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}/details")
    public ResponseEntity<User> updateUserDetails(@PathVariable Long id, @RequestBody DetailsRequest request) {
        System.out.println("PUT /api/users/" + id + "/details called");
        System.out.println("Phone: " + request.getPhone() + ", Address: " + request.getAddress());
        return userRepository.findById(id)
                .map(user -> {
                    System.out.println("User found: " + user.getFullName());
                    if (request.getPhone() != null) user.setPhone(request.getPhone());
                    if (request.getAddress() != null) user.setAddress(request.getAddress());
                    return ResponseEntity.ok(userRepository.save(user));
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    public static class AddressRequest {
        private String address;
        
        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }
    }
    
    public static class DetailsRequest {
        private String phone;
        private String address;
        
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }
    }
}