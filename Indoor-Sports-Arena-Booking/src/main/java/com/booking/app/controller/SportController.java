package com.booking.app.controller;

import com.booking.app.ResponseDto;
import com.booking.app.exception.ResourceNotFoundException;
import com.booking.app.model.Sport;
import com.booking.app.model.User;
import com.booking.app.repository.SportRepository;
import com.booking.app.requestDTO.SportRequestDTO;
import com.booking.app.responseDTO.SportListResponseDTO;
import com.booking.app.responseDTO.SportResponseDTO;
import com.booking.app.service.SportService;
import com.booking.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/sports")
public class SportController {

    @Autowired
    private SportService sportservice;
    @Autowired
    private SportRepository sportRepository;
    /**
     * Saves data in the Sport table. METHOD = Post
     *
     * @param sportRequestDTO
     *            Object containing all the details of the attributes.
     * @return SportResponseDTO containing the message to be displayed.
     */
    @PostMapping("/add")
    public ResponseEntity<ResponseDto> addSport(@Valid @RequestBody SportRequestDTO sportRequestDTO){
        sportRequestDTO.setSportName(sportRequestDTO.getSportName().toLowerCase(Locale.ROOT));
        sportservice.save(new Sport(sportRequestDTO));
      //  return new SportResponseDTO("Sport added succesfully",0,null,0) ;
        return new ResponseEntity<>(new ResponseDto(new SportResponseDTO(
                "Sport added succesfully",0,null,0,null), null), HttpStatus.OK);

    }
    /**
     * Retrieves all the records in Sport table. METHOD = Get.
     *
     * @param null.
     * @return SportListResponseDTO of all the records.
     */
    @GetMapping
    public ResponseEntity<ResponseDto> getAllSports(){
        return new ResponseEntity<>(new ResponseDto(sportservice.listAll(), null), HttpStatus.OK);
    }
    /**
     * Deletes data in the Sport table. METHOD = Delete.
     *
     * @param sportRequestDTO
     *              Object that contains the Sportid to be deleted
     *
     * @return SportResponseDTO containing the message to be displayed.
     * @throws ResourceNotFoundException
     *             if given Id does not exist
     */
    @DeleteMapping("/{sid}")
    public ResponseEntity<ResponseDto> deleteSport(@PathVariable int sid) throws ResourceNotFoundException {
        String name=sportservice.delete(sid);
        return new ResponseEntity<>(new ResponseDto(new SportResponseDTO(
                "Sport "+name+" has been deleted succesfully"
                ,0,null,0,null), null), HttpStatus.OK);

    }
    @GetMapping("/{sid}")
    public ResponseEntity<ResponseDto> getBySportId(@PathVariable int sid) throws ResourceNotFoundException {
        return new ResponseEntity<>(new ResponseDto(sportservice.getbysid(sid), null), HttpStatus.OK);
    }

}
