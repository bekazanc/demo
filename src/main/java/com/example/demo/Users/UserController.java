package com.example.demo.Users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;



    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            User registeredUser = userService.saveUser(user);
            return ResponseEntity.ok("User saved successfully: " + registeredUser.getEmail());
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Error occurred during registration: " + e.getMessage());
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        try {

            User user = userService.findByEmail(loginRequest.getEmail());


            if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                return ResponseEntity.status(401).body("Wrong Password");
            }


            return ResponseEntity.ok("Login successful! Welcome " + user.getName());
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("User is not found: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error occurred: " + e.getMessage());
        }
    }
}
