package com.example.appfacturacion;
import  retrofit2.Call;
import  retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
public interface ApiUser {

    @FormUrlEncoded
    @POST("auth/")
    Call<LoginResponse> LOGIN_CALL(
     @Field("email") String email,
     @Field("password") String password
    );
    @FormUrlEncoded
    @POST("user/correlativoboleta/")
    Call<CorrelativoBoletaResponse> CORRELATIVO_BOLETA_CALL(
            @Header("Authorization") String authorization,
            @Field("nropos") String nropos
    );
    @FormUrlEncoded
    @POST("user/correlativofactura/")
    Call<CorrelativoFacturaResponse> CORRELATIVO_FACTURA_CALL(
            @Header("Authorization") String authorization,
            @Field("nropos") String nropos
    );
    @FormUrlEncoded
    @POST("ruc/")
    Call<ConsultaRucResponse> CONSULTA_RUC_CALL(
            @Field("ruc") String ruc
    );
    @FormUrlEncoded
    @POST("dni/")
    Call<ConsultaDniResponse> CONSULTA_DNI_CALL(
            @Field("dni") String dni
    );

}
