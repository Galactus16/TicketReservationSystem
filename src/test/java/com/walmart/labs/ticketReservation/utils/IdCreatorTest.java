package com.walmart.labs.ticketReservation.utils;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class IdCreatorTest {

    private ReservationIdCreator resvCreator;
    private Set<String> resvSet;
    private boolean duplicate;

    class CreateIDThread implements Runnable {
        private String id;

        @Override
        public void run() {
            try {
                id = resvCreator.createId();
                synchronized (resvSet) {
                    if (resvSet.contains(id)) duplicate = true;
                    resvSet.add(id);
                }

            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        public String getUniqueId() {
            return id;
        }
    }

    @Test
    public void createUniqueIds() {

        resvCreator = new ReservationIdCreator();
        resvSet = new HashSet<>();
        duplicate = false;

        for (int n = 0; n < 2000; n++) {
            CreateIDThread runner = new CreateIDThread();
            Thread thread = new Thread(runner);
            thread.start();
        }
        Assert.assertFalse(duplicate);
    }

}
