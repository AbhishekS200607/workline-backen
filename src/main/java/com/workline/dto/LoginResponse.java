package com.workline.dto;

public class LoginResponse {
    private String userType;
    private Long userId;
    private String fullName;
    
    public LoginResponse(String userType, Long userId, String fullName) {
        this.userType = userType;
        this.userId = userId;
        this.fullName = fullName;
    }
    
    public String getUserType() { return userType; }
    public void setUserType(String userType) { this.userType = userType; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
}