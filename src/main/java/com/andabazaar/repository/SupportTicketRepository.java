package com.andabazaar.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.andabazaar.entity.SupportTicket;
import com.andabazaar.enums.TicketStatus;

public interface SupportTicketRepository
        extends JpaRepository<SupportTicket, Long> {

    Optional<SupportTicket> findByTicketNumber( String ticketNumber);

    boolean existsByTicketNumber( String ticketNumber);

    List<SupportTicket> findByUserIdOrderByCreatedAtDesc( Long userId);

    List<SupportTicket> findByStatusOrderByCreatedAtDesc( TicketStatus status);

    List<SupportTicket> findAllByOrderByCreatedAtDesc();

    long countByStatus( TicketStatus status);
}