package com.rrhh.uth.data.controller;

import java.io.IOException;

import com.rrhh.uth.data.entity.ResponseEmployee;
import com.rrhh.uth.data.service.RRHHRepositoryImpl;
import com.rrhh.uth.views.gestióndeempleados.EmployeeViewModel;

public class EmployeeInteractorImpl implements EmployeeInteractor {

	private RRHHRepositoryImpl modelo;
	private EmployeeViewModel vista;
	
	public EmployeeInteractorImpl(EmployeeViewModel vista) {
		super();
		this.modelo = RRHHRepositoryImpl.getInstance("https://apex.oracle.com/", 600000L);
		this.vista = vista;
	}

	@Override
	public void consultarEmpleados() {
		try {
			ResponseEmployee respuesta = this.modelo.getEmployees();
			this.vista.refrescarGridEmpleados(respuesta.getItems());
		}catch(IOException e) {
			e.printStackTrace();
		}
		
	}
}
