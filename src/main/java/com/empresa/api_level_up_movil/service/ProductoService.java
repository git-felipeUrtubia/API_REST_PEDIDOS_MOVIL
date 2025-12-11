package com.empresa.api_level_up_movil.service;

import com.empresa.api_level_up_movil.dto.request.ProductoRequestDTO;
import com.empresa.api_level_up_movil.dto.response.ProductoResponseDTO;
import com.empresa.api_level_up_movil.model.Producto;
import com.empresa.api_level_up_movil.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductoService {

    @Autowired
    ProductoRepository productoRepo;

    public ProductoResponseDTO saveProducto(ProductoRequestDTO req) {
        try {
            Producto prod = new Producto();

            prod.setNombre(req.getNombre());
            prod.setDescripcion(req.getDescripcion());
            prod.setPrecio(req.getPrecio());
            prod.setCategoria(req.getCategoria());
            prod.setStock(req.getStock());
            prod.setEstado(req.getEstado());


            if (req.getPoster() != null && !req.getPoster().isEmpty()) {

                // 1. APUNTAMOS A LA CARPETA 'public/img' DE TU FRONTEND
                String rutaFrontend = "C:/Users/Felipe/Desktop/Informatica/level_up/public/img/";

                // 2. Generar nombre único
                String nombreArchivo = UUID.randomUUID().toString() + "_" + req.getPoster().getOriginalFilename();
                Path rutaCompleta = Paths.get(rutaFrontend + nombreArchivo);

                // 3. ¡ESCRIBIR EL ARCHIVO FÍSICAMENTE EN TU CARPETA DE REACT!
                Files.copy(req.getPoster().getInputStream(), rutaCompleta, StandardCopyOption.REPLACE_EXISTING);

                // 4. GUARDAR LA RUTA "CORTA" EN LA BD
                prod.setPoster("http://localhost:5173/public/img/" + nombreArchivo);

            }

            productoRepo.save(prod);


            ProductoResponseDTO res = new ProductoResponseDTO();

            res.setId_producto(prod.getId_producto());
            res.setNombre(prod.getNombre());
            res.setDescripcion(prod.getDescripcion());
            res.setPoster(prod.getPoster());
            res.setPrecio(prod.getPrecio());
            res.setCategoria(prod.getCategoria());
            res.setStock(prod.getStock());
            res.setEstado(prod.getEstado());

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
                producto.setPoster(prod.getPoster().getOriginalFilename());
                producto.setPrecio(prod.getPrecio());
                producto.setCategoria(prod.getCategoria());
                producto.setStock(prod.getStock());
                producto.setEstado(prod.getEstado());

                productoRepo.save(producto);

            }
            return "Productos guardados con exito!";

        } catch (Exception e) {
            System.out.println("Error Service: " + e.getMessage());
            return null;
        }

    }

    public List<ProductoResponseDTO> findAllProductos() {

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
            resDTO.setEstado(prod.getEstado());
            res.add(resDTO);

        }
        return res;

    }

    public ProductoResponseDTO findProductoById(Long id) {
        Producto producto = productoRepo.findById(id).orElse(null);

        if (producto == null) {
            return null;
        }

        ProductoResponseDTO res = new ProductoResponseDTO();
        res.setId_producto(producto.getId_producto());
        res.setNombre(producto.getNombre());
        res.setDescripcion(producto.getDescripcion());
        res.setPoster(producto.getPoster());
        res.setPrecio(producto.getPrecio());
        res.setCategoria(producto.getCategoria());
        res.setStock(producto.getStock());
        res.setEstado(producto.getEstado());
        return res;
    }

    public ProductoResponseDTO updateProductoById(ProductoRequestDTO.ProductoUpdate req) {

        // Ruta de tu carpeta frontend
        String RUTA_FRONTEND = "C:/Users/Felipe/Desktop/Informatica/level_up/public/img/";

        // 1. CORRECCIÓN: Usamos findById directamente (Más rápido y eficiente)
        // Asumo que req tiene el ID, o lo pasas como argumento al método
        Optional<Producto> prodOpt = productoRepo.findById(req.getId_producto());

        if (prodOpt.isPresent()) {
            Producto prod = prodOpt.get();

            // Actualizamos datos básicos
            prod.setNombre(req.getNombre());
            prod.setDescripcion(req.getDescripcion());
            prod.setPrecio(req.getPrecio());
            prod.setCategoria(req.getCategoria());
            prod.setStock(req.getStock());

            // 2. CORRECCIÓN: Validamos si hay imagen NUEVA antes de hacer nada
            if (req.getPoster() != null && !req.getPoster().isEmpty()) {

                // A) --- BORRAR LA IMAGEN VIEJA ---
                // Importante: Leemos la ruta ANTES de sobreescribirla en el objeto
                String rutaViejaBD = prod.getPoster();

                if (rutaViejaBD != null && !rutaViejaBD.contains("default")) {
                    try {
                        // Obtenemos el índice de la última barra "/"
                        int indexUltimaBarra = rutaViejaBD.lastIndexOf("/");

                        // Cortamos el string desde esa barra en adelante para obtener solo "nombre_imagen.jpg"
                        String nombreArchivoViejo = rutaViejaBD.substring(indexUltimaBarra + 1);

                        // Ahora sí construimos la ruta física limpia
                        Path rutaFisicaVieja = Paths.get(RUTA_FRONTEND + nombreArchivoViejo);

                        // 3. CORRECCIÓN: ¡Ejecutamos el borrado!
                        Files.deleteIfExists(rutaFisicaVieja);

                    } catch (IOException e) {
                        System.out.println("No se pudo borrar la anterior, continuamos...");
                    }
                }

                // B) --- GUARDAR LA IMAGEN NUEVA ---
                try {
                    String nombreNuevo = UUID.randomUUID().toString() + "_" + req.getPoster().getOriginalFilename();
                    Path rutaFisicaNueva = Paths.get(RUTA_FRONTEND + nombreNuevo);

                    if (!Files.exists(Paths.get(RUTA_FRONTEND))) {
                        Files.createDirectories(Paths.get(RUTA_FRONTEND));
                    }

                    Files.copy(req.getPoster().getInputStream(), rutaFisicaNueva, StandardCopyOption.REPLACE_EXISTING);

                    // 4. CORRECCIÓN: AHORA SÍ actualizamos la referencia en el objeto
                    prod.setPoster("/img/" + nombreNuevo);

                } catch (IOException e) {
                    throw new RuntimeException("Error crítico al guardar imagen: " + e.getMessage());
                }
            }
            // Si no mandaron imagen nueva, 'prod.poster' se queda igual (no hacemos nada)

            productoRepo.save(prod);

            // Retornar respuesta
            ProductoResponseDTO res = new ProductoResponseDTO();
            res.setId_producto(prod.getId_producto());
            res.setNombre(prod.getNombre());
            res.setDescripcion(prod.getDescripcion());
            res.setPoster(prod.getPoster());
            res.setPrecio(prod.getPrecio());
            res.setCategoria(prod.getCategoria());
            res.setStock(prod.getStock());
            return res;
        }

        return null;
    }

    public ProductoResponseDTO deleteProductoById(Long id) {

        Producto prod = productoRepo.findById(id).orElse(null);

        if(prod.getEstado().equals("ACTIVO")) {
            return null;
        }

        productoRepo.delete(prod);
        ProductoResponseDTO res = new ProductoResponseDTO();
        res.setId_producto(prod.getId_producto());
        res.setNombre(prod.getNombre());
        res.setDescripcion(prod.getDescripcion());
        res.setPoster(prod.getPoster());
        res.setPrecio(prod.getPrecio());
        res.setCategoria(prod.getCategoria());
        res.setStock(prod.getStock());
        res.setEstado(prod.getEstado());
        return res;

    }

    public List<ProductoResponseDTO> findByCategoria(String req) {
        List<ProductoResponseDTO> res = new ArrayList<>();

        for( Producto p : productoRepo.findAll() ) {
            if(p.getCategoria().equals(req)) {
                ProductoResponseDTO dto = new ProductoResponseDTO();
                dto.setId_producto(p.getId_producto());
                dto.setNombre(p.getNombre());
                dto.setDescripcion(p.getDescripcion());
                dto.setPoster(p.getPoster());
                dto.setPrecio(p.getPrecio());
                dto.setCategoria(p.getCategoria());
                dto.setStock(p.getStock());
                res.add(dto);
            }
        }
        if (res.isEmpty()) {
            return null;
        }
        return res;
    }

}
