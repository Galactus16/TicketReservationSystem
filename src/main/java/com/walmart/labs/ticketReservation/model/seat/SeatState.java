package com.walmart.labs.ticketReservation.model.seat;

/**
 * This ENUM defines the SeatState allowed.
 */
public enum SeatState {

    OPEN("Open for reservation, holding or processing."),
    HOLD("At present on hold - can be freed(expire) or reserved next"),
    RESERVED("Booked - can only be cancelled now"),
    PROCESSING("Transitional State when application is working on changing the state of the seat.");

    private final String stateDesc;

    /**
     * To initialize the ErrorCategory ENUM
     *
     * @param stateDesc - The description for the given the ENUM
     */
    SeatState(String stateDesc) {
        this.stateDesc = stateDesc;
    }

    /**
     * Get the Description for the given ENUM seatState
     *
     * @return The String corresponding to the ENUM seatState description
     */
    public String getStateDesc() {
        return stateDesc;
    }
}
