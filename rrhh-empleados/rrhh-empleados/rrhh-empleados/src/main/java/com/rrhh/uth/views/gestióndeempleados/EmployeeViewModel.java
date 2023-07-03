package com.rrhh.uth.views.gestióndeempleados;

import java.util.List;

import com.rrhh.uth.data.entity.Empleado;

public interface EmployeeViewModel {
	void refrescarGridEmpleados(List<Empleado> empleados);
}
