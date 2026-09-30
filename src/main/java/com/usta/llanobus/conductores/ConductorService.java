package com.usta.llanobus.conductores;

import com.usta.llanobus.comun.LlanoBusException;
import com.usta.llanobus.db.BaseDeDatos;
import java.util.Collection;

public class ConductorService {
    private final BaseDeDatos db;

    public ConductorService(BaseDeDatos db) { this.db = db; }

    public Conductor registrar(String nombre) {
        Conductor c = new Conductor(db.siguienteId("conductor"), nombre, "DISPONIBLE");
        db.conductores.put(c.id(), c);
        return c;
    }

    public Conductor buscarPorId(int id) {
        Conductor c = db.conductores.get(id);
        if (c == null) throw new LlanoBusException("No se encontró conductor asignado");
        return c;
    }

    public Collection<Conductor> listar() { return db.conductores.values(); }
}