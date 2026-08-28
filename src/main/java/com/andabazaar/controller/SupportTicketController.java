package com.andabazaar.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.andabazaar.dto.support.SupportReplyDto;
import com.andabazaar.dto.support.SupportTicketRequestDto;
import com.andabazaar.dto.support.SupportTicketResponseDto;
import com.andabazaar.service.SupportTicketService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/support-tickets")
@RequiredArgsConstructor
public class SupportTicketController {

    private final SupportTicketService supportTicketService;

    // Create ticket
    @PostMapping
    public ResponseEntity<SupportTicketResponseDto> createTicket(
            @Valid @RequestBody SupportTicketRequestDto request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        supportTicketService.createTicket(request)
                );
    }

    // Get ticket by ID
    @GetMapping("/{id}")
    public ResponseEntity<SupportTicketResponseDto> getTicketById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                supportTicketService.getTicketById(id)
        );
    }

    // Get ticket by ticket number
    @GetMapping("/number/{ticketNumber}")
    public ResponseEntity<SupportTicketResponseDto> getTicketByNumber(
            @PathVariable String ticketNumber) {

        return ResponseEntity.ok(
                supportTicketService.getTicketByNumber(
                        ticketNumber
                )
        );
    }

    // Get all tickets
    @GetMapping
    public ResponseEntity<List<SupportTicketResponseDto>> getAllTickets() {

        return ResponseEntity.ok(
                supportTicketService.getAllTickets()
        );
    }

    // Get user's tickets
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SupportTicketResponseDto>> getUserTickets(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                supportTicketService.getUserTickets(userId)
        );
    }

    // Get tickets by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<SupportTicketResponseDto>> getTicketsByStatus(
            @PathVariable String status) {

        return ResponseEntity.ok(
                supportTicketService.getTicketsByStatus(status)
        );
    }

    // Update ticket
    @PutMapping("/{id}")
    public ResponseEntity<SupportTicketResponseDto> updateTicket(
            @PathVariable Long id,
            @Valid @RequestBody SupportTicketRequestDto request) {

        return ResponseEntity.ok(
                supportTicketService.updateTicket(
                        id,
                        request
                )
        );
    }

    // Close ticket
    @PutMapping("/{id}/close")
    public ResponseEntity<Void> closeTicket(
            @PathVariable Long id,
            @RequestParam String resolution) {

        supportTicketService.closeTicket(
                id,
                resolution
        );

        return ResponseEntity.ok().build();
    }

    // Reopen ticket
    @PutMapping("/{id}/reopen")
    public ResponseEntity<Void> reopenTicket(
            @PathVariable Long id) {

        supportTicketService.reopenTicket(id);

        return ResponseEntity.ok().build();
    }

    // Add reply
    @PostMapping("/{ticketId}/replies")
    public ResponseEntity<Void> addReply(
            @PathVariable Long ticketId,
            @Valid @RequestBody SupportReplyDto request) {

        supportTicketService.addReply(
                ticketId,
                request
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    // Count tickets by status
    @GetMapping("/count/status/{status}")
    public ResponseEntity<Long> countByStatus(
            @PathVariable String status) {

        return ResponseEntity.ok(
                supportTicketService.countByStatus(status)
        );
    }

    // Delete ticket
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(
            @PathVariable Long id) {

        supportTicketService.deleteTicket(id);

        return ResponseEntity.noContent().build();
    }
}