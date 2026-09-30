package com.usta.llanobus.comercializacion;

import com.usta.llanobus.clientes.Cliente;
import com.usta.llanobus.clientes.ClienteService;
import com.usta.llanobus.comun.LlanoBusException;
import com.usta.llanobus.comunicaciones.ComunicacionesService;
import com.usta.llanobus.db.BaseDeDatos;
import com.usta.llanobus.financiera.Pago;
import com.usta.llanobus.financiera.PagoService;
import com.usta.llanobus.viajes.Viaje;
import com.usta.llanobus.viajes.ViajeService;

public class TiqueteService {
    private final BaseDeDatos db;
    private final ClienteService clienteService;
    private final ViajeService viajeService;
    private final PagoService pagoService;
    private final ComunicacionesService comunicacionesService;

    public TiqueteService(BaseDeDatos db, ClienteService clienteService, ViajeService viajeService,
                          PagoService pagoService, ComunicacionesService comunicacionesService) {
        this.db = db;
        this.clienteService = clienteService;
        this.viajeService = viajeService;
        this.pagoService = pagoService;
        this.comunicacionesService = comunicacionesService;
    }

    /**
     * Una sola transacción atraviesa varios módulos del mismo proceso:
     * Comercialización (05) -> Gestión Financiera (06) -> Comunicaciones (08).
     */
    public Tiquete comprarTiquete(int clienteId, int viajeId, int asiento) {
        Cliente cliente = clienteService.buscarPorId(clienteId);   // módulo 01
        Viaje viaje = viajeService.buscarViaje(viajeId);           // módulo 04

        if (!viaje.asientoDisponible(asiento)) {
            throw new LlanoBusException("400 - Asiento " + asiento + " no disponible");
        }

        viaje.ocupar(asiento);
        Tiquete tiquete = new Tiquete(db.siguienteId("tiquete"), clienteId, viajeId,
                asiento, viaje.getPrecio(), "pendiente");
        db.tiquetes.put(tiquete.getId(), tiquete);

        Pago pago = pagoService.procesarPago(tiquete.getId(), tiquete.getValor());   // módulo 06

        if (!"APROBADO".equals(pago.estado())) {
            viaje.liberar(asiento);
            tiquete.setEstado("cancelado");
            throw new LlanoBusException("Pago rechazado, tiquete cancelado");
        }

        tiquete.setEstado("confirmado");
        comunicacionesService.enviarConfirmacion(cliente, tiquete); // módulo 08
        return tiquete;
    }
}