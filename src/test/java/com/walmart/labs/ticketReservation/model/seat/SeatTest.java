package com.walmart.labs.ticketReservation.model.seat;

import com.walmart.labs.ticketReservation.exception.TicketReservationException;
import com.walmart.labs.ticketReservation.model.seat.Seat;
import com.walmart.labs.ticketReservation.model.venue.Venue;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class SeatTest {

    @Test
    public void seatInitialization() {
        Seat s1 = new Seat(2, 5);
        Seat s2 = new Seat(0, 0);
        Seat s3 = new Seat(-2, -8);
        Seat s4 = new Seat(-5, 9);

        // Initialized seats have ID as assigned to them.
        Assert.assertEquals("2-5", s1.getId());
        Assert.assertEquals("0-0", s2.getId());
        Assert.assertEquals("-2--8", s3.getId());
        Assert.assertEquals("-5-9", s4.getId());

        // Initialized seats start with open seat state.
        Assert.assertEquals(SeatState.OPEN, s1.getSeatState());
        Assert.assertEquals(SeatState.OPEN, s2.getSeatState());
        Assert.assertEquals(SeatState.OPEN, s3.getSeatState());
        Assert.assertEquals(SeatState.OPEN, s4.getSeatState());

        // Initialized seats have given row num
        Assert.assertEquals(2, s1.getRow());
        Assert.assertEquals(0, s2.getRow());
        Assert.assertEquals(-2, s3.getRow());
        Assert.assertEquals(-5, s4.getRow());

        // Initialized seats have given seatNum
        Assert.assertEquals(5, s1.getSeatNum());
        Assert.assertEquals(0, s2.getSeatNum());
        Assert.assertEquals(-8, s3.getSeatNum());
        Assert.assertEquals(9, s4.getSeatNum());

    }

    @Test
    public void seatStateChange() {

        Seat s1 = new Seat(4, 0);
        Seat s2 = new Seat(7, 4);
        Seat s3 = new Seat(-1, -1);

        s1.setStateToHold();
        s2.setStateToReserved();
        s3.setStateToProcessing();

        Assert.assertEquals(SeatState.HOLD, s1.getSeatState());
        Assert.assertEquals(SeatState.RESERVED, s2.getSeatState());
        Assert.assertEquals(SeatState.PROCESSING, s3.getSeatState());

        s3.setStateToOpen();
        Assert.assertEquals(SeatState.OPEN, s3.getSeatState());
    }

    @Test(expected = IllegalArgumentException.class)
    public void processSeatsNull0() throws Exception {
        new Venue(20, 20).processSeats(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void processSeatsNull1() throws Exception {
        new Venue(20, 20).processSeats(new ArrayList<Seat>(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void processSeatsNoSeats() throws Exception {
        new Venue(20, 20).processSeats(new ArrayList<Seat>(), SeatState.RESERVED);
    }

    //Helper funtion for some of the test cases here
    public boolean checkAllSeatsIsInState(Venue venue, SeatState seatState) {
        for (int r = 0; r < venue.getNumberOfRows(); r++) {
            for (int c = 0; c < venue.getNumberOfSeatsEachRow(); c++) {
                if (!venue.getSeat(r, c).getSeatState().equals(seatState)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Test
    public void processSeats0() throws Exception {
        Venue venue1 = new Venue(5, 5);


        List<Seat> seats = new ArrayList<Seat>();
        for (int r = 0; r < venue1.getNumberOfRows(); r++) {
            for (int c = 0; c < venue1.getNumberOfSeatsEachRow(); c++) {
                seats.add(venue1.getSeat(r, c));
            }
        }

        //Try to hold the seat
        venue1.processSeats(seats, SeatState.HOLD);

        //Try to overwrite with the same SeatState
        venue1.processSeats(seats, SeatState.HOLD);

        // Test if all the seats are held
        Assert.assertTrue(checkAllSeatsIsInState(venue1, SeatState.HOLD));

        venue1.processSeats(seats, SeatState.RESERVED);

        // Test if all the seats are reserved
        Assert.assertTrue(checkAllSeatsIsInState(venue1, SeatState.RESERVED));

        Assert.assertEquals(0, venue1.getOpenSeatCount());
        Assert.assertEquals(0, venue1.getHeldSeatCount());
        Assert.assertEquals(25, venue1.getReservedSeatCount());
    }

    @Test
    public void processSeats1() throws Exception {
        Venue venue1 = new Venue(5, 5);

        List<Seat> seats = new ArrayList<Seat>();

        /**
         *
         * H H H O O
         * H H H O O
         * H H H O O
         * O O O O O
         * O O O O O
         */

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                seats.add(venue1.getSeat(r, c));
            }
        }
        venue1.processSeats(seats, SeatState.HOLD);

        // Test if they are held or not
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                Assert.assertEquals(SeatState.HOLD, venue1.getSeat(r, c).getSeatState());
            }
        }

        // Test if the rest are Open
        for (int r = 3; r <= 4; r++) {
            for (int c = 3; c <= 4; c++) {
                Assert.assertEquals(SeatState.OPEN, venue1.getSeat(r, c).getSeatState());
            }
        }

        for (int r = 0; r <= 4; r++) {
            for (int c = 3; c <= 4; c++) {
                Assert.assertEquals(SeatState.OPEN, venue1.getSeat(r, c).getSeatState());
            }
        }

        Assert.assertEquals(16, venue1.getOpenSeatCount());
        Assert.assertEquals(9, venue1.getHeldSeatCount());
        Assert.assertEquals(0, venue1.getReservedSeatCount());
    }

    @Test(expected = TicketReservationException.class)
    public void openSelectSeats0() throws Exception {
        new Venue(5, 5).openSelectedSeats(null);
    }

    @Test
    public void openSelectSeats1() throws Exception {
        Venue venue1 = new Venue(5, 5);


        List<Seat> seats = new ArrayList<Seat>();
        for (int r = 0; r < venue1.getNumberOfRows(); r++) {
            for (int c = 0; c < venue1.getNumberOfSeatsEachRow(); c++) {
                seats.add(venue1.getSeat(r, c));
            }
        }

        //Try to hold the seat
        venue1.processSeats(seats, SeatState.HOLD);

        // Test if all the seats are held
        Assert.assertTrue(checkAllSeatsIsInState(venue1, SeatState.HOLD));

        //Open all the seats
        venue1.openSelectedSeats(seats);

        // Test if all the seats are open
        Assert.assertTrue(checkAllSeatsIsInState(venue1, SeatState.OPEN));

    }
}
