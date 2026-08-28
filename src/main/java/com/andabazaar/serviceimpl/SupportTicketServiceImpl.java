package com.andabazaar.serviceimpl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.andabazaar.dto.support.SupportReplyDto;
import com.andabazaar.dto.support.SupportTicketRequestDto;
import com.andabazaar.dto.support.SupportTicketResponseDto;
import com.andabazaar.entity.SupportMessage;
import com.andabazaar.entity.SupportTicket;
import com.andabazaar.entity.User;
import com.andabazaar.enums.TicketStatus;
import com.andabazaar.exception.BadRequestException;
import com.andabazaar.exception.ResourceNotFoundException;
import com.andabazaar.repository.SupportMessageRepository;
import com.andabazaar.repository.SupportTicketRepository;
import com.andabazaar.repository.UserRepository;
import com.andabazaar.service.SupportTicketService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class SupportTicketServiceImpl
        implements SupportTicketService {

    private final SupportTicketRepository ticketRepository;

    private final SupportMessageRepository messageRepository;

    private final UserRepository userRepository;

    // =========================================================
    // CREATE TICKET
    // =========================================================

    @Override
    public SupportTicketResponseDto createTicket(
            SupportTicketRequestDto request) {

        User user = findUser(request.getUserId());

        SupportTicket ticket = SupportTicket.builder()
                .user(user)
                .subject(request.getSubject().trim())
                .description(request.getDescription().trim())
                .priority(request.getPriority())
                .category(request.getCategory())
                .status(TicketStatus.OPEN)
                .build();

        return mapToResponse(
                ticketRepository.save(ticket)
        );
    }

    // =========================================================
    // GET TICKET BY ID
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public SupportTicketResponseDto getTicketById(
            Long id) {

        return mapToResponse(
                findTicket(id)
        );
    }

    // =========================================================
    // GET TICKET BY NUMBER
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public SupportTicketResponseDto getTicketByNumber(
            String ticketNumber) {

        SupportTicket ticket =
                ticketRepository.findByTicketNumber(
                        ticketNumber
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Support ticket not found with number: "
                                        + ticketNumber
                        )
                );

        return mapToResponse(ticket);
    }

    // =========================================================
    // GET ALL TICKETS
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<SupportTicketResponseDto> getAllTickets() {

        return ticketRepository
                .findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =========================================================
    // GET USER TICKETS
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<SupportTicketResponseDto> getUserTickets(
            Long userId) {

        if (!userRepository.existsById(userId)) {

            throw new ResourceNotFoundException(
                    "User not found with id: " + userId
            );
        }

        return ticketRepository
                .findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =========================================================
    // GET TICKETS BY STATUS
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<SupportTicketResponseDto> getTicketsByStatus(
            String status) {

        TicketStatus ticketStatus =
                parseStatus(status);

        return ticketRepository
                .findByStatusOrderByCreatedAtDesc(
                        ticketStatus
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =========================================================
    // UPDATE TICKET
    // =========================================================

    @Override
    public SupportTicketResponseDto updateTicket(
            Long id,
            SupportTicketRequestDto request) {

        SupportTicket ticket =
                findTicket(id);

        User user =
                findUser(request.getUserId());

        ticket.setUser(user);

        ticket.setSubject(
                request.getSubject().trim()
        );

        ticket.setDescription(
                request.getDescription().trim()
        );

        ticket.setPriority(
                request.getPriority()
        );

        ticket.setCategory(
                request.getCategory()
        );

        return mapToResponse(
                ticketRepository.save(ticket)
        );
    }

    // =========================================================
    // CLOSE TICKET
    // =========================================================

    @Override
    public void closeTicket(
            Long id,
            String resolution) {

        SupportTicket ticket =
                findTicket(id);

        if (ticket.getStatus() ==
                TicketStatus.CLOSED) {

            throw new BadRequestException(
                    "Ticket is already closed"
            );
        }

        ticket.setStatus(
                TicketStatus.CLOSED
        );

        ticket.setResolution(
                resolution
        );

        ticket.setResolvedAt(
                LocalDateTime.now()
        );

        ticketRepository.save(ticket);
    }

    // =========================================================
    // REOPEN TICKET
    // =========================================================

    @Override
    public void reopenTicket(Long id) {

        SupportTicket ticket =
                findTicket(id);

        if (ticket.getStatus() !=
                TicketStatus.CLOSED) {

            throw new BadRequestException(
                    "Only closed ticket can be reopened"
            );
        }

        ticket.setStatus(
                TicketStatus.OPEN
        );

        ticket.setResolution(null);

        ticket.setResolvedAt(null);

        ticketRepository.save(ticket);
    }

    // =========================================================
    // DELETE TICKET
    // =========================================================

    @Override
    public void deleteTicket(Long id) {

        SupportTicket ticket =
                findTicket(id);

        List<SupportMessage> messages =
                messageRepository
                        .findByTicketIdOrderByCreatedAtAsc(id);

        if (!messages.isEmpty()) {

            messageRepository.deleteAll(messages);
        }

        ticketRepository.delete(ticket);
    }

    // =========================================================
    // ADD REPLY
    // =========================================================

    @Override
    public void addReply(
            Long ticketId,
            SupportReplyDto request) {

        SupportTicket ticket =
                findTicket(ticketId);

        User user =
                findUser(request.getUserId());

        // -----------------------------------------------------
        // CLOSED TICKET CHECK
        // -----------------------------------------------------

        if (ticket.getStatus() ==
                TicketStatus.CLOSED) {

            throw new BadRequestException(
                    "Cannot reply to a closed ticket"
            );
        }

        // -----------------------------------------------------
        // ADMIN REPLY
        // -----------------------------------------------------
        //
        // adminReply frontend/request se aayega.
        //
        // true  = Admin reply
        // false = Normal user reply
        //
        // Boolean.TRUE.equals() use kiya hai taaki
        // null hone par bhi false rahe.
        // -----------------------------------------------------

        boolean isAdminReply =
                Boolean.TRUE.equals(
                        request.getAdminReply()
                );

        SupportMessage message =
                SupportMessage.builder()
                        .ticket(ticket)
                        .user(user)
                        .message(
                                request.getMessage()
                                        .trim()
                        )
                        .adminReply(isAdminReply)
                        .build();

        messageRepository.save(message);
    }

    // =========================================================
    // COUNT BY STATUS
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public long countByStatus(
            String status) {

        return ticketRepository.countByStatus(
                parseStatus(status)
        );
    }

    // =========================================================
    // FIND TICKET
    // =========================================================

    private SupportTicket findTicket(
            Long id) {

        return ticketRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Support ticket not found with id: "
                                        + id
                        )
                );
    }

    // =========================================================
    // FIND USER
    // =========================================================

    private User findUser(
            Long id) {

        return userRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: "
                                        + id
                        )
                );
    }

    // =========================================================
    // PARSE STATUS
    // =========================================================

    private TicketStatus parseStatus(
            String status) {

        try {

            return TicketStatus.valueOf(
                    status.trim().toUpperCase()
            );

        } catch (IllegalArgumentException |
                 NullPointerException e) {

            throw new BadRequestException(
                    "Invalid ticket status: " + status
            );
        }
    }

    // =========================================================
    // MAP RESPONSE
    // =========================================================

    private SupportTicketResponseDto mapToResponse(
            SupportTicket ticket) {

        return SupportTicketResponseDto.builder()
                .id(ticket.getId())
                .ticketNumber(
                        ticket.getTicketNumber()
                )
                .userId(
                        ticket.getUser().getId()
                )
                .subject(
                        ticket.getSubject()
                )
                .description(
                        ticket.getDescription()
                )
                .status(
                        ticket.getStatus()
                )
                .priority(
                        ticket.getPriority()
                )
                .category(
                        ticket.getCategory()
                )
                .assignedTo(
                        ticket.getAssignedTo()
                )
                .resolution(
                        ticket.getResolution()
                )
                .resolvedAt(
                        ticket.getResolvedAt()
                )
                .createdAt(
                        ticket.getCreatedAt()
                )
                .updatedAt(
                        ticket.getUpdatedAt()
                )
                .build();
    }
}