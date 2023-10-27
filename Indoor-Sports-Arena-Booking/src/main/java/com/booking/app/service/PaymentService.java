package com.booking.app.service;

import com.booking.app.model.Booking;
import com.booking.app.model.CourtSlots;
import com.booking.app.model.enums.BookingStatus;
import com.booking.app.model.enums.SlotStatus;
import com.booking.app.model.enums.Transactionstatus;
import com.booking.app.repository.CourtSlotsRepository;
import com.booking.app.requestDTO.PaymentRequestDTO;
import com.booking.app.responseDTO.PaymentDTO;
import com.booking.app.exception.ResourceNotFoundException;
import com.booking.app.model.Payment;
import com.booking.app.repository.BookingRepository;
import com.booking.app.repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository ;
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CourtSlotsService courtSlotsService;

    @Autowired
    CourtSlotsRepository courtSlotsRepository;
    /**
     * Function to save an object to Payment Table
     *
     * @param payment
     *            An object with details of Payment Table
     * @return null
     */
    public void save(Payment payment){
        paymentRepository.save(payment);
    }
    /**
     * Function to retrieve all records in Payment Table
     * Additional Functionality-
     *     passes the payment object to the function createpaymentDTO and recieves a DTO object.
     *
     * @return List of all records in Payment Table
     */
    public List<PaymentDTO> listAll(){
        List<Payment> list=paymentRepository.findAll();
    List<PaymentDTO> paymentDTOList=new ArrayList<PaymentDTO>();
        for (Payment payment1: list) {
            paymentDTOList.add(this.createpaymentDTO(payment1));
        }
        return paymentDTOList;
    }
    /**
     * Function to retrieve a single record in Payment Details Table
     *
     * @param id
     *            A specific payment Id of the type UUID
     * @return List of objects with given ID from Payment Details table
     * @throws ResourceNotFoundException
     */
    public PaymentDTO getByPaymentId(UUID id) throws ResourceNotFoundException {
        Payment payment = this.paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment with ID " + id + " not found!"));
        return createpaymentDTO(payment);
    }


    /**
     * Function to Create a PaymentDTO object using the payment object
     * Additional Functionality -
     *      Gets the booking object using the PaymentId
     *
     * @param payment
     *            An object with details of Payment Table
     * @return PaymentDTO Object
     */
    public PaymentDTO createpaymentDTO(Payment payment)  {
    String msg=payment.getTransactionStatus().equals(Transactionstatus.SUCCESS) ? "Successfull": "Unsuccessful";
     return new PaymentDTO(msg,payment.getPaymentId(),payment.getPaymentMode()
                ,payment.getAmount(),null,
                0
                , String.valueOf(payment.getPaymentCreateDate()),null,0);//payment.getTransactionStatus()
    }
//check if already paid in payment table first for double payment
    //also booking and cslots values change only if booking.getTotalCost()==paymentRequestDTO.getAmount()&&
//                (paymentRequestDTO.getTransactionDetails().equals("success" )
    /**
     * Function to make a Payment using the paymentRequest object
     * Additional Functionality -
     *      Sets the payment id in the booking table and if succesfyl changes the Status in the booking and courtSlots table
     *
     * @param paymentRequestDTO
     *            An object with details of Payment Table
     * @return PaymentDTO Object
     */
    public PaymentDTO addPayment(PaymentRequestDTO paymentRequestDTO) throws ResourceNotFoundException {

        Booking booking=bookingRepository.findByBookingReferenceNo(paymentRequestDTO.getBookingReferenceNo())
                .orElseThrow(
                () -> new ResourceNotFoundException("Booking with reference number" +
                        " " + paymentRequestDTO.getBookingReferenceNo() + " not found!"));
        Payment payment=new Payment(null,paymentRequestDTO.getPaymentMode(),paymentRequestDTO.getAmount()
                ,Transactionstatus.FAIL,null);
        this.save(payment);
        if(booking.getBookingStatus().equals(BookingStatus.CONFIRMED)){
            throw new ResourceNotFoundException("Payment has already been made,refund will be given " +
                    "in case of double payment ");
        }
        if(booking.getCourtSlots().getSlotStatus().equals(SlotStatus.AVAILABLE)){
            throw new ResourceNotFoundException("Booking Invalid ");
        }

        booking.setPayment(payment);
        int id=booking.getCourtSlots().getCourtslotId();
            CourtSlots courtSlots=courtSlotsRepository.findById(id).orElseThrow(
                    () -> new ResourceNotFoundException("Courtslot with courtslotId "+ id + " not found!"));
            PaymentDTO paymentDTO=new PaymentDTO("Payment was Successfully made and your Court has been booked"
                    ,payment.getPaymentId(),payment.getPaymentMode(),paymentRequestDTO.getAmount()
            ,courtSlots.getSport().getSportName(),courtSlots.getCourt().getCourtNo(),
                    String.valueOf(payment.getPaymentCreateDate())
                    ,null,0);
        if((booking.getTotalCost()<=paymentRequestDTO.getAmount())&&
                (paymentRequestDTO.getTransactionStatus().equals(Transactionstatus.SUCCESS))
                &&(booking.getCourtSlots().getSlotStatus().equals(SlotStatus.RESERVED))){
            booking.setBookingStatus( BookingStatus.CONFIRMED);
            payment.setTransactionStatus(Transactionstatus.SUCCESS);
            courtSlots.setSlotStatus(SlotStatus.BOOKED);
            paymentDTO.setChange(paymentRequestDTO.getAmount()-booking.getTotalCost());
            this.save(payment);
            booking.setPayment(payment);
            bookingRepository.save(booking);
            courtSlotsRepository.save(courtSlots);
        }
        else{
            bookingRepository.save(booking);
            courtSlotsRepository.save(courtSlots);
            throw new ResourceNotFoundException("Payment was unsuccessful");
        }
    return paymentDTO;
    }
}


/**
 * Function to retrieve a single record in Payment Details Table
 *
 * @param courtslotId
 *            A specific CourtSlot Id
 * @return List of objects with given ID from Payment Details table
 * @throws ResourceNotFoundException
 */
    /*public PaymentDTO getByPaymentcsId(int courtslotId) throws ResourceNotFoundException {
        log.info("1");
        CourtSlots courtSlots=courtSlotsRepository.findById(courtslotId).orElseThrow(
                () -> new ResourceNotFoundException("CourtSlot with ID " + courtslotId + " not found!"));
        log.info("2");
        Booking booking=bookingRepository.getByCourtSlotsCourtslotId(courtslotId);
        log.info("3");
        Payment payment=paymentRepository.findById(booking.getPayment().getPaymentId()).orElseThrow(
                () -> new ResourceNotFoundException("Payment with ID " + booking.getPayment().getPaymentId()
                        + " not found!"));;
        log.info("4");
                        System.out.println(payment.toString());
       // return createpaymentDTO(payment);
        log.info("5");
return new PaymentDTO();
    }*/