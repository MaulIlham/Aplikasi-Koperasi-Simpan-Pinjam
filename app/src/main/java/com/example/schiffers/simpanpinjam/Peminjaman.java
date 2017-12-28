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
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.text.TextWatcher;
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
import java.util.List;

public class Peminjaman extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG="Pinjaman";
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDataSetListener;
    ImageView imageView;
    final Context c=this;
    DBHelper dbHelper;
    ListView list;
    public static final String KEYPREF="Key Preference";
    public static final String KEYUSER="Nama";
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peminjaman);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dbHelper=new DBHelper(this);
        loadpinjaman();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflater=LayoutInflater.from(c);
                View mView =layoutInflater.inflate(R.layout.inputpinjaman,null);
                AlertDialog.Builder alertDialogBuilderUserInput=new AlertDialog.Builder(c);
                alertDialogBuilderUserInput.setView(mView);

                final EditText dnilai=(EditText)mView.findViewById(R.id.edtpinjaman1);
                final EditText dbunga=(EditText)mView.findViewById(R.id.edtpinjaman2);
                final EditText dangsuran=(EditText)mView.findViewById(R.id.edtpinjaman3);
                final EditText djmlhangsuran=(EditText)mView.findViewById(R.id.edtpinjaman4);
                final EditText dtotal=(EditText)mView.findViewById(R.id.edtpinjaman5);

                final Spinner nam=(Spinner)mView.findViewById(R.id.spinnerpinjaman);
                List<String> users=new ArrayList<String>();
                ArrayAdapter<String> adapter;


                dbunga.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        int hasil,a,b;
                        a=Integer.parseInt(dbunga.getText().toString());
                        b=(Integer.parseInt(dnilai.getText().toString())*a)/100;
                        hasil=b+Integer.parseInt(dnilai.getText().toString());
                        dtotal.setText(hasil+"");
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                dangsuran.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        int hasil,a,b;
                        a=Integer.parseInt(dangsuran.getText().toString());
                        b=Integer.parseInt(dtotal.getText().toString());
                        hasil=b/a;
                        djmlhangsuran.setText(hasil+"");
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                imageView=(ImageView)mView.findViewById(R.id.imgtanggal3);
                users=dbHelper.getAllUsers();
                adapter=new ArrayAdapter<String>(c,R.layout.spinnerlist,R.id.txtspinner,users);
                nam.setAdapter(adapter);
                mDisplayDate=(TextView)mView.findViewById(R.id.txttglpinjam);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar cal=Calendar.getInstance();
                        int year=cal.get(Calendar.YEAR);
                        int month=cal.get(Calendar.MONTH);
                        int day=cal.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog dialog=new DatePickerDialog(c,
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
                //////////////////////
                alertDialogBuilderUserInput.setCancelable(false).setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper.insertDataPinjaman(nam.getSelectedItem().toString(),Integer.parseInt(dnilai.getText().toString()),Integer.parseInt(dbunga.getText().toString()),Integer.parseInt(dangsuran.getText().toString()),
                                0,Integer.parseInt(dtotal.getText().toString()),0,mDisplayDate.getText().toString(),"Belum Lunas");
                        loadpinjaman();

                    }

                })
                        .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog=alertDialogBuilderUserInput.create();
                alertDialog.setTitle("Input Data Pinjaman");
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
            public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {
                Cursor cursor=dbHelper.selectedPinjam(id);
                Intent intent = new Intent(getApplicationContext(),DetilPinjam.class);
                String sendid=cursor.getString(0);
                String sendName=cursor.getString(1);
                String sendnilai=cursor.getString(2);
                String sendbunga=cursor.getString(3);
                String sendangsuran=cursor.getString(4);
                String sendjumlah=cursor.getString(5);
                String sendtotal=cursor.getString(6);
                String sendterbayar=cursor.getString(7);
                String sendtanggal=cursor.getString(8);
                String sendstatus=cursor.getString(9);
                intent.putExtra("sendid",sendid);
                intent.putExtra("sendnama",sendName);
                intent.putExtra("sendnilai",sendnilai);
                intent.putExtra("sendbunga",sendbunga);
                intent.putExtra("sendangsuran",sendangsuran);
                intent.putExtra("sendjmlangsuran",sendjumlah);
                intent.putExtra("sendtotal",sendtotal);
                intent.putExtra("sendterbayar",sendterbayar);
                intent.putExtra("sendtanggal",sendtanggal);
                intent.putExtra("sendstatus",sendstatus);
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

        if (id == R.id.nav_simpanan) {
            Intent intent=new Intent(getApplicationContext(),Simpanan.class);
            startActivity(intent);
        } else if (id == R.id.nav_home) {
            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_anggota) {
            Intent intent=new Intent(getApplicationContext(),Anggota.class);
            startActivity(intent);
        }  else if (id == R.id.nav_pengaturan) {
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
    public void loadpinjaman(){
        Cursor cursor = null;
        try {
            cursor = dbHelper.readAllPinjaman();
        } catch (Exception e) {
            Toast.makeText(this,"Data Salah",Toast.LENGTH_LONG).show();
        }
        String[] from = new String[]{"nama", "total", "terbayar"};
        int[] to = new int[]{R.id.txtpinjam1, R.id.txtpinjam2,R.id.txtpinjam3};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(Peminjaman.this, R.layout.listpinjaman, cursor, from, to);
        adapter.notifyDataSetChanged();
        list = (ListView) findViewById(R.id.listpinjaman);
        list.setAdapter(adapter);
    }
}
