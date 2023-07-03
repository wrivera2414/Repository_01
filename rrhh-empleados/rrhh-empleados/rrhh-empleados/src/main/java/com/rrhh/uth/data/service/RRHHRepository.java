package com.rrhh.uth.data.service;

import com.rrhh.uth.data.entity.ResponseEmployee;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface RRHHRepository {

	@Headers({
	    "Content-Type: application/json",
	    "Accept-Charset: utf-8",
	    "User-Agent: Retrofit-Sample-App"
	})
	@GET("/pls/apex/ingenieria_uth/rrhh/trabajadores/")
	Call<ResponseEmployee> obtenerEmpleados();
	
	
}
