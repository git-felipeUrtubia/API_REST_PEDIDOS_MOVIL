package com.empresa.api_level_up_movil.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.time.LocalDate;

@JsonPropertyOrder({
        "id_pago",
        "numero_pago",
        "subtotal",
        "iva",
        "monto",
        "tipo",
        "fecha_registro"
})
@Data
public class PagoResponseDTO {
    private Long id_pago;
    private int numero_pago;
    private double subtotal;
    private double iva;
    private int monto;
    private String tipo;
    private LocalDate fecha_registro;
}
