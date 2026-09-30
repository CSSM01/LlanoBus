package com.usta.llanobus.financiera;

import com.usta.llanobus.db.BaseDeDatos;

public class PagoService {
    private final BaseDeDatos db;

    public PagoService(BaseDeDatos db) { this.db = db; }

    /** Simula el procesamiento del pago (aprobado si el monto es positivo). */
    public Pago procesarPago(int tiqueteId, double monto) {
        String estado = monto > 0 ? "APROBADO" : "RECHAZADO";
        Pago p = new Pago(db.siguienteId("pago"), tiqueteId, monto, estado);
        db.pagos.put(p.id(), p);
        return p;
    }
}