package edu.miu.PhotoAppApiUsers.controllers;

import edu.miu.PhotoAppApiUsers.dto.UserDTO;
import edu.miu.PhotoAppApiUsers.model.CreateUserRequest;
import edu.miu.PhotoAppApiUsers.model.CreateUserResponse;
import edu.miu.PhotoAppApiUsers.model.UserResponseModel;
import edu.miu.PhotoAppApiUsers.service.UserService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    UserService userService;
    @Autowired
    private Environment env;

    @GetMapping("/status/check")
    public String status(){
        return "working "+env.getProperty("token.secret");
    }

    @PostMapping
    public ResponseEntity<CreateUserResponse> createUser(@Valid @RequestBody CreateUserRequest userDetails){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserDTO userDTO = modelMapper.map(userDetails, UserDTO.class);
        UserDTO resDTO = userService.createUser(userDTO);
        CreateUserResponse response = modelMapper.map(resDTO, CreateUserResponse.class);
        return new ResponseEntity(response,HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseModel> getUser(@PathVariable("userId") String userId){
        UserDTO userDTO = userService.getUserById(userId);
        UserResponseModel userResponseModel = new ModelMapper().map(userDTO, UserResponseModel.class);

        return new ResponseEntity<>(userResponseModel, HttpStatus.OK);
    }

}
