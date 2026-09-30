package com.usta.llanobus.financiera;

public record Pago(int id, int tiqueteId, double monto, String estado) {}