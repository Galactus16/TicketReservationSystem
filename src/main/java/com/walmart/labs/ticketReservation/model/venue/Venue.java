package com.walmart.labs.ticketReservation.model.venue;

import com.walmart.labs.ticketReservation.exception.TicketReservationException;
import com.walmart.labs.ticketReservation.model.seat.Seat;
import com.walmart.labs.ticketReservation.model.seat.SeatState;

import java.util.ArrayList;
import java.util.List;

/**
 * Venue defines the location where all the seat are located. We can have multiple venues with their own different seats
 * Venue has maxOccupancy - maximum capacity. We keep track of all the open, held and reserved seats in the venue.
 */
public class Venue {

    private List<List<Seat>> seats;

    private int maxOccupancy;

    private int openSeatCount;

    private int heldSeatCount;

    private int reservedSeatCount;

    /**
     * Initialize the Venue
     * @param row - Number of rows in the Venue
     * @param column - Number of seats each row
     */
    public Venue(int row, int column) {
        if (row <= 0) row = 1;
        if (column <= 0) column = 1;

        seats = new ArrayList<List<Seat>>();
        for (int i = 0; i < row; i++) {
            List<Seat> singleRow = new ArrayList<>();
            for (int j = 0; j < column; j++) {
                singleRow.add(new Seat(row, column));
            }
            seats.add(singleRow);
        }

        this.maxOccupancy = row * column;
        this.openSeatCount = maxOccupancy;
        this.reservedSeatCount = 0;
        this.heldSeatCount = 0;
    }


    public List<List<Seat>> getSeats() {
        return seats;
    }

    public int getNumberOfRows() {
        return seats.size();
    }

    public int getNumberOfSeatsEachRow() {
        return seats.get(0).size();
    }

    public int getMaxOccupancy() {
        return maxOccupancy;
    }

    public int getOpenSeatCount() {
        return openSeatCount;
    }

    public int getHeldSeatCount() {
        return heldSeatCount;
    }

    public int getReservedSeatCount() {
        return reservedSeatCount;
    }

    /**
     * Get an specific seat
     * @param row - row number
     * @param column - column number
     * @return Seat object for that location
     */
    public Seat getSeat(int row, int column) {
        Seat s = null;
        if (row >= 0 && column >= 0 && row < seats.size() && column < seats.get(0).size()) {
            s = seats.get(row).get(column);
        }
        return s;
    }

    /**
     * Process the specific seat - ie change the seatState to given state
     * @param seat - the seat object which is to be processed
     * @param seatState - the final seatState the seat should have
     * @throws Exception - Exceptions
     */
    public synchronized void processSeat(Seat seat, SeatState seatState) throws Exception {
        List<Seat> seatList = new ArrayList<>();
        seatList.add(seat);
        processSeats(seatList, seatState);
    }

    /**
     * Process list of seats
     * @param seats - The list of seats to be processed
     * @param seatState - The seatState to which all needs to be moved
     * @throws Exception
     */
    public synchronized void processSeats(List<Seat> seats, SeatState seatState) throws Exception {

        if (seats == null || seats.size() == 0) {
            throw new IllegalArgumentException("Seats to process are null or empty.");
        }

        if (seatState == null) {
            throw new IllegalArgumentException("SeatState to change should be valid one.");
        }

        for (Seat seat : seats) {
            SeatState currentSeatState = seat.getSeatState();

            if (currentSeatState.equals(seatState)) {
                //Case where the Race condition happened
                continue; //Do nothing
            }

            //Update the seatState and the counter for the corresponding state
            switch (seatState) {
                case OPEN:
                    seat.setStateToOpen();
                    openSeatCount++;
                    break;
                case HOLD:
                    seat.setStateToHold();
                    heldSeatCount++;
                    break;
                case RESERVED:
                    seat.setStateToReserved();
                    reservedSeatCount++;
                    break;
                case PROCESSING:
                    seat.setStateToProcessing();
                    break;
                default:
            }

            //Decrease the currentSeatState counter as we changed the state
            switch (currentSeatState) {
                case OPEN:
                    openSeatCount--;
                    break;
                case HOLD:
                    heldSeatCount--;
                    break;
                case RESERVED:
                    reservedSeatCount--;
                    break;
                default:
            }

        }
    }

    /**
     * Methods to moved the list of seats to SeatState.OPEN
     * @param seats - seats which needs to be put in OPEN state
     * @throws TicketReservationException - Exception
     */
    public void openSelectedSeats(List<Seat> seats) throws TicketReservationException {

        if (seats == null) {
            throw new TicketReservationException("There are no seats to open.");
        }

        try {
            this.processSeats(seats, SeatState.OPEN);
        } catch (Exception e) {
            throw new TicketReservationException("Error clearing the held seats");
        }
    }

}
