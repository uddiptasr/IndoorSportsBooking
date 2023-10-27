package com.booking.app.service;

import com.booking.app.model.Payment;
import com.booking.app.responseDTO.BookingDTO;
import com.booking.app.exception.ResourceNotFoundException;
import com.booking.app.model.Booking;
import com.booking.app.repository.BookingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository ;
    /**
     * Function to save an object to booking Table
     *
     * @param booking
     *            An object with details of booking Table
     * @return null
     */
    public void save(Booking booking){
        bookingRepository.save(booking);
    }
    /**
     * Function to retrieve all records in Booking Table
     *
     * @return List of all records in Booking Table
     */
    public List<BookingDTO> listAll(){
        List<Booking> bookingList = bookingRepository.findAll();
        List<BookingDTO> bookingDTOList = new ArrayList<BookingDTO>();
        for(int i=0;i<bookingList.size();i++){
            bookingDTOList.add(this.createbookDTO(bookingList.get(i)));
        }
        return bookingDTOList;
    }

    /**
     * Function to retrieve a single record in Booking Table
     *
     * @param id
     *            A specific bookingId
     * @return  Objects with given ID from Booking table
     * @throws ResourceNotFoundException
     */
    public BookingDTO getByBookingId(Integer id) throws ResourceNotFoundException {
        Booking booking = this.bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking with ID " + id + " not found!"));
        return this.createbookDTO(booking);
    }
    /**
     * Function to retrieve a getAllCourts of records in Booking table
     *
     * @param userId
     *            A specific UserId
     * @return List of objects with given UserId from Booking table
     * @throws ResourceNotFoundException
     */
    public List<BookingDTO> getByUserId(Integer userId) throws ResourceNotFoundException {
        List<Booking> bookingList = bookingRepository.findByUserUserId(userId);
        if(bookingList.size()==0){
                throw new ResourceNotFoundException("Booking with User ID " + userId + " not found!");
        }
        List<BookingDTO> bookingDTOList = new ArrayList<BookingDTO>();
        for(int i=0;i<bookingList.size();i++){
            bookingDTOList.add(this.createbookDTO(bookingList.get(i)));
        }
        return bookingDTOList;
    }
    /**
     * Function to create a BookingDTO object using the booking object
     *
     * @param booking
     *            An object with details of Booking Table
     * @return BookingDTO object
     */
    public BookingDTO createbookDTO(Booking booking)  {
        //booking.getPayment().getPaymentId() is giving null error
        Optional<Payment> payment= Optional.ofNullable(booking.getPayment());
        UUID id=null;
        //BookingStatus status=BookingStatus.PENDING;
        if(payment.isPresent()){
            id=payment.get().getPaymentId();
        }
        return new BookingDTO(booking.getBookingReferenceNo(),id,booking.getBookingStatus(),booking.getUser().getUserId(),
                String.valueOf(booking.getCreateDate())
                ,booking.getCourtSlots().getSport().getSportName(),booking.getCourtSlots().getCourt().getCourtNo()
                ,booking.getCourtSlots().getStartTime() ,booking.getCourtSlots().getEndTime()
                ,booking.getCourtSlots().getDate(),booking.getTotalCost());
    }
}


/*
*   // return bookingDTO;
        bookingDTO.setBookingId(booking.getBookingId());
        bookingDTO.setPaymentId(booking.getPayment().getPaymentId());
        bookingDTO.setSportName(booking.getPayment().getCourtslots().getSport().getSportName());//jsonunwrapped
        bookingDTO.setCourtNo(booking.getPayment().getCourtslots().getCourt().getCourtNo());
        bookingDTO.setStartTime(booking.getPayment().getCourtslots().getStartTime());
        bookingDTO.setEndTime(booking.getPayment().getCourtslots().getEndTime());
        bookingDTO.setDate(booking.getPayment().getCourtslots().getDate());
        bookingDTO.setTotalCost(booking.getTotalCost());
        bookingDTO.setCourtSlotId(booking.getPayment().getCourtslots().getCourtslotId());
        bookingDTO.setUserId(booking.getUser().getUserId());
* */