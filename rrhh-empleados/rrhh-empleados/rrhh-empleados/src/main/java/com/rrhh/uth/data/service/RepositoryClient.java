package com.rrhh.uth.data.service;

import java.util.concurrent.TimeUnit;

import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RepositoryClient {
	
	private Retrofit retrofit;
	private HttpLoggingInterceptor interceptor = null;
	
	public RepositoryClient(String url, Long timeout) {
		this.interceptor = new HttpLoggingInterceptor();
		this.interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
		
		OkHttpClient client = new OkHttpClient.Builder()
				.addInterceptor(interceptor)
				.connectTimeout(timeout, TimeUnit.MICROSECONDS)
				.writeTimeout(timeout, TimeUnit.MICROSECONDS)
				.readTimeout(timeout, TimeUnit.MICROSECONDS)
				.build();
		retrofit = new Retrofit.Builder()
				.client(client)
				.baseUrl(url)
				.addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSZ").create()))
				.build();
	}
	
	public RRHHRepository getDatabaseService() {
		return retrofit.create(RRHHRepository.class);
	}

}
