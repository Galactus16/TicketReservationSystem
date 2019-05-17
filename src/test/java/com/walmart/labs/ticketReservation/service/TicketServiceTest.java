package com.walmart.labs.ticketReservation.service;

import com.walmart.labs.ticketReservation.exception.TicketReservationException;
import com.walmart.labs.ticketReservation.model.seat.Seat;
import com.walmart.labs.ticketReservation.model.seat.SeatHold;
import com.walmart.labs.ticketReservation.model.venue.Venue;
import org.junit.Assert;
import org.junit.Test;

public class TicketServiceTest {

    @Test(expected = TicketReservationException.class)
    public void ticketServiceVenueNull() throws TicketReservationException {
        new TicketServiceImpl(null, 30);
    }

    @Test
    public void findNextSeat0() throws TicketReservationException {
        Venue venue1 = new Venue(2, 1);
        TicketServiceImpl service = new TicketServiceImpl(venue1, 30);
        Seat seat1 = service.findBestSeat();
        Seat seat2 = service.findBestSeat();
        Seat seat3 = service.findBestSeat();
        Seat seat4 = service.findBestSeat();

        Assert.assertNotNull(seat1);
        Assert.assertNotNull(seat2);
        Assert.assertNull(seat3);
        Assert.assertNull(seat4);
    }

    @Test
    public void findBestSeat1() throws TicketReservationException {
        Venue venue1 = new Venue(2, 2);
        TicketServiceImpl service = new TicketServiceImpl(venue1, 30);
        Seat seat1 = service.findBestSeat();
        Seat seat2 = service.findBestSeat();
        Seat seat3 = service.findBestSeat();
        Seat seat4 = service.findBestSeat();

        //Everytime different seat should be bestSeat
        Assert.assertTrue(seat1 != seat2 && seat1 != seat3 && seat1 != seat4 &&
                seat2 != seat3 && seat2 != seat4 && seat3 != seat4);
    }

    @Test
    public void findAndHoldTheAvailableSeats() throws TicketReservationException {
        Venue venue1 = new Venue(4, 4);
        TicketServiceImpl service = new TicketServiceImpl(venue1, 500);

        // Availability
        Assert.assertEquals(16, service.numSeatsAvailable());

        service.findAndHoldSeats(2, "abcd@gmail.com");
        Assert.assertEquals(14, service.numSeatsAvailable());
        service.findAndHoldSeats(5, "efgh@gmail.com");
        Assert.assertEquals(9, service.numSeatsAvailable());
        service.findAndHoldSeats(2, "ijkl@gmail.com");
        Assert.assertEquals(7, service.numSeatsAvailable());
        service.findAndHoldSeats(6, "mnop@gmail.com");
        Assert.assertEquals(1, service.numSeatsAvailable());
    }

    @Test
    public void reserveSeatsTest() throws TicketReservationException {
        Venue venue1 = new Venue(5, 5);
        TicketServiceImpl service = new TicketServiceImpl(venue1, 500);

        String email = "anupam.gupta@gmail.com";
        SeatHold seatHold1 = service.findAndHoldSeats(3, email);
        SeatHold seatHold2 = service.findAndHoldSeats(6, email);
        SeatHold seatHold3 = service.findAndHoldSeats(4, email);
        SeatHold seatHold4 = service.findAndHoldSeats(8, email);
        SeatHold seatHold5 = service.findAndHoldSeats(4, email);
        Assert.assertEquals(0, venue1.getOpenSeatCount());
        Assert.assertEquals(25, venue1.getHeldSeatCount());
        Assert.assertEquals(0, venue1.getReservedSeatCount());

        Assert.assertNotNull(service.reserveSeats(seatHold2.getSeatHoldId(), email));
        Assert.assertEquals(0, venue1.getOpenSeatCount());
        Assert.assertEquals(19, venue1.getHeldSeatCount());
        Assert.assertEquals(6, venue1.getReservedSeatCount());

        Assert.assertNotNull(service.reserveSeats(seatHold1.getSeatHoldId(), email));
        Assert.assertNotNull(service.reserveSeats(seatHold3.getSeatHoldId(), email));
        Assert.assertNotNull(service.reserveSeats(seatHold4.getSeatHoldId(), email));
        Assert.assertNotNull(service.reserveSeats(seatHold5.getSeatHoldId(), email));
        Assert.assertEquals(0, venue1.getOpenSeatCount());
        Assert.assertEquals(0, venue1.getHeldSeatCount());
        Assert.assertEquals(25, venue1.getReservedSeatCount());
    }

}
