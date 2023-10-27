package com.booking.app.service;

import com.booking.app.exception.ResourceNotFoundException;
import com.booking.app.model.Court;
import com.booking.app.model.Sport;
import com.booking.app.repository.CourtRepository;
import com.booking.app.repository.SportRepository;
import com.booking.app.responseDTO.SportListResponseDTO;
import com.booking.app.responseDTO.SportResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SportService {

    @Autowired
    private SportRepository sportrepo;
    @Autowired
    private CourtRepository courtRepository;
    /**
     * Function to save an object to Sport Table
     *
     * @param sport
     *            An object with details of Sport Table
     * @return null
     */
    public void save(Sport sport){

        sportrepo.save(sport);
    }
    /**
     * Function to retrieve all records in Sport Table
     *
     * @return List of all records in Sport Table
     */
    public SportListResponseDTO listAll(){

        List<Sport> sportList=sportrepo.findAll();
        List<SportResponseDTO> sportResponseDTOList=new ArrayList<SportResponseDTO>();
        for (Sport sport:sportList ) {
            int sportId=sport.getSportId();
            List<String> courtNo = new ArrayList<String>();
            List<Court> courtList = courtRepository.findBySportSportId(sportId);
            for (Court court :courtList  ) {
                courtNo.add("Court "+court.getCourtNo());
            }
            SportResponseDTO sportResponseDTO=new SportResponseDTO(null,sport.getSportId()
            ,sport.getSportName(),sport.getPrice(),courtNo);
            sportResponseDTOList.add(sportResponseDTO);
        }
        return new SportListResponseDTO(null,sportResponseDTOList);
    }
    /**
     * Function to delete an object in Sport Table
     *
     * @param id
     *            SportID of the required record
     * @return null
     *
     * @throws ResourceNotFoundException
     */
    public String delete(Integer id) throws ResourceNotFoundException {
        Sport sport =sportrepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Sport with ID " + id + " not found!"));
        String name=sport.getSportName();
            sportrepo.deleteById(id);
            return name;


    }

    public SportResponseDTO getbysid(int sid) throws ResourceNotFoundException {
        Sport sport =sportrepo.findById(sid).orElseThrow(()->new ResourceNotFoundException(
                "Sport with ID " + sid + " not found!"
        ));
        List<String> courtNo = new ArrayList<String>();
        List<Court> courtList = courtRepository.findBySportSportId(sid);
        for (Court court :courtList  ) {
            courtNo.add("Court "+court.getCourtNo());
        }
        return new SportResponseDTO(null,sid
                ,sport.getSportName(),sport.getPrice(),courtNo);
    }
}
