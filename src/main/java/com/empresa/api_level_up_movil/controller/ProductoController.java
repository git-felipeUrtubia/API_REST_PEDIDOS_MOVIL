package com.empresa.api_level_up_movil.controller;

import com.empresa.api_level_up_movil.dto.request.ProductoRequestDTO;
import com.empresa.api_level_up_movil.dto.response.ProductoResponseDTO;
import com.empresa.api_level_up_movil.dto.response.UserResponseDTO;
import com.empresa.api_level_up_movil.service.JwtService;
import com.empresa.api_level_up_movil.service.ProductoService;
import com.empresa.api_level_up_movil.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v2/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @PostMapping
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

        }catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @PostMapping("/many")
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
    public ResponseEntity<List<ProductoResponseDTO>> listarProductos() {

        try {

            List<ProductoResponseDTO> res = productoService.findAllProductos();
            if (res == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
            return ResponseEntity.ok(res);

        }catch (Exception e) {
            System.out.println("Error Controller: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @GetMapping("/{id}")
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
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @PutMapping("/update")
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

        }catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @DeleteMapping("/delete/{id}")
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

        }catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/cat/{req}")
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
