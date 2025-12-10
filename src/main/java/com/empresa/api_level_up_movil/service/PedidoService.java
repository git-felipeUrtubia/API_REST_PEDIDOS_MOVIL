package com.empresa.api_level_up_movil.service;

import com.empresa.api_level_up_movil.dto.request.DetallePedidoRequestDTO;
import com.empresa.api_level_up_movil.dto.request.PagoRequestDTO;
import com.empresa.api_level_up_movil.dto.request.PedidoRequestDTO;
import com.empresa.api_level_up_movil.dto.response.DetallePedidoResponseDTO;
import com.empresa.api_level_up_movil.dto.response.PagoResponseDTO;
import com.empresa.api_level_up_movil.dto.response.PedidoResponseDTO;
import com.empresa.api_level_up_movil.dto.response.UserResponseDTO;
import com.empresa.api_level_up_movil.model.*;
import com.empresa.api_level_up_movil.repository.UserRepository;
import com.empresa.api_level_up_movil.repository.PedidoRepository;
import com.empresa.api_level_up_movil.repository.ProductoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductoRepository productoRepo;


    public PedidoResponseDTO savePedido(PedidoRequestDTO req) {


        if (req.getDetalles().isEmpty()) {
            return null;
        }

        Pedido ped = new Pedido();

        ped.setNumero_pedido(req.getNumero_pedido());
        ped.setEstado(req.getEstado());
        User user = userRepo.findById(req.getUser_id()).get();
        ped.setUser(user);

        List<DetallePedido> detalle_pedidos = new ArrayList<>();
        for (DetallePedidoRequestDTO det : req.getDetalles()) {

            DetallePedido detalle = new DetallePedido();

            detalle.setCant(det.getCant());

            Producto prod = productoRepo.findById(det.getId_producto()).get();
            prod.setEstado("ACTIVO");
            detalle.setProducto(prod);
            detalle.setSubtotal(prod.getPrecio() * det.getCant());
            detalle.setPedido(ped);

            prod.setStock(prod.getStock() - det.getCant());
            productoRepo.save(prod);

            detalle_pedidos.add(detalle);

        }
        ped.setDetalle_pedidos(detalle_pedidos);

        List<Pago> pagos = new ArrayList<>();
        for(PagoRequestDTO pago : req.getPagos()) {
            Pago p = new Pago();
            p.setNumero_pago(pago.getNumero_pago());
            p.setSubtotal(pago.getSubtotal());
            p.setIva(pago.getIva());
            p.setMonto(pago.getMonto());
            p.setTipo(pago.getTipo());
            p.setFecha_registro(pago.getFecha_registro());
            p.setPedido(ped);
            pagos.add(p);
        }
        ped.setPagos(pagos);

        pedidoRepo.save(ped);

        PedidoResponseDTO res = new PedidoResponseDTO();

        res.setId_pedido(ped.getId_pedido());
        res.setId_user(ped.getUser().getId_user());
        res.setNumero_pedido(ped.getNumero_pedido());
        res.setEstado(ped.getEstado());
        List<DetallePedidoResponseDTO> dtRes = new ArrayList<>();
        for (DetallePedido det : ped.getDetalle_pedidos()) {

            DetallePedidoResponseDTO d = new DetallePedidoResponseDTO();

            d.setCant(det.getCant());
            d.setId_producto(det.getProducto().getId_producto());
            dtRes.add(d);

        }
        res.setDetalle_pedidos(dtRes);

        List<PagoResponseDTO> pagosResDTO = new ArrayList<>();
        for (Pago pago : ped.getPagos()) {
            PagoResponseDTO p = new PagoResponseDTO();
            p.setId_pago(pago.getId_pago());
            p.setNumero_pago(pago.getNumero_pago());
            p.setSubtotal(pago.getSubtotal());
            p.setIva(pago.getIva());
            p.setMonto(pago.getMonto());
            p.setTipo(pago.getTipo());
            p.setFecha_registro(pago.getFecha_registro());
            pagosResDTO.add(p);
        }
        res.setPagos(pagosResDTO);

        return res;

    }

    public List<PedidoResponseDTO> findAllPedidos(String email) {



        UserResponseDTO user = userService.getUserByEmail(email);
        if(user.getRol().equals("user")) {
            throw new AccessDeniedException("El usuario con rol 'user' no tiene permiso para ver todos los pedidos.");
        }


        List<Pedido> pedidos = pedidoRepo.findAll();

        List<PedidoResponseDTO> res = new ArrayList<>();
        for (Pedido ped : pedidos) {

            PedidoResponseDTO dto = new PedidoResponseDTO();

            dto.setId_pedido(ped.getId_pedido());
            dto.setId_user(ped.getUser().getId_user());
            dto.setNumero_pedido(ped.getNumero_pedido());
            dto.setEstado(ped.getEstado());
            List<DetallePedidoResponseDTO> dtRes = new ArrayList<>();
            for (DetallePedido det : ped.getDetalle_pedidos()) {

                DetallePedidoResponseDTO d = new DetallePedidoResponseDTO();

                d.setCant(det.getCant());
                d.setId_producto(det.getProducto().getId_producto());
                dtRes.add(d);

            }
            dto.setDetalle_pedidos(dtRes);

            List<PagoResponseDTO> pagosResDTO = new ArrayList<>();
            for (Pago pago : ped.getPagos()) {
                PagoResponseDTO p = new PagoResponseDTO();
                p.setId_pago(pago.getId_pago());
                p.setNumero_pago(pago.getNumero_pago());
                p.setSubtotal(pago.getSubtotal());
                p.setIva(pago.getIva());
                p.setMonto(pago.getMonto());
                p.setTipo(pago.getTipo());
                p.setFecha_registro(pago.getFecha_registro());
                pagosResDTO.add(p);
            }
            dto.setPagos(pagosResDTO);

            res.add(dto);
        }
        return res;

    }

    public boolean actualizarEstadoPedido(Long id_pedido) {

        Pedido ped = pedidoRepo.findById(id_pedido).orElse(null);


        if (ped == null) {
            return false;
        }

        if ("Completado".equalsIgnoreCase(ped.getEstado())) {
            ped.setEstado("Pendiente");
        } else {
            ped.setEstado("Completado");
        }

        pedidoRepo.save(ped);
        return true;
    }

    public boolean deleteByState() {

        List<Long> ids = new ArrayList<>();
        List<Pedido> pedidos = pedidoRepo.findAll();
        for (Pedido ped : pedidos) {
            if (ped.getEstado().equals("Completado")) {
                List<DetallePedido> det = ped.getDetalle_pedidos();
                for (DetallePedido d : det) {
                    Long idProd = d.getProducto().getId_producto();
                    Producto producto = productoRepo.findById(idProd).get();
                    producto.setEstado("INACTIVO");
                    productoRepo.save(producto);
                }

                ids.add(ped.getId_pedido());
            }
        }

        if (ids.isEmpty()) {
            System.out.println("No se encontraron pedidos completados");
            return false;
        }

        for (Long id : ids) {
            pedidoRepo.deleteById(id);
        }

        return true;
    }

    public PedidoResponseDTO.ById getPedidoById(Long id_pedido) {

        Pedido ped = pedidoRepo.findById(id_pedido).orElse(null);

        if (ped == null) {
            return null;
        }

        PedidoResponseDTO.ById dto = new PedidoResponseDTO.ById();

        dto.setId_user(ped.getUser().getId_user());
        dto.setNumero_pedido(ped.getNumero_pedido());
        dto.setEstado(ped.getEstado());

        List<DetallePedidoResponseDTO.ById> dtRes = new ArrayList<>();
        for (DetallePedido det : ped.getDetalle_pedidos()) {

            DetallePedidoResponseDTO.ById d = new DetallePedidoResponseDTO.ById();
            d.setCant(det.getCant());
            d.setId_producto(det.getProducto().getId_producto());
            Producto producto = productoRepo.findById(det.getProducto().getId_producto()).orElse(null);
            d.setNombre(producto.getNombre());
            dtRes.add(d);
        }
        dto.setDetalle_pedidos(dtRes);

        List<PagoResponseDTO> pagosResDTO = new ArrayList<>();
        for (Pago pago : ped.getPagos()) {
            PagoResponseDTO p = new PagoResponseDTO();
            p.setId_pago(pago.getId_pago());
            p.setNumero_pago(pago.getNumero_pago());
            p.setSubtotal(pago.getSubtotal());
            p.setIva(pago.getIva());
            p.setMonto(pago.getMonto());
            p.setTipo(pago.getTipo());
            p.setFecha_registro(pago.getFecha_registro());
            pagosResDTO.add(p);
        }
        dto.setPagos(pagosResDTO);
        return dto;

    }

    public List<PedidoResponseDTO.ById> getPedidosByIdUser(Long id_user) {

        User user = userRepo.findById(id_user).orElse(null);
        if (user == null) {
            return null;
        }

        List<PedidoResponseDTO.ById> DTOS = new ArrayList<>();
        for (Pedido ped : user.getPedidos()) {
            PedidoResponseDTO.ById dto = new  PedidoResponseDTO.ById();
            dto.setNumero_pedido(ped.getNumero_pedido());
            dto.setEstado(ped.getEstado());

            List<PagoResponseDTO> pagosResDTO = new ArrayList<>();

            List<Pago> pagos = ped.getPagos();
            for (Pago pago : pagos) {
                PagoResponseDTO p = new PagoResponseDTO();

                p.setId_pago(pago.getId_pago());
                p.setNumero_pago(pago.getNumero_pago());
                p.setSubtotal(pago.getSubtotal());
                p.setIva(pago.getIva());
                p.setMonto(pago.getMonto());
                p.setTipo(pago.getTipo());
                p.setFecha_registro(pago.getFecha_registro());
                pagosResDTO.add(p);
            }
            dto.setPagos(pagosResDTO);

            List<DetallePedidoResponseDTO.ById> detalle_pedidosResDTO = new ArrayList<>();
            for (DetallePedido det : ped.getDetalle_pedidos()) {

                DetallePedidoResponseDTO.ById d = new DetallePedidoResponseDTO.ById();

                d.setCant(det.getCant());
                d.setId_producto(det.getProducto().getId_producto());
                Producto producto = productoRepo.findById(det.getProducto().getId_producto()).orElse(null);
                d.setNombre(producto.getNombre());
                detalle_pedidosResDTO.add(d);
            }
            dto.setDetalle_pedidos(detalle_pedidosResDTO);
            DTOS.add(dto);
        }
        return DTOS;
    }

}
