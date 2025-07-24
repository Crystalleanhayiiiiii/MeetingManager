package com.example.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDateTime;

// Đánh dấu lớp này là một Entity JPA
@Entity
public class User {

    @Id  // Đánh dấu đây là khóa chính
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Tự động sinh giá trị cho ID (Auto increment)
    private Long id;

    @Column(nullable = false, unique = true)  // Đảm bảo username là duy nhất và không thể null
    private String username;

    @Column(nullable = false, unique = true)  // Đảm bảo email là duy nhất và không thể null
    private String email;

    @Column(nullable = false)  // Đảm bảo mật khẩu không thể null
    private String password;  // Mật khẩu đã được mã hóa

    private String firstName;
    private String lastName;

    @Enumerated(EnumType.STRING)
    private Role role;  // Vai trò của người dùng (USER, ADMIN, ROOM_MANAGER)

    @Enumerated(EnumType.STRING)
    private Status status;  // Trạng thái tài khoản (ACTIVE, INACTIVE)

    private String address;  // Địa chỉ người dùng
    private String phoneNumber;  // Số điện thoại người dùng

    private LocalDateTime createdAt;  // Thời gian tạo tài khoản
    private LocalDateTime updatedAt;  // Thời gian cập nhật tài khoản

    // Các getter và setter

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Enum cho vai trò người dùng
    public enum Role {
        USER,
        ADMIN,
        ROOM_MANAGER
    }

    // Enum cho trạng thái tài khoản
    public enum Status {
        ACTIVE,
        INACTIVE
    }
}
