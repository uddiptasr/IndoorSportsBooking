# Indoor-Sports-Arena-Booking

## Project Deliverables

- [x] Design an indoor sports arena online booking application using Spring Boot and MySQL RDBMS.

- [x] Every sport/game can have one or more courts/boards (Ex: Badminton can have 4 courts in our sports arena)

- [x] Customers can book a sport/game with timeslots in multiples of 30 minutes. (i.e 30m, 1h, 1.5h etc). The slots can begin in multiples of 15 minutes (10.15 to 10.45, 15:30 to 17:00). only one court/board can be booked.

- [x] The timing of the arena will be different for weekdays and weekends. But no weekly holiday.

- [x] Every sport/game will have a fixed price per 30 minute slot. (Ex: Badminton can cost 100 rs for 30 minutes, Billiards can cost 200 rs for 30 minutes etc)

- [x] There can be holidays during which booking should not be allowed. A separate service tells whether a given day is a holiday or not for the arena.

- [x] The number of players is not a factor. (6 people can book a badminton court but only 4 can play at a time)

- [x] The customers have to be registered on our application for them to be able to book. Users use a username and password to register

## Implementation
1. User registration
2. User login.
3. Given a date and a game/sport, return the list of available courts/boards and slots for the day.
4. Given a sport, display the available courts/boards and available slots for the next 5 days.
5. A dummy payment API to collect the details of the amount paid, date and mode of payment.
6. After payment is done, a Book API to Book the game/sport + court/board for a given a date and time duration and generate an orderId
7. Given a userid, show all the past bookings
8. Given an orderid, show the booking details.
