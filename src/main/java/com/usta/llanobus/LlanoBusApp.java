package com.usta.llanobus;

import com.usta.llanobus.buses.Bus;
import com.usta.llanobus.buses.BusService;
import com.usta.llanobus.carga.CargaService;
import com.usta.llanobus.clientes.Cliente;
import com.usta.llanobus.clientes.ClienteService;
import com.usta.llanobus.comercializacion.Tiquete;
import com.usta.llanobus.comercializacion.TiqueteService;
import com.usta.llanobus.comun.LlanoBusException;
import com.usta.llanobus.comunicaciones.ComunicacionesService;
import com.usta.llanobus.conductores.Conductor;
import com.usta.llanobus.conductores.ConductorService;
import com.usta.llanobus.db.BaseDeDatos;
import com.usta.llanobus.financiera.PagoService;
import com.usta.llanobus.inteligencia.InteligenciaService;
import com.usta.llanobus.seguridad.SeguridadService;
import com.usta.llanobus.viajes.Ruta;
import com.usta.llanobus.viajes.Viaje;
import com.usta.llanobus.viajes.ViajeService;

import java.util.List;

public class LlanoBusApp {

    public static void main(String[] args) {
        // ===== Una sola base de datos, una sola aplicación =====
        BaseDeDatos db = new BaseDeDatos();

        // ===== Se instancian los 10 módulos dentro del mismo proceso =====
        ClienteService clientes = new ClienteService(db);
        ConductorService conductores = new ConductorService(db);
        BusService buses = new BusService(db);
        ViajeService viajes = new ViajeService(db, buses, conductores);
        PagoService pagos = new PagoService(db);
        ComunicacionesService comunicaciones = new ComunicacionesService();
        TiqueteService tiquetes = new TiqueteService(db, clientes, viajes, pagos, comunicaciones);
        CargaService carga = new CargaService(db, viajes);
        InteligenciaService inteligencia = new InteligenciaService(db);
        SeguridadService seguridad = new SeguridadService(db);

        // ===== Datos de prueba =====
        clientes.registrar("Ana Gómez", "ana@mail.com", "1234");
        Conductor conductor = conductores.registrar("Carlos Pérez");
        Bus bus = buses.registrar("ABC-123", 40);
        Ruta ruta = viajes.registrarRuta("Villavicencio", "Bogota");
        Viaje viaje = viajes.programarViaje(ruta.codigo(), "2026-10-15 08:00",
                bus.id(), conductor.id(), 45000);

        System.out.println("=== FLUJO DE COMPRA DE TIQUETE ===");

        // 0. SEGURIDAD: el cliente inicia sesión (módulo 10)
        Cliente cliente = seguridad.iniciarSesion("ana@mail.com", "1234");
        System.out.println("[SEGURIDAD] Sesión iniciada: " + cliente.nombre());

        // 1. CONSULTA: busca ruta, horario y disponibilidad (módulo 04)
        List<Ruta> rutas = viajes.consultarRutas("Villavicencio", "Bogota");
        System.out.println("[VIAJES] Rutas: " + rutas);
        List<Viaje> horarios = viajes.consultarHorarios(rutas.get(0).codigo());
        horarios.forEach(h -> System.out.println("[VIAJES] Horario: " + h));

        // 2, 3 y 4. SELECCIÓN + PAGO + CONFIRMACIÓN (módulos 05 -> 06 -> 08)
        Tiquete tiquete = tiquetes.comprarTiquete(cliente.id(), viaje.getId(), 12);
        System.out.println("[COMERCIALIZACIÓN] Resultado: " + tiquete);

        // ===== Casos de error (los del contrato) =====
        System.out.println("\n=== CASOS DE ERROR ===");
        try {
            tiquetes.comprarTiquete(cliente.id(), viaje.getId(), 12); // asiento repetido
        } catch (LlanoBusException e) {
            System.out.println("Error: " + e.getMessage());
        }
        try {
            tiquetes.comprarTiquete(999, viaje.getId(), 5);            // cliente inexistente
        } catch (LlanoBusException e) {
            System.out.println("Error: " + e.getMessage());
        }
        try {
            viajes.consultarRutas("Villavicencio", "Cali");            // ruta inexistente
        } catch (LlanoBusException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // ===== Otros módulos =====
        System.out.println("\n=== OTROS MÓDULOS ===");
        System.out.println("[CARGA] " + carga.registrarEncomienda(viaje.getId(), "Caja de repuestos", 15.5));
        inteligencia.reporteVentas();
    }
}