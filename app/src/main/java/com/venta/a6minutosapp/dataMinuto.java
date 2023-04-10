package com.venta.a6minutosapp;

import java.io.Serializable;

public class dataMinuto implements Serializable {
    public String getFC() {
        return FC;
    }

    public void setFC(String FC) {
        this.FC = FC;
    }

    public String getSaturacion() {
        return Saturacion;
    }

    public void setSaturacion(String saturacion) {
        Saturacion = saturacion;
    }

    public String getTAS() {
        return TAS;
    }

    public void setTAS(String TAS) {
        this.TAS = TAS;
    }

    public String getTAD() {
        return TAD;
    }

    public void setTAD(String TAD) {
        this.TAD = TAD;
    }

    public String getMMII() {
        return MMII;
    }

    public void setMMII(String MMII) {
        this.MMII = MMII;
    }

    public String getDisnea() {
        return Disnea;
    }

    public void setDisnea(String disnea) {
        Disnea = disnea;
    }

    public String getMinuto() {
        return Minuto;
    }

    public void setMinuto(String minuto) {
        Minuto = minuto;
    }

    public String FC;
    public String Saturacion;
    public String TAS;
    public String TAD;
    public String MMII;
    public String Disnea;
    public String Minuto;


}
