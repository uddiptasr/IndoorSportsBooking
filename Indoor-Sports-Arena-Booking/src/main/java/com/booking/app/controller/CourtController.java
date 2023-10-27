package com.booking.app.controller;

import com.booking.app.ResponseDto;
import com.booking.app.exception.ResourceNotFoundException;
import com.booking.app.model.Court;
import com.booking.app.model.Sport;
import com.booking.app.requestDTO.CourtRequestDTO;
import com.booking.app.responseDTO.CourtListResponseDTO;
import com.booking.app.service.CourtService;
import com.booking.app.service.SportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courts")
public class CourtController {

    @Autowired
    private CourtService courtservice;
    /**
     * Saves data in the Court table. METHOD = Post.
     *  @param courtRequestDTO
     * 	             Object containing all the details of the attributes.
     *
     * @return CourtListResponseDTO with a message for successful entry.
     */
    @PostMapping("/add")
    public ResponseEntity<ResponseDto> addCourt(@RequestBody CourtRequestDTO courtRequestDTO ) throws ResourceNotFoundException {
        //return courtservice.addCourt(courtRequestDTO);
        return new ResponseEntity<>(new ResponseDto(courtservice.addCourt(courtRequestDTO), null), HttpStatus.OK);

    }
    /**
     * Retrieves all the records in Court table. METHOD = Get.
     *
     * @param null.
     * @return CourtListResponseDTO of all the records.
     */
    @GetMapping
    public ResponseEntity<ResponseDto> getAllCourts(){

       // return new CourtListResponseDTO(null,courtservice.listAll());
        return new ResponseEntity<>(new ResponseDto(new CourtListResponseDTO(null,courtservice.listAll()), null), HttpStatus.OK);

    }
}
