package com.usta.llanobus.clientes;

import com.usta.llanobus.comun.LlanoBusException;
import com.usta.llanobus.db.BaseDeDatos;

public class ClienteService {
    private final BaseDeDatos db;

    public ClienteService(BaseDeDatos db) { this.db = db; }

    public Cliente registrar(String nombre, String email, String password) {
        Cliente c = new Cliente(db.siguienteId("cliente"), nombre, email, password);
        db.clientes.put(c.id(), c);
        return c;
    }

    public Cliente buscarPorId(int id) {
        Cliente c = db.clientes.get(id);
        if (c == null) throw new LlanoBusException("404 - Cliente inexistente: " + id);
        return c;
    }
}