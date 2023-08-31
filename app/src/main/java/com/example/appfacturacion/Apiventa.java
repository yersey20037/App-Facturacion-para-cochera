package com.example.appfacturacion;
import  retrofit2.Call;
import  retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface Apiventa {


    @FormUrlEncoded
    @POST("venta/")
    Call<Venta> VENTA_REGISTRO(
            @Header("Authorization") String authorization,
            @Field("cdtipodoc") String cdtipodoc,
            @Field("nrodocumento") String nrodocumento,
            @Field("mtototal") double mtototal,
            @Field("cdcliente") String cdcliente,
            @Field("nroplaca") String nroplaca,
            @Field("fecdocumento") String fecdocumento,
            @Field("rscliente") String rscliente,
            @Field("drcliente") String drcliente,
            @Field("cdarticulo") String cdarticulo,
            @Field("dsarticulo") String dsarticulo,
            @Field("precio") double precio,
            @Field("cantidad") String cantidad,
            @Field("estado") String estado,
            @Field("nropos") String nropos,
            @Field("id_estacion") String id_estacion,
            @Field("fechaproceso") String fechaproceso,
            @Field("turno") String turno
    );
    @FormUrlEncoded
    @POST("user/reporteturno/")
    Call<ConsultaventasturnoResponse> REPORTE_TURNO(
            @Header("Authorization") String authorization,
            @Field("nropos") String nropos
    );
    @FormUrlEncoded
    @POST("user/reportediario/")
    Call<ConsultaventasturnoResponse> REPORTE_DIA(
            @Header("Authorization") String authorization,
            @Field("nropos") String nropos
    );
}
