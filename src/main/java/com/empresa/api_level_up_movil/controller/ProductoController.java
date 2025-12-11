package com.empresa.api_level_up_movil.controller;

import com.empresa.api_level_up_movil.dto.request.ProductoRequestDTO;
import com.empresa.api_level_up_movil.dto.response.ProductoResponseDTO;
import com.empresa.api_level_up_movil.dto.response.UserResponseDTO;
import com.empresa.api_level_up_movil.service.JwtService;
import com.empresa.api_level_up_movil.service.ProductoService;
import com.empresa.api_level_up_movil.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v2/productos")
@Tag(name = "Productos", description = "Operaciones relacionadas con productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @PostMapping
    @Operation(summary = "Guardar Producto", description = "Guarda un producto en la base de datos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "500", description = "Error interno al guardar")
    })
    public ResponseEntity<ProductoResponseDTO> guardarProducto(@RequestHeader("Authorization") String authHeader ,@ModelAttribute ProductoRequestDTO req) {
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

            ProductoResponseDTO res = productoService.saveProducto(req);
            if (res == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
            return ResponseEntity.ok(res);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/many")
    @Operation(summary = "Guardar muchos productos", description = "Guarda múltiples productos en una sola petición")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Productos guardados con éxito"),
            @ApiResponse(responseCode = "500", description = "Error al guardar los productos")
    })
    public ResponseEntity<String> guardarMuchosProductos(@RequestBody List<ProductoRequestDTO> req) {
        try {
            String res = productoService.saveManyProductos(req);

            if (res == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
            return ResponseEntity.ok(res);

        } catch (Exception e) {
            System.out.println("Error Controller: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    @Operation(summary = "Obtener todos los productos", description = "Obtiene una lista de todos los productos disponibles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "500", description = "Error al listar productos")
    })
    public ResponseEntity<List<ProductoResponseDTO>> listarProductos() {
        try {
            List<ProductoResponseDTO> res = productoService.findAllProductos();
            if (res == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
            return ResponseEntity.ok(res);

        } catch (Exception e) {
            System.out.println("Error Controller: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por ID", description = "Busca un producto específico por su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto encontrado"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ProductoResponseDTO> getProductoById(@RequestHeader("Authorization") String authHeader ,@PathVariable Long id) {
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

            ProductoResponseDTO res = productoService.findProductoById(id);
            if (res == null) {
                return ResponseEntity.notFound().build();
            }
            System.out.println( ResponseEntity.ok(res) );
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/update")
    @Operation(summary = "Actualizar producto", description = "Actualiza la información de un producto existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto actualizado correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "500", description = "Error al actualizar el producto")
    })
    public ResponseEntity<ProductoResponseDTO> updateProduct(@RequestHeader("Authorization") String authHeader, @ModelAttribute ProductoRequestDTO.ProductoUpdate req) {
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

            ProductoResponseDTO res = productoService.updateProductoById(req);
            if (res == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
            System.out.println( ResponseEntity.ok(res) );
            return ResponseEntity.ok(res);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Eliminar producto", description = "Elimina un producto de la base de datos por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto eliminado correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "500", description = "Error al eliminar el producto")
    })
    public ResponseEntity<ProductoResponseDTO> deleateProductoById(@RequestHeader("Authorization") String authHeader,@PathVariable Long id) {
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

            ProductoResponseDTO res = productoService.deleteProductoById(id);
            if (res == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
            System.out.println( ResponseEntity.ok(res) );
            return ResponseEntity.ok(res);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/cat/{req}")
    @Operation(summary = "Buscar por categoría", description = "Obtiene lista de productos filtrados por el nombre de la categoría")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Búsqueda exitosa"),
            @ApiResponse(responseCode = "500", description = "Error al realizar la búsqueda")
    })
    public ResponseEntity<List<ProductoResponseDTO>> findByCategoria(@PathVariable String req ) {
        try {
            List<ProductoResponseDTO> res = productoService.findByCategoria(req);
            if (res == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
            return ResponseEntity.ok(res);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}