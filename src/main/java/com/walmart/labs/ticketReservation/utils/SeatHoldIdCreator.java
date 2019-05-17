package com.walmart.labs.ticketReservation.utils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Implementation if the generic Id Creator for seatHoldId
 */
public class SeatHoldIdCreator implements IdCreator {

    private static AtomicInteger idCounter = new AtomicInteger(0);


    public synchronized String createId() {
        return String.valueOf(idCounter.getAndIncrement());
    }
}
