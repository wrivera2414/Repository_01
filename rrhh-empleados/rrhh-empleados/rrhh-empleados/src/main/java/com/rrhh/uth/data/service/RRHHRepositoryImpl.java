package com.rrhh.uth.data.service;

import java.io.IOException;

import com.rrhh.uth.data.entity.ResponseEmployee;

import retrofit2.Call;
import retrofit2.Response;



public class RRHHRepositoryImpl {

	private static RRHHRepositoryImpl instance;
	private RepositoryClient client;
	
	private RRHHRepositoryImpl(String url, Long timeout) {
		this.client = new RepositoryClient(url, timeout);
	}
	
	//IMPLEMENTANDO PATRÓN SINGLETON
	public static RRHHRepositoryImpl getInstance(String url, Long timeout) {
		if(instance == null) {
			synchronized (RRHHRepositoryImpl.class) {
				if(instance == null) {
					instance = new RRHHRepositoryImpl(url, timeout);
				}
			}
		}
		return instance;
	}
	
	public ResponseEmployee getEmployees() throws IOException {
		Call<ResponseEmployee> call = client.getDatabaseService().obtenerEmpleados();
		Response<ResponseEmployee> response = call.execute(); //AQUI ES DONDE SE CONSULTA A LA URL DE LA BASE DE DATOS
		if(response.isSuccessful()){
			return response.body();
		}else {
			return null;
		}
	}
}
