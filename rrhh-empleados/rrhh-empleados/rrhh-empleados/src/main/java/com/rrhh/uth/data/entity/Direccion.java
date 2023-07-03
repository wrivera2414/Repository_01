package com.rrhh.uth.data.entity;


public class Direccion extends AbstractEntity {

    private String nombre;
    private String detalle;
    
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDetalle() {
		return detalle;
	}
	public void setDetalle(String detalle) {
		this.detalle = detalle;
	}

}
