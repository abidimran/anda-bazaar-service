package com.andabazaar.serviceimpl;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.andabazaar.dto.user.UserRequestDto;
import com.andabazaar.dto.user.UserResponseDto;
import com.andabazaar.entity.User;
import com.andabazaar.enums.RoleType;
import com.andabazaar.enums.UserStatus;
import com.andabazaar.exception.BadRequestException;
import com.andabazaar.exception.ResourceNotFoundException;
import com.andabazaar.repository.UserRepository;
import com.andabazaar.service.AdminService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto createAdmin( UserRequestDto request) {

        if (userRepository.existsByEmail(
                request.getEmail())) {

            throw new BadRequestException("Email already registered");
        }

        if (userRepository.existsByPhone(
                request.getPhone())) {

            throw new BadRequestException("Phone number already registered");
        }

        User admin = User.builder()
                .firstName(request.getFirstName().trim())
                .lastName(request.getLastName().trim())
                .email(request.getEmail()
                        .trim()
                        .toLowerCase())
                .phone(request.getPhone().trim())
                .password( passwordEncoder.encode( request.getPassword()))
                .role(RoleType.ADMIN)
                .status(UserStatus.ACTIVE)
                .preferredLanguage( request.getPreferredLanguage())
                .preferredCity( request.getPreferredCity())
                .notificationEnabled( request.getNotificationEnabled() == null || request.getNotificationEnabled())
                .build();

        User savedAdmin =
                userRepository.save(admin);

        return mapToResponse(savedAdmin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDto> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getUser(Long id) {

        User user = findUser(id);

        return mapToResponse(user);
    }

    @Override
    public UserResponseDto changeUserStatus( Long id, String status) {

        User user = findUser(id);

        try {

            UserStatus newStatus =
                    UserStatus.valueOf( status.toUpperCase());

            user.setStatus(newStatus);

        } catch (IllegalArgumentException ex) {

            throw new BadRequestException("Invalid user status: " + status);
        }

        return mapToResponse(
                userRepository.save(user));
    }

    @Override
    public void deleteUser(Long id) {

        User user = findUser(id);

        userRepository.delete(user);
    }

    private User findUser(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id: "
                                        + id));
    }

    private UserResponseDto mapToResponse( User user) {

        return UserResponseDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .status(user.getStatus())
                .profileImage(user.getProfileImage())
                .preferredLanguage( user.getPreferredLanguage())
                .preferredCity( user.getPreferredCity())
                .notificationEnabled( user.getNotificationEnabled())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}