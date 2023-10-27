package com.booking.app.controller;


import com.booking.app.ResponseDto;
import com.booking.app.model.Sport;
import com.booking.app.requestDTO.CourtSlotsRequestDTO;
import com.booking.app.requestDTO.FreeSlotsRequestDTO;
import com.booking.app.responseDTO.CourtSlotListResponseDTO;
import com.booking.app.responseDTO.CourtSlotsResponseDTO;
import com.booking.app.exception.ResourceNotFoundException;
import com.booking.app.model.CourtSlots;
import com.booking.app.repository.SportRepository;
import com.booking.app.responseDTO.SlotsDTO;
import com.booking.app.service.CourtSlotsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//import org.apache.commons.collections.ListUtils;
import javax.validation.Valid;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/slots")
public class CourtSlotsController {

    @Autowired
    private CourtSlotsService courtslotservice;

    @Autowired
    private SportRepository sportRepository;

    /**
     * Gets the total cost according to the sport and the timings specified in RequestBody . METHOD = Get.
     *
     * @param courtSlotsRequestDTO
     * 	                               Object containing all the details of the attributes.
     *
     * @return ResponseDto containing a message specifying the estimated cost.
     * @throws ResourceNotFoundException
     */
    @GetMapping("/getcost")//user
    public ResponseEntity<ResponseDto> getCost(@Valid @RequestBody CourtSlotsRequestDTO courtSlotsRequestDTO) throws ResourceNotFoundException, ParseException {
        return new ResponseEntity<>(new ResponseDto(courtslotservice.getcostDTO(courtSlotsRequestDTO), null), HttpStatus.OK);
    }
    /**
     * Saves data in the CourtSlots table. METHOD = Post.
     *
     * @param courtSlotsRequestDTO
     *              Object containing all the details such as UserId(Specifies the user which is booking the slot)
     *              , paymentmode(Specifies the payment method) and other attributes
     *
     * @return ResponseDto containing message for successful entry.
     * @throws ResourceNotFoundException
     */
    @PostMapping("/add")
    public ResponseEntity<ResponseDto> addSlot(@Valid @RequestBody CourtSlotsRequestDTO courtSlotsRequestDTO) throws ResourceNotFoundException, ParseException {
        CourtSlots courtslot=courtslotservice.convert(courtSlotsRequestDTO);
        //return courtslotservice.save(courtslot,courtSlotsRequestDTO.getUserId(),courtSlotsRequestDTO.getPaymentMode());
        return new ResponseEntity<>(new ResponseDto(courtslotservice.save(
                courtslot,courtSlotsRequestDTO.getUserId())
                , null), HttpStatus.OK);
    }
    /**
     * Retrieves all the records in CourtSlots table. METHOD = Get. REQUEST = null.
     *
     * @return List of all the records
     */
    @GetMapping
    public ResponseEntity<ResponseDto> getAllSlots(){
        //return courtslotservice.listAll();
        return new ResponseEntity<>(new ResponseDto(courtslotservice.listAll(), null), HttpStatus.OK);
    }
    /**
     * Retrieves all the records in CourtSlots table according to the SportId,Date,CourtID. METHOD = Get.
     *
     * @param courtSlotsRequestDTO
     *          DTO Object that contains the SportId,Date,CourtId.
     * @return List of all the records.
     * @throws ResourceNotFoundException
     */
    @GetMapping("/findbysiddatecid")
    public ResponseEntity<ResponseDto> findBySidCidDate(@RequestBody CourtSlotsRequestDTO courtSlotsRequestDTO ) throws ResourceNotFoundException {

        List<CourtSlots> courtSlotsList= courtslotservice.findbysiddatecid(courtSlotsRequestDTO.getSportId(),
                courtSlotsRequestDTO.getDate(),courtSlotsRequestDTO.getCourtId());

        //new CourtSlotListResponseDTO("Required List of Courts",courtSlotsResponseDTOList);
        return new ResponseEntity<>(new ResponseDto(courtslotservice.convertToCourtSlotList(courtSlotsList), null), HttpStatus.OK);
    }
    /**
     * Retrieves the free Slots of all the courts according to the sport and date
     * specified from the CourtSlots table.METHOD = Get.
     *
     * @param courtSlotsRequestDTO
     *                DTO Object that contains the SportId,Date.
     * @return ResponseDto containing the List of all the free slots for the given day.
     * @throws ResourceNotFoundException
     */
    //api for giving the free slots based on sid and date(3rd)
    @GetMapping("/day")
    public ResponseEntity<ResponseDto> findBySidDate(@Valid @RequestBody FreeSlotsRequestDTO courtSlotsRequestDTO) throws ResourceNotFoundException {
        int sportId=courtSlotsRequestDTO.getSportId();
        String date=courtSlotsRequestDTO.getDate();

        Sport sport=sportRepository.findById(sportId).orElseThrow(
                () -> new ResourceNotFoundException("Sport with ID " + sportId + " not found!"));
        //output.add("List of Available Courts on "+date+" for the sport "+
          //      sportRepository.getBySportId(sportId).getSportName());
        //output.addAll(courtslotservice.findbysiddate(sportId,date));
        Map<String, Map<String, List< SlotsDTO >>> map= new LinkedHashMap<>();
        map.put(date,courtslotservice.findbysiddate(sportId,date));

        return new ResponseEntity<>(new ResponseDto(map, null), HttpStatus.OK);
    }
    /**
     * Retrieves the free Slots for the next 5 days of all the courts according to the sport and date
     * specified from the CourtSlots table.METHOD = Get.
     *
     * @param courtSlotsRequestDTO
     *                     DTO Object that contains the SportId,Date.
     * @return ResponseDto containing the List of all the free slots for the next five days.
     * @throws ResourceNotFoundException
     */
    //api for 5 days(4th)
    @GetMapping("/fivedays")
    public ResponseEntity<ResponseDto> fiveDays(@Valid @RequestBody FreeSlotsRequestDTO courtSlotsRequestDTO) throws ResourceNotFoundException {
        int sportId=courtSlotsRequestDTO.getSportId();
        String date=courtSlotsRequestDTO.getDate();
        Sport sport=sportRepository.findById(sportId).orElseThrow(
                () -> new ResourceNotFoundException("Sport with ID " + sportId + " not found!"));

        return new ResponseEntity<>(new ResponseDto(//new CourtSlotListResponseDTO(
                //"List of Available Courts for the next 5 day for the sport "+
               // sport.getSportName(),null,
                courtslotservice.fivedays(sportId,date)//)
                , null), HttpStatus.OK);

        }
    /**
     * Function to delete an object in CourtSlots Table
     *
     *  @param courtSlotsRequestDTO
     *                          DTO Object that contains the CourtSlotID.
     * @return esponseDto containing message for successful Deletion
     * @throws ResourceNotFoundException
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> deleteById(@Valid @PathVariable int id ) throws ResourceNotFoundException {

        courtslotservice.deleteByCourtSlotId(id);

        //return new CourtSlotsResponseDTO("User deleted succesfully",0,0,0,0,null,null,null,null);//return the deleted user
        return new ResponseEntity<>(new ResponseDto(new CourtSlotsResponseDTO("CourtSlot deleted succesfully"
                ,null,0,0,0,0,null,null,null,null)
                , null), HttpStatus.OK);
    }

}




   /*@GetMapping("/slots/getcost/{userId}")
    public String getcost(@RequestBody CourtSlots courtslot ){
        int totalCost= courtslotservice.getcost(courtslot);

        Boolean f=courtslotservice.checker(courtslot);
        if(f)
        {
            return "the total cost for "+sportRepository.getBysportid(courtslot.getSport().getSportId()).getSport_name()+
                    "from "+courtslot.getStartTime()+
                    " to "+courtslot.getEndTime()+" on the date "+ courtslot.getDate()+" is "+ totalCost;
        }
        else{
            return "not valid";
        }


    }
    @PostMapping("/slots/addslot/{userId}/{paymentMode}")
    public String addSlot(@RequestBody CourtSlots courtslot,@PathVariable int userId ,@PathVariable String paymentMode){
        //check if its a holiday
        return courtslotservice.save(courtslot,userId,paymentMode);

    }*/