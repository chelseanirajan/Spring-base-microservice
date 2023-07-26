package edu.miu.PhotoAppApiUsers.service;

import edu.miu.PhotoAppApiUsers.dto.UserDTO;
import edu.miu.PhotoAppApiUsers.entity.AlbumsServiceClient;
import edu.miu.PhotoAppApiUsers.entity.UserEntity;
import edu.miu.PhotoAppApiUsers.model.AlbumResponseModel;
import edu.miu.PhotoAppApiUsers.repository.UserRepository;
import feign.FeignException;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
public class UserServiceImpl implements UserService {

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    UserRepository userRepository;

//    RestTemplate restTemplate;

    AlbumsServiceClient albumsServiceClient;

    BCryptPasswordEncoder bCryptPasswordEncoder;

    Environment environment;


    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, AlbumsServiceClient albumsServiceClient, Environment environment) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.albumsServiceClient = albumsServiceClient;
        this.environment = environment;
    }

    @Override
    public UserDTO createUser(UserDTO userDetails) {
        userDetails.setUserId(UUID.randomUUID().toString());
        userDetails.setEncryptedPassword(bCryptPasswordEncoder.encode(userDetails.getPassword()));
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity userEntity = modelMapper.map(userDetails, UserEntity.class);
        userRepository.save(userEntity);
        return modelMapper.map(userEntity, UserDTO.class);
    }

    @Override
    public UserDTO getUserDetailsByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) throw new UsernameNotFoundException(email);
        return new ModelMapper().map(userEntity, UserDTO.class);
    }

    @Override
    public UserDTO getUserById(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null) throw new UsernameNotFoundException("User not found");
        UserDTO userDTO = new ModelMapper().map(userEntity, UserDTO.class);

//        String baseUrl = String.format(environment.getProperty("albums.url"), userId);
//        ResponseEntity<List<AlbumResponseModel>> response =
//                restTemplate.exchange(baseUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<AlbumResponseModel>>(){});
//        List<AlbumResponseModel> albumResponseModels = response.getBody();
        logger.info("Before calling albums microservice...");
        List<AlbumResponseModel> albumResponseModels = albumsServiceClient.getAlbums(userId);
        logger.info("After calling albums microservice...");
//        try {
//        } catch (FeignException ex) {
//            logger.error(ex.getLocalizedMessage());
//        }
        userDTO.setAlbums(albumResponseModels);
        return userDTO;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(username);
        if (userEntity == null) throw new UsernameNotFoundException("Username is not found");
        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), true, true, true, true, new ArrayList<>());
    }
}
