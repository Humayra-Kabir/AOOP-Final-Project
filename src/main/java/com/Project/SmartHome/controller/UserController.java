package com.Project.SmartHome.controller;

import com.Project.SmartHome.dto.UserDto;
import com.Project.SmartHome.entity.User;
import com.Project.SmartHome.entity.UserRole;
import com.Project.SmartHome.Reposatory.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/test")
    public String test() {
        return "User Controller is working!";
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/user/{id}")
    public User getUserById(@PathVariable Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @GetMapping("/user/email")
    public User getUserByEmail(@RequestParam String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @GetMapping("/addUser")
    public User addUser(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String passwordHash,
            @RequestParam String phone,
            @RequestParam String role) {

        User newUser = new User();
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setEmail(email);
        newUser.setPasswordHash(passwordHash);
        newUser.setPhone(phone);
        newUser.setRole(UserRole.valueOf(role));
        newUser.setIsEmailVerified(false);

        return userRepository.save(newUser);
    }

    @PostMapping("/saveUser")
    public User saveUser(@RequestBody UserDto userDto) {
        User newUser = new User();
        newUser.setFirstName(userDto.getFirstName());
        newUser.setLastName(userDto.getLastName());
        newUser.setEmail(userDto.getEmail());
        newUser.setPasswordHash(userDto.getPasswordHash());
        newUser.setPhone(userDto.getPhone());
        newUser.setRole(UserRole.valueOf(userDto.getRole()));
        newUser.setIsEmailVerified(false);

        return userRepository.save(newUser);
    }

    @PutMapping("/updateUser/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser != null) {
            existingUser.setFirstName(userDto.getFirstName());
            existingUser.setLastName(userDto.getLastName());
            existingUser.setEmail(userDto.getEmail());
            existingUser.setPhone(userDto.getPhone());
            return userRepository.save(existingUser);
        }
        return null;
    }

    @DeleteMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "User deleted successfully";
    }
}
