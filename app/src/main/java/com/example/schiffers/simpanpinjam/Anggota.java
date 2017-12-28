package com.example.schiffers.simpanpinjam;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class Anggota extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String KEYPREF="Key Preference";
    public static final String KEYUSER="Nama";
    SharedPreferences sharedPreferences;
    private static final String TAG="Anggota";
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDataSetListener;
    private String[] arraySpinner=new String[]{"Laki-Laki","Perempuan"};
    ImageView imageView;
    ListView list;
    DBHelper dbHelper;
    final Context c=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anggota);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dbHelper=new DBHelper(this);
        loadanggota();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflater=LayoutInflater.from(c);
                View mView =layoutInflater.inflate(R.layout.inputanggota,null);
                AlertDialog.Builder alertDialogBuilderUserInput=new AlertDialog.Builder(c);
                alertDialogBuilderUserInput.setView(mView);

                final Spinner spinner=(Spinner)mView.findViewById(R.id.spinneranggota);
                final ArrayAdapter<String> adapter=new ArrayAdapter<String>(c,android.R.layout.simple_spinner_item,arraySpinner);
                spinner.setAdapter(adapter);

                imageView=(ImageView)mView.findViewById(R.id.imgtanggal);
                final EditText nama=(EditText)mView.findViewById(R.id.edtanggota1);
                final EditText telepon=(EditText)mView.findViewById(R.id.edtanggota3);
                final EditText email=(EditText)mView.findViewById(R.id.edtanggota2);
                final EditText alamat=(EditText)mView.findViewById(R.id.edtanggota4);

                mDisplayDate=(TextView)mView.findViewById(R.id.txtanggota5);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar cal=Calendar.getInstance();
                        int year=cal.get(Calendar.YEAR);
                        int month=cal.get(Calendar.MONTH);
                        int day=cal.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog dialog=new DatePickerDialog(Anggota.this,
                                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                                mDataSetListener,
                                year,month,day);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();
                    }
                });
                mDataSetListener=new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month=month+1;
                        Log.d(TAG,"onDataSet: yyyy/mm/dd: "+year+"/"+month+"/"+day);
                        String date=day+"-"+month+"-"+year;
                        mDisplayDate.setText(date);
                    }
                };

                /////
                alertDialogBuilderUserInput.setCancelable(false).setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper.insertDataAnggota(nama.getText().toString(),spinner.getSelectedItem().toString(),mDisplayDate.getText().toString(),
                                telepon.getText().toString(),email.getText().toString(),alamat.getText().toString());
                        Toast.makeText(c,nama.getText().toString()+telepon.getText().toString(),Toast.LENGTH_LONG).show();
                        loadanggota();
                    }
                })
                        .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog=alertDialogBuilderUserInput.create();
                alertDialog.setTitle("Input Data Anggota");
                alertDialog.show();


            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor c=dbHelper.selectedAnggota(id);
                Intent intent = new Intent(getApplicationContext(),DetilAnggota.class);

                String sendId=c.getString(0);
                String sendNama=c.getString(1);
                String sendKel=c.getString(2);
                String sendTanggal=c.getString(3);
                String sendTelepon=c.getString(4);
                String sendEmail=c.getString(5);
                String sendAlamat=c.getString(6);

                intent.putExtra("sendid",sendId);
                intent.putExtra("sendnama",sendNama);
                intent.putExtra("sendkelamin",sendKel);
                intent.putExtra("sendtanggal",sendTanggal);
                intent.putExtra("sendtelepon",sendTelepon);
                intent.putExtra("sendemail",sendEmail);
                intent.putExtra("sendalamat",sendAlamat);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_pinjaman) {
            Intent intent=new Intent(getApplicationContext(),Peminjaman.class);
            startActivity(intent);
        } else if (id == R.id.nav_simpanan) {
            Intent intent=new Intent(getApplicationContext(),Simpanan.class);
            startActivity(intent);
        } else if (id == R.id.nav_home) {
            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_pengaturan) {
            LayoutInflater layoutInflater=LayoutInflater.from(c);
            View mView =layoutInflater.inflate(R.layout.pengaturan,null);
            AlertDialog.Builder alertDialogBuilderUserInput=new AlertDialog.Builder(c);
            alertDialogBuilderUserInput.setView(mView);
            final EditText dnama=(EditText)mView.findViewById(R.id.edtnamakoperasi);
            final EditText dalam=(EditText)mView.findViewById(R.id.edtalamatkoperasi);
            final EditText nomor=(EditText)mView.findViewById(R.id.edtnomorkoperasi);
            alertDialogBuilderUserInput.setCancelable(false).setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String shared=dnama.getText().toString();
                    String shared2=nomor.getText().toString();
                    String shared3=dalam.getText().toString();
                    SharedPreferences.Editor editor= sharedPreferences.edit();
                    editor.putString(KEYUSER,shared);
                    editor.putString("alamat",shared3);
                    editor.putString("nomor",shared2);
                    editor.apply();
                }

            }).setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alertDialog=alertDialogBuilderUserInput.create();
            alertDialog.setTitle("Pengaturan");
            alertDialog.show();
        } else if (id == R.id.nav_share) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void loadanggota(){
        Cursor cursor = null;
        try {
            cursor = dbHelper.readAllAnggota();
        } catch (Exception e) {
            Toast.makeText(this,"Data Salah",Toast.LENGTH_LONG).show();
        }
        String[] from = new String[]{"_id", "nama"};
        int[] to = new int[]{R.id.txtanggota2, R.id.txtanggota3};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(Anggota.this, R.layout.listanggota, cursor, from, to);
        adapter.notifyDataSetChanged();
        list = (ListView) findViewById(R.id.lstanggota);
        list.setAdapter(adapter);
    }
}
