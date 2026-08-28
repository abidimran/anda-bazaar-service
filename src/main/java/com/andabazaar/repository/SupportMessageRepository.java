package com.andabazaar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.andabazaar.entity.SupportMessage;

public interface SupportMessageRepository
        extends JpaRepository<SupportMessage, Long> {

    List<SupportMessage> findByTicketIdOrderByCreatedAtAsc(
            Long ticketId
    );

    List<SupportMessage> findByUserIdOrderByCreatedAtDesc(
            Long userId
    );

    long countByTicketId(
            Long ticketId
    );
}