package com.example.appfacturacion;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

public class FacturaActivity extends AppCompatActivity {
    private boolean isBold, isUnderLine;
    private String testFont;
    private int record;
    private String[] mStrings = new String[]{"CP437", "CP850", "CP860", "CP863", "CP865", "CP857", "CP737", "CP928", "Windows-1252", "CP866", "CP852", "CP858", "CP874", "Windows-775", "CP855", "CP862", "CP864", "GB18030", "BIG5", "KSC5601", "utf-8"};
    String placa = "";
    String ruc="";
    String razonsocial="";
    String direccion="";
    private ProgressBar pbProgressLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factura);
        pbProgressLogin = findViewById(R.id.pbProgressLogin);
        init();
        Numero_a_Letra d= new Numero_a_Letra();


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
        Bundle intet = getIntent().getExtras();
        String tokenI = intet.getString("token");
        String cpe_fac_serie = intet.getString("cpe_fac_serie");
        String cpe_fac_nrodoc = intet.getString("cpe_fac_nrodoc");
        String nropos = intet.getString("nropos");
        String id_estacion = intet.getString("id_estacion");
        String fechaproceso =intet.getString("fechaproceso");
        String turno= intet.getString("turno");
        TextView correlativo = findViewById(R.id.textFSerie);
        correlativo.setText(cpe_fac_serie + "-" + cpe_fac_nrodoc);
        EditText edtplacafactura = findViewById(R.id.editTextplacafactura);
        EditText edtmonto=findViewById(R.id.editTextMonto);
        EditText edtruc=findViewById(R.id.editTextRuc);
        EditText edtrazonsocial=findViewById(R.id.editTextRazonSocial);
        EditText edtdireccion=findViewById(R.id.editTextDireccion);
        ImageButton btn_buscar_ruc= findViewById(R.id.btnbuscar);
        btn_buscar_ruc.setOnClickListener(new View.OnClickListener() {
            HttpLoggingInterceptor logeo = new HttpLoggingInterceptor();
            @Override
            public void onClick(View v) {
                ruc = edtruc.getText().toString().trim();
                edtrazonsocial.getText().clear();
                edtdireccion.getText().clear();
                if (ruc.equals("")) {
                    Toast.makeText(FacturaActivity.this, "INGRESE RUC", Toast.LENGTH_SHORT).show();
                } else {
                    pbProgressLogin.setVisibility(View.VISIBLE);
                    logeo.setLevel(HttpLoggingInterceptor.Level.BODY);
                    OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
                    httpClient.addInterceptor(logeo);
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("https://geasacperu.com/apifac/public/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(httpClient.build())
                            .build();
                    ApiUser login =retrofit.create(ApiUser.class);
                    Call<ConsultaRucResponse> call =login.CONSULTA_RUC_CALL(ruc);
                    call.enqueue(new Callback<ConsultaRucResponse>() {
                        @Override
                        public void onResponse(Call<ConsultaRucResponse> call, Response<ConsultaRucResponse> response) {
                            if (response.isSuccessful() && response.body() !=null){
                                 razonsocial= response.body().getMessage().getRazonsocial();
                                 direccion= response.body().getMessage().getDireccion();
                                edtrazonsocial.setText(razonsocial);
                                edtdireccion.setText(direccion);
                            }
                            pbProgressLogin.setVisibility(View.GONE);
                        }
                        @Override
                        public void onFailure(Call<ConsultaRucResponse> call, Throwable t) {
                            Toast toast=Toast.makeText(FacturaActivity.this, "RUC NO ENCONTRADO", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER,0,0);
                            toast.show();
                            pbProgressLogin.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });
        Button btn_factura = findViewById(R.id.btn_grabarf);
        btn_factura.setOnClickListener(new View.OnClickListener() {
            HttpLoggingInterceptor logeo = new HttpLoggingInterceptor();
            private boolean isNumeric(String cadena){
                try {
                    Integer.parseInt(cadena);
                    return true;
                } catch (NumberFormatException nfe){
                    return false;
                }
            }
            @Override
            public void onClick(View v) {
                placa = edtplacafactura.getText().toString().trim();
                String monto= edtmonto.getText().toString().trim();
                ruc=edtruc.getText().toString().trim();
                razonsocial = edtrazonsocial.getText().toString().trim();
                direccion=edtdireccion.getText().toString().trim();
                if (placa.equals("")) {
                    Toast.makeText(FacturaActivity.this, "INGRESE PLACA", Toast.LENGTH_SHORT).show();
                } else if(!isNumeric(monto)){
                    Toast.makeText(FacturaActivity.this, "INGRESE UN MONTO VALIDO", Toast.LENGTH_SHORT).show();

                }else {
                    Double igv =Double.parseDouble(monto)*0.18;
                    //String.format("%.2f", precio);
                    Double subtotal=Double.parseDouble(monto)-igv;
                    Double total=Double.parseDouble(monto);
                    String letras= d.Convertir(monto, true);
                    logeo.setLevel(HttpLoggingInterceptor.Level.BODY);
                    OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
                    httpClient.addInterceptor(logeo);
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("https://geasacperu.com/apifac/public/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(httpClient.build())
                            .build();
                    Apiventa venta = retrofit.create(Apiventa.class);
                    Call<Venta> call = venta.VENTA_REGISTRO(
                            "bearer " + tokenI,
                            cpe_fac_serie,
                            cpe_fac_nrodoc,
                            total,
                            ruc,
                            placa,
                            fechayhoraactual,
                            razonsocial,
                            direccion,
                            "00001",
                            "Alquiler Cochera",
                            total,
                            "1",
                            "0",
                            nropos,
                            id_estacion,
                            fechaproceso,
                            turno
                    );
                    call.enqueue(new Callback<Venta>() {
                        @Override
                        public void onResponse(Call<Venta> call, Response<Venta> response) {
                            if (response.isSuccessful() && response.body() != null && response.code() == 200) {
                                edtplacafactura.getText().clear();
                                String respuesta = response.body().getMessage();
                                Intent intent = new Intent(FacturaActivity.this, MainActivity.class);
                                intent.putExtra("token", tokenI);
                                intent.putExtra("nropos", nropos);
                                intent.putExtra("id_estacion",id_estacion);
                                intent.putExtra("turno",turno);
                                intent.putExtra("fechaproceso",fechaproceso);
                                startActivity(intent);
                                //***************INICIO DE LA IMPRESION*************
                                String content = "ocurrio un error";
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
                                    SunmiPrintHelper.getInstance().printText(" \n", 10, true, isUnderLine, testFont);
                                    SunmiPrintHelper.getInstance().printText(fechayhoraactual + "\n", 16, true, isUnderLine, testFont);
                                    SunmiPrintHelper.getInstance().printText("------------------------------------------------\n", 16, isBold, isUnderLine, testFont);
                                    SunmiPrintHelper.getInstance().printText("FACTURA DE VENTA ELECTRÓNICA\n", 16, true, isUnderLine, testFont);
                                    SunmiPrintHelper.getInstance().printText(cpe_fac_serie + "-" + cpe_fac_nrodoc + "\n", 16, true, isUnderLine, testFont);
                                    SunmiPrintHelper.getInstance().printText("------------------------------------------------\n", 16, isBold, isUnderLine, testFont);
                                    SunmiPrintHelper.getInstance().setAlign(0);
                                    SunmiPrintHelper.getInstance().printText("RUC: "+ruc+"\n", 16, true, isUnderLine, testFont);
                                    SunmiPrintHelper.getInstance().printText("Razón Social: "+ razonsocial+"\n", 16, true, isUnderLine, testFont);
                                    SunmiPrintHelper.getInstance().printText("Dirección: "+ direccion+"\n", 16, true, isUnderLine, testFont);
                                    SunmiPrintHelper.getInstance().printText("------------------------------------------------\n", 16, isBold, isUnderLine, testFont);
                                    String[] texto = {"Prod", "Cantidad", "Precio", "Importe"};
                                    int[] espaciodecolumna = {5, 5, 5, 5};
                                    int[] alineacion = {0, 0, 1, 2};
                                    SunmiPrintHelper.getInstance().setFontSize(16);
                                    SunmiPrintHelper.getInstance().printTable(texto, espaciodecolumna, alineacion);
                                    SunmiPrintHelper.getInstance().printText("------------------------------------------------\n", 16, isBold, isUnderLine, testFont);
                                    String[] texto2 = {"Cochera", "1.000", String.format("%.2f", total), String.format("%.2f", total)};
                                    int[] espaciodecolumna2 = {5, 5, 5, 5};
                                    int[] alineacion2 = {0, 0, 1, 2};
                                    SunmiPrintHelper.getInstance().printTable(texto2, espaciodecolumna2, alineacion2);
                                    SunmiPrintHelper.getInstance().printText("------------------------------------------------\n", 16, isBold, isUnderLine, testFont);
                                    String[] texto3 = {"", "***TOTAL DSCTO", "S/:", "0.00"};
                                    int[] espaciodecolumna3 = {1, 6, 5, 5};
                                    int[] alineacion3 = {0, 0, 2, 2};
                                    SunmiPrintHelper.getInstance().printTable(texto3, espaciodecolumna3, alineacion3);
                                    String[] texto4 = {"", "***TOTAL", "S/:", "0.00"};
                                    int[] espaciodecolumna4 = {1, 6, 5, 5};
                                    int[] alineacion4 = {0, 0, 2, 2};
                                    SunmiPrintHelper.getInstance().printTable(texto4, espaciodecolumna4, alineacion4);
                                    String[] texto5 = {"", "***OP. EXONERADA", "S/:", "0.00"};
                                    int[] espaciodecolumna5 = {1, 6, 5, 5};
                                    int[] alineacion5 = {0, 0, 2, 2};
                                    SunmiPrintHelper.getInstance().printTable(texto5, espaciodecolumna5, alineacion5);
                                    String[] texto6 = {"", "***OP. INAFECTA", "S/:", "0.00"};
                                    int[] espaciodecolumna6 = {1, 6, 5, 5};
                                    int[] alineacion6 = {0, 0, 2, 2};
                                    SunmiPrintHelper.getInstance().printTable(texto6, espaciodecolumna6, alineacion6);
                                    String[] texto7 = {"", "***OP. GRAVADA", "S/:", String.format("%.2f", subtotal)};
                                    int[] espaciodecolumna7 = {1, 6, 5, 5};
                                    int[] alineacion7 = {0, 0, 2, 2};
                                    SunmiPrintHelper.getInstance().printTable(texto7, espaciodecolumna7, alineacion7);
                                    String[] texto8 = {"", "***I.S.C.", "S/:", "0.00"};
                                    int[] espaciodecolumna8 = {1, 6, 5, 5};
                                    int[] alineacion8 = {0, 0, 2, 2};
                                    SunmiPrintHelper.getInstance().printTable(texto8, espaciodecolumna8, alineacion8);
                                    String[] texto9 = {"", "***I.G.V. 18.00%", "S/:", String.format("%.2f", igv)};
                                    int[] espaciodecolumna9 = {1, 6, 5, 5};
                                    int[] alineacion9 = {0, 0, 2, 2};
                                    SunmiPrintHelper.getInstance().printTable(texto9, espaciodecolumna9, alineacion9);
                                    String[] texto10 = {"", "***IMPORTE TOTAL", "S/:", String.format("%.2f", total)};
                                    int[] espaciodecolumna10 = {1, 6, 5, 5};
                                    int[] alineacion10 = {0, 0, 2, 2};
                                    SunmiPrintHelper.getInstance().printTable(texto10, espaciodecolumna10, alineacion10);
                                    SunmiPrintHelper.getInstance().printText(" \n", 10, true, isUnderLine, testFont);
                                    SunmiPrintHelper.getInstance().setAlign(0);
                                    SunmiPrintHelper.getInstance().printText(letras+"\n", 16, true, isUnderLine, testFont);
                                    String[] texto11 = {"EFECTIVO", "S/:", String.format("%.2f", total)};
                                    int[] espaciodecolumna11 = {5, 5, 5};
                                    int[] alineacion11 = {0, 1, 2};
                                    SunmiPrintHelper.getInstance().printTable(texto11, espaciodecolumna11, alineacion11);
                                    SunmiPrintHelper.getInstance().printText("USUARIO: " + nropos + "\n", 16, true, isUnderLine, testFont);
                                    SunmiPrintHelper.getInstance().printText("Placa: " + placa + "\n", 16, true, isUnderLine, testFont);
                                    SunmiPrintHelper.getInstance().printText(" \n", 10, true, isUnderLine, testFont);
                                    SunmiPrintHelper.getInstance().setAlign(1);
                                    SunmiPrintHelper.getInstance().printText("Representación impresa de la FACTURA ELECTRÓNICA\n", 16, true, isUnderLine, testFont);
                                    SunmiPrintHelper.getInstance().printText("para consultar el documento visita\n", 16, true, isUnderLine, testFont);
                                    SunmiPrintHelper.getInstance().printText("http://elfiel.e-siges.com\n", 16, true, isUnderLine, testFont);
                                    SunmiPrintHelper.getInstance().printText(" \n", 10, true, isUnderLine, testFont);
                                    SunmiPrintHelper.getInstance().printText(" \n", 10, true, isUnderLine, testFont);
                                    SunmiPrintHelper.getInstance().printText(" \n", 10, true, isUnderLine, testFont);
                                    SunmiPrintHelper.getInstance().printQr("http://elfiel.e-siges.com", 9, 16);
                                    SunmiPrintHelper.getInstance().printText(" \n", 10, true, isUnderLine, testFont);
                                    SunmiPrintHelper.getInstance().printText(" \n", 10, true, isUnderLine, testFont);
                                    SunmiPrintHelper.getInstance().printText(" \n", 10, true, isUnderLine, testFont);
                                    SunmiPrintHelper.getInstance().printText("Emitido desde www.e-siges.com\n", 16, true, isUnderLine, testFont);
                                    SunmiPrintHelper.getInstance().printText("LA EMPRESA NO SE HACE RESPONSABLE POR LOS DAÑOS CAUSADOS EN SU VEHÍCULO NI DE LA SUSTRACCIÓN DE OBJETOS DE VALOR\n", 26, isBold, isUnderLine, testFont);
                                    SunmiPrintHelper.getInstance().feedPaper();
                                } else {
                                    Toast.makeText(FacturaActivity.this, "no hizo nada", Toast.LENGTH_SHORT).show();
                                    printByBluTooth(content);
                                }
                                //***************FIN DE LA IMPRESION****************
                                //BluetoothPrint imprimir= new BluetoothPrint(getApplicationContext(), getResources());
                                //imprimir.FindBluetoothDevice();
                                //imprimir.openBluetoothPrinter();
                                //imprimir.checkConnection();
                                //imprimir.printData(fechayhoraactual,"este",placa);
                                //*******imprimir.disconnectBT();
                                Toast.makeText(FacturaActivity.this, respuesta, Toast.LENGTH_SHORT).show();
                            } else {
                                //String respuesta=response.body().getMessage();
                                Toast.makeText(FacturaActivity.this, "error", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Venta> call, Throwable t) {
                            Toast.makeText(FacturaActivity.this, "Algo anda mal", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

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