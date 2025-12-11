package com.empresa.api_level_up_movil.controller;

import com.empresa.api_level_up_movil.dto.request.PedidoRequestDTO;
import com.empresa.api_level_up_movil.dto.response.PedidoResponseDTO;
import com.empresa.api_level_up_movil.dto.response.UserResponseDTO;
import com.empresa.api_level_up_movil.service.JwtService;
import com.empresa.api_level_up_movil.service.PedidoService;
import com.empresa.api_level_up_movil.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v2/pedidos")
@Tag(name = "Pedidos", description = "Operaciones relacionadas con la gestión de pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @PostMapping
    @Operation(summary = "Crear pedido", description = "Registra un nuevo pedido para el usuario autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido creado exitosamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado (Token inválido o faltante)"),
            @ApiResponse(responseCode = "500", description = "Error interno al procesar el pedido")
    })
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
    @Operation(summary = "Listar pedidos", description = "Obtiene los pedidos asociados al usuario autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de pedidos obtenida con éxito"),
            @ApiResponse(responseCode = "401", description = "No autorizado o acceso denegado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
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
    @Operation(summary = "Actualizar estado del pedido", description = "Modifica el estado de un pedido existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado actualizado correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno al actualizar")
    })
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
    @Operation(summary = "Eliminar pedidos completados", description = "Elimina pedidos según su estado. Requiere rol ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedidos eliminados correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Prohibido (Requiere rol Admin)"),
            @ApiResponse(responseCode = "404", description = "No se encontraron pedidos para eliminar"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
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
    @Operation(summary = "Obtener pedido por ID", description = "Busca un pedido específico por su ID. Requiere rol ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Prohibido (Requiere rol Admin)"),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
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
    @Operation(summary = "Obtener pedidos por Usuario", description = "Obtiene todos los pedidos asociados a un ID de usuario específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Búsqueda exitosa"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "404", description = "No se encontraron pedidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
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