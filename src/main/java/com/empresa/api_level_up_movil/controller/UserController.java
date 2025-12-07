package com.empresa.api_level_up_movil.controller;

import com.empresa.api_level_up_movil.dto.request.UserRequestDTO;
import com.empresa.api_level_up_movil.dto.response.UserResponseDTO;
import com.empresa.api_level_up_movil.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v2/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDTO> guardarUser(@RequestBody UserRequestDTO req) {

        try {

            UserResponseDTO resp = userService.saveUser(req);
            if (resp != null) {
                return ResponseEntity.ok(resp);
            }
            return ResponseEntity.badRequest().build();

        } catch (Exception e) {
            System.out.println("Error Controller: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }

    }

}
