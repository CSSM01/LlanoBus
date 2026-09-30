package com.usta.llanobus.buses;

import com.usta.llanobus.comun.LlanoBusException;
import com.usta.llanobus.db.BaseDeDatos;

import java.util.Collection;

public class BusService {
    private final BaseDeDatos db;

    public BusService(BaseDeDatos db) {
        this.db = db;
    }

    public Bus registrar(String placa, int capacidad) {
        Bus b = new Bus(db.siguienteId("bus"), placa, capacidad);
        db.buses.put(b.id(), b);
        return b;
    }

    public Bus buscarPorId(int id) {
        Bus b = db.buses.get(id);
        if (b == null) throw new LlanoBusException("Bus no encontrado: " + id);
        return b;
    }

    public Collection<Bus> listar() {
        return db.buses.values();
    }

}