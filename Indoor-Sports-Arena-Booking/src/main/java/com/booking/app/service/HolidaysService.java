package com.booking.app.service;

import com.booking.app.exception.ResourceNotFoundException;
import com.booking.app.model.CourtSlots;
import com.booking.app.model.Holidays;
import com.booking.app.model.Sport;
import com.booking.app.repository.CourtSlotsRepository;
import com.booking.app.repository.HolidaysRepository;
import com.booking.app.repository.SportRepository;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class HolidaysService {

    @Autowired
    private HolidaysRepository holidaysRepository;
    @Autowired
    private CourtSlotsRepository courtSlotsRepository;
    /**
     * Function to save an object to Holidays Table
     *
     * @param holidays
     *            An object with details of Holidays Table
     * @return null
     */
    public void save(Holidays holidays) throws ResourceNotFoundException {
       if(holidaysRepository.existsByDate(holidays.getDate())){
           throw new ResourceNotFoundException("Holiday already Exists!");

       }
           else{
               List<CourtSlots> courtSlotsList=courtSlotsRepository.findByDate(holidays.getDate());
           for (CourtSlots courtSlot:courtSlotsList) {
               courtSlotsRepository.deleteById(courtSlot.getCourtslotId());
           }
               holidaysRepository.save(holidays);
           }
    }
    /**
     * Function to retrieve all records in Holidays Table
     *
     * @return List of all records in Holidays Table
     */
    public List<Holidays> listAll(){

        return holidaysRepository.findAll();
    }
    /**
     * Function to delete an object in Holidays Table
     *
     * @param date
     *            date of the required record
     * @return null
     *
     * @throws ResourceNotFoundException
     */
    public void delete(String date) throws ResourceNotFoundException {
        Holidays holiday =holidaysRepository.findByDate(date).orElseThrow(
                () -> new ResourceNotFoundException("Holiday does not Exist!"));

            holidaysRepository.deleteById(holiday.getHolidayId());

    }
}
