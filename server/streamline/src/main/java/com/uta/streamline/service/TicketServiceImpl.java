package com.uta.streamline.service;

import com.uta.streamline.dto.CommentDetails;
import com.uta.streamline.dto.ProjectDetails;
import com.uta.streamline.dto.TicketDetails;
import com.uta.streamline.entities.Comment;
import com.uta.streamline.entities.Project;
import com.uta.streamline.entities.Ticket;
import com.uta.streamline.entities.User;
import com.uta.streamline.enums.Priority;
import com.uta.streamline.enums.Status;
import com.uta.streamline.repository.ProjectRepository;
import com.uta.streamline.repository.TicketRepository;
import com.uta.streamline.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TicketServiceImpl {
    private final TicketRepository ticketRepository;
    private final UserServiceImpl userService;
    private final ProjectRepository projectRepository;

    public TicketDetails create(TicketDetails ticketDetails) {
        Ticket ticket = new Ticket();

        ticket.setAssignee(userService.findByUsername(ticketDetails.getAssignee()));
        ticket.setAssignedTo(userService.findByUsername(ticketDetails.getAssignedTo()));
        ticket.setCreateDate(ticketDetails.getCreateDate());
        ticket.setDescription(ticketDetails.getDescription());
        ticket.setDueDate(ticketDetails.getDueDate());
        ticket.setEstimatedTime(ticketDetails.getEstimatedTime());
        ticket.setActualTime(ticketDetails.getActualTime());
        ticket.setPriority(Priority.HIGH);
        ticket.setStatus(Status.OPEN);
        ticket.setSummary(ticketDetails.getSummary());

        Ticket createdTicket = ticketRepository.save(ticket);
        ticketDetails.setTicketId(createdTicket.getTicketId());
        return ticketDetails;
    }

    public TicketDetails createOrUpdate(TicketDetails ticketDetails) {
        Ticket ticket = null;
        if (!Objects.isNull(ticketDetails.getTicketId())) {
            ticket = ticketRepository.getById(ticketDetails.getTicketId());
        }
        else {
            ticket = new Ticket();
        }
        ticket.setAssignee(userService.findByUsername(ticketDetails.getAssignee()));
        ticket.setAssignedTo(userService.findByUsername(ticketDetails.getAssignedTo()));
        ticket.setCreateDate(ticketDetails.getCreateDate());
        ticket.setDescription(ticketDetails.getDescription());
        ticket.setDueDate(ticketDetails.getDueDate());
        ticket.setEstimatedTime(ticketDetails.getEstimatedTime());
        ticket.setActualTime(ticketDetails.getActualTime());
        ticket.setPriority(Priority.valueOf(ticketDetails.getPriority()));
        ticket.setStatus(Status.valueOf(ticketDetails.getStatus()));
        ticket.setSummary(ticketDetails.getSummary());

        Ticket createdTicket = ticketRepository.save(ticket);
        ticketDetails.setTicketId(createdTicket.getTicketId());
        return ticketDetails;
    }

    public List<TicketDetails> getAllTickets() {
        List<Ticket> tickets = ticketRepository.findAll();
        List<TicketDetails> ticketDetailsList = new ArrayList<>();
        for (Ticket ticket : tickets) {
            ticketDetailsList.add(mapTicketToTicketDetails(ticket));
        }
        return ticketDetailsList;
    }

    public List<TicketDetails> getTicketsByAssignee(Long userId) {
        Optional<List<Ticket>> ticketsOptional = ticketRepository.findTicketByAssignee(userId);
        List<TicketDetails> ticketDetailsList = new ArrayList<>();
        for (Ticket ticket : ticketsOptional.get()) {
            ticketDetailsList.add(mapTicketToTicketDetails(ticket));
        }
        return ticketDetailsList;
    }

    public List<TicketDetails> getTicketsByAssignedTo(Long userId) {
        Optional<List<Ticket>> ticketsOptional = ticketRepository.findTicketByAssignedTo(userId);
        List<TicketDetails> ticketDetailsList = new ArrayList<>();
        for (Ticket ticket : ticketsOptional.get()) {
            ticketDetailsList.add(mapTicketToTicketDetails(ticket));
        }
        return ticketDetailsList;
    }

    public TicketDetails getTicketByTicketId(Long ticketId) {
        Ticket ticket = ticketRepository.getById(ticketId);
        return mapTicketToTicketDetails(ticket);
    }

    public void deleteTicket(Long ticketId) {
        ticketRepository.deleteById(ticketId);
    }

    private TicketDetails mapTicketToTicketDetails(Ticket ticket) {
        TicketDetails ticketDetails = new TicketDetails();
        ticketDetails.setTicketId(ticket.getTicketId());
        ticketDetails.setActualTime(ticket.getActualTime());
        ticketDetails.setEstimatedTime(ticket.getEstimatedTime());
        ticketDetails.setAssignee(ticket.getAssignee().getUserName());
        ticketDetails.setAssignedTo(ticket.getAssignedTo().getUserName());
        ticketDetails.setPriority(ticket.getPriority().name());
        ticketDetails.setStatus(ticket.getStatus().name());
        ticketDetails.setDescription(ticket.getDescription());
        ticketDetails.setSummary(ticket.getSummary());
        ticketDetails.setDueDate(ticket.getDueDate());
        return ticketDetails;
    }

    public List<TicketDetails> getTicketsByProjectId(Long projectId) {
        List<Ticket> tickets = projectRepository.findById(projectId).get().getTickets();
        List<TicketDetails> ticketDetailsList = new ArrayList<>();
        for (Ticket ticket : tickets) {
            ticketDetailsList.add(mapTicketToTicketDetails(ticket));
        }
        return ticketDetailsList;
    }

    public List<CommentDetails> getCommentsByTicket(Long ticketId) {
        List<Comment> comments = ticketRepository.getById(ticketId).getComments();
        List<CommentDetails> commentDetailsList = new ArrayList<>();
        for (Comment comment : comments) {
            commentDetailsList.add(mapCommentToCommentDetails(comment));
        }
        return commentDetailsList;
    }

    private CommentDetails mapCommentToCommentDetails(Comment comment) {
        CommentDetails commentDetails = new CommentDetails();
        commentDetails.setCommentId(comment.getCommentId());
        commentDetails.setTicket(comment.getTicket());
        commentDetails.setCommentText(comment.getCommentText());
        commentDetails.setTimestamp(comment.getTimestamp());
        commentDetails.setUser(comment.getUser());
        return commentDetails;
    }
}
