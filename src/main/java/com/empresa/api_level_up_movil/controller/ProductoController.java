package com.empresa.api_level_up_movil.controller;

import com.empresa.api_level_up_movil.dto.request.ProductoRequestDTO;
import com.empresa.api_level_up_movil.dto.response.ProductoResponseDTO;
import com.empresa.api_level_up_movil.service.ProductoService;
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

    @PostMapping
    public ResponseEntity<ProductoResponseDTO> guardarProducto(@RequestBody ProductoRequestDTO req) {

        try {

            ProductoResponseDTO res = productoService.saveProducto(req);
            if (res == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
            return ResponseEntity.ok(res);

        }catch (Exception e) {
            System.out.println("Error Controller: " + e.getMessage());
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

}
