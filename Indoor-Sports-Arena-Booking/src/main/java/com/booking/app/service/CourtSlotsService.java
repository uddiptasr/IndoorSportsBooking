package com.booking.app.service;

import com.booking.app.TimeDifference;
import com.booking.app.model.enums.SlotStatus;
import com.booking.app.model.enums.BookingStatus;
import com.booking.app.requestDTO.CourtSlotsRequestDTO;
import com.booking.app.responseDTO.CourtSlotListResponseDTO;
import com.booking.app.responseDTO.CourtSlotsResponseDTO;
import com.booking.app.exception.ResourceNotFoundException;
import com.booking.app.model.*;
import com.booking.app.repository.*;
import com.booking.app.responseDTO.GetCostResponseDTO;
import com.booking.app.responseDTO.SlotsDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class CourtSlotsService {

    @Autowired
    private CourtSlotsRepository courtslotsrepo;

    @Autowired
    private CourtRepository courtRepository;

    @Autowired
    private HolidaysRepository holidaysRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private SportRepository sportRepository;

    @Autowired
    private BookingRepository bookingRepository;


    /**
     * Function to save an object to CourtSlots Table and
     * passes the Object to the payment Table and creates an entry in it and then
     * passes this object to the Booking table and creates the Booking.
     * Additional Functionality -
     *      Checks if the userId Given as a parameter Exists
     *      Checks if the given date in the courtslots object is a holiday
     *      Computes the Duration using the start and end time and checks if the duration is valid (multiples of 30)
     *      Passes the courtslots object to the checker function which checks the given timing of the slots for the date is valid
     *      Computes the total cost using the getCost function
     *      Creates the payment and booking objects only if the slot is valid
     * @param courtslot
     *            An object with details of CourtSlots Table
     * @param UserId
     *            Specifies the user which is booking the slot.
     * @param paymentmode
     *            Specifies the payment method.
     *
     * @return Object that is saved
     * @throws ResourceNotFoundException
     */
    public CourtSlotsResponseDTO save(CourtSlots courtslot, int uid) throws ResourceNotFoundException, ParseException {
      // courtslot.setSlotStatus(courtslot.getSlotStatus().toUpperCase(Locale.ROOT));
        CourtSlotsResponseDTO courtSlotsResponseDTO=new CourtSlotsResponseDTO();
        User user=userRepository.findById(uid).orElseThrow(
                () -> new ResourceNotFoundException("User with ID " + uid + " not found!"));
        //check for holiday
        if(holidaysRepository.existsByDate(courtslot.getDate())){
            throw new ResourceNotFoundException(courtslot.getDate()+" is a Holiday");
           // courtSlotsResponseDTO.setMessage(courtslot.getDate()+" is a Holiday");
           // return courtSlotsResponseDTO;
        }
        int duration =this.getduration(courtslot);
        System.out.println(duration);
        if(duration%30!=0){
            throw new ResourceNotFoundException("Duration should be in multiples of 30");
            //courtSlotsResponseDTO.setMessage("Duration should be in multiples of 30");
            //return courtSlotsResponseDTO;
        }
        //check if the time slot is vaild and courtslot has been saved
        // so that booking can be saved too
        if(this.checker(courtslot)){

            //save courtslots
            courtslotsrepo.save(courtslot);
            log.info("1");
            Booking booking=new Booking(0,user,null,courtslot,this.getcost(courtslot)
                    ,null, BookingStatus.PENDING,null);
            log.info("2");
            bookingService.save(booking);
            log.info("3");
            String date= String.valueOf(booking.getCreateDate()).substring(0,4)
                    +String.valueOf(booking.getCreateDate()).substring(5,7)
                    +String.valueOf(booking.getCreateDate()).substring(8,10);
            booking.setBookingReferenceNo(date+String.valueOf(booking.getBookingId()));
            bookingService.save(booking);
            courtSlotsResponseDTO.setMessage("Courtslots added successfully");

            return courtSlotsResponseDTO;
        }
        throw new ResourceNotFoundException("Courtslots invalid");
        //courtSlotsResponseDTO.setMessage("Courtslots invalid");
        //return courtSlotsResponseDTO;
    }
    /**
     * Function to check if the given courtslots object is valid based on the availabilty of the courts on that day
     * Additional Functionality -
     *      Checks if the given date in the courtslots object is a holiday
     *      Computes the Duration using the start and end time and checks if the duration is valid (multiples of 30)
     *      Gets all the slots booked based on the the sportId courtId and date and stores it in a getAllCourts
     *      Checks if the given start and time of the given object is valid by comparing it
     *          with the start and times of each object in the getAllCourts of courtslots objects which was created previously
     * @param courtslots
     *          An object with details of CourtSlots Table
     *
     * @return Boolean value
     */
    public Boolean checker(CourtSlots courtslots) throws ResourceNotFoundException, ParseException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        LocalDateTime now = LocalDateTime.now();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy h:m");
        log.info(String.valueOf(dtf.format(now)));
        log.info(String.valueOf(sdf.parse(dtf.format(now))));
        log.info(String.valueOf(sdf.parse(courtslots.getDate()+" "+courtslots.getStartTime() )));
       if (sdf.parse(courtslots.getDate()+" "+courtslots.getStartTime() ).before(sdf.parse(dtf.format(now)))){
            throw new ResourceNotFoundException("Please enter a future Date and time");
        }
        int duration =this.getduration(courtslots);
        if(holidaysRepository.existsByDate(courtslots.getDate())){
            throw new ResourceNotFoundException(courtslots.getDate()+" Is a holiday");
        }
        if(duration%30!=0){
            throw new ResourceNotFoundException("Duration should be in multiples of 30");
        }
        if( courtslots.getStartTime().equals(courtslots.getEndTime())){
            throw new ResourceNotFoundException("Start time and endTime cannot be the same");
        }

        List<CourtSlots> courtSlotsList = this.findbysiddatecid(courtslots.getSport().getSportId(),
                courtslots.getDate(), courtslots.getCourt().getCourtId());

        int cstarttime = Integer.parseInt(courtslots.getStartTime().replace(":", ""));
        //converting startime and et to (int)1200 form 12:00
        int cendtime = Integer.parseInt(courtslots.getEndTime().replace(":", ""));
        int openingtime,closingtime,day=daychecker(courtslots.getDate());
        int intdate=Integer.parseInt(courtslots.getDate().replace("-",""));
        //checks for weekday/weekend
        if(day==0||day==6){
            openingtime=600;
            closingtime=2200;
            log.info(day+"weekend");
        }
        else{
            log.info(day+"weekday");
            openingtime=900;
            closingtime=2100;
        }
        //checking if slots are valid
        if(cstarttime < openingtime ){
            throw new ResourceNotFoundException("Enter a StartTime after "+openingtime);
             }
        if(cendtime > closingtime){
            throw new ResourceNotFoundException("Enter a EndTime before "+closingtime); }
        if(cstarttime>=cendtime){
              throw new ResourceNotFoundException("StartTime cannot be before or equal to EndTime");
            }
        if((cstarttime%100)%15!=0){
            throw new ResourceNotFoundException("StartTime should Be a multiple of 15");
        }
        boolean f = true;


        for (int i = 0; i < courtSlotsList.size(); i++) {

            if(courtSlotsList.get(i).getSlotStatus().equals(SlotStatus.AVAILABLE)){
                continue;
            }
            int istarttime = Integer.parseInt(courtSlotsList.get(i).getStartTime().replace(":", ""));
            int iendtime = Integer.parseInt(courtSlotsList.get(i).getEndTime().replace(":", ""));

            if (((cendtime != iendtime) && (cstarttime != istarttime)) && ((cendtime < iendtime && cendtime <= istarttime)
                    || (cstarttime > istarttime && cstarttime >= iendtime)) && cstarttime >= openingtime && cendtime <= closingtime) {
                f = true;
            } else {
                if(cstarttime < openingtime )
                throw new ResourceNotFoundException("Court opens at "+openingtime);

                if(cendtime > closingtime)
                    throw new ResourceNotFoundException("Court closes at "+closingtime);
                throw new ResourceNotFoundException("Timeslot is not Free please select any other slot ");
            }
        }
        //needed this as booking was still creating the slot
        return f;
    }
    /**
     * Function to retrieve all records in CourtSlots Table
     *
     * @return List of all records in CourtSlots Table
     */
    public CourtSlotListResponseDTO listAll(){
        List<CourtSlots> courtSlotsList=courtslotsrepo.findAll();
        List<CourtSlotsResponseDTO> courtSlotsResponseDTOS=new ArrayList<CourtSlotsResponseDTO>();
        for (CourtSlots courtslot:courtSlotsList ) {
            CourtSlotsResponseDTO courtSlotsResponseDTO=new CourtSlotsResponseDTO(null,courtslot.getSlotStatus(),
                    courtslot.getCourtslotId(),courtslot.getCourt().getCourtId()
                    ,courtslot.getCourt().getCourtNo(),courtslot.getSport().getSportId(),courtslot.getSport().getSportName()
                    ,courtslot.getStartTime(),courtslot.getEndTime(),courtslot.getDate());
            courtSlotsResponseDTOS.add(courtSlotsResponseDTO);
        }
        return new CourtSlotListResponseDTO(null,courtSlotsResponseDTOS,null);

    }
    /**
     * Function to get the getAllCourts of boooked slots for the given sportId and date and courtId
     * @param sid
     * @param date
     * @param courtId
     * @return List of the booked slots for the given date
     */
    public List<CourtSlots> findbysiddatecid(int sid, String date, int cid) throws ResourceNotFoundException {
            Sport sport=sportRepository.findById(sid).orElseThrow(
                    () -> new ResourceNotFoundException("Sport with ID " + sid + " not found!"));
            Court court=courtRepository.findById(cid).orElseThrow(
                    () -> new ResourceNotFoundException("Court with ID " + cid + " not found!"));
        return courtslotsrepo.findBySportSportIdAndDateAndCourtCourtId(sid,date,cid);
    }
    /**
     * Function to get the getAllCourts of available slots for the given sportId and date
     * Additional Functionality -
     *      Iterates for all the courtids associated with the sportId
     *          and gets the courtslots booked using date,sportId and the courtId as parameters
     *      Checks if the given date is belongs to the weekend or weekday and
     *          adjusts the opening and closing time of the arena based on thee day of the week
     *      Uses the findbysiddate in each iteration to get the free slots for the day
     *      It is iterated 5 times
     * @param sid
     * @param date
     *
     * @return List of the free slots for the given date
     */
    public Map<String, List<SlotsDTO>> findbysiddate(int sid, String date) throws ResourceNotFoundException {

        //find cid by sid
        //check if sportId is valid
        List<CourtSlotsResponseDTO> courtSlotsResponseDTOList=new ArrayList<CourtSlotsResponseDTO>();
        List<Court> courtids=courtRepository.findBySportSportId(sid);//findCourtIdBySportSportId
        for (Court court:courtids ) {
            System.out.println(court);
        }

        List<String > output = new ArrayList<String>();
        //output.addSlot("List of Available Courts on "+date+" for the sport "+sportRepository.getSport_nameBysportid(sid).getSport_name());
        //System.out.println(courtids.size()+"Size");
        Map<String, List<SlotsDTO>> map = new LinkedHashMap<>();

        for (int i1=0;i1<courtids.size();i1++) {
            int value =courtids.get(i1).getCourtId();
            List <SlotsDTO> slotsDTOList=new  ArrayList<SlotsDTO>();
            //System.out.println(courtids.get(i1)+ " "+value);
            //find all courts for cid,sid,date
            //cslotsbysidciddate is getAllCourts fo cslots for each court id
            List<CourtSlots> cslotsbysidciddate = this.findbysiddatecid(sid, date,value );//z
            //sort acc to starttime
            Collections.sort(cslotsbysidciddate, new Comparator<CourtSlots>() {
                @Override
                public int compare(CourtSlots a1, CourtSlots a2) {
                    int x=Integer.parseInt(a1.getStartTime().replace(":", ""));
                    int y=Integer.parseInt(a2.getStartTime().replace(":", ""));
                    return x- y;
                }
            });
            //get courtNo for cid
            Court court=courtRepository.findById(value).orElseThrow(
                    () -> new ResourceNotFoundException("Court with ID " + value + " not found!"));
                   // findCourtNoByCourtId(value);

            int cid= court.getCourtNo();

            int openingtime,closingtime;

            if(daychecker(date)==0||daychecker(date)==6){
                openingtime=600;
                closingtime=2200;
                System.out.println(daychecker(date)+"weekend");
            }
            else{
                System.out.println(daychecker(date)+"weekday");
                openingtime=900;
                closingtime=2100;
            }
            int i=openingtime;
                //get available tslots starting comparison with 9:00 and 0th starttime as getAllCourts is sorted
                for(int j=0;j<cslotsbysidciddate.size();j++){
                    if(cslotsbysidciddate.get(j).getSlotStatus().equals(SlotStatus.AVAILABLE)){
                        continue;
                    }
                    CourtSlotsResponseDTO courtSlotsResponseDTO=new CourtSlotsResponseDTO();

                    int x1=Integer.parseInt(cslotsbysidciddate.get(j).getStartTime().replace(":", ""));
                    //x1 is the jth start time if i is lesser than x1 make i-x1 an interval or else just skip and increment
                    //if we have slots like 10-11 and 11-12 i=11 for 2nd iteration so ignores if and i=12
                    if(i<x1){
                        String min=String.valueOf(i%100);
                        //as it gave 9:0
                        if(min.length()==1){
                            min+="0";
                        }
                        String s=String.valueOf(i/100)+":"+min;
                        //addSlot only if duration is more than slotsize
                        if(this.getduration1(s,cslotsbysidciddate.get(j).getStartTime())>=30){
                     slotsDTOList.add(new SlotsDTO(s+"-"+cslotsbysidciddate.get(j).getStartTime()
                     , SlotStatus.AVAILABLE));
                        }
                        if(this.getduration1(cslotsbysidciddate.get(j).getStartTime()
                                ,cslotsbysidciddate.get(j).getEndTime())>=30){
                        slotsDTOList.add(new SlotsDTO(
                                cslotsbysidciddate.get(j).getStartTime()+"-"
                                +cslotsbysidciddate.get(j).getEndTime()
                                ,cslotsbysidciddate.get(j).getSlotStatus()));
                        }
                       //System.out.println("Court no: "+cid+" From-"+s+" To "+cslotsbysidciddate.get(j).getStartTime());
                        //what if no time slot occupied that day
                        // solved in next if condn
                    }//for continuous slots
                    else if(i==x1){
                        String min=String.valueOf(i%100);
                        //as it gave 9:0
                        if(min.length()==1){
                            min+="0";
                        }
                        String s=String.valueOf(i/100)+":"+min;
                        //addSlot only if duration is more than slotsize
                        if(this.getduration1(s,cslotsbysidciddate.get(j).getEndTime())>=30){
                            slotsDTOList.add(new SlotsDTO(s+"-"
                                    +cslotsbysidciddate.get(j).getEndTime()
                                    ,cslotsbysidciddate.get(j).getSlotStatus()));


                        }
                    }
                    i=Integer.parseInt(cslotsbysidciddate.get(j).getEndTime().replace(":", ""));

                }
                if(i<closingtime){
                    String min=String.valueOf(i%100);
                    //as it gave 9:0
                    if(min.length()==1){
                        min+="0";
                    }
                    String s=String.valueOf(i/100)+":"+min;
                    //System.out.println("Court no: "+cid+" From-"+s+" To 21:00");
                    String closingtimestring;
                    if(closingtime==2100){
                         closingtimestring="21:00";
                    }
                    else{
                        closingtimestring="22:00";
                    }
                    if(this.getduration1(s,closingtimestring)>=30){

                        SlotsDTO slotsDTO =new SlotsDTO();
                    slotsDTO.setTimings(s+"-"+closingtimestring);
                       // slotsDTO.setStatus("Free");
                        slotsDTO.setStatus(SlotStatus.AVAILABLE);
                        slotsDTOList.add(slotsDTO);

                    }
                }
            map.put(""+cid,slotsDTOList);
        }

        System.out.println(output);
        return map;
    }
 /**
     * Function to get the getAllCourts of available slots for the next 5 days based on the sport and date
     * Additional Functionality -
     *      Checks if the given date in the courtslots object is a holiday
     *      Uses the findbysiddate in each iteration to get the free slots for the day
     *      It is iterated 5 times
     * @param sid
     * @param date
     *
     * @return List of the free slots for the next 5 days
     */
    public Map<String, Map<String, List<SlotsDTO>>> fivedays(int sid, String date) throws ResourceNotFoundException {

        Map<String, Map<String, List<SlotsDTO>>> map = new LinkedHashMap<>();

        for(int i=0;i<5;i++) {
            //check for holiday
            CourtSlotsResponseDTO courtSlotsResponseDTO=new CourtSlotsResponseDTO();
            if(holidaysRepository.existsByDate(date)){
               map.put(date ,null);//+"is a Holiday"
              // System.out.println(date +"is a Holiday");

            }
            else {
                map.put(date,this.findbysiddate(sid, date));
            }
            date=this.getNextDate1(date);
        }
       // return courtSlotsResponseDTOList;
        return map;
    }

    /**
     * Function to compute the total cost based on the duration computed by the getduration function
     * @param courtslot
     *            An object with details of CourtSlots Table
     * @return Total cost
     */
    public int getcost(CourtSlots courtslot) {
        return (this.getduration(courtslot)/30)*sportRepository.getBySportId(courtslot.getSport().getSportId()).getPrice();
    }
    /**
     * Function to convert the Dto object to the courtslot object .
     * @param courtslotDTO
     *            An object with details of CourtSlotsResponseDTO Table
     * @return courtslot object
     */
    public CourtSlots convert(CourtSlotsRequestDTO courtSlotsRequestDTO) throws ResourceNotFoundException {
        Sport sport=sportRepository.findById(courtSlotsRequestDTO.getSportId()).orElseThrow(
                () -> new ResourceNotFoundException("Sport ID " + courtSlotsRequestDTO.getSportId() + " not found!"));
        Court court =courtRepository.findBySportAndCourtNo(sport,courtSlotsRequestDTO.getCourtNumber()).orElseThrow(
                () -> new ResourceNotFoundException("Court no " + courtSlotsRequestDTO.getCourtNumber() + " not found!"));

        return new CourtSlots(0,court,sport,courtSlotsRequestDTO.getStartTime(),courtSlotsRequestDTO.getEndTime()
                ,courtSlotsRequestDTO.getDate()
                , SlotStatus.RESERVED);
    }


    public void deleteByCourtSlotId(Integer id) throws ResourceNotFoundException {
        System.out.println(id+"cs");
        CourtSlots  courtSlots=courtslotsrepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("CourtSlot " +id + " not found!"));
        try{
            Booking booking= bookingRepository.findByCourtSlotsCourtslotId(id);
            booking.setBookingStatus(BookingStatus.FAILED);
            bookingRepository.save(booking);
            courtSlots.setSlotStatus(SlotStatus.AVAILABLE);
            courtslotsrepo.save(courtSlots);
            //    courtslotsrepo.deleteById(id);
        }
        catch (NoSuchElementException e ){
            throw new ResourceNotFoundException("CourtSlot with ID " + id + " not found!");
        }

    }

    public CourtSlotListResponseDTO convertToCourtSlotList(List<CourtSlots> courtSlotsList) {
        List<CourtSlotsResponseDTO> courtSlotsResponseDTOList=new ArrayList<CourtSlotsResponseDTO>();
        for (CourtSlots courtSlot:courtSlotsList ) {
            CourtSlotsResponseDTO courtSlotsResponseDTO=new CourtSlotsResponseDTO(null,courtSlot.getSlotStatus(),
                    courtSlot.getCourtslotId(),courtSlot.getCourt().getCourtId()
                    ,courtSlot.getCourt().getCourtNo(),courtSlot.getSport().getSportId(),
                    courtSlot.getSport().getSportName(),courtSlot.getStartTime(),
                    courtSlot.getEndTime(),courtSlot.getDate());
            courtSlotsResponseDTOList.add(courtSlotsResponseDTO);
        }
        return new CourtSlotListResponseDTO(
                "Required List of CourtSlots",courtSlotsResponseDTOList,null);
    }

    public GetCostResponseDTO getcostDTO(CourtSlotsRequestDTO courtSlotsRequestDTO) throws ResourceNotFoundException, ParseException {
        CourtSlots courtslot=this.convert(courtSlotsRequestDTO);

        int totalcost= this.getcost(courtslot);

        if(this.checker(courtslot))
        {  return new GetCostResponseDTO(totalcost,courtslot.getSport().getPrice(),courtslot.getSport().getSportName()
                ,courtslot.getStartTime(),courtslot.getEndTime(),this.getduration(courtslot)/60 +" Hour" +" "
                +(this.getduration(courtslot)%60)+" minutes",courtslot.getDate(),null);

        }
        else{
            GetCostResponseDTO getCostResponseDTO=new GetCostResponseDTO();
            getCostResponseDTO.setMessage("Not valid");
            return getCostResponseDTO;}
    }
    @Scheduled(cron="0 0/5 * * * *")
    public void crondeleteByStatus() throws ResourceNotFoundException {
        List<CourtSlots> courtSlotsList=courtslotsrepo.findAll();
        log.info("1");
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp timestamp1 = new Timestamp(System.currentTimeMillis());
        for (CourtSlots courtSlots:courtSlotsList) {
            Booking booking=bookingRepository.findByCourtSlotsCourtslotId(courtSlots.getCourtslotId());
            Timestamp timestamp2=booking.getCreateDate();
            long diff=timestamp2.compareTo(timestamp1);
            log.info(timestamp2.toString()+" "+timestamp1.toString());
            TimeDifference timeDifference=findDifference(timestamp2.toString(),timestamp1.toString());
            System.out.println(timeDifference.toString());
            if(courtSlots.getSlotStatus().equals(SlotStatus.RESERVED)){
                if(timeDifference.getDifference_In_Days()==0&&timeDifference.getDifference_In_Hours()==0
                        &&timeDifference.getDifference_In_Years()==0&&timeDifference.getDifference_In_Minutes()<=10){
                    log.info("skip"+timeDifference.getDifference_In_Minutes());
                }
                else {
                    log.info("deleted");
                    booking.setBookingStatus(BookingStatus.FAILED);
                    bookingRepository.save(booking);
                    courtSlots.setSlotStatus(SlotStatus.AVAILABLE);
                    courtslotsrepo.save(courtSlots);
                    //courtslotsrepo.delete(courtSlots);

                }
            }
        }//make it check for creation time stamp

    }
    /**
     * Function to get the next date according to the calender
     * Additional Functionality -
     *      Changes the format from dd-mm-yyyy to yyyy-mm-dd for the computational purposes
     * @param curDate
     *             The currentdate
     * @return The next date
     */
    public static String getNextDate1(String curDate) {
        String startDateString = curDate;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String cdate=LocalDate.parse(startDateString,formatter).format(formatter2);
        //System.out.println(cdate);
        String nxt=LocalDate.parse(cdate).plusDays(1).toString();
        String startDateString1 = nxt;
        String cdatef=LocalDate.parse(startDateString1,formatter2).format(formatter);
        //System.out.println(nxt);
        return cdatef;
    }
    /**
     * Function to compute the duration using the Starttime and the Endtime of the courtslot
     * @param courtslot
     *            An object with details of CourtSlots Table
     * @return The duration in minutes
     */
    private int getduration(CourtSlots courtslot) {
        int time1 = Integer.parseInt(courtslot.getStartTime().replace(":", ""));
        //converting startime and et to (int)1200 form 12:00
        int time2 = Integer.parseInt(courtslot.getEndTime().replace(":", ""));
        int hourDiff = time2 / 100 - time1 / 100 - 1;

        // difference between minutes
        int minDiff = time2 % 100 + (60 - time1 % 100);

        if (minDiff >= 60) {
            hourDiff++;
            minDiff = minDiff - 60;
        }
        return (hourDiff*60)+minDiff;
    }
    /**
     * Function to compute the duration using the Starttime and the Endtime passed to the function
     * @param s
     *            The start time of the game
     * @param e
     *            The end time of the game
     * @return The duration in minutes
     */
    private int getduration1(String s, String e) {
        int time1 = Integer.parseInt(s.replace(":", ""));
        //converting startime and et to (int)1200 form 12:00
        int time2 = Integer.parseInt(e.replace(":", ""));
        int hourDiff = time2 / 100 - time1 / 100 - 1;

        // difference between minutes
        int minDiff = time2 % 100 + (60 - time1 % 100);

        if (minDiff >= 60) {
            hourDiff++;
            minDiff = minDiff - 60;
        }
        return (hourDiff*60)+minDiff;
    }

    /**
     * Function to get the day of the week based on the Date parameter.
     * @param date
     * @return Day
     */
    private int daychecker(String date) {
        int intdate=Integer.parseInt(date.replace("-",""));
        System.out.println(intdate+"date int");
        int y=intdate%10000;intdate/=10000;
        int m=intdate%100;intdate/=100;
        int d=intdate;
        System.out.println(d+" "+m+" "+y+" ");
        int t[] = { 0, 3, 2, 5, 0, 3, 5, 1, 4, 6, 2, 4 };
        y -= (m < 3) ? 1 : 0;
        System.out.println(d+" "+m+" "+y+" ");
        int x = (y + y / 4 - y / 100 + y / 400 + t[m - 1] + d) % 7;
        System.out.println(x);
        return x;
    }
    static TimeDifference findDifference(String start_date,
                                         String end_date)
    {
        // SimpleDateFormat converts the
        // string format to date object
        SimpleDateFormat sdf
                = new SimpleDateFormat(
                "dd-MM-yyyy HH:mm:ss");

        // Try Class
        try {

            // parse method is used to parse
            // the text from a string to
            // produce the date
            Date d1 = sdf.parse(start_date);
            Date d2 = sdf.parse(end_date);

            // Calucalte time difference
            // in milliseconds
            long difference_In_Time
                    = d2.getTime() - d1.getTime();

            // Calucalte time difference in seconds,
            // minutes, hours, years, and days
            long difference_In_Seconds
                    = TimeUnit.MILLISECONDS
                    .toSeconds(difference_In_Time)
                    % 60;

            long difference_In_Minutes
                    = TimeUnit
                    .MILLISECONDS
                    .toMinutes(difference_In_Time)
                    % 60;

            long difference_In_Hours
                    = TimeUnit
                    .MILLISECONDS
                    .toHours(difference_In_Time)
                    % 24;

            long difference_In_Days
                    = TimeUnit
                    .MILLISECONDS
                    .toDays(difference_In_Time)
                    % 365;

            long difference_In_Years
                    = TimeUnit
                    .MILLISECONDS
                    .toDays(difference_In_Time)
                    / 365l;

            // Print the date difference in
            // years, in days, in hours, in
            // minutes, and in seconds
            System.out.print(
                    "Difference"
                            + " between two dates is: ");

            // Print result
            System.out.println(
                    difference_In_Years
                            + " years, "
                            + difference_In_Days
                            + " days, "
                            + difference_In_Hours
                            + " hours, "
                            + difference_In_Minutes
                            + " minutes, "
                            + difference_In_Seconds
                            + " second.pts");
            return new TimeDifference(difference_In_Years,difference_In_Days,difference_In_Hours
                    ,difference_In_Minutes,difference_In_Seconds);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


}

/*
convert
        CourtSlots courtSlots=new CourtSlots();

        System.out.println(sport);
        courtSlots.setSport(sport);
     //   List<Court> court =new ArrayList<Court>() ;
        //Court court=courtRepository.findByCourtNoAndSportSportId(courtSlotsRequestDTO.getCourtNumber(),sport.getSportId());//check properly
        System.out.println(courtSlotsRequestDTO.getCourtNumber()+" " +sport.getSportId());
        System.out.println(court);
        courtSlots.setCourt(court);
       // courtSlots.setCourt(court);
        courtSlots.setDate(courtSlotsRequestDTO.getDate());
        courtSlots.setStartTime(courtSlotsRequestDTO.getStartTime());
        courtSlots.setEndTime(courtSlotsRequestDTO.getEndTime());
        System.out.println(courtSlots);
        return courtSlots;


        // int dateToday=Integer.parseInt(dtf.format(now).substring(0,8));
       // int timeToday=Integer.parseInt(dtf.format(now).substring(9));
       // log.info(String.valueOf(dateToday));
     //   log.info(String.valueOf(intdate));
        //String curdate=dtf.format(now).substring(0,2)+"-"+dtf.format(now).substring(2,4)+"-"+
         //       dtf.format(now).substring(4,11)+":"+dtf.format(now).substring(11);
        String curdate=dtf.format(now);
        String gdate=courtslots.getDate()+" "+courtslots.getStartTime();

        System.out.println((curdate)+" xcxcxc "+(gdate));
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy h:m");
        System.out.println(sdf.parse(curdate)+" xcxcxc "+
                sdf.parse(courtslots.getDate()+" "+courtslots.getStartTime()));
        System.out.println(sdf.parse(dtf.format(now)).before(sdf.parse(courtslots.getDate()+" "+courtslots.getStartTime())));

public Map<String, List<SlotsDTO>> findbysiddate(int sid, String date) throws ResourceNotFoundException {

        //find cid by sid
        //check if sportId is valid
        List<CourtSlotsResponseDTO> courtSlotsResponseDTOList=new ArrayList<CourtSlotsResponseDTO>();
        List<Court> courtids=courtRepository.findBySportSportId(sid);//findCourtIdBySportSportId
        for (Court court:courtids ) {
            System.out.println(court);
        }

        List<String > output = new ArrayList<String>();
        //output.addSlot("List of Available Courts on "+date+" for the sport "+sportRepository.getSport_nameBysportid(sid).getSport_name());
        //System.out.println(courtids.size()+"Size");
        Map<String, List<SlotsDTO>> map = new LinkedHashMap<>();
        for (int i1=0;i1<courtids.size();i1++) {
            int value =courtids.get(i1).getCourtId();
            List <SlotsDTO> slotsDTOList=new  ArrayList<SlotsDTO>();
            //System.out.println(courtids.get(i1)+ " "+value);
            //find all courts for cid,sid,date
            //cslotsbysidciddate is getAllCourts fo cslots for each court id
            List<CourtSlots> cslotsbysidciddate = this.findbysiddatecid(sid, date,value );//z
            //sort acc to starttime
            Collections.sort(cslotsbysidciddate, new Comparator<CourtSlots>() {
                @Override
                public int compare(CourtSlots a1, CourtSlots a2) {
                    int x=Integer.parseInt(a1.getStartTime().replace(":", ""));
                    int y=Integer.parseInt(a2.getStartTime().replace(":", ""));
                    return x- y;
                }
            });
            //get courtNo for cid
            Court court=courtRepository.findById(value).orElseThrow(
                    () -> new ResourceNotFoundException("Court with ID " + value + " not found!"));
                   // findCourtNoByCourtId(value);

            int cid= court.getCourtNo();

            int openingtime,closingtime;

            if(daychecker(date)==0||daychecker(date)==6){
                openingtime=600;
                closingtime=2200;
                System.out.println(daychecker(date)+"weekend");
            }
            else{
                System.out.println(daychecker(date)+"weekday");
                openingtime=900;
                closingtime=2100;
            }
            int i=openingtime;
                //get available tslots starting comparison with 9:00 and 0th starttime as getAllCourts is sorted
                for(int j=0;j<cslotsbysidciddate.size();j++){
                    CourtSlotsResponseDTO courtSlotsResponseDTO=new CourtSlotsResponseDTO();

                    int x1=Integer.parseInt(cslotsbysidciddate.get(j).getStartTime().replace(":", ""));
                    //x1 is the jth start time if i is lesser than x1 make i-x1 an interval or else just skip and increment
                    //if we have slots like 10-11 and 11-12 i=11 for 2nd iteration so ignores if and i=12
                    if(i<x1){
                        String min=String.valueOf(i%100);
                        //as it gave 9:0
                        if(min.length()==1){
                            min+="0";
                        }
                        String s=String.valueOf(i/100)+":"+min;
                        //addSlot only if duration is more than slotsize
                        if(this.getduration1(s,cslotsbysidciddate.get(j).getStartTime())>=30){
                            courtSlotsResponseDTO.setMessage("Court no: "+cid+" From-"+s+" To "+cslotsbysidciddate.get(j).getStartTime());
                            courtSlotsResponseDTOList.add(courtSlotsResponseDTO);
                         //   SlotsDTO slotsDTO =new SlotsDTO();
                          //  slotsDTO.setTimings(" From-"+s+" To "+cslotsbysidciddate.get(j).getStartTime());
                     //slotsDTO.setStatus("Free");
                     slotsDTOList.add(new SlotsDTO(" From-"+s+" To "+cslotsbysidciddate.get(j).getStartTime()
                     ,"Free"));
                     slotsDTOList.add(new SlotsDTO(" From-"+
                             cslotsbysidciddate.get(j).getStartTime()+" To "
                             +cslotsbysidciddate.get(j).getEndTime()
                                    ,cslotsbysidciddate.get(j).getSlotStatus()));
                       //     slotsDTO.setTimings(" From-"+
                       //             cslotsbysidciddate.get(j).getStartTime()+" To "
                        //            +cslotsbysidciddate.get(j).getEndTime());
                        //    slotsDTO.setStatus(cslotsbysidciddate.get(j).getSlotStatus());
                         //   slotsDTOList.add(slotsDTO);
                        output.add("Court no: "+cid+" From-"+s+" To "
                                +cslotsbysidciddate.get(j).getStartTime()+" Free");
                            output.add("Court no: "+cid+" From-"+cslotsbysidciddate.get(j).getStartTime()+" To "
                                    +cslotsbysidciddate.get(j).getEndTime()+" "+
                                    cslotsbysidciddate.get(j).getSlotStatus());
                      courtSlotsResponseDTO.setMessage("Court no: "+cid+" From-"+
                              cslotsbysidciddate.get(j).getStartTime()+" To "
                              +cslotsbysidciddate.get(j).getEndTime()+" "+
                              cslotsbysidciddate.get(j).getSlotStatus());
                   courtSlotsResponseDTOList.add(courtSlotsResponseDTO);
                        }
                       //System.out.println("Court no: "+cid+" From-"+s+" To "+cslotsbysidciddate.get(j).getStartTime());
                        //what if no time slot occupied that day
                        // solved in next if condn
                    }
                    i=Integer.parseInt(cslotsbysidciddate.get(j).getEndTime().replace(":", ""));

                }
                if(i<closingtime){
                    String min=String.valueOf(i%100);
                    //as it gave 9:0
                    if(min.length()==1){
                        min+="0";
                    }
                    String s=String.valueOf(i/100)+":"+min;
                    //System.out.println("Court no: "+cid+" From-"+s+" To 21:00");
                    String closingtimestring;
                    if(closingtime==2100){
                         closingtimestring="21:00";
                    }
                    else{
                        closingtimestring="22:00";
                    }
                    CourtSlotsResponseDTO courtSlotsResponseDTO1=new CourtSlotsResponseDTO();
                    if(this.getduration1(s,closingtimestring)>=30){
                        courtSlotsResponseDTO1.setMessage("Court no: "+cid+" From-"+s+" To "+closingtimestring+" Free");
                        courtSlotsResponseDTOList.add(courtSlotsResponseDTO1);
                    output.add("Court no: "+cid+" From-"+s+" To "+closingtimestring+" Free");
                        SlotsDTO slotsDTO =new SlotsDTO();
                    slotsDTO.setTimings("From-"+s+" To "+closingtimestring);
                        slotsDTO.setStatus("Free");
                        slotsDTOList.add(slotsDTO);

                    }
                }
            map.put("Court no:"+cid,slotsDTOList);
        }

        System.out.println(output);
        return map;
    }

//public Map<String, Map<String, List<SlotsDTO>>> fivedays(int sid, String date) throws ResourceNotFoundException {
    List<String> finalx=new ArrayList<String >();
    Map<String, Map<String, List<SlotsDTO>>> map = new LinkedHashMap<>();
    List<CourtSlotsResponseDTO> courtSlotsResponseDTOList=new ArrayList<CourtSlotsResponseDTO>();
    for(int i=0;i<5;i++) {
        //check for holiday
        CourtSlotsResponseDTO courtSlotsResponseDTO=new CourtSlotsResponseDTO();
        if(holidaysRepository.existsByDate(date)){
            courtSlotsResponseDTO.setMessage(date +"is a Holiday");
            courtSlotsResponseDTOList.add(courtSlotsResponseDTO);
            finalx.add(date +"is a Holiday");
            map.put(date +"is a Holiday",null);
            System.out.println(date +"is a Holiday");
            finalx.add("");
        }
        else {
            System.out.println(date);
            courtSlotsResponseDTO.setMessage(date+":");
            courtSlotsResponseDTOList.add(courtSlotsResponseDTO);
            finalx.add(date+":");
            //List<CourtSlotsResponseDTO> courtSlotsResponseDTOS=findbysiddate(sid, date);
            //List<String> temp=this.findbysiddate(sid, date);
            map.put(date,this.findbysiddate(sid, date));
            // courtSlotsResponseDTOList.addAll(courtSlotsResponseDTOS);
            //finalx.addAll(temp);
            finalx.add("");
            //System.out.println();

        }
        date=this.getNextDate1(date);
    }
    // return courtSlotsResponseDTOList;
    return map;
}
*/