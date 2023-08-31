package com.example.appfacturacion;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.util.Log;
import android.view.Display;
import android.widget.Toast;

//import com.example.johnhy.rafrago.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

/**
 * Created by JOHNHY on 27/02/2018.
 */

public class BluetoothPrint extends Context {
    // ArrayList<DetallePedido> arrayList = new ArrayList<>();
    Context context;
    // String cliente,fecha,total;
    BluetoothAdapter bluetoothAdapter;
    BluetoothSocket bluetoothSocket;
    BluetoothDevice bluetoothDevice;

    OutputStream outputStream;
    InputStream inputStream;
    Thread thread;
    Resources resources;


    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;

    public static final byte[] ESC_ALIGN_LEFT = new byte[]{0x1b, 'a', 0x00};
    public static final byte[] ESC_ALIGN_RIGHT = new byte[]{0x1b, 'a', 0x02};
    public static final byte[] ESC_ALIGN_CENTER = new byte[]{0x1b, 'a', 0x01};
    public static final byte[] ESC_CANCEL_BOLD = new byte[]{0x1B, 0x45, 0};
    public static final byte[] FEED_LINE = new byte[]{10};

    public static byte[] format = {27, 33, 0};
    public static byte[] arrayOfByte1 = {27, 33, 0};


    public BluetoothPrint(Context context, Resources resources) {
        this.context = context;
        this.resources = resources;
    }

    public void FindBluetoothDevice() {

        try {

            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (bluetoothAdapter == null) {
                //  lblPrinterName.setText("No Bluetooth Adapter found");
            }
            if (bluetoothAdapter.isEnabled()) {
                Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                context.startActivity(enableBT);
            }

            Set<BluetoothDevice> pairedDevice = bluetoothAdapter.getBondedDevices();

            if (pairedDevice.size() > 0) {
                for (BluetoothDevice pairedDev : pairedDevice) {
                    Toast.makeText(context, pairedDev.getName().toString(), Toast.LENGTH_LONG).show();
                    // My Bluetoth printer name is BTP_F09F1A
                    Log.e("device: ", pairedDev.getName());
                    if (pairedDev.getName().equals("InnerPrinter")) {
                        bluetoothDevice = pairedDev;
                        Toast.makeText(context, "Bluetooth Conectado Correctamente", Toast.LENGTH_LONG).show();
                        //lblPrinterName.setText("Bluetooth Printer Attached: "+pairedDev.getName());
                        break;
                    }
                }
                if (bluetoothDevice == null) {
                    Toast.makeText(context, "No fue posible conectar con la impresora intente de nuevo", Toast.LENGTH_LONG).show();
                }
            }

            //lblPrinterName.setText("Bluetooth Printer Attached");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    // Open Bluetooth Printer

    public void openBluetoothPrinter() {
        try {

            //Standard uuid from string //
            UUID uuidSting = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(uuidSting);
            bluetoothSocket.connect();
            outputStream=bluetoothSocket.getOutputStream();
            inputStream=bluetoothSocket.getInputStream();

            beginListenData();

        }catch (Exception ex){

        }
    }

    public boolean checkConnection(){
        if(bluetoothSocket!=null){
            if(bluetoothSocket.isConnected()){
                Toast.makeText(context,"Estoy aqui esta conectado",Toast.LENGTH_LONG).show();
                return true;
            }
        }
        return false;
    }

    public void beginListenData(){
        try{

            final Handler handler =new Handler();
            final byte delimiter=10;
            stopWorker =false;
            readBufferPosition=0;
            readBuffer = new byte[1024];

            thread=new Thread(new Runnable() {
                @Override
                public void run() {

                    while (!Thread.currentThread().isInterrupted() && !stopWorker){
                        try{
                            int byteAvailable = inputStream.available();
                            if(byteAvailable>0){
                                byte[] packetByte = new byte[byteAvailable];
                                inputStream.read(packetByte);

                                for(int i=0; i<byteAvailable; i++){
                                    byte b = packetByte[i];
                                    if(b==delimiter){
                                        byte[] encodedByte = new byte[readBufferPosition];
                                        System.arraycopy(
                                                readBuffer,0,
                                                encodedByte,0,
                                                encodedByte.length
                                        );
                                        final String data = new String(encodedByte,"US-ASCII");
                                        readBufferPosition=0;
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                //lblPrinterName.setText(data);
                                            }
                                        });
                                    }else{
                                        readBuffer[readBufferPosition++]=b;
                                    }
                                }
                            }
                        }catch(Exception ex){
                            stopWorker=true;
                        }
                    }

                }
            });

            thread.start();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    // Printing Text to Bluetooth Printer //
    public void printData(String fecha,String cliente,String total) {
        try{
            // Bold

            format[2] = ((byte)(0x8 | arrayOfByte1[2]));
            String cabecera1 = "EL FIEL INCOMPRENDIDO S.A.C.\n";
            outputStream.write(ESC_CANCEL_BOLD);
            outputStream.write(cabecera1.getBytes());

            outputStream.write(FEED_LINE);
            format[2] = ((byte)(0x8 | arrayOfByte1[2]));
            String cabecera2 =  "Principal: Pj 13 Mz F Lt 16-A\n " +
                                "Urb Canto Grande Lima-Lima-SJL\n";
            outputStream.write(ESC_CANCEL_BOLD);
            outputStream.write(cabecera2.getBytes());

            outputStream.write(FEED_LINE);
            format[2] = ((byte)(0x8 | arrayOfByte1[2]));
            String cabecera3 =  "Sucursal: Car. Panamericana Sur Km 130 Sec. Pampa Los Lobos\n" +
                                "Sub Lote A-1\n " +
                                "Lima-Canete-Cerro Azul";
            outputStream.write(ESC_CANCEL_BOLD);
            outputStream.write(cabecera3.getBytes());

            outputStream.write(FEED_LINE);
            format[2] = ((byte)(0x8 | arrayOfByte1[2]));
            String ruc = "RUC 20547701209\n";
            outputStream.write(ESC_CANCEL_BOLD);
            outputStream.write(ruc.getBytes());

            format[2] = ((byte)(0x8 | arrayOfByte1[2]));
            String fecha1 = fecha+"\n";
            outputStream.write(ESC_CANCEL_BOLD);
            outputStream.write(fecha1.getBytes());

            format[2] = ((byte)(0x8 | arrayOfByte1[2]));
            String linea = "________________________________\n";
            outputStream.write(ESC_CANCEL_BOLD);
            outputStream.write(linea.getBytes());

            format[2] = ((byte)(0x8 | arrayOfByte1[2]));
            String tipodocumento = "BOLETA DE  VENTA ELECTRONICA\n";
            outputStream.write(ESC_CANCEL_BOLD);
            outputStream.write(tipodocumento.getBytes());

            format[2] = ((byte)(0x8 | arrayOfByte1[2]));
            String nrodocumento = "B002-00005727\n";
            outputStream.write(ESC_CANCEL_BOLD);
            outputStream.write(nrodocumento.getBytes());

            format[2] = ((byte)(0x8 | arrayOfByte1[2]));
            outputStream.write(ESC_CANCEL_BOLD);
            outputStream.write(linea.getBytes());


            String hello2 ="COSTO: 10.00 SOLES\n";
            // Width
            format[2] = ((byte) (0x20 | arrayOfByte1[2]));
            outputStream.write(ESC_ALIGN_LEFT);
            outputStream.write(hello2.getBytes());
            // Underline
            String hello3 ="Cliente: Sin Nombre\n";
            format[2] = ((byte)(0x80 | arrayOfByte1[2]));
            outputStream.write(ESC_ALIGN_LEFT);
            outputStream.write(hello3.getBytes());
            // Small
            String hello4 ="DNI: 00000000\n";
            format[2] = ((byte)(0x1 | arrayOfByte1[2]));
            outputStream.write(format);
            outputStream.write(hello4.getBytes());

            format[2] = ((byte)(0x8 | arrayOfByte1[2]));
            String hello5 = "PLACA: "+ total+"\n";
            outputStream.write(ESC_CANCEL_BOLD);
            outputStream.write(hello5.getBytes());
            //printPhoto(R.drawable.logo);

            String msg1 = "\n";
            outputStream.write(ESC_ALIGN_CENTER);
            outputStream.write(msg1.getBytes());

            String msg2 = "\n";
            outputStream.write(ESC_ALIGN_CENTER);
            outputStream.write(msg2.getBytes());

            String msg3 = "\n";
            outputStream.write(ESC_ALIGN_CENTER);
            outputStream.write(msg3.getBytes());
            String msg4 = "\n";
            outputStream.write(ESC_ALIGN_CENTER);
            outputStream.write(msg4.getBytes());

            String msg5 = "\n";
            outputStream.write(ESC_ALIGN_CENTER);
            outputStream.write(msg5.getBytes());

            String msg6 = "\n";
            outputStream.write(ESC_ALIGN_CENTER);
            outputStream.write(msg6.getBytes());
        }catch (Exception ex){
            ex.printStackTrace();
            Toast.makeText(context,""+ex,Toast.LENGTH_LONG).show();
        }
    }

    // Disconnect Printer //
    public void disconnectBT() {
        try {
            stopWorker=true;
            outputStream.close();
            inputStream.close();
            bluetoothSocket.close();
            //lblPrinterName.setText("Printer Disconnected.");
        }catch (Exception ex){
            ex.printStackTrace();

        }
    }
    public void printPhoto(int img) {
        try {
            Bitmap bmp = BitmapFactory.decodeResource(resources,
                    img);
            if(bmp!=null){
                byte[] command = Utils.decodeBitmap(bmp);
                outputStream.write(ESC_ALIGN_CENTER);
                outputStream.write(command);

            }else{
                Log.e("Print Photo error", "the file isn't exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PrintTools", "the file isn't exists");
            Toast.makeText(context,"Ahora estoy aca en imagen"+e,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public AssetManager getAssets() {
        return null;
    }

    @Override
    public Resources getResources() {
        return null;
    }

    @Override
    public PackageManager getPackageManager() {
        return null;
    }

    @Override
    public ContentResolver getContentResolver() {
        return null;
    }

    @Override
    public Looper getMainLooper() {
        return null;
    }

    @Override
    public Context getApplicationContext() {
        return null;
    }

    @Override
    public void setTheme(int resid) {

    }

    @Override
    public Resources.Theme getTheme() {
        return null;
    }

    @Override
    public ClassLoader getClassLoader() {
        return null;
    }

    @Override
    public String getPackageName() {
        return null;
    }

    @Override
    public ApplicationInfo getApplicationInfo() {
        return null;
    }

    @Override
    public String getPackageResourcePath() {
        return null;
    }

    @Override
    public String getPackageCodePath() {
        return null;
    }

    @Override
    public SharedPreferences getSharedPreferences(String name, int mode) {
        return null;
    }

    @Override
    public boolean moveSharedPreferencesFrom(Context sourceContext, String name) {
        return false;
    }

    @Override
    public boolean deleteSharedPreferences(String name) {
        return false;
    }

    @Override
    public FileInputStream openFileInput(String name) throws FileNotFoundException {
        return null;
    }

    @Override
    public FileOutputStream openFileOutput(String name, int mode) throws FileNotFoundException {
        return null;
    }

    @Override
    public boolean deleteFile(String name) {
        return false;
    }

    @Override
    public File getFileStreamPath(String name) {
        return null;
    }

    @Override
    public File getDataDir() {
        return null;
    }

    @Override
    public File getFilesDir() {
        return null;
    }

    @Override
    public File getNoBackupFilesDir() {
        return null;
    }

    @Nullable
    @Override
    public File getExternalFilesDir(@Nullable String type) {
        return null;
    }

    @Override
    public File[] getExternalFilesDirs(String type) {
        return new File[0];
    }

    @Override
    public File getObbDir() {
        return null;
    }

    @Override
    public File[] getObbDirs() {
        return new File[0];
    }

    @Override
    public File getCacheDir() {
        return null;
    }

    @Override
    public File getCodeCacheDir() {
        return null;
    }

    @Nullable
    @Override
    public File getExternalCacheDir() {
        return null;
    }

    @Override
    public File[] getExternalCacheDirs() {
        return new File[0];
    }

    @Override
    public File[] getExternalMediaDirs() {
        return new File[0];
    }

    @Override
    public String[] fileList() {
        return new String[0];
    }

    @Override
    public File getDir(String name, int mode) {
        return null;
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {
        return null;
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory, @Nullable DatabaseErrorHandler errorHandler) {
        return null;
    }

    @Override
    public boolean moveDatabaseFrom(Context sourceContext, String name) {
        return false;
    }

    @Override
    public boolean deleteDatabase(String name) {
        return false;
    }

    @Override
    public File getDatabasePath(String name) {
        return null;
    }

    @Override
    public String[] databaseList() {
        return new String[0];
    }

    @Override
    public Drawable getWallpaper() {
        return null;
    }

    @Override
    public Drawable peekWallpaper() {
        return null;
    }

    @Override
    public int getWallpaperDesiredMinimumWidth() {
        return 0;
    }

    @Override
    public int getWallpaperDesiredMinimumHeight() {
        return 0;
    }

    @Override
    public void setWallpaper(Bitmap bitmap) throws IOException {

    }

    @Override
    public void setWallpaper(InputStream data) throws IOException {

    }

    @Override
    public void clearWallpaper() throws IOException {

    }

    @Override
    public void startActivity(Intent intent) {

    }

    @Override
    public void startActivity(Intent intent, @Nullable Bundle options) {

    }

    @Override
    public void startActivities(Intent[] intents) {

    }

    @Override
    public void startActivities(Intent[] intents, Bundle options) {

    }

    @Override
    public void startIntentSender(IntentSender intent, @Nullable Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags) throws IntentSender.SendIntentException {

    }

    @Override
    public void startIntentSender(IntentSender intent, @Nullable Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags, @Nullable Bundle options) throws IntentSender.SendIntentException {

    }

    @Override
    public void sendBroadcast(Intent intent) {

    }

    @Override
    public void sendBroadcast(Intent intent, @Nullable String receiverPermission) {

    }

    @Override
    public void sendOrderedBroadcast(Intent intent, @Nullable String receiverPermission) {

    }

    @Override
    public void sendOrderedBroadcast(@NonNull Intent intent, @Nullable String receiverPermission, @Nullable BroadcastReceiver resultReceiver, @Nullable Handler scheduler, int initialCode, @Nullable String initialData, @Nullable Bundle initialExtras) {

    }

    @Override
    public void sendBroadcastAsUser(Intent intent, UserHandle user) {

    }

    @Override
    public void sendBroadcastAsUser(Intent intent, UserHandle user, @Nullable String receiverPermission) {

    }

    @Override
    public void sendOrderedBroadcastAsUser(Intent intent, UserHandle user, @Nullable String receiverPermission, BroadcastReceiver resultReceiver, @Nullable Handler scheduler, int initialCode, @Nullable String initialData, @Nullable Bundle initialExtras) {

    }

    @Override
    public void sendStickyBroadcast(Intent intent) {

    }

    @Override
    public void sendStickyOrderedBroadcast(Intent intent, BroadcastReceiver resultReceiver, @Nullable Handler scheduler, int initialCode, @Nullable String initialData, @Nullable Bundle initialExtras) {

    }

    @Override
    public void removeStickyBroadcast(Intent intent) {

    }

    @Override
    public void sendStickyBroadcastAsUser(Intent intent, UserHandle user) {

    }

    @Override
    public void sendStickyOrderedBroadcastAsUser(Intent intent, UserHandle user, BroadcastReceiver resultReceiver, @Nullable Handler scheduler, int initialCode, @Nullable String initialData, @Nullable Bundle initialExtras) {

    }

    @Override
    public void removeStickyBroadcastAsUser(Intent intent, UserHandle user) {

    }

    @Nullable
    @Override
    public Intent registerReceiver(@Nullable BroadcastReceiver receiver, IntentFilter filter) {
        return null;
    }

    @Nullable
    @Override
    public Intent registerReceiver(@Nullable BroadcastReceiver receiver, IntentFilter filter, int flags) {
        return null;
    }

    @Nullable
    @Override
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter, @Nullable String broadcastPermission, @Nullable Handler scheduler) {
        return null;
    }

    @Nullable
    @Override
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter, @Nullable String broadcastPermission, @Nullable Handler scheduler, int flags) {
        return null;
    }

    @Override
    public void unregisterReceiver(BroadcastReceiver receiver) {

    }

    @Nullable
    @Override
    public ComponentName startService(Intent service) {
        return null;
    }

    @Nullable
    @Override
    public ComponentName startForegroundService(Intent service) {
        return null;
    }

    @Override
    public boolean stopService(Intent service) {
        return false;
    }

    @Override
    public boolean bindService(Intent service, @NonNull ServiceConnection conn, int flags) {
        return false;
    }

    @Override
    public void unbindService(@NonNull ServiceConnection conn) {

    }

    @Override
    public boolean startInstrumentation(@NonNull ComponentName className, @Nullable String profileFile, @Nullable Bundle arguments) {
        return false;
    }

    @Override
    public Object getSystemService(@NonNull String name) {
        return null;
    }

    @Nullable
    @Override
    public String getSystemServiceName(@NonNull Class<?> serviceClass) {
        return null;
    }

    @Override
    public int checkPermission(@NonNull String permission, int pid, int uid) {
        return 0;
    }

    @Override
    public int checkCallingPermission(@NonNull String permission) {
        return 0;
    }

    @Override
    public int checkCallingOrSelfPermission(@NonNull String permission) {
        return 0;
    }

    @Override
    public int checkSelfPermission(@NonNull String permission) {
        return 0;
    }

    @Override
    public void enforcePermission(@NonNull String permission, int pid, int uid, @Nullable String message) {

    }

    @Override
    public void enforceCallingPermission(@NonNull String permission, @Nullable String message) {

    }

    @Override
    public void enforceCallingOrSelfPermission(@NonNull String permission, @Nullable String message) {

    }

    @Override
    public void grantUriPermission(String toPackage, Uri uri, int modeFlags) {

    }

    @Override
    public void revokeUriPermission(Uri uri, int modeFlags) {

    }

    @Override
    public void revokeUriPermission(String toPackage, Uri uri, int modeFlags) {

    }

    @Override
    public int checkUriPermission(Uri uri, int pid, int uid, int modeFlags) {
        return 0;
    }

    @Override
    public int checkCallingUriPermission(Uri uri, int modeFlags) {
        return 0;
    }

    @Override
    public int checkCallingOrSelfUriPermission(Uri uri, int modeFlags) {
        return 0;
    }

    @Override
    public int checkUriPermission(@Nullable Uri uri, @Nullable String readPermission, @Nullable String writePermission, int pid, int uid, int modeFlags) {
        return 0;
    }

    @Override
    public void enforceUriPermission(Uri uri, int pid, int uid, int modeFlags, String message) {

    }

    @Override
    public void enforceCallingUriPermission(Uri uri, int modeFlags, String message) {

    }

    @Override
    public void enforceCallingOrSelfUriPermission(Uri uri, int modeFlags, String message) {

    }

    @Override
    public void enforceUriPermission(@Nullable Uri uri, @Nullable String readPermission, @Nullable String writePermission, int pid, int uid, int modeFlags, @Nullable String message) {

    }

    @Override
    public Context createPackageContext(String packageName, int flags) throws PackageManager.NameNotFoundException {
        return null;
    }

    @Override
    public Context createContextForSplit(String splitName) throws PackageManager.NameNotFoundException {
        return null;
    }

    @Override
    public Context createConfigurationContext(@NonNull Configuration overrideConfiguration) {
        return null;
    }

    @Override
    public Context createDisplayContext(@NonNull Display display) {
        return null;
    }

    @Override
    public Context createDeviceProtectedStorageContext() {
        return null;
    }

    @Override
    public boolean isDeviceProtectedStorage() {
        return false;
    }
}
