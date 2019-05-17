### Assumptions

1. The venue has the seating arrangement in the rectangural ROW * COLUMN form. This denotes the total capacity of the Venue
2. TicketService interface hs no signature to handle exception. Because of this reasons - I have returned null at many place where I could return some specific exceptions.
3. Users can hold any number of seats. User must HOLD a seat before reserving them. There is a defailtTimeOut before which the held seats should be reserved.
4. Best Seat definition was left to the developer and so I have taken the priority to be front row has greater priority than last row. Left seat has higher priority than right seat.

### Instructions

#### Installing

You should have git and Java on your system
```
git clone https://github.com/Galactus16/TicketReservationSystem.git
```

#### Building

`./gradlew assemble`

#### Testing

`./gradlew check`