package com.booking.app.controller;

import com.booking.app.ResponseDto;
import com.booking.app.exception.ResourceNotFoundException;
import com.booking.app.model.Holidays;
import com.booking.app.responseDTO.HolidaysListDTO;
import com.booking.app.service.HolidaysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/holidays")
public class HolidaysController {

    @Autowired
    private HolidaysService holidaysService;
    /**
     * Saves data in the Holidays table. METHOD = Post
     *
     * @param holidays
     *            Object containing all the details of the attributes.
     * @return String containing the message to be displayed.
     * @throws ResourceNotFoundException
     */
    @PostMapping("/add")
    public ResponseEntity<ResponseDto> addHoliday(@RequestBody Holidays holidays ) throws ResourceNotFoundException {
        holidaysService.save(holidays);
       return new ResponseEntity<>(new ResponseDto(new HolidaysListDTO("holiday added succesfully",null,null), null), HttpStatus.OK);

    }
    /**
     * Retrieves all the records in Holidays table. METHOD = Get.
     *
     * @param null.
     * @return List of all the records.
     */
    @GetMapping
    public ResponseEntity<ResponseDto> getAllHolidays(){
 return new ResponseEntity<>(new ResponseDto(new HolidaysListDTO("Holidays:",holidaysService.listAll(),null), null), HttpStatus.OK);

    }
    /**
     * Deletes data in the Holidays table. METHOD = Delete
     *
     * @param holidaysListDTO
     *            object containing the date of record that should be deleted.
     * @return String containing the message to be displayed.
     * @throws ResourceNotFoundException
     *             If given Id does not exist
     */
    @DeleteMapping("/deleteholidays")
    public ResponseEntity<ResponseDto> deleteByDate(@RequestBody HolidaysListDTO holidaysListDTO) throws ResourceNotFoundException {

        holidaysService.delete(holidaysListDTO.getDate());

       // return new HolidaysListDTO("holiday deleted succesfully",null,holidaysListDTO.getDate()) ;
        return new ResponseEntity<>(new ResponseDto(new HolidaysListDTO("holiday deleted succesfully",
                null,holidaysListDTO.getDate()), null), HttpStatus.OK);

    }

}
