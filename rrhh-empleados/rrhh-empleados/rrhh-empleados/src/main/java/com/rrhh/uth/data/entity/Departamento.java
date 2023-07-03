package com.rrhh.uth.data.entity;

public class Departamento extends AbstractEntity {

    private String nombre;
    private String ubicacion;

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
	public String getUbicacion() {
		return ubicacion;
	}
	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}
   

}
