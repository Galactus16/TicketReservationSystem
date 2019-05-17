package com.walmart.labs.ticketReservation.service;

import com.walmart.labs.ticketReservation.exception.TicketReservationException;
import com.walmart.labs.ticketReservation.model.seat.Seat;
import com.walmart.labs.ticketReservation.model.seat.SeatHold;
import com.walmart.labs.ticketReservation.model.seat.SeatState;
import com.walmart.labs.ticketReservation.model.venue.Venue;
import com.walmart.labs.ticketReservation.utils.IdCreator;
import com.walmart.labs.ticketReservation.utils.ReservationIdCreator;
import com.walmart.labs.ticketReservation.utils.SeatHoldIdCreator;
import com.walmart.labs.ticketReservation.utils.Utils;

import java.util.*;

/**
 * The implementation of the TicketService Interface
 */
public class TicketServiceImpl implements TicketService {

    private Venue venue;

    //Map to hold SeatHold : Key -> seatHoldId, value -> seatHold Object
    private Map<Integer, SeatHold> seatHoldMap;

    //Map to hold concrete reservation : Key -> reservationId, value -> seatHold Object
    private Map<String, SeatHold> reservationsMap;

    private int seatHoldTimeOut;

    private static IdCreator reservationIdCreator;
    private static IdCreator seatHoldIdCreator;

    private static int DEFAULT_SEAT_HOLD_TIMEOUT = 5;

    /**
     *  Initialize the ticket reservation service with just Venue and default seatHoldTimeOut
     * @param venue - the venue where the service needs to start
     * @throws TicketReservationException - exception
     */
    public TicketServiceImpl(Venue venue) throws TicketReservationException {
        this(venue, DEFAULT_SEAT_HOLD_TIMEOUT);
    }

    /**
     * Initialize the ticket reservation service with just Venue and given seatHoldTimeOut
     * @param venue - the venue where the service needs to start
     * @param seatHoldTimeOut - The duration of the timeOut after which HELD(non-reserved) seats are freed up
     * @throws TicketReservationException - exception
     */
    public TicketServiceImpl(Venue venue, int seatHoldTimeOut) throws TicketReservationException {

        if (venue == null) {
            throw new TicketReservationException("You need to provide the venue.");
        }

        this.venue = venue;

        //Set the seeatHoldTimeOut
        this.seatHoldTimeOut = (seatHoldTimeOut > 0) ? seatHoldTimeOut : DEFAULT_SEAT_HOLD_TIMEOUT;

        seatHoldMap = new HashMap<Integer, SeatHold>();
        reservationsMap = new HashMap<String, SeatHold>();
        reservationIdCreator = new ReservationIdCreator();
        seatHoldIdCreator = new SeatHoldIdCreator();
    }

    /**
     * Get OPEN seat count
     * @return Returns the total number of OPEN seats in the venue
     */
    public int numSeatsAvailable() {
        return venue.getOpenSeatCount();
    }

    /**
     * Method to return the best possible seat in the whole Venue
     * Currently I don't have priority attached with seat,
     * default inherent priority assumed is left-front row > right-last row
     * @return return the best possible seat in the whole Venue
     */
    public Seat findBestSeat() {

        //Check for availability of any seats
        if (numSeatsAvailable() == 0) {
            return null; //As the Interface is fixed, other option is to extend Interface
        }

        //This is not a great way to choose bestSeat : but bestSeat depends on Venue architecture
        // For now I am just looping over from 1st(Front) row and left most (0,0) to last row(Back)-right most seat
        // and the first OPEN seat is the best seat

        for (int row = 0; row < venue.getNumberOfRows(); row++) {
            for (int column = 0; column < venue.getNumberOfSeatsEachRow(); column++) {
                Seat seat = venue.getSeat(row, column);

                //Find if this seat is OPEN : if OPEN then process it for reservation
                if (seat.getSeatState().equals(SeatState.OPEN)) {
                    try {
                        venue.processSeat(seat, SeatState.PROCESSING);
                    } catch (Exception e) {
                        //throw new TicketReservationException("There is an exception processing seat");
                    }

                    return seat;
                }
            }
        }
        return null;
    }

    /**
     * This method does kind of garbage collection of the seats held beyond the seatHoldTimeOut
     * Must be called recursively within some minutes
     */
    public void collectExpiredSeatHolds() {
        Date currentDate = new Date();
        for (Map.Entry<Integer, SeatHold> entry : seatHoldMap.entrySet()) {
            SeatHold seatHold = entry.getValue();
            long timePassed = currentDate.getTime() - seatHold.getCreationTS().getTime();
            if (timePassed >= seatHoldTimeOut) { //Then free the resource
                try {
                    venue.processSeats(seatHold.getSeatsHold(), SeatState.OPEN);
                } catch (Exception exception) {
                    // Error freeing the resource
                }
                seatHoldMap.remove(seatHold.getSeatHoldId(), seatHold);
            }
        }
    }

    /**
     * finds and holds the best possible n (numSeats) seats
     * @param numSeats      the number of seats to find and hold
     * @param customerEmail unique identifier for the customer
     * @return SeatHold object with the heldseats information
     */
    public synchronized SeatHold findAndHoldSeats(int numSeats, String customerEmail) {
        //Check for Availability for Seats
        if (numSeats > numSeatsAvailable()) {
            return null;
        }

        //Check if the user Email is Valid
        if (!Utils.validate(customerEmail)) {
            return null;
        }

        //Now let's try to books all of these seats
        List<Seat> seats = new ArrayList<>();
        for (int i = 0; i < numSeats; i++) {
            Seat seat = findBestSeat();
            if (seat == null) { //No seats can be found - remove others from Hold
                try {
                    venue.processSeats(seats, SeatState.OPEN);
                } catch (Exception e) { //Exception Opening the Held seats
                }
                return null;
            }
            seats.add(seat);
        }

        //Hold all the seats
        try {
            venue.processSeats(seats, SeatState.HOLD);
        } catch (Exception e) {
            try {
                venue.processSeats(seats, SeatState.OPEN);
            } catch (Exception innerException) { //Exception Opening the Held seats
            }
        }

        SeatHold seatHold;
        int seatHoldId = Integer.valueOf(seatHoldIdCreator.createId());
        try {
            seatHold = new SeatHold(seatHoldId, customerEmail, seats);
        } catch (TicketReservationException tre) {
            //Some exception where seats can't be held
            try {
                venue.processSeats(seats, SeatState.OPEN);
            } catch (Exception e) { //Exception Opening the Held seats
            }
            return null;
        }

        seatHoldMap.put(seatHoldId, seatHold);
        return seatHold;
    }

    /**
     * reserve seats after succcessfully holding it.
     * @param seatHoldId    the seat hold identifier
     * @param customerEmail the email address of the customer to which the
     *                      seat hold is assigned
     * @return - ReservationId if reservation is confirmed
     */
    public synchronized String reserveSeats(int seatHoldId, String customerEmail) {

        //Check if the user Email is Valid
        if (!Utils.validate(customerEmail)) {
            return null;
        }

        SeatHold seatHold = seatHoldMap.get(seatHoldId);

        if (seatHold == null) { //Invalid seatHoldId : Either timeOuted or was never there
            return null;
        }

        if (!customerEmail.equals(seatHold.getCustomerEmail())) { //seatHold exists but not for this user
            return null;
        }

        try {
            venue.processSeats(seatHold.getSeatsHold(), SeatState.RESERVED);
        } catch (Exception e) {
            try {
                //Exception reserving the held seats so trying to open it
                venue.processSeats(seatHold.getSeatsHold(), SeatState.OPEN);
            } catch (Exception innerExp) {
                //Exception Opening the held seats
            }
        }

        String reservationId = reservationIdCreator.createId();
        seatHold.setReservationId(reservationId);
        reservationsMap.put(reservationId, seatHold);
        seatHoldMap.remove(seatHoldId, seatHold);
        return reservationId;
    }
}
