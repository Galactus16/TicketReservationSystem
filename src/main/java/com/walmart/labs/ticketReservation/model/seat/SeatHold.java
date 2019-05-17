package com.walmart.labs.ticketReservation.model.seat;

import com.walmart.labs.ticketReservation.exception.TicketReservationException;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * SeatHold denotes the group or list of Seats and create a atomic unit
 * Each of this unit has an Id, and userEmail who booked these list of seats
 * The seats are held until seatHoldTimeOut
 * If the seat held are finally reserved - then we would have reservationId
 */
public class SeatHold {

    private final int seatHoldId;
    private final String customerEmail;
    private List<Seat> seatsHold;
    private Date creationTS;
    private String reservationId;

    public SeatHold(int seatHoldId, String customerEmail, List<Seat> seatsHold) throws TicketReservationException {
        this.seatHoldId = seatHoldId;
        this.customerEmail = customerEmail;
        this.seatsHold = new LinkedList<Seat>(seatsHold);
        creationTS = new Date();
    }

    public int getSeatHoldId() {
        return seatHoldId;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public List<Seat> getSeatsHold() {
        return seatsHold;
    }

    public Date getCreationTS() {
        return creationTS;
    }

    public String getReservationId() {
        return reservationId;
    }

    /**
     * Should only be called when the all the seats in SeatHold are reserved
     * @param reservationId - the reservationId after the held stage
     */
    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

}
