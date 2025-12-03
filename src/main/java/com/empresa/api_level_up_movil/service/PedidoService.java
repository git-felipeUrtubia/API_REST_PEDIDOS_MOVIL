package com.empresa.api_level_up_movil.service;

import com.empresa.api_level_up_movil.dto.request.DetallePedidoRequestDTO;
import com.empresa.api_level_up_movil.dto.request.PedidoRequestDTO;
import com.empresa.api_level_up_movil.dto.response.DetallePedidoResponseDTO;
import com.empresa.api_level_up_movil.dto.response.PedidoResponseDTO;
import com.empresa.api_level_up_movil.model.Cliente;
import com.empresa.api_level_up_movil.model.DetallePedido;
import com.empresa.api_level_up_movil.model.Pedido;
import com.empresa.api_level_up_movil.model.Producto;
import com.empresa.api_level_up_movil.repository.ClienteRepository;
import com.empresa.api_level_up_movil.repository.PedidoRepository;
import com.empresa.api_level_up_movil.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepo;

    @Autowired
    private ClienteRepository clienteRepo;

    @Autowired
    private ProductoRepository productoRepo;


    public PedidoResponseDTO savePedido(PedidoRequestDTO req) {

        try {
            if (req.getDetalles().isEmpty()) {
                return null;
            }

            Pedido ped = new Pedido();

            ped.setNumero_pedido(req.getNumero_pedido());

            Cliente cliente = clienteRepo.findById(req.getCliente_id()).get();
            ped.setCliente(cliente);

            List<DetallePedido> detalle_pedidos = new ArrayList<>();
            for (DetallePedidoRequestDTO det : req.getDetalles()) {

                DetallePedido detalle = new DetallePedido();

                detalle.setCant(det.getCant());

                Producto prod = productoRepo.findById(det.getId_producto()).get();
                detalle.setProducto(prod);
                detalle.setSubtotal(prod.getPrecio() * det.getCant());
                detalle.setPedido(ped);

                prod.setStock(prod.getStock() - det.getCant());
                productoRepo.save(prod);

                detalle_pedidos.add(detalle);

            }
            ped.setDetalle_pedidos(detalle_pedidos);

            pedidoRepo.save(ped);

            PedidoResponseDTO res = new PedidoResponseDTO();

            res.setId_pedido(ped.getId_pedido());
            res.setId_cliente(ped.getCliente().getId_cliente());
            res.setNumero_pedido(ped.getNumero_pedido());

            List<DetallePedidoResponseDTO> dtRes = new ArrayList<>();
            for (DetallePedido det : ped.getDetalle_pedidos()) {

                DetallePedidoResponseDTO d = new DetallePedidoResponseDTO();

                d.setCant(det.getCant());
                d.setId_producto(det.getProducto().getId_producto());
                dtRes.add(d);

            }

            res.setDetalle_pedidos(dtRes);
            return res;

        } catch (Exception e) {
            System.out.println("Error Service: " + e.getMessage());
            return null;
        }

    }

    public List<PedidoResponseDTO> findAllPedidos() {

        try {

            List<Pedido> pedidos = pedidoRepo.findAll();

            List<PedidoResponseDTO> res = new ArrayList<>();
            for (Pedido ped : pedidos) {

                PedidoResponseDTO dto = new PedidoResponseDTO();

                dto.setId_pedido(ped.getId_pedido());
                dto.setId_cliente(ped.getCliente().getId_cliente());
                dto.setNumero_pedido(ped.getNumero_pedido());

                List<DetallePedidoResponseDTO> dtRes = new ArrayList<>();
                for (DetallePedido det : ped.getDetalle_pedidos()) {

                    DetallePedidoResponseDTO d = new DetallePedidoResponseDTO();

                    d.setCant(det.getCant());
                    d.setId_producto(det.getProducto().getId_producto());
                    dtRes.add(d);

                }

                dto.setDetalle_pedidos(dtRes);
                res.add(dto);
            }
            return res;

        } catch (Exception e) {
            System.out.println("Error Service: " + e.getMessage());
            return null;
        }

    }

}
