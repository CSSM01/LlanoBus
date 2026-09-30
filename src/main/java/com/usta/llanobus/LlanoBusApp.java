package com.usta.llanobus;

import com.usta.llanobus.buses.Bus;
import com.usta.llanobus.buses.BusService;
import com.usta.llanobus.carga.CargaService;
import com.usta.llanobus.clientes.ClienteService;
import com.usta.llanobus.comercializacion.TiqueteService;
import com.usta.llanobus.comunicaciones.ComunicacionesService;
import com.usta.llanobus.conductores.Conductor;
import com.usta.llanobus.conductores.ConductorService;
import com.usta.llanobus.db.BaseDeDatos;
import com.usta.llanobus.financiera.PagoService;
import com.usta.llanobus.inteligencia.InteligenciaService;
import com.usta.llanobus.interfaz.MenuConsola;
import com.usta.llanobus.seguridad.SeguridadService;
import com.usta.llanobus.viajes.Ruta;
import com.usta.llanobus.viajes.ViajeService;

public class LlanoBusApp {

    public static void main(String[] args) {
        // Una sola base de datos, una sola aplicación
        BaseDeDatos db = new BaseDeDatos();

        // Los 10 módulos dentro del mismo proceso
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

        cargarDatosIniciales(clientes, conductores, buses, viajes);

        // Interfaz de usuario
        MenuConsola menu = new MenuConsola(clientes, conductores, buses, viajes,
                tiquetes, carga, inteligencia, seguridad);
        menu.iniciar();
    }

    /**
     * Datos mínimos para poder probar sin tener que registrar todo primero.
     */
    private static void cargarDatosIniciales(ClienteService clientes, ConductorService conductores,
                                             BusService buses, ViajeService viajes) {
        clientes.registrar("Ana Gómez", "ana@mail.com", "1234");
        Conductor conductor = conductores.registrar("Carlos Pérez");
        Bus bus = buses.registrar("ABC-123", 40);
        Ruta ruta = viajes.registrarRuta("Villavicencio", "Bogota");
        viajes.programarViaje(ruta.codigo(), "2026-10-15 08:00", bus.id(), conductor.id(), 45000);
    }
}