package org.andersen.service.booking;


import org.andersen.entity.booking.Booking;
import org.andersen.entity.users.Customer;
import org.andersen.entity.workspace.Availability;
import org.andersen.entity.workspace.Workspace;
import org.andersen.service.workspace.WorkspaceServiceImpl;

import java.util.Collections;
import java.util.List;

public class BookingServiceImpl implements BookingService{
    private final List<Booking> bookings;
    private final WorkspaceServiceImpl workspaceServiceImpl;

    public List<Booking> getAllBookings() {
        return Collections.unmodifiableList(bookings);
    }

    public BookingServiceImpl(List<Booking> bookings, WorkspaceServiceImpl workspaceServiceImpl) {
        this.bookings = bookings;
        this.workspaceServiceImpl = workspaceServiceImpl;
    }

    public void makeReservation(Customer customer, int workspaceIndex, int availabilityIndex) {
        Workspace workspace = workspaceServiceImpl.getAllWorkspaces().get(workspaceIndex);
        Availability availability = workspace.getAvailabilities().get(availabilityIndex);

        if (availability.getRemaining() > 0) {
            availability.decrement();
            Booking booking = new Booking(customer, workspace, availability.getDate(), availability.getTime());
            bookings.add(booking);
            customer.getBookings().add(booking);
        }
    }

    public void cancelReservation(Customer customer, int bookingIndex) {
        Booking booking = customer.getBookings().remove(bookingIndex);
        bookings.remove(booking);

        booking.getWorkspace().getAvailabilities().stream()
                .filter(avail -> avail.getDate().equals(booking.getDate()) &&
                        avail.getTime().equals(booking.getTime()))
                .findFirst()
                .ifPresent(Availability::increment);
    }

    public List<Booking> getCustomerBookings(Customer customer) {
        return customer.getBookings();
    }
}