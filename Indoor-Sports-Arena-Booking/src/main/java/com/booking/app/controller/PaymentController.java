package com.booking.app.controller;

import com.booking.app.ResponseDto;
import com.booking.app.requestDTO.PaymentRequestDTO;
import com.booking.app.responseDTO.PaymentDTO;
import com.booking.app.exception.ResourceNotFoundException;
import com.booking.app.model.Payment;
import com.booking.app.responseDTO.PaymentListResponseDTO;
import com.booking.app.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;
    /**
     * Saves data in the payment table. METHOD = Post
     *
     * @param payment
     *            Object containing all the details of the attributes.
     * @return String containing the message to be displayed.
     */
    @PostMapping("/add")
    public ResponseEntity<ResponseDto> addPayment(@RequestBody Payment payment ){
        paymentService.save(payment);

        return new ResponseEntity<>(new ResponseDto("payment details added succesfully", null), HttpStatus.OK);

    }
    /**
     * Function to make a Payment using the paymentRequest object METHOD = Post
     *
     * @param paymentRequestDTO
     *            An object with details of Payment Table
     * @return PaymentDTO Object
     */
    @PostMapping("/pay")
    public ResponseEntity<ResponseDto> addPayment(@Valid @RequestBody PaymentRequestDTO paymentRequestDTO ) throws ResourceNotFoundException {
        PaymentDTO paymentDTO=paymentService.addPayment(paymentRequestDTO);
     return new ResponseEntity<>(new ResponseDto(paymentDTO,
                null), HttpStatus.OK);


    }
    /**
     * Retrieves all the records in Payments table. METHOD = Get.
     *
     * @param null.
     * @return PaymentListResponseDTO of all the records.
     */
    @GetMapping
    public ResponseEntity<ResponseDto> list(){

        return new ResponseEntity<>(new ResponseDto(new PaymentListResponseDTO(
                "Payments:",paymentService.listAll()), null), HttpStatus.OK);
    }
    /**
     * Retrieves one record from Payment table with the given CourtSlotId. METHOD = Get.
     *
     * @param paymentDTO
     *          Object containing PaymentID .
     * @return Object containing all the details of that Payment.
     * @throws ResourceNotFoundException
     *             if given Id does not exist
     */
    @GetMapping("/getbypid")
    public ResponseEntity<ResponseDto>  getbypid(@RequestBody PaymentDTO paymentDTO) throws ResourceNotFoundException {
        return new ResponseEntity<>(new ResponseDto(paymentService.getByPaymentId(paymentDTO.getPaymentId()), null), HttpStatus.OK);
    }






    /**
     * Retrieves one record from Payment table with the given CourtSlotId. METHOD = Get.
     *
     *  @param paymentDTO
     *      *          Object containing the CourtSlotId .
     * @return Object containing all the details of that bill.
     * @throws ResourceNotFoundException
     *             if given Id does not exist
     */
   /* @GetMapping("/csid")
    public ResponseEntity<ResponseDto> getbycsid(@RequestBody PaymentDTO paymentDTO ) throws ResourceNotFoundException {

        return new ResponseEntity<>(new ResponseDto( paymentService.getByPaymentcsId(paymentDTO.getCourtSlotId()), null), HttpStatus.OK);

    }*/
}
