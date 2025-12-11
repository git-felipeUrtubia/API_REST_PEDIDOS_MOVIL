package com.empresa.api_level_up_movil.controller;

import com.empresa.api_level_up_movil.dto.request.LoginRequestDTO;
import com.empresa.api_level_up_movil.dto.request.UserRequestDTO;
import com.empresa.api_level_up_movil.dto.response.UserResponseDTO;
import com.empresa.api_level_up_movil.service.JwtService;
import com.empresa.api_level_up_movil.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v2/auth")
@Tag(name = "Autenticación y Usuarios", description = "Endpoints para registro, login y gestión de perfil de usuario")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    @Operation(summary = "Registrar usuario", description = "Crea una nueva cuenta de usuario en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o error en el registro")
    })
    public ResponseEntity<UserResponseDTO> guardarUser(@RequestBody UserRequestDTO req) {

        try {

            UserResponseDTO resp = userService.saveUser(req);
            if (resp != null) {
                return ResponseEntity.ok(resp);
            }
            return ResponseEntity.badRequest().build();

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }

    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Autentica al usuario y devuelve un token JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticación exitosa, token generado"),
            @ApiResponse(responseCode = "400", description = "Credenciales incorrectas o error en la solicitud")
    })
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
    @Operation(summary = "Obtener perfil", description = "Obtiene la información del usuario actual basado en el token JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil obtenido correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado (Token inválido o expirado)")
    })
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

    @PutMapping("/user/update/{id}")
    @Operation(summary = "Actualizar usuario", description = "Actualiza los datos de un usuario específico por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Error en la solicitud"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<?> updateUser(@RequestHeader("Authorization")  String authHeader, @PathVariable Long id, @RequestBody UserRequestDTO.UpdateUser req) {

        try {

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            String token = authHeader.substring(7);
            String email = jwtService.validateTokenAndGetEmail(token);

            UserResponseDTO userRes = userService.getUserByEmail(email);

            if (userRes == null) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            UserResponseDTO res = userService.updateUser(id, req);
            if (res == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return ResponseEntity.ok(res);

        }catch (Exception ex){
            ex.printStackTrace();
            return ResponseEntity.badRequest().build();
        }

    }

}