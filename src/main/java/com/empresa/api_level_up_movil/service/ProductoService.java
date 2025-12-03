package com.empresa.api_level_up_movil.service;

import com.empresa.api_level_up_movil.dto.request.ProductoRequestDTO;
import com.empresa.api_level_up_movil.dto.response.ProductoResponseDTO;
import com.empresa.api_level_up_movil.model.Producto;
import com.empresa.api_level_up_movil.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    @Autowired
    ProductoRepository productoRepo;

    public ProductoResponseDTO saveProducto(ProductoRequestDTO req) {
        try {
            Producto prod = new Producto();

            prod.setNombre(req.getNombre());
            prod.setDescripcion(req.getDescripcion());
            prod.setPoster(req.getPoster());
            prod.setPrecio(req.getPrecio());
            prod.setCategoria(req.getCategoria());
            prod.setStock(req.getStock());

            productoRepo.save(prod);


            ProductoResponseDTO res = new ProductoResponseDTO();

            res.setId_producto(prod.getId_producto());
            res.setNombre(prod.getNombre());
            res.setDescripcion(prod.getDescripcion());
            res.setPoster(prod.getPoster());
            res.setPrecio(prod.getPrecio());
            res.setCategoria(prod.getCategoria());
            res.setStock(prod.getStock());

            return res;
        }catch (Exception e) {
            System.out.println("Error Service: " + e.getMessage());
            return null;
        }
    }

    public String saveManyProductos(List<ProductoRequestDTO> req) {

        try {

            for (ProductoRequestDTO prod : req) {

                Producto producto = new Producto();

                producto.setNombre(prod.getNombre());
                producto.setDescripcion(prod.getDescripcion());
                producto.setPoster(prod.getPoster());
                producto.setPrecio(prod.getPrecio());
                producto.setCategoria(prod.getCategoria());
                producto.setStock(prod.getStock());

                productoRepo.save(producto);

            }
            return "Productos guardados con exito!";

        } catch (Exception e) {
            System.out.println("Error Service: " + e.getMessage());
            return null;
        }

    }

    public List<ProductoResponseDTO> findAllProductos() {

        try {
            List<Producto> productos = productoRepo.findAll();
            List<ProductoResponseDTO> res = new ArrayList<>();

            for (Producto prod : productos) {

                ProductoResponseDTO resDTO = new ProductoResponseDTO();

                resDTO.setId_producto(prod.getId_producto());
                resDTO.setNombre(prod.getNombre());
                resDTO.setDescripcion(prod.getDescripcion());
                resDTO.setPoster(prod.getPoster());
                resDTO.setPrecio(prod.getPrecio());
                resDTO.setCategoria(prod.getCategoria());
                resDTO.setStock(prod.getStock());
                res.add(resDTO);

            }
            return res;
        }catch (Exception e) {
            System.out.println("Error Service: " + e.getMessage());
            return null;
        }

    }

}
