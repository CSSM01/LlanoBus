package com.usta.llanobus.viajes;

import com.usta.llanobus.buses.Bus;
import com.usta.llanobus.buses.BusService;
import com.usta.llanobus.comun.LlanoBusException;
import com.usta.llanobus.conductores.ConductorService;
import com.usta.llanobus.db.BaseDeDatos;

import java.util.List;

public class ViajeService {
    private final BaseDeDatos db;
    private final BusService busService;
    private final ConductorService conductorService;

    public ViajeService(BaseDeDatos db, BusService busService, ConductorService conductorService) {
        this.db = db;
        this.busService = busService;
        this.conductorService = conductorService;
    }

    public Ruta registrarRuta(String origen, String destino) {
        String codigo = origen.substring(0, 3).toUpperCase() + "-" + destino.substring(0, 3).toUpperCase();
        Ruta r = new Ruta(codigo, origen, destino);
        db.rutas.put(codigo, r);
        return r;
    }

    public Viaje programarViaje(String rutaCodigo, String fechaHora, int busId, int conductorId, double precio) {
        if (!db.rutas.containsKey(rutaCodigo)) throw new LlanoBusException("Ruta no existe: " + rutaCodigo);
        Bus bus = busService.buscarPorId(busId);          // llamada interna al módulo 03
        conductorService.buscarPorId(conductorId);        // llamada interna al módulo 02
        Viaje v = new Viaje(db.siguienteId("viaje"), rutaCodigo, fechaHora,
                bus.id(), conductorId, bus.capacidad(), precio);
        db.viajes.put(v.getId(), v);
        return v;
    }

    /** Operación: Consultar rutas (entrada: origen y destino). */
    public List<Ruta> consultarRutas(String origen, String destino) {
        List<Ruta> resultado = db.rutas.values().stream()
                .filter(r -> r.origen().equalsIgnoreCase(origen) && r.destino().equalsIgnoreCase(destino))
                .toList();
        if (resultado.isEmpty()) throw new LlanoBusException("No se encontraron rutas");
        return resultado;
    }

    /** Operación: Consultar horarios (entrada: código de ruta). */
    public List<Viaje> consultarHorarios(String rutaCodigo) {
        List<Viaje> resultado = db.viajes.values().stream()
                .filter(v -> v.getRutaCodigo().equals(rutaCodigo))
                .toList();
        if (resultado.isEmpty()) throw new LlanoBusException("No existen horarios disponibles");
        return resultado;
    }

    public Viaje buscarViaje(int id) {
        Viaje v = db.viajes.get(id);
        if (v == null) throw new LlanoBusException("404 - Viaje no encontrado: " + id);
        return v;
    }
}