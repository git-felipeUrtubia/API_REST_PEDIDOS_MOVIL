package com.empresa.api_level_up_movil.controller;

import com.empresa.api_level_up_movil.dto.request.PedidoRequestDTO;
import com.empresa.api_level_up_movil.dto.response.PedidoResponseDTO;
import com.empresa.api_level_up_movil.dto.response.UserResponseDTO;
import com.empresa.api_level_up_movil.service.JwtService;
import com.empresa.api_level_up_movil.service.PedidoService;
import com.empresa.api_level_up_movil.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.AccessDeniedException;


import java.util.List;

@RestController
@RequestMapping("api/v2/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<PedidoResponseDTO> createPedido(@RequestHeader("Authorization") String authHeader,  @RequestBody PedidoRequestDTO req) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String token = authHeader.substring(7);

        try {

            String email = jwtService.validateTokenAndGetEmail(token);
            UserResponseDTO user = userService.getUserByEmail(email);

            if (user != null) {

                PedidoResponseDTO res = pedidoService.savePedido(req);
                if (res == null) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
                return ResponseEntity.ok(res);

            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @GetMapping
    public ResponseEntity<List<PedidoResponseDTO>> getAllPedidos(@RequestHeader("Authorization") String authHeader) {
        try {

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            String token = authHeader.substring(7);

            String email = jwtService.validateTokenAndGetEmail(token);

            List<PedidoResponseDTO> res = pedidoService.findAllPedidos(email);
            if (res == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
            return ResponseEntity.ok(res);

        } catch (AccessDeniedException e) {
            System.out.println("Error de Autorización (Rol 'user'): " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            System.out.println("Error Controller: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/update/state/{id_pedido}")
    public ResponseEntity<?> actualizarEstadoPedido(@RequestHeader("Authorization") String authHeader,  @PathVariable Long id_pedido) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            String token = authHeader.substring(7);
            String email = jwtService.validateTokenAndGetEmail(token);

            UserResponseDTO user = userService.getUserByEmail(email);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            boolean actualizado = pedidoService.actualizarEstadoPedido(id_pedido);

            if (actualizado) {
                return ResponseEntity.ok("Estado actualizado correctamente");
            } else {
                // Si devuelve false, es que el ID no existía
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El pedido no existe");
            }

        }catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/delete/state")
    public ResponseEntity<?> deleteByState(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            String token = authHeader.substring(7);
            String email = jwtService.validateTokenAndGetEmail(token);

            UserResponseDTO user = userService.getUserByEmail(email);


            if (user == null || !user.getRol().equalsIgnoreCase("admin")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado");
            }

            boolean eliminado = pedidoService.deleteByState();

            if (eliminado) {
                return ResponseEntity.ok("Pedidos completados eliminados correctamente");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron pedidos completados");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id_pedido}")
    public ResponseEntity<?> findPedidoById(@RequestHeader("Authorization") String authHeader, @PathVariable Long id_pedido) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            String token = authHeader.substring(7);
            String email = jwtService.validateTokenAndGetEmail(token);

            UserResponseDTO user = userService.getUserByEmail(email);


            if (user == null || !user.getRol().equals("admin")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado");
            }

            PedidoResponseDTO.ById dto = pedidoService.getPedidoById(id_pedido);
            if (dto == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(dto);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/user/{id_user}")
    public ResponseEntity<?> findPedidoByIdUser(@RequestHeader("Authorization") String authHeader, @PathVariable Long id_user) {
        try {

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            String token = authHeader.substring(7);
            String email = jwtService.validateTokenAndGetEmail(token);

            UserResponseDTO user = userService.getUserByEmail(email);

            List<PedidoResponseDTO.ById> dto = pedidoService.getPedidosByIdUser(id_user);

            if (dto == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(dto);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
