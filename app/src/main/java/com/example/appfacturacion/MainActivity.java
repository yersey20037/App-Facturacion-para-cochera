package com.example.appfacturacion;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private boolean isBold, isUnderLine;
    private String testFont;
    private int record;
    private String[] mStrings = new String[]{"CP437", "CP850", "CP860", "CP863", "CP865", "CP857", "CP737", "CP928", "Windows-1252", "CP866", "CP852", "CP858", "CP874", "Windows-775", "CP855", "CP862", "CP864", "GB18030", "BIG5", "KSC5601", "utf-8"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn_factura = findViewById(R.id.BtnFactura);
        Button btn_boleta = findViewById(R.id.BtnBoleta);
        Button btn_reporte_turno = findViewById(R.id.BtnReporteTurno);
        Button btn_reporte_diario = findViewById(R.id.BtnReporteDiario);
        init();

        isBold = false;
        isUnderLine = false;
        testFont = null;
        record = 17;
        //******************OBTENER FECHA Y HORA ACTUAL*************//
        TimeZone timeZone = TimeZone.getTimeZone("America/Lima");
        Date date = new Date();
        String dayFormat = "yyyy-MM-dd HH:mm:ss";
        DateFormat requiredFormat = new SimpleDateFormat(dayFormat);
        requiredFormat.setTimeZone(timeZone);
        String fechayhoraactual = requiredFormat.format(date).toUpperCase();
        //**********************************************************//
        //////////////////////////BOTON FACTURA//////////////////////////////////
        btn_factura.setOnClickListener(new View.OnClickListener() {
            Bundle intet = getIntent().getExtras();
            HttpLoggingInterceptor loggin = new HttpLoggingInterceptor();

            @Override
            public void onClick(View v) {
                String tokenI = intet.getString("token");
                String nropos = intet.getString("nropos");
                String id_estacion = intet.getString("id_estacion");

                loggin.setLevel(HttpLoggingInterceptor.Level.BODY);

                OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
                httpClient.addInterceptor(loggin);
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://geasacperu.com/apifac/public/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(httpClient.build())
                        .build();
                ApiUser correlativo = retrofit.create(ApiUser.class);
                Call<CorrelativoFacturaResponse> call = correlativo.CORRELATIVO_FACTURA_CALL("bearer " + tokenI, nropos);
                call.enqueue(new Callback<CorrelativoFacturaResponse>() {
                    @Override
                    public void onResponse(Call<CorrelativoFacturaResponse> call, Response<CorrelativoFacturaResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getCpe_fac_nrodoc()!= null) {
                            String cpe_fac_serie = response.body().getCpe_fac_serie();
                            String cpe_fac_nrodoc = response.body().getCpe_fac_nrodoc();
                            String fechaproceso = response.body().getFechaproceso();
                            String turno = response.body().getTurno();
                            Intent intent = new Intent(MainActivity.this, FacturaActivity.class);
                            intent.putExtra("cpe_fac_serie", cpe_fac_serie);
                            intent.putExtra("cpe_fac_nrodoc", cpe_fac_nrodoc);
                            intent.putExtra("token", tokenI);
                            intent.putExtra("nropos", nropos);
                            intent.putExtra("id_estacion", id_estacion);
                            intent.putExtra("fechaproceso", fechaproceso);
                            intent.putExtra("turno", turno);
                            startActivity(intent);
                        } else {
                            Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<CorrelativoFacturaResponse> call, Throwable t) {

                    }
                });
            }
        });
        ////////////////////////////////BOTON BOLETA///////////////////////////////////////////
        btn_boleta.setOnClickListener(new View.OnClickListener() {
            Bundle intet = getIntent().getExtras();

            HttpLoggingInterceptor loggin = new HttpLoggingInterceptor();

            @Override
            public void onClick(View v) {
                String tokenI = intet.getString("token");
                String nropos = intet.getString("nropos");
                String id_estacion = intet.getString("id_estacion");
                loggin.setLevel(HttpLoggingInterceptor.Level.BODY);

                OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
                httpClient.addInterceptor(loggin);
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://geasacperu.com/apifac/public/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(httpClient.build())
                        .build();
                ApiUser correlativo = retrofit.create(ApiUser.class);
                Call<CorrelativoBoletaResponse> call = correlativo.CORRELATIVO_BOLETA_CALL("bearer " + tokenI, nropos);
                call.enqueue(new Callback<CorrelativoBoletaResponse>() {
                    @Override
                    public void onResponse(Call<CorrelativoBoletaResponse> call, Response<CorrelativoBoletaResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getCpe_bol_nrodoc()!= null) {

                            String cpe_bol_serie = response.body().getCpe_bol_serie();
                            String cpe_bol_nrodoc = response.body().getCpe_bol_nrodoc();
                            String fechaproceso = response.body().getFechaproceso();
                            String turno = response.body().getTurno();
                            Intent intent = new Intent(MainActivity.this, BoletaActivity.class);
                            intent.putExtra("cpe_bol_serie", cpe_bol_serie);
                            intent.putExtra("cpe_bol_nrodoc", cpe_bol_nrodoc);
                            intent.putExtra("token", tokenI);
                            intent.putExtra("nropos", nropos);
                            intent.putExtra("id_estacion", id_estacion);
                            intent.putExtra("fechaproceso", fechaproceso);
                            intent.putExtra("turno", turno);
                            startActivity(intent);
                        } else {
                            Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<CorrelativoBoletaResponse> call, Throwable t) {

                    }
                });

            }
        });
        ///////////////////////////////////BOTON REPORTE TURNO///////////////////////////////////////////////
        btn_reporte_turno.setOnClickListener(new View.OnClickListener() {
            Bundle intet = getIntent().getExtras();

            HttpLoggingInterceptor loggin = new HttpLoggingInterceptor();

            @Override
            public void onClick(View v) {
                String tokenI = intet.getString("token");
                String nropos = intet.getString("nropos");
                loggin.setLevel(HttpLoggingInterceptor.Level.BODY);

                OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
                httpClient.addInterceptor(loggin);
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://geasacperu.com/apifac/public/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(httpClient.build())
                        .build();
                Apiventa correlativo = retrofit.create(Apiventa.class);
                Call<ConsultaventasturnoResponse> call = correlativo.REPORTE_TURNO("bearer " + tokenI, nropos);
                call.enqueue(new Callback<ConsultaventasturnoResponse>() {
                    @Override
                    public void onResponse(Call<ConsultaventasturnoResponse> call, Response<ConsultaventasturnoResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            ////////////////////////////////////////////////////////////////////////////
                            String content = "ocurrio un error";
                            String  fechaprocesoremoto= response.body().getMessage()[0].getFechaproceso();
                            String  turnoremoto= response.body().getMessage()[0].getTurno();
                            if (!BluetoothUtil.isBlueToothPrinter) {
                                SunmiPrintHelper.getInstance().setAlign(1);
                                SunmiPrintHelper.getInstance().printText("EL FIEL INCOMPRENDIDO S.A.C.\n", 24, true, isUnderLine, testFont);
                                SunmiPrintHelper.getInstance().printText(" \n", 10, isBold, isUnderLine, testFont);
                                SunmiPrintHelper.getInstance().printText("Principal: Pj 13 Mz F Lt 16-A Urb Canto Grande\n", 16, isBold, isUnderLine, testFont);
                                SunmiPrintHelper.getInstance().printText("Lima-Lima-San Juan de Lurigancho\n", 18, isBold, isUnderLine, testFont);
                                SunmiPrintHelper.getInstance().printText(" \n", 10, isBold, isUnderLine, testFont);
                                SunmiPrintHelper.getInstance().printText("Sucursal: Car Panamericana Sur Km 130 Sec. Pampa\n", 16, isBold, isUnderLine, testFont);
                                SunmiPrintHelper.getInstance().printText("Los Lobos Sub Lote A-1 Lima-Cañete-Cerro Azul\n", 16, isBold, isUnderLine, testFont);
                                SunmiPrintHelper.getInstance().printText(" \n", 10, isBold, isUnderLine, testFont);
                                SunmiPrintHelper.getInstance().printText("RUC 20547701209\n", 16, true, isUnderLine, testFont);
                                SunmiPrintHelper.getInstance().printText(" \n", 16, true, isUnderLine, testFont);
                                SunmiPrintHelper.getInstance().printText("CIERRE DE TURNO\n", 24, true, isUnderLine, testFont);
                                SunmiPrintHelper.getInstance().printText(" \n", 16, true, isUnderLine, testFont);
                                SunmiPrintHelper.getInstance().printText("Fecha de Impresion: " +fechayhoraactual + "\n", 18, true, isUnderLine, testFont);
                                SunmiPrintHelper.getInstance().printText(" \n", 16, true, isUnderLine, testFont);
                                SunmiPrintHelper.getInstance().printText("Fecha de Proceso: " + fechaprocesoremoto+"\n", 24, true, isUnderLine, testFont);
                                SunmiPrintHelper.getInstance().printText(" \n", 16, true, isUnderLine, testFont);
                                SunmiPrintHelper.getInstance().printText("Turno: " + turnoremoto+"\n", 24, true, isUnderLine, testFont);
                                SunmiPrintHelper.getInstance().printText(" \n", 16, true, isUnderLine, testFont);
                                String[] texto={"Placa","Precio"};
                                int[] espaciodecolumna={5,5};
                                int[] alineacion={0,0};
                                SunmiPrintHelper.getInstance().setFontSize(24);
                                SunmiPrintHelper.getInstance().printTable(texto,espaciodecolumna,alineacion);
                                SunmiPrintHelper.getInstance().printText("------------------------------------------------\n", 16, isBold, isUnderLine, testFont);
                                double total=0;

                                for (int i = 0; i < response.body().getMessage().length; i++) {
                                    String placa = response.body().getMessage()[i].getPlaca();
                                    double monto = response.body().getMessage()[i].getMonto();

                                    String[] texto2={placa, String.valueOf(monto)};
                                    int[] espaciodecolumna2={5,5};
                                    int[] alineacion2={0,0};
                                    total=total+monto;
                                    SunmiPrintHelper.getInstance().printTable(texto2,espaciodecolumna2,alineacion2);
                                    SunmiPrintHelper.getInstance().printText(" \n", 10, true, isUnderLine, testFont);
                                    //SunmiPrintHelper.getInstance().printText(placa + monto+"\n", 24, isBold, isUnderLine, testFont);
                                }
                                SunmiPrintHelper.getInstance().setFontSize(30);
                                String[] texto2={"TOTAL", String.valueOf(total)};
                                int[] espaciodecolumna2={5,5};
                                int[] alineacion2={0,0};
                                SunmiPrintHelper.getInstance().printText("------------------------------------------------\n", 16, isBold, isUnderLine, testFont);
                                SunmiPrintHelper.getInstance().setFontSize(30);
                                SunmiPrintHelper.getInstance().printTable(texto2,espaciodecolumna2,alineacion2);
                                SunmiPrintHelper.getInstance().printText("------------------------------------------------\n", 16, isBold, isUnderLine, testFont);
                                SunmiPrintHelper.getInstance().printText(" \n", 10, true, isUnderLine, testFont);
                                SunmiPrintHelper.getInstance().printText("USUARIO: " + nropos + "\n", 24, true, isUnderLine, testFont);
                                SunmiPrintHelper.getInstance().printText(" \n", 10, true, isUnderLine, testFont);
                                SunmiPrintHelper.getInstance().feedPaper();
                            } else {
                                Toast.makeText(MainActivity.this, "no hizo nada", Toast.LENGTH_SHORT).show();
                                printByBluTooth(content);
                            }
                            ////////////////////////////////////////////////////////////////////////////
                        } else {
                            Toast.makeText(MainActivity.this, "Error ", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<ConsultaventasturnoResponse> call, Throwable t) {
                        Log.e("venta2", t.getMessage());
                    }
                });
            }
            private void printByBluTooth(String content) {
                try {
                    if (isBold) {
                        BluetoothUtil.sendData(ESCUtil.boldOn());
                    } else {
                        BluetoothUtil.sendData(ESCUtil.boldOff());
                    }

                    if (isUnderLine) {
                        BluetoothUtil.sendData(ESCUtil.underlineWithOneDotWidthOn());
                    } else {
                        BluetoothUtil.sendData(ESCUtil.underlineOff());
                    }

                    if (record < 17) {
                        BluetoothUtil.sendData(ESCUtil.singleByte());
                        BluetoothUtil.sendData(ESCUtil.setCodeSystemSingle(codeParse(record)));
                    } else {
                        BluetoothUtil.sendData(ESCUtil.singleByteOff());
                        BluetoothUtil.sendData(ESCUtil.setCodeSystem(codeParse(record)));
                    }

                    BluetoothUtil.sendData(content.getBytes(mStrings[record]));
                    BluetoothUtil.sendData(ESCUtil.nextLine(3));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            private byte codeParse(int value) {
                byte res = 0x00;
                switch (value) {
                    case 0:
                        res = 0x00;
                        break;
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                        res = (byte) (value + 1);
                        break;
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                    case 11:
                        res = (byte) (value + 8);
                        break;
                    case 12:
                        res = 21;
                        break;
                    case 13:
                        res = 33;
                        break;
                    case 14:
                        res = 34;
                        break;
                    case 15:
                        res = 36;
                        break;
                    case 16:
                        res = 37;
                        break;
                    case 17:
                    case 18:
                    case 19:
                        res = (byte) (value - 17);
                        break;
                    case 20:
                        res = (byte) 0xff;
                        break;
                    default:
                        break;
                }
                return (byte) res;
            }
        });
        ///////////////////////////////////BOTON REPORTE DIARIO//////////////////////////////////////////////
        btn_reporte_diario.setOnClickListener(new View.OnClickListener() {
            Bundle intet = getIntent().getExtras();

            HttpLoggingInterceptor loggin = new HttpLoggingInterceptor();

            @Override
            public void onClick(View v) {
                String tokenI = intet.getString("token");
                String nropos = intet.getString("nropos");
                String turno =intet.getString("turno");
                String fechaproceso =intet.getString("fechaproceso");
                loggin.setLevel(HttpLoggingInterceptor.Level.BODY);
                OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
                httpClient.addInterceptor(loggin);
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://geasacperu.com/apifac/public/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(httpClient.build())
                        .build();
                Apiventa correlativo = retrofit.create(Apiventa.class);
                Call<ConsultaventasturnoResponse> call = correlativo.REPORTE_DIA("bearer " + tokenI, nropos);
                call.enqueue(new Callback<ConsultaventasturnoResponse>() {
                    @Override
                    public void onResponse(Call<ConsultaventasturnoResponse> call, Response<ConsultaventasturnoResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {

                            ////////////////////////////////////////////////////////////////////////////
                            String content = "ocurrio un error";
                            String  fechaprocesoremoto= response.body().getMessage()[0].getFechaproceso();
                            if (!BluetoothUtil.isBlueToothPrinter) {
                                SunmiPrintHelper.getInstance().setAlign(1);
                                SunmiPrintHelper.getInstance().printText("EL FIEL INCOMPRENDIDO S.A.C.\n", 24, true, isUnderLine, testFont);
                                SunmiPrintHelper.getInstance().printText(" \n", 10, isBold, isUnderLine, testFont);
                                SunmiPrintHelper.getInstance().printText("Principal: Pj 13 Mz F Lt 16-A Urb Canto Grande\n", 16, isBold, isUnderLine, testFont);
                                SunmiPrintHelper.getInstance().printText("Lima-Lima-San Juan de Lurigancho\n", 18, isBold, isUnderLine, testFont);
                                SunmiPrintHelper.getInstance().printText(" \n", 10, isBold, isUnderLine, testFont);
                                SunmiPrintHelper.getInstance().printText("Sucursal: Car Panamericana Sur Km 130 Sec. Pampa\n", 16, isBold, isUnderLine, testFont);
                                SunmiPrintHelper.getInstance().printText("Los Lobos Sub Lote A-1 Lima-Cañete-Cerro Azul\n", 16, isBold, isUnderLine, testFont);
                                SunmiPrintHelper.getInstance().printText(" \n", 10, isBold, isUnderLine, testFont);
                                SunmiPrintHelper.getInstance().printText("RUC 20547701209\n", 16, true, isUnderLine, testFont);
                                SunmiPrintHelper.getInstance().printText(" \n", 16, true, isUnderLine, testFont);
                                SunmiPrintHelper.getInstance().printText("CIERRE DIARIO\n", 24, true, isUnderLine, testFont);
                                SunmiPrintHelper.getInstance().printText(" \n", 16, true, isUnderLine, testFont);
                                SunmiPrintHelper.getInstance().printText("Fecha de Impresión: " +fechayhoraactual + "\n", 18, true, isUnderLine, testFont);
                                SunmiPrintHelper.getInstance().printText(" \n", 16, true, isUnderLine, testFont);
                                SunmiPrintHelper.getInstance().printText("Fecha de Proceso: " + fechaprocesoremoto+"\n", 24, true, isUnderLine, testFont);
                                SunmiPrintHelper.getInstance().printText(" \n", 16, true, isUnderLine, testFont);
                                SunmiPrintHelper.getInstance().printText("------------------------------------------------\n", 16, isBold, isUnderLine, testFont);
                                String[] texto={"Placa","Precio","Turno"};
                                int[] espaciodecolumna={5,5,5};
                                int[] alineacion={0,1,2};
                                SunmiPrintHelper.getInstance().setFontSize(24);
                                SunmiPrintHelper.getInstance().printTable(texto,espaciodecolumna,alineacion);
                                SunmiPrintHelper.getInstance().printText("------------------------------------------------\n", 16, isBold, isUnderLine, testFont);
                                double total=0;
                                for (int i = 0; i < response.body().getMessage().length; i++) {
                                    String placa = response.body().getMessage()[i].getPlaca();
                                    double monto = response.body().getMessage()[i].getMonto();
                                    String turno=response.body().getMessage()[i].getTurno();
                                    String[] texto2={placa, String.valueOf(monto),turno};
                                    total=total+monto;
                                    int[] espaciodecolumna2={5,5,5};
                                    int[] alineacion2={0,1,2};
                                    SunmiPrintHelper.getInstance().printTable(texto2,espaciodecolumna2,alineacion2);
                                    SunmiPrintHelper.getInstance().printText(" \n", 10, true, isUnderLine, testFont);

                                }

                                String[] texto2={"TOTAL", String.valueOf(total),""};
                                int[] espaciodecolumna2={5,5,5};
                                int[] alineacion2={0,1,2};
                                SunmiPrintHelper.getInstance().printText("------------------------------------------------\n", 16, isBold, isUnderLine, testFont);
                                SunmiPrintHelper.getInstance().setFontSize(30);
                                SunmiPrintHelper.getInstance().printTable(texto2,espaciodecolumna2,alineacion2);
                                SunmiPrintHelper.getInstance().printText("------------------------------------------------\n", 16, isBold, isUnderLine, testFont);
                                SunmiPrintHelper.getInstance().printText(" \n", 10, true, isUnderLine, testFont);
                                SunmiPrintHelper.getInstance().printText("USUARIO: " + nropos + "\n", 24, true, isUnderLine, testFont);
                                SunmiPrintHelper.getInstance().printText(" \n", 10, true, isUnderLine, testFont);
                                SunmiPrintHelper.getInstance().feedPaper();
                            } else {
                                Toast.makeText(MainActivity.this, "no hizo nada", Toast.LENGTH_SHORT).show();
                                printByBluTooth(content);
                            }
                            ////////////////////////////////////////////////////////////////////////////
                        } else {
                            Toast.makeText(MainActivity.this, "Error ", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<ConsultaventasturnoResponse> call, Throwable t) {
                        Log.e("venta2", t.getMessage());
                    }
                });
            }
            private void printByBluTooth(String content) {
                try {
                    if (isBold) {
                        BluetoothUtil.sendData(ESCUtil.boldOn());
                    } else {
                        BluetoothUtil.sendData(ESCUtil.boldOff());
                    }

                    if (isUnderLine) {
                        BluetoothUtil.sendData(ESCUtil.underlineWithOneDotWidthOn());
                    } else {
                        BluetoothUtil.sendData(ESCUtil.underlineOff());
                    }

                    if (record < 17) {
                        BluetoothUtil.sendData(ESCUtil.singleByte());
                        BluetoothUtil.sendData(ESCUtil.setCodeSystemSingle(codeParse(record)));
                    } else {
                        BluetoothUtil.sendData(ESCUtil.singleByteOff());
                        BluetoothUtil.sendData(ESCUtil.setCodeSystem(codeParse(record)));
                    }

                    BluetoothUtil.sendData(content.getBytes(mStrings[record]));
                    BluetoothUtil.sendData(ESCUtil.nextLine(3));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            private byte codeParse(int value) {
                byte res = 0x00;
                switch (value) {
                    case 0:
                        res = 0x00;
                        break;
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                        res = (byte) (value + 1);
                        break;
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                    case 11:
                        res = (byte) (value + 8);
                        break;
                    case 12:
                        res = 21;
                        break;
                    case 13:
                        res = 33;
                        break;
                    case 14:
                        res = 34;
                        break;
                    case 15:
                        res = 36;
                        break;
                    case 16:
                        res = 37;
                        break;
                    case 17:
                    case 18:
                    case 19:
                        res = (byte) (value - 17);
                        break;
                    case 20:
                        res = (byte) 0xff;
                        break;
                    default:
                        break;
                }
                return (byte) res;
            }
        });
    }

    private void init() {
        SunmiPrintHelper.getInstance().initSunmiPrinterService(this);
    }
}