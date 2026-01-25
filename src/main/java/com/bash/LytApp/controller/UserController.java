package com.bash.LytApp.controller;

import com.bash.LytApp.dto.UserCreateDto;
import com.bash.LytApp.dto.UserDto;
import com.bash.LytApp.dto.UserResponseDto;
import com.bash.LytApp.dto.UserUpdateDto;
import com.bash.LytApp.security.AuthenticatedUser;
import com.bash.LytApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
//@CrossOrigin(origins = "http://localhost:3000")
@CrossOrigin(origins = "http://localhost:63342")
public class UserController {

    private final UserService userService;
    private final AuthenticatedUser authenticatedUser;

    public UserController(UserService userService, AuthenticatedUser authenticatedUser) {
        this.userService = userService;
        this.authenticatedUser = authenticatedUser;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        try {
            List<UserResponseDto> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getUserProfile() {
        try {
            Long userId = authenticatedUser.getUserId();
            UserResponseDto user = userService.getUserById(userId);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

//    @PostMapping
//    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserCreateDto userCreateDto) {
//        try {
//            UserResponseDto createdUser = userService.createUser(userCreateDto);
//            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
//        } catch (RuntimeException e) {
//            return ResponseEntity.badRequest().build();
//        }
//    }

//    @PutMapping("/update")
//    public ResponseEntity<UserResponseDto> updateUser(@RequestBody UserUpdateDto userUpdateDto) {
//
//        //running updates with the try/catch statement
//           try {
//               Long userId = authenticatedUser.getUserId();
//              UserResponseDto updatedUser = userService.updateUser(userId, userUpdateDto);
//              return ResponseEntity.ok(updatedUser);
//          } catch (RuntimeException e) {
//              return ResponseEntity.notFound().build();
//          }
//    }

//    @DeleteMapping("/delete")
//    public ResponseEntity<?> deleteUser() {
//        try {
//            Long userId = authenticatedUser.getUserId();
//            userService.deleteUser(userId);
//            return ResponseEntity.ok("Deleted!");
//        } catch (RuntimeException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
}
