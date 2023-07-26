package edu.miu.PhotoAppApiUsers.service;

import edu.miu.PhotoAppApiUsers.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    public UserDTO createUser(UserDTO userDetails);
    public UserDTO getUserDetailsByEmail(String email);

    UserDTO getUserById(String userId);
}
