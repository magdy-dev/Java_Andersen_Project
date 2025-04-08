package com.andersen.domain.mapper;

import com.andersen.domain.dto.booking.BookingDto;
import com.andersen.domain.dto.userrole.UserDto;
import com.andersen.domain.dto.workspace.WorkspaceDto;
import com.andersen.domain.entity.booking.Booking;
import com.andersen.domain.entity.role.User;
import com.andersen.domain.entity.workspace.Workspace;

/**
 * Mapper class for converting between Booking entities and Booking Data Transfer Objects (DTOs).
 * This class provides methods to translate Booking data to and from User and Workspace representations
 * to facilitate data transfer across different layers of the application.
 */
public class BookingMapper {

    /**
     * Converts a Booking entity to a BookingDto.
     *
     * @param booking the Booking entity to be converted
     * @return a BookingDto representing the provided Booking entity, or null if the booking is null
     */
    public static BookingDto toDto(Booking booking) {
        if (booking == null) return null;

        return new BookingDto(
                booking.getId(),
                UserDto.toDto(booking.getCustomer()),
                WorkspaceDto.toDto(booking.getWorkspace()),
                booking.getStartTime(),
                booking.getEndTime(),
                booking.getStatus(),
                booking.isActive(),
                booking.getTotalPrice()
        );
    }

    /**
     * Converts a BookingDto to a Booking entity.
     *
     * @param dto       the BookingDto to be converted
     * @param customer  the User entity representing the customer making the booking
     * @param workspace the Workspace entity associated with the booking
     * @return a Booking entity representing the provided BookingDto, or null if the dto is null
     */
    public static Booking toEntity(BookingDto dto, User customer, Workspace workspace) {
        if (dto == null) return null;

        Booking booking = new Booking();
        booking.setId(dto.getId());
        booking.setCustomer(customer);
        booking.setWorkspace(workspace);
        booking.setStartTime(dto.getStartTime());
        booking.setEndTime(dto.getEndTime());
        booking.setStatus(dto.getStatus());
        booking.setActive(dto.isActive());
        booking.setTotalPrice(dto.getTotalPrice());

        return booking;
    }
}