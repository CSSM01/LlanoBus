package com.usta.llanobus.db;

import com.usta.llanobus.buses.Bus;
import com.usta.llanobus.carga.Encomienda;
import com.usta.llanobus.clientes.Cliente;
import com.usta.llanobus.comercializacion.Tiquete;
import com.usta.llanobus.conductores.Conductor;
import com.usta.llanobus.financiera.Pago;
import com.usta.llanobus.viajes.Ruta;
import com.usta.llanobus.viajes.Viaje;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/** Base de datos única compartida por los 10 módulos (simulada en memoria). */
public class BaseDeDatos {
    public final Map<Integer, Cliente> clientes = new HashMap<>();
    public final Map<Integer, Conductor> conductores = new HashMap<>();
    public final Map<Integer, Bus> buses = new HashMap<>();
    public final Map<String, Ruta> rutas = new HashMap<>();
    public final Map<Integer, Viaje> viajes = new HashMap<>();
    public final Map<Integer, Tiquete> tiquetes = new HashMap<>();
    public final Map<Integer, Pago> pagos = new HashMap<>();
    public final Map<Integer, Encomienda> encomiendas = new HashMap<>();

    private final Map<String, AtomicInteger> secuencias = new HashMap<>();

    public int siguienteId(String tabla) {
        return secuencias.computeIfAbsent(tabla, t -> new AtomicInteger(1)).getAndIncrement();
    }
}