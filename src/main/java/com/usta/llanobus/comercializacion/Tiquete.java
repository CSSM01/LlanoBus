package com.usta.llanobus.comercializacion;

public class Tiquete {
    private final int id;
    private final int clienteId;
    private final int viajeId;
    private final int asiento;
    private final double valor;
    private String estado;

    public Tiquete(int id, int clienteId, int viajeId, int asiento, double valor, String estado) {
        this.id = id;
        this.clienteId = clienteId;
        this.viajeId = viajeId;
        this.asiento = asiento;
        this.valor = valor;
        this.estado = estado;
    }

    public int getId()          { return id; }
    public int getClienteId()   { return clienteId; }
    public int getViajeId()     { return viajeId; }
    public int getAsiento()     { return asiento; }
    public double getValor()    { return valor; }
    public String getEstado()   { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    @Override
    public String toString() {
        return "Tiquete{id=" + id + ", cliente=" + clienteId + ", viaje=" + viajeId
                + ", asiento=" + asiento + ", estado=" + estado + "}";
    }
}