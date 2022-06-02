package com.mastera.atelier.Services;

import com.mastera.atelier.Models.Role;
import com.mastera.atelier.Models.User;
import com.mastera.atelier.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User register(String firstname, String lastname, String phone, String username, String password, MultipartFile file, String role) throws IOException {
        if(userRepository.findUserByUsername(username) == null){
            User user = new User(firstname,lastname,phone,username,password,file.getBytes(),role);
            userRepository.save(user);
            return user;
        }
        return null;
    }

    public User login(String username, String password){
        User user = userRepository.findUserByUsername(username);
        if(user.getPassword().equals(password)){
            return user;
        }
        return null;
    }

    public void update(User user, String newpass){
        user.setPassword(newpass);
        userRepository.save(user);
    }

    public List<User> getMasterUsers(){
        return userRepository.findUsersByRole(Role.MASTER.name());
    }

    public List<User> getAll(){
        return userRepository.findAll();
    }

    public User getUserByUsername(String username){
        return userRepository.findUserByUsername(username);
    }

    public User delete(String username){
        User user = userRepository.findUserByUsername(username);
        userRepository.delete(user);
        return user;
    }
}
