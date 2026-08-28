package com.andabazaar.service;

import java.util.List;

import com.andabazaar.dto.support.SupportReplyDto;
import com.andabazaar.dto.support.SupportTicketRequestDto;
import com.andabazaar.dto.support.SupportTicketResponseDto;

public interface SupportTicketService {

    SupportTicketResponseDto createTicket( SupportTicketRequestDto request);

    SupportTicketResponseDto getTicketById( Long id);

    SupportTicketResponseDto getTicketByNumber( String ticketNumber);

    List<SupportTicketResponseDto> getAllTickets();

    List<SupportTicketResponseDto> getUserTickets( Long userId);

    List<SupportTicketResponseDto> getTicketsByStatus( String status);

    SupportTicketResponseDto updateTicket( Long id, SupportTicketRequestDto request);

    void closeTicket( Long id, String resolution);

    void reopenTicket( Long id);

    void deleteTicket( Long id);

    void addReply( Long ticketId, SupportReplyDto request);

    long countByStatus( String status);
}