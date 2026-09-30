package com.usta.llanobus.inteligencia;

import com.usta.llanobus.comercializacion.Tiquete;
import com.usta.llanobus.db.BaseDeDatos;

public class InteligenciaService {
    private final BaseDeDatos db;

    public InteligenciaService(BaseDeDatos db) { this.db = db; }

    public void reporteVentas() {
        long confirmados = db.tiquetes.values().stream()
                .filter(t -> "confirmado".equals(t.getEstado())).count();
        double total = db.tiquetes.values().stream()
                .filter(t -> "confirmado".equals(t.getEstado()))
                .mapToDouble(Tiquete::getValor).sum();
        System.out.println("[INTELIGENCIA] Tiquetes confirmados: " + confirmados
                + " | Total recaudado: $" + total);
    }
}