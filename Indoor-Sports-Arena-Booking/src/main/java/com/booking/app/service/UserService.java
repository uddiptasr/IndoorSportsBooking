package com.booking.app.service;

import com.booking.app.model.Payment;
import com.booking.app.requestDTO.UserRequestDTO;
import com.booking.app.responseDTO.BookingDTO;
import com.booking.app.responseDTO.UserResponseDTO;
import com.booking.app.exception.ResourceNotFoundException;
import com.booking.app.model.Booking;
import com.booking.app.model.CourtSlots;
import com.booking.app.model.User;
import com.booking.app.repository.BookingRepository;
import com.booking.app.repository.CourtSlotsRepository;
import com.booking.app.repository.PaymentRepository;
import com.booking.app.repository.UserRepository;
import com.booking.app.responseDTO.UserListResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
@Slf4j
@Service
public class UserService {
    @Autowired
    private UserRepository urepo;
    @Autowired
    private BookingRepository bookingRepository ;

    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private CourtSlotsRepository courtSlotsRepository;
    /**
     * Function to retrieve all records in User Table
     *
     * @return UserListResponseDTO of all records in User Table
     */
    public UserListResponseDTO getAllUsers(){
        List<User> users = urepo.findAll();
        List<UserResponseDTO> userResponseDTOS =new ArrayList<UserResponseDTO>();
        for (User user:users)
        {
            userResponseDTOS.add(new UserResponseDTO(null,user.getUserId(),user.getFirstName(),user.getLastName()
                    , String.valueOf(user.getCreateDate())
                    ,String.valueOf(user.getLastModifiedDate())));

        }
        UserListResponseDTO userListResponseDTO =new UserListResponseDTO(userResponseDTOS);
        return userListResponseDTO;
    }
    /**
     * Function to retrieve a single record in User Table
     * Additional Functionality -
     * 	  Retrieves the getAllCourts of Booking objects which were booked by the userId
     * 	  Converts the getAllCourts of Booking Objects to a getAllCourts of BookingDTO objects
     * 	  Creates a UserResponseDTO objext using the user and the BookingDTO object.
     *
     * @param userRequestDTO
     *            Contains the UserId to retrieve a single record
     * @return UserResponseDTO Object with given ID of all records in User Table
     * @throws ResourceNotFoundException
     */
    public UserResponseDTO getByUserId(UserRequestDTO userRequestDTO) throws ResourceNotFoundException {
        //find all bookings of userId
        int id=userRequestDTO.getUserId();

        User user=this.urepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID " + id + " not found!"));

        return new UserResponseDTO(null,id,user.getFirstName(),user.getLastName()
                , String.valueOf(user.getCreateDate())
                ,String.valueOf(user.getLastModifiedDate()));
    }

    /**
     * Function to save an object to User Table
     *
     * @param userRequestDTO
     *            An object with details of User Table
     * @return UserResponseDTO Object with given ID of all records in User Table
     */
    public UserResponseDTO save(UserRequestDTO userRequestDTO){
        User user=new User(userRequestDTO);
        urepo.save(user);
        userRequestDTO.setUserId(user.getUserId());
        return new UserResponseDTO(userRequestDTO);
    }
    /**
     * Checks if the Passoword entered for the username is correct.

     * @param userRequestDTO
     *               Object containing the userId and password
     * @return UserResponseDTO with String containing the message to be displayed.
     * @throws ResourceNotFoundException
     *             if given Id does not exist
     */
    public UserResponseDTO  authenticate(UserRequestDTO userRequestDTO) throws ResourceNotFoundException {
        User checker =urepo.findById(userRequestDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User with ID " + userRequestDTO.getUserId() + " not found!"));
        UserResponseDTO userResponseDTO=new UserResponseDTO();
        if(userRequestDTO.getPassword().equals(checker.getPassword())) {

            userResponseDTO =getByUserId(userRequestDTO);

            userResponseDTO.setMesssage("User "+userRequestDTO.getUserId()+" has been logged in successfully");
            return userResponseDTO;
        }
        else {
            throw new ResourceNotFoundException("Invalid credentials");
           // userResponseDTO.setMesssage("Invalid credentials");
            //return userResponseDTO;
        }
    }

    /**
     * Function to Update an object in User Table
     *
     * @param userRequestDTO
     *            An object with new details of User Table
     *
     * @return Object that is updated and saved
     * @throws ResourceNotFoundException
     */
    public UserResponseDTO updateUser( UserRequestDTO userRequestDTO) throws ResourceNotFoundException {
        try{
            User user=urepo.findById(userRequestDTO.getUserId()).orElseThrow(
                    () -> new ResourceNotFoundException("User with ID " + userRequestDTO.getUserId() + " not found!"));

            String cdate=String.valueOf(user.getCreateDate());
            this.save(userRequestDTO);
            UserResponseDTO userResponseDTO=new UserResponseDTO(userRequestDTO);
            userResponseDTO.setCreateDate(cdate);
            userResponseDTO.setLastModifiedDate(String.valueOf(user.getLastModifiedDate()));
            return userResponseDTO;
        }
        catch (NoSuchElementException e){
            throw new ResourceNotFoundException("User with ID " + userRequestDTO.getUserId() + " not found!");
        }

    }

    /**
     * Function to delete an object in User Table
     *
     * @param userRequestDTO
     *            object which contains userID of the required record
     * @return UserResponseDTO
     *
     * @throws ResourceNotFoundException
     */

    public UserResponseDTO delete(UserRequestDTO userRequestDTO) throws ResourceNotFoundException {
        int id=userRequestDTO.getUserId();
        try {
            User user=urepo.findById(id).orElseThrow(
                    () -> new ResourceNotFoundException("User with ID " + userRequestDTO.getUserId() + " not found!"));
            UserResponseDTO userResponseDTO=new UserResponseDTO("Deleting user with id "+id,
                    id,user.getFirstName(), user.getLastName() , String.valueOf(user.getCreateDate())

                    ,String.valueOf(user.getLastModifiedDate()));
            try {
                DateTime currentDateTime = new DateTime();
                String currentDate = LocalDate.parse(String.valueOf(currentDateTime).substring(0, 10),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd")).format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                List<Booking> bookingList = bookingRepository.getByUserUserId(id);
                for (Booking booking : bookingList) {
                    CourtSlots courtSlot = booking.getCourtSlots();
                    if (format.parse(courtSlot.getDate()).compareTo(format.parse(currentDate)) > 0) {
                        //bookingRepository.deleteById(booking.getBookingId());
                        Optional<Payment> payment= Optional.ofNullable(booking.getPayment());
                        UUID pid=null;
                        //BookingStatus status=BookingStatus.PENDING;
                        log.info("1");
                        if(payment.isPresent()){
                            pid=payment.get().getPaymentId();
                           // paymentRepository.deleteById(pid);
                            log.info("2");
                        }
                        log.info("3");
                    //    bookingRepository.deleteById(booking.getBookingId());
                        courtSlotsRepository.deleteById(courtSlot.getCourtslotId());

                        log.info("4");
                    }
                }
                urepo.deleteById(id);

                return userResponseDTO;
            } catch (NoSuchElementException | ParseException e) {
                throw new ResourceNotFoundException("User with ID " + id + " not found!");
            }
        }catch (NoSuchElementException e){
            throw new ResourceNotFoundException("User with ID " + id + " not found!");
        }
    }



    /**
     * Function to Create a BookingDTO object using the booking object
     * @param booking
     *            An object with details of Booking Table
     * @return BookingDTO Object
     */
   /* private BookingDTO createbookDTO(Booking booking) {

        return new BookingDTO(booking.getBookingId(),null,0,String.valueOf(booking.getCreateDate())

                ,booking.getCourtSlots().getSport().getSportName(),
                booking.getCourtSlots().getCourt().getCourtNo()
        ,booking.getCourtSlots().getStartTime(),booking.getCourtSlots().getEndTime()
        ,booking.getCourtSlots().getDate(),booking.getTotalCost());

    }
*/



}

     /*
     /**
     * Function to retrieve a single record in User Table
     *
     * @param id
     *            Contains the UserId
     * @return User Object with given ID of all records in User Table
     * @throws ResourceNotFoundException

    public User get(Integer id) throws ResourceNotFoundException {
    try {
        return urepo.findById(id).get();
    }
         catch (NoSuchElementException e){
            throw new ResourceNotFoundException("User with ID " + id + " not found!");
        }
    }
      */

      // return bookingDTO;
        /*bookingDTO.setBookingId(booking.getBookingId());
        bookingDTO.setPaymentId(booking.getPayment().getPaymentId());
        bookingDTO.setSportName(booking.getPayment().getCourtslots().getSport().getSportName());//jsonunwrapped
        bookingDTO.setCourtNo(booking.getPayment().getCourtslots().getCourt().getCourtNo());
        bookingDTO.setStartTime(booking.getPayment().getCourtslots().getStartTime());
        bookingDTO.setEndTime(booking.getPayment().getCourtslots().getEndTime());
        bookingDTO.setDate(booking.getPayment().getCourtslots().getDate());
        bookingDTO.setTotalCost(booking.getTotalCost());
        return bookingDTO;

    public User getByUserId(Integer id) throws ResourceNotFoundException {
        User user = this.urepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID " + id + " not found!"));
        return user;
     }
       public boolean exist( Integer id) throws ResourceNotFoundException {
        try{
            User user2= this.get(id);
            return true;
        }
        catch (NoSuchElementException e){
            throw new ResourceNotFoundException("User with ID " + id + " not found!");
        }

    }

public void delete(Integer id) throws ResourceNotFoundException {
        try {
            DateTime currentDateTime = new DateTime();
            System.out.println(currentDateTime);
            String currentDate = LocalDate.parse(String.valueOf(currentDateTime).substring(0, 10),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd")).format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            System.out.println(currentDate);
            List<Booking> bookingList = new ArrayList<Booking>();
            bookingList = bookingRepository.findByUserUserId(id);
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            for (Booking booking : bookingList) {
                System.out.println(booking.getPayment().getCourtslots().getCourtslotId());

                courtSlotsService.deleteByCourtSlotId(booking.getPayment().getCourtslots().getCourtslotId());
                /*CourtSlots courtSlot = booking.getPayment().getCourtslots();
                courtSlotsRepository.deleteById(booking.getPayment().getCourtslots().getCourtslotId());
                if (format.parse(courtSlot.getDate()).compareTo(format.parse(currentDate)) > 0) {
                    System.out.println("after" + courtSlot.getCourtslotId() + courtSlot);

                    //courtSlotsRepository.deleteById(booking.getPayment().getCourtslots().getCourtslotId());
                    //System.out.println("after1" + courtSlot.getCourtslotId() + courtSlot);
                    // DELETE FROM `proj`.`court_slots` WHERE (`courtslot_id` = '7');
                    //  }
                }

                urepo.deleteById(id);

                        }
                        } catch (NoSuchElementException  e) {
                        throw new ResourceNotFoundException("User with ID " + id + " not found!");
                        }
                        }

     */
