package com.andersen.domain.mapper;

import com.andersen.domain.dto.booking.BookingDto;
import com.andersen.domain.entity.booking.Booking;
import com.andersen.domain.entity.role.User;
import com.andersen.domain.entity.workspace.Workspace;

public class BookingMapper {

    public static BookingDto toDto(Booking booking) {
        if (booking == null) return null;

        return new BookingDto(
                booking.getId(),
                booking.getCustomer() != null ? booking.getCustomer().getId() : null,
                booking.getWorkspace() != null ? booking.getWorkspace().getId() : null,
                booking.getStartTime(),
                booking.getEndTime(),
                booking.getStatus(),
                booking.isActive(),
                booking.getTotalPrice()
        );
    }

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
