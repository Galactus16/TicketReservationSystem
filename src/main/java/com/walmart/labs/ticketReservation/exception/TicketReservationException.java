package com.walmart.labs.ticketReservation.exception;

/**
 * Exception handles exception in the TicketReservation service
 */
public class TicketReservationException extends Exception {


    public TicketReservationException() {
        super();
    }

    public TicketReservationException(String message) {
        super(message);
    }

    public TicketReservationException(String message, Throwable cause) {
        super(message, cause);
    }

    public TicketReservationException(Throwable cause) {
        super(cause);
    }

}
