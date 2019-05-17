package com.walmart.labs.ticketReservation.model.seat;

import com.walmart.labs.ticketReservation.exception.TicketReservationException;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class SeatHoldTest {

    @Test
    public void seatHoldInitialization() throws TicketReservationException {

        List<Seat> seatList = new ArrayList<>(Arrays.asList(new Seat(1, 3),
                new Seat(2, 7),
                new Seat(3, 2)));

        SeatHold seatHold1 = new SeatHold(7, "anupam.gupta@gmail.com", seatList);

        Date date = new Date();

        Assert.assertEquals(7, seatHold1.getSeatHoldId());
        Assert.assertEquals("anupam.gupta@gmail.com", seatHold1.getCustomerEmail());
        Assert.assertTrue(seatList.containsAll(seatHold1.getSeatsHold()));
        Assert.assertTrue(seatHold1.getCreationTS().getTime() - date.getTime() <= 2);

    }

}
