package com.andabazaar.serviceimpl;

import com.andabazaar.dto.user.UserProfileDto;
import com.andabazaar.dto.user.UserRequestDto;
import com.andabazaar.dto.user.UserResponseDto;
import com.andabazaar.enums.RoleType;
import com.andabazaar.enums.UserStatus;
import com.andabazaar.exception.BadRequestException;
import com.andabazaar.exception.ResourceNotFoundException;
import com.andabazaar.mapper.UserMapper;
import com.andabazaar.repository.UserRepository;
import com.andabazaar.repository.entity.User;
import com.andabazaar.service.UserService;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto createUser(UserRequestDto request) {
        // -----------------------------------------------------
        // CHECK EMAIL
        // -----------------------------------------------------
        String email = request.getEmail()
                .trim()
                .toLowerCase();
        if (userRepository.existsByEmail(email)) {
            throw new BadRequestException("Email already registered");
        }

        // -----------------------------------------------------
        // CHECK PHONE
        // -----------------------------------------------------
        String phone = request.getPhone().trim();
        if (userRepository.existsByPhone(phone)) {
            throw new BadRequestException("Phone number already registered");
        }

        // -----------------------------------------------------
        // CREATE NORMAL USER
        // IMPORTANT:
        // Role is ALWAYS USER here.
        //
        // Even if request contains:
        // "role": "ADMIN"
        //
        // it will still create USER.
        // -----------------------------------------------------
        User user = User.builder()
                .firstName( request.getFirstName().trim() )
                .lastName( request.getLastName().trim() )
                .email(email)
                .phone(phone)
                .password( passwordEncoder.encode( request.getPassword() ) )
                .role(RoleType.USER)
                .status(UserStatus.ACTIVE)
                .preferredLanguage( request.getPreferredLanguage() )
                .preferredCity( request.getPreferredCity() )
                .notificationEnabled( request.getNotificationEnabled() == null ? true : request.getNotificationEnabled() )
                .build();
        // -----------------------------------------------------
        // SAVE USER
        // -----------------------------------------------------
        User savedUser = userRepository.save(user);
        // -----------------------------------------------------
        // RESPONSE
        // -----------------------------------------------------
        return userMapper.toResponseDto(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getUserById(Long id) {
        User user = findUser(id);
        return userMapper.toResponseDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponseDto)
                .toList();
    }

    @Override
    public UserResponseDto updateUser( Long id, UserRequestDto request) {
        User user = findUser(id);
        // -----------------------------------------------------
        // NORMALIZE EMAIL
        // -----------------------------------------------------
        String email = request.getEmail()
                .trim()
                .toLowerCase();
        // -----------------------------------------------------
        // CHECK EMAIL DUPLICATE
        // -----------------------------------------------------
        if (!user.getEmail().equals(email)
                && userRepository.existsByEmail(email)) {
            throw new BadRequestException("Email already registered");
        }

        // -----------------------------------------------------
        // NORMALIZE PHONE
        // -----------------------------------------------------
        String phone = request.getPhone().trim();
        // -----------------------------------------------------
        // CHECK PHONE DUPLICATE
        // -----------------------------------------------------
        if (!user.getPhone().equals(phone)
                && userRepository.existsByPhone(phone)) {
            throw new BadRequestException("Phone number already registered");
        }

        // -----------------------------------------------------
        // UPDATE BASIC INFORMATION
        // -----------------------------------------------------
        user.setFirstName( request.getFirstName().trim());
        user.setLastName( request.getLastName().trim());
        user.setEmail(email);
        user.setPhone(phone);
        // -----------------------------------------------------
        // UPDATE PASSWORD
        // -----------------------------------------------------
        if (request.getPassword() != null
                && !request.getPassword().isBlank()) {
            user.setPassword( passwordEncoder.encode( request.getPassword() ));
        }

        // -----------------------------------------------------
        // ROLE
        //
        // IMPORTANT:
        // UserService normal update should NOT be used
        // to create/promote an ADMIN.
        //
        // Existing role is kept unchanged.
        // Admin role management should be handled separately
        // through AdminService.
        // -----------------------------------------------------
        // DO NOT USE:
        // user.setRole(request.getRole());
        // -----------------------------------------------------
        // PREFERRED LANGUAGE
        // -----------------------------------------------------
        user.setPreferredLanguage( request.getPreferredLanguage());
        // -----------------------------------------------------
        // PREFERRED CITY
        // -----------------------------------------------------
        user.setPreferredCity( request.getPreferredCity());
        // -----------------------------------------------------
        // NOTIFICATIONS
        // -----------------------------------------------------
        if (request.getNotificationEnabled() != null) {
            user.setNotificationEnabled( request.getNotificationEnabled());
        }

        // -----------------------------------------------------
        // SAVE
        // -----------------------------------------------------
        User updatedUser = userRepository.save(user);
        return userMapper.toResponseDto(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        User user = findUser(id);
        userRepository.delete(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileDto getProfile(Long id) {
        User user = findUser(id);
        return userMapper.toProfileDto(user);
    }

    @Override
    public UserResponseDto changeUserStatus( Long id, String status) {
        User user = findUser(id);
        if (status == null || status.isBlank()) {
            throw new BadRequestException("User status is required");
        }

        try {
            UserStatus newStatus = UserStatus.valueOf( status.trim().toUpperCase());
            user.setStatus(newStatus);
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("Invalid user status: " + status);
        }

        User updatedUser = userRepository.save(user);
        return userMapper.toResponseDto(updatedUser);
    }

    private User findUser(Long id) {
        if (id == null) {
            throw new BadRequestException("User ID is required");
        }

        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }
}
