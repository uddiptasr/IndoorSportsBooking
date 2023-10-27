package com.booking.app.controller;

import com.booking.app.ResponseDto;
import com.booking.app.responseDTO.BookingDTO;
import com.booking.app.exception.ResourceNotFoundException;
import com.booking.app.model.Booking;
import com.booking.app.responseDTO.BookingListResponseDTO;
import com.booking.app.responseDTO.SportResponseDTO;
import com.booking.app.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;
    /**
     * Saves data in the Booking table. METHOD = Post.
     * @param booking
     *      	             Object containing all the details of the attributes.
     * @return String for successful entry.
     */
    @PostMapping("/addbooking")
    public ResponseEntity<ResponseDto> addBooking(@RequestBody Booking booking ){
        bookingService.save(booking);
        return new ResponseEntity<>(new ResponseDto(new
                BookingListResponseDTO("Booking added succesfully",null), null), HttpStatus.OK);
    }
    /**
     * Retrieves all the records in Booking table. METHOD = Get.
     *
     * @return List of all the records.
     */
    @GetMapping
    public ResponseEntity<ResponseDto> getAllBookings(){
        return new ResponseEntity<>(new ResponseDto(new BookingListResponseDTO(null,
                bookingService.listAll()), null), HttpStatus.OK);
    }
    /**
     * Retrieves the records in Booking table with the given BookingId. METHOD = Get.
     *
     * @param id
     *          BookingId.
     * @return List of all the records which contain that BookingId according to the format specified
     *          by BookingDTO.
     * @throws ResourceNotFoundException
     *             if given Id does not exist
     */
    @GetMapping("/bid/{id}")
    public ResponseEntity<ResponseDto> getbyBookingId(@PathVariable int id ) throws ResourceNotFoundException {
    return new ResponseEntity<>(new ResponseDto(bookingService.getByBookingId(id), null), HttpStatus.OK);
    }
    /**
     * Retrieves the records in Booking table with the given userId. METHOD = Get.
     *
     * @param id
     *                 UserId.
     * @return List of all the records which contain that BookingId according to the format specified
     * by BookingDTO.
     * @throws ResourceNotFoundException
     *             if given Id does not exist
     */
    @GetMapping("/uid/{id}")
    public ResponseEntity<ResponseDto>  getbyuid(@PathVariable int id) throws ResourceNotFoundException {

        return new ResponseEntity<>(new ResponseDto(new BookingListResponseDTO("Bookings made by UserId "+id+" are:"
                ,bookingService.getByUserId(id)), null), HttpStatus.OK);

    }
}
