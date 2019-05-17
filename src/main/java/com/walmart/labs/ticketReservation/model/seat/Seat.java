package com.walmart.labs.ticketReservation.model.seat;

/**
 * Seat is the atomic entity in this service. Seat consists of the row number and the seatNum.
 * The id is formed in this format : "rowNum-seatNum"
 * Initially all seats are opened when the Venue begins
 */
public class Seat {


    private String id;
    private int row;
    private int seatNum;
    private SeatState state;

    public Seat(int row, int seatNum) {
        this.row = row;
        this.seatNum = seatNum;
        this.id = String.format("%d-%d", this.row, this.seatNum);
        state = SeatState.OPEN;
    }

    public SeatState getSeatState() {
        return state;
    }

    public String getId() {
        return id;
    }

    public int getSeatNum() {
        return seatNum;
    }

    public int getRow() {
        return row;
    }

    public void setStateToOpen() {
        state = SeatState.OPEN;
    }

    public void setStateToHold() {
        state = SeatState.HOLD;
    }

    public void setStateToReserved() {
        state = SeatState.RESERVED;
    }

    public void setStateToProcessing() {
        state = SeatState.PROCESSING;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Seat) {
            return this.id.equals(((Seat) o).id);
        } else {
            throw new IllegalArgumentException(String.format("Type is invalid: %s", o.getClass().getName()));
        }
    }

}
