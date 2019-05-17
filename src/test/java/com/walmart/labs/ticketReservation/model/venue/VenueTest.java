package com.walmart.labs.ticketReservation.model.venue;

import org.junit.Assert;
import org.junit.Test;

public class VenueTest {


    @Test
    public void venueInitialization() {

        Venue venue1 = new Venue(-4, -7);
        Venue venue2 = new Venue(5, 3);
        Venue venue3 = new Venue(0, 0);

        // Initialized venue are not null
        Assert.assertNotNull(venue1);
        Assert.assertNotNull(venue2);
        Assert.assertNotNull(venue3);

        // Initialized venue has maxOccupancy
        Assert.assertEquals(1, venue1.getMaxOccupancy());
        Assert.assertEquals(15, venue2.getMaxOccupancy());
        Assert.assertEquals(1, venue3.getMaxOccupancy());

        // Initialized Venue has counts
        Assert.assertEquals(1, venue1.getOpenSeatCount());
        Assert.assertEquals(0, venue1.getHeldSeatCount());
        Assert.assertEquals(0, venue1.getReservedSeatCount());
        Assert.assertEquals(15, venue2.getOpenSeatCount());
        Assert.assertEquals(0, venue2.getHeldSeatCount());
        Assert.assertEquals(0, venue2.getReservedSeatCount());
        Assert.assertEquals(1, venue3.getOpenSeatCount());
        Assert.assertEquals(0, venue3.getHeldSeatCount());
        Assert.assertEquals(0, venue3.getReservedSeatCount());

        // Initialized venue has these rows and seatsEachRow
        Assert.assertEquals(1, venue1.getNumberOfRows());
        Assert.assertEquals(1, venue1.getNumberOfSeatsEachRow());
        Assert.assertEquals(5, venue2.getNumberOfRows());
        Assert.assertEquals(3, venue2.getNumberOfSeatsEachRow());
        Assert.assertEquals(1, venue3.getNumberOfRows());
        Assert.assertEquals(1, venue3.getNumberOfSeatsEachRow());

    }

    @Test
    public void venueSeatInitialization() {

        Venue venue1 = new Venue(50, 50);

        // Return a valid seat
        Assert.assertNotNull(venue1.getSeat(45, 0));
        Assert.assertNotNull(venue1.getSeat(49, 49));
        Assert.assertNotNull(venue1.getSeat(0, 0));
        Assert.assertNotNull(venue1.getSeat(0, 47));

        // Return null
        Assert.assertNull(venue1.getSeat(-4, 8));
        Assert.assertNull(venue1.getSeat(67, 40));
        Assert.assertNull(venue1.getSeat(-3, -3));
        Assert.assertNull(venue1.getSeat(8, -2));

        // Get seats
        Assert.assertNotNull(venue1.getSeats());

    }

}
