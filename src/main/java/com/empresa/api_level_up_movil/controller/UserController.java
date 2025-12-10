package com.empresa.api_level_up_movil.controller;

import com.empresa.api_level_up_movil.dto.request.LoginRequestDTO;
import com.empresa.api_level_up_movil.dto.request.UserRequestDTO;
import com.empresa.api_level_up_movil.dto.response.UserResponseDTO;
import com.empresa.api_level_up_movil.service.JwtService;
import com.empresa.api_level_up_movil.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v2/auth")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
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

    @PostMapping("/login")
    public ResponseEntity<String> Login(@RequestBody LoginRequestDTO req) {
        try {

            String token = userService.getToken(req);

            if (token == null) {
                return ResponseEntity.badRequest().build();

            }

            System.out.println("TOKEN GENERADO: "+ token);
            return ResponseEntity.ok(token);

        }catch (Exception ex){
            System.out.println("Error Controller: " + ex.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/perfil")
    public ResponseEntity<UserResponseDTO> getUserByToken(@RequestHeader("Authorization")  String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }


        String token = authHeader.substring(7);
        try {

            String email = jwtService.validateTokenAndGetEmail(token);

            UserResponseDTO userRes = userService.getUserByEmail(email);

            if (userRes == null) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }


            return ResponseEntity.ok(userRes);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

}
