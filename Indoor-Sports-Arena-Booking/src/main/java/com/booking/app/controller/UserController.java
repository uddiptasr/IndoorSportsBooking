package com.booking.app.controller;

import com.booking.app.ResponseDto;
import com.booking.app.requestDTO.UserRequestDTO;
import com.booking.app.responseDTO.UserResponseDTO;
import com.booking.app.exception.ResourceNotFoundException;
import com.booking.app.responseDTO.UserListResponseDTO;
import com.booking.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService uservice;

    /**
     * Retrieves all the records in user table. METHOD = Get.
     *
     * @param null.
     * @return UserListResponseDTO of all the records.
     */
    @GetMapping
    public ResponseEntity<ResponseDto> getAllUsers(){

        //return uservice.getAllUsers();
        return new ResponseEntity<>(new ResponseDto(uservice.getAllUsers(), null), HttpStatus.OK);

    }
    /**
     * Retrieves the records in user table with the given User Id.
     * METHOD = Get.
     *
     * @param userRequestDTO
     * @return List of all the records which contain that UserId..
     * @throws ResourceNotFoundException
     *             if given Id does not exist
     */
    @GetMapping("/{uid}")
    public ResponseEntity<ResponseDto> getByUserId(@PathVariable int uid) throws ResourceNotFoundException {
        UserRequestDTO userRequestDTO=new UserRequestDTO();  userRequestDTO.setUserId(uid);
        return new ResponseEntity<>(new ResponseDto(uservice.getByUserId(userRequestDTO), null), HttpStatus.OK);
//controlleradvice
    }

    /**
     * Updates data in the User table. METHOD = Put.
     *
     * @param userRequestDTO
     *                  Object containing all the details of the attributes and the UserId which is to be updated.
     * @return UserResponseDTO Object of the updated record.
     * @throws ResourceNotFoundException
     *             if given Id does not exist
     */
    @PutMapping
    public ResponseEntity<ResponseDto> updateUser(@Valid @RequestBody UserRequestDTO userRequestDTO) throws ResourceNotFoundException {
        UserResponseDTO userResponseDTO= uservice.updateUser(userRequestDTO);
       // userResponseDTO.setMesssage("User has been updated to");

        //return  userResponseDTO;
        return new ResponseEntity<>(new ResponseDto(userResponseDTO, null), HttpStatus.OK);
        }
    /**
     * Saves data in the User table. METHOD = Post
     *
     * @param userRequestDTO
     *            Object containing all the details of the attributes.
     * @return UserResponseDTO with String containing the message to be displayed.
     */
    @PostMapping("/add")
    public ResponseEntity<ResponseDto> registerUser(@Valid @RequestBody UserRequestDTO userRequestDTO ){
        UserResponseDTO userResponseDTO=uservice.save(userRequestDTO);
        userResponseDTO.setMesssage("User has been registered Succesfully");
        //return userResponseDTO;
        return new ResponseEntity<>(new ResponseDto(userResponseDTO, null), HttpStatus.OK);
    }

    /**
     * Deletes data in the User table. METHOD = Delete.
     *
     * @param userRequestDTO
     *              Object containing the userId
     * @return UserResponseDTO with String containing the message to be displayed.
     * @throws ResourceNotFoundException
     *             if given Id does not exist
     */
    @DeleteMapping("/{uid}")
        public ResponseEntity<ResponseDto> deleteByUserId(@PathVariable int uid) throws ResourceNotFoundException {
      UserRequestDTO userRequestDTO=new UserRequestDTO();  userRequestDTO.setUserId(uid);
        return new ResponseEntity<>(new ResponseDto(uservice.delete(userRequestDTO), null), HttpStatus.OK);
     }
    /**
     * Checks if the Passoword entered for the userId is correct.
     * METHOD = Get.
     *
     * @param userRequestDTO
     *               Object containing the userId and password
     * @return UserResponseDTO with String containing the message to be displayed.
     * @throws ResourceNotFoundException
     *             if given Id does not exist
     */
    @RequestMapping("/login")
    @ResponseBody
    public ResponseEntity<ResponseDto> authenticate(@RequestBody UserRequestDTO userRequestDTO) throws ResourceNotFoundException {
        return new ResponseEntity<>(new ResponseDto(uservice.authenticate(userRequestDTO), null), HttpStatus.OK);
    }
}

/*
    @GetMapping("/user/{id}")
    public User get(@PathVariable Integer id) throws ResourceNotFoundException {
        return uservice.getByUserId(id);

    }
    */
  /*
    @RequestMapping("/home")
    @ResponseBody
    public ModelAndView home(){
        System.out.println("hi");
        ModelAndView mv= new ModelAndView("view/home.jsp");
        return mv;
    }
    @RequestMapping("/register")
    @ResponseBody
    public ModelAndView register(){
        System.out.println("hi");
        ModelAndView mv1= new ModelAndView("view/AddUser.jsp");
        return mv1;
    }
    @RequestMapping("/login")
    @ResponseBody
    public ModelAndView login(){
        System.out.println("hi");
        ModelAndView mv1= new ModelAndView("view/login.jsp");
        return mv1;
    }*/
