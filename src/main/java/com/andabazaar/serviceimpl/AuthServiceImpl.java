package com.andabazaar.serviceimpl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.andabazaar.dto.auth.LoginRequestDto;
import com.andabazaar.dto.auth.LoginResponseDto;
import com.andabazaar.dto.auth.RegisterRequestDto;
import com.andabazaar.dto.notification.NotificationRequestDto;
import com.andabazaar.dto.user.UserResponseDto;
import com.andabazaar.entity.User;
import com.andabazaar.enums.NotificationType;
import com.andabazaar.enums.RoleType;
import com.andabazaar.enums.UserStatus;
import com.andabazaar.exception.BadRequestException;
import com.andabazaar.repository.UserRepository;
import com.andabazaar.security.JwtService;
import com.andabazaar.service.AuthService;
import com.andabazaar.service.NotificationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final NotificationService notificationService;


    // =========================================================
    // REGISTER
    // =========================================================

    @Override
    public UserResponseDto register(RegisterRequestDto request) {

        System.out.println("========================================");
        System.out.println("REGISTER START");
        System.out.println("EMAIL = " + request.getEmail());
        System.out.println("PHONE = " + request.getPhone());
        System.out.println("========================================");


        // =====================================================
        // NORMALIZE EMAIL
        // =====================================================

        String email = request.getEmail()
                .trim()
                .toLowerCase();


        // =====================================================
        // CHECK EMAIL
        // =====================================================

        if (userRepository.existsByEmail(email)) {

            System.out.println( "REGISTER FAILED: EMAIL ALREADY EXISTS");

            throw new BadRequestException(
                    "Email already registered");
        }


        // =====================================================
        // CHECK PHONE
        // =====================================================

        if (userRepository.existsByPhone(
                request.getPhone())) {

            System.out.println( "REGISTER FAILED: PHONE ALREADY EXISTS");

            throw new BadRequestException(
                    "Phone number already registered");
        }


        // =====================================================
        // CREATE USER
        // =====================================================

        User user = User.builder()

                .firstName( request.getFirstName().trim()
                )

                .lastName( request.getLastName().trim()
                )

                .email(email)

                .phone( request.getPhone().trim()
                )

                .password( passwordEncoder.encode( request.getPassword()
                        )
                )

                .role(RoleType.USER)

                .status(UserStatus.ACTIVE)

                .preferredLanguage( request.getPreferredLanguage()
                )

                .preferredCity( request.getPreferredCity()
                )

                .notificationEnabled(true)

                .build();


        // =====================================================
        // SAVE USER
        // =====================================================

        User savedUser =
                userRepository.save(user);

        System.out.println( "USER SAVED SUCCESSFULLY");

        System.out.println( "USER ID = " + savedUser.getId());


        // =====================================================
        // WELCOME NOTIFICATION
        // =====================================================

        /*
         * Notification failure should NOT stop
         * registration response.
         */

        try {

            notificationService.createNotification( NotificationRequestDto.builder()

                            .userId( savedUser.getId()
                            )

                            .type( NotificationType.SYSTEM )

                            .title( "Welcome to Anda Bazaar" )

                            .message( "Welcome to Anda Bazaar! " + "Your account has been " + "created successfully." )

                            .build());

            System.out.println( "WELCOME NOTIFICATION CREATED");

        } catch (Exception e) {

            System.out.println( "WELCOME NOTIFICATION FAILED");

            e.printStackTrace();

            /*
             * Don't throw exception here.
             *
             * User registration has already succeeded.
             */
        }


        // =====================================================
        // RESPONSE
        // =====================================================

        UserResponseDto response =
                mapToResponse(savedUser);


        System.out.println( "REGISTER RESPONSE READY");

        System.out.println( "REGISTER SUCCESS");

        System.out.println( "========================================");


        return response;
    }


    // =========================================================
    // LOGIN
    // =========================================================

    @Override
    public LoginResponseDto login( LoginRequestDto request) {

        System.out.println("========================================");
        System.out.println("LOGIN START");
        System.out.println( "EMAIL = " + request.getEmail());
        System.out.println("========================================");


        // =====================================================
        // NORMALIZE EMAIL
        // =====================================================

        String email = request.getEmail()
                .trim()
                .toLowerCase();


        // =====================================================
        // AUTHENTICATE
        // =====================================================

        authenticationManager.authenticate( new UsernamePasswordAuthenticationToken( email, request.getPassword() ));


        System.out.println( "AUTHENTICATION SUCCESS");


        // =====================================================
        // FIND USER
        // =====================================================

        User user =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new BadRequestException(
                                        "User not found"
                                ));


        // =====================================================
        // CHECK STATUS
        // =====================================================

        if (user.getStatus() != UserStatus.ACTIVE) {

            throw new BadRequestException(
                    "User account is not active");
        }


        // =====================================================
        // GENERATE JWT
        // =====================================================

        String token =
                jwtService.generateToken(user);


        System.out.println( "JWT TOKEN GENERATED");

        System.out.println( "USER ID = " + user.getId());

        System.out.println( "ROLE = " + user.getRole());


        // =====================================================
        // RESPONSE
        // =====================================================

        LoginResponseDto response =
                LoginResponseDto.builder()

                        .token(token)

                        .tokenType("Bearer")

                        .userId(user.getId())

                        .firstName( user.getFirstName()
                        )

                        .lastName( user.getLastName()
                        )

                        .email( user.getEmail()
                        )

                        .role( user.getRole()
                        )

                        .build();


        System.out.println( "LOGIN SUCCESS");

        System.out.println( "========================================");


        return response;
    }


    // =========================================================
    // MAP USER RESPONSE
    // =========================================================

    private UserResponseDto mapToResponse( User user) {

        return UserResponseDto.builder()

                .id( user.getId()
                )

                .firstName( user.getFirstName()
                )

                .lastName( user.getLastName()
                )

                .email( user.getEmail()
                )

                .phone( user.getPhone()
                )

                .role( user.getRole()
                )

                .status( user.getStatus()
                )

                .profileImage( user.getProfileImage()
                )

                .preferredLanguage( user.getPreferredLanguage()
                )

                .preferredCity( user.getPreferredCity()
                )

                .notificationEnabled( user.getNotificationEnabled()
                )

                .createdAt( user.getCreatedAt()
                )

                .updatedAt( user.getUpdatedAt()
                )

                .build();
    }
    
    
    
 // =========================================================
 // CURRENT LOGGED-IN USER
 // =========================================================

 @Override
 public UserResponseDto getCurrentUser(String email) {

     System.out.println("========================================");
     System.out.println("GET CURRENT USER");
     System.out.println("EMAIL = " + email);
     System.out.println("========================================");

     User user = userRepository.findByEmail( email.trim().toLowerCase()
     ).orElseThrow(() ->
             new BadRequestException(
                     "User not found"
             ));

     // =====================================================
     // CHECK STATUS
     // =====================================================

     if (user.getStatus() != UserStatus.ACTIVE) {

         throw new BadRequestException(
                 "User account is not active");
     }

     System.out.println( "CURRENT USER ID = " + user.getId());

     System.out.println( "CURRENT USER EMAIL = " + user.getEmail());

     System.out.println( "CURRENT USER ROLE = " + user.getRole());

     return mapToResponse(user);
 }
}