package com.usta.llanobus.clientes;

import com.usta.llanobus.comun.LlanoBusException;
import com.usta.llanobus.db.BaseDeDatos;

public class ClienteService {
    private final BaseDeDatos db;

    public ClienteService(BaseDeDatos db) {
        this.db = db;
    }

    public Cliente registrar(String nombre, String email, String password) {
        if (nombre.isBlank() || email.isBlank() || password.isBlank()) {
            throw new LlanoBusException("400 - Todos los campos son obligatorios");
        }
        boolean existe = db.clientes.values().stream()
                .anyMatch(c -> c.email().equalsIgnoreCase(email));
        if (existe) throw new LlanoBusException("400 - Ya existe un cliente con ese email");

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