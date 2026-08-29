package com.andabazaar.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.andabazaar.dto.support.SupportReplyDto;
import com.andabazaar.dto.support.SupportTicketRequestDto;
import com.andabazaar.dto.support.SupportTicketResponseDto;
import com.andabazaar.service.SupportTicketService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Support Tickets", description = "Customer support ticket management")
@RestController
@RequestMapping("/api/support-tickets")
@RequiredArgsConstructor
public class SupportTicketController {

    private final SupportTicketService supportTicketService;

    // Create ticket
    @Operation(summary = "Create Ticket")
    @PostMapping
    public ResponseEntity<SupportTicketResponseDto> createTicket(@Valid @RequestBody SupportTicketRequestDto request) {

 return ResponseEntity.status(HttpStatus.CREATED).body(supportTicketService.createTicket(request));
    }

    // Get ticket by ID
    @Operation(summary = "Get Ticket By Id")
    @GetMapping("/{id}")
    public ResponseEntity<SupportTicketResponseDto> getTicketById(@PathVariable Long id) {

 return ResponseEntity.ok(supportTicketService.getTicketById(id));
    }

    // Get ticket by ticket number
    @Operation(summary = "Get Ticket By Number")
    @GetMapping("/number/{ticketNumber}")
    public ResponseEntity<SupportTicketResponseDto> getTicketByNumber(@PathVariable String ticketNumber) {

 return ResponseEntity.ok(supportTicketService.getTicketByNumber(ticketNumber));
    }

    // Get all tickets
    @Operation(summary = "Get All Tickets")
    @GetMapping
    public ResponseEntity<List<SupportTicketResponseDto>> getAllTickets() {

 return ResponseEntity.ok(supportTicketService.getAllTickets());
    }

    // Get user's tickets
    @Operation(summary = "Get User Tickets")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SupportTicketResponseDto>> getUserTickets(@PathVariable Long userId) {

 return ResponseEntity.ok(supportTicketService.getUserTickets(userId));
    }

    // Get tickets by status
    @Operation(summary = "Get Tickets By Status")
    @GetMapping("/status/{status}")
    public ResponseEntity<List<SupportTicketResponseDto>> getTicketsByStatus(@PathVariable String status) {

 return ResponseEntity.ok(supportTicketService.getTicketsByStatus(status));
    }

    // Update ticket
    @Operation(summary = "Update Ticket")
    @PutMapping("/{id}")
    public ResponseEntity<SupportTicketResponseDto> updateTicket(@PathVariable Long id,
            @Valid @RequestBody SupportTicketRequestDto request) {

 return ResponseEntity.ok(supportTicketService.updateTicket(id, request));
    }

    // Close ticket
    @Operation(summary = "Close Ticket")
    @PutMapping("/{id}/close")
    public ResponseEntity<Void> closeTicket(@PathVariable Long id, @RequestParam String resolution) {

        supportTicketService.closeTicket( id, resolution);

 return ResponseEntity.ok().build();
    }

    // Reopen ticket
    @Operation(summary = "Reopen Ticket")
    @PutMapping("/{id}/reopen")
    public ResponseEntity<Void> reopenTicket(@PathVariable Long id) {

        supportTicketService.reopenTicket(id);

 return ResponseEntity.ok().build();
    }

    // Add reply
    @Operation(summary = "Add Reply")
    @PostMapping("/{ticketId}/replies")
    public ResponseEntity<Void> addReply(@PathVariable Long ticketId, @Valid @RequestBody SupportReplyDto request) {

        supportTicketService.addReply( ticketId, request);

 return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // Count tickets by status
    @Operation(summary = "Count By Status")
    @GetMapping("/count/{status}")
    public ResponseEntity<Long> countByStatus(@PathVariable String status) {

 return ResponseEntity.ok(supportTicketService.countByStatus(status));
    }

    // Delete ticket
    @Operation(summary = "Delete Ticket")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {

        supportTicketService.deleteTicket(id);

 return ResponseEntity.noContent().build();
    }
}