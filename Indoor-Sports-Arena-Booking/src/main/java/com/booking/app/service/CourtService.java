package com.booking.app.service;

import com.booking.app.exception.ResourceNotFoundException;
import com.booking.app.model.Court;
import com.booking.app.model.Sport;
import com.booking.app.repository.CourtRepository;
import com.booking.app.repository.SportRepository;
import com.booking.app.requestDTO.CourtRequestDTO;
import com.booking.app.responseDTO.CourtListResponseDTO;
import com.booking.app.responseDTO.CourtResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourtService{

    @Autowired
    private CourtRepository courtrepo;
    @Autowired
    private SportRepository sportRepository;

    /**
     * Function to save an object to Court Table
     *
     * @param courtRequestDTO
     *            An object with details of Court Table
     * @return null
     */
    public CourtListResponseDTO addCourt(CourtRequestDTO courtRequestDTO) throws ResourceNotFoundException {

           if( courtrepo.existsBySportSportIdAndCourtNo(courtRequestDTO.getSportId(),courtRequestDTO.getCourtNo())) {
               throw  new ResourceNotFoundException("courtNumber "+courtRequestDTO.getCourtNo()+" for sportId "+ courtRequestDTO.getSportId()+" exists !");
           }
           else if(courtRequestDTO.getCourtNo() == 0){
               throw  new ResourceNotFoundException("Enter valid court number");
           }
        else {
               courtrepo.save(this.convertDTO(courtRequestDTO));
               return new CourtListResponseDTO("Court added succesfully for "+
                       sportRepository.getBySportId(courtRequestDTO.getSportId()).getSportName(),null);

           }
    }
    /**
     * Function to retrieve all records in Court Table
     *
     * @return List of all records in Court Table
     */
    public List<CourtResponseDTO> listAll(){
        List<Court> courtList=courtrepo.findAll();
        List<CourtResponseDTO> courtResponseDTOList=new ArrayList<CourtResponseDTO>();
        for (Court court:courtList) {
            courtResponseDTOList.add(new CourtResponseDTO(court));
        }
        return courtResponseDTOList;
    }
    public Court convertDTO(CourtRequestDTO courtRequestDTO) throws ResourceNotFoundException {
        Sport sport =sportRepository.findById(courtRequestDTO.getSportId()).orElseThrow(() -> new ResourceNotFoundException("Sport not found!"));
        return new Court(courtRequestDTO.getCourtId(),sport,courtRequestDTO.getCourtNo());

    }
}
