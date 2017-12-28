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
import java.util.List;

public class Simpanan extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG="Simpanan";
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDataSetListener;
    ImageView imageView;
    ListView list;
    final Context c=this;
    DBHelper dbHelper;
    public static final String KEYPREF="Key Preference";
    public static final String KEYUSER="Nama";
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simpanan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHelper=new DBHelper(this);
        loadsimpanan();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflater=LayoutInflater.from(c);
                View mView =layoutInflater.inflate(R.layout.inputsimpanan,null);
                AlertDialog.Builder alertDialogBuilderUserInput=new AlertDialog.Builder(c);
                alertDialogBuilderUserInput.setView(mView);

                final EditText dnilai=(EditText)mView.findViewById(R.id.edtsimpanan1);
                final EditText dketerangan=(EditText)mView.findViewById(R.id.edtsimpanan2);

                final Spinner nam=(Spinner)mView.findViewById(R.id.spinnersimpanan);
                List<String> users=new ArrayList<String>();
                ArrayAdapter<String> adapter;

                imageView=(ImageView)mView.findViewById(R.id.imgtanggal2);
                users=dbHelper.getAllUsers();
                adapter=new ArrayAdapter<String>(c,R.layout.spinnerlist,R.id.txtspinner,users);
                nam.setAdapter(adapter);
                mDisplayDate=(TextView)mView.findViewById(R.id.txttglsimpanan);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar cal=Calendar.getInstance();
                        int year=cal.get(Calendar.YEAR);
                        int month=cal.get(Calendar.MONTH);
                        int day=cal.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog dialog=new DatePickerDialog(Simpanan.this,
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
                        dbHelper.insertDataSimpan(nam.getSelectedItem().toString(),Integer.parseInt(dnilai.getText().toString()),mDisplayDate.getText().toString(),dketerangan.getText().toString());
                        loadsimpanan();
                    }

                })
                        .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog=alertDialogBuilderUserInput.create();
                alertDialog.setTitle("Input Data Simpanan");
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
                Cursor cursor=dbHelper.selectedSimpan(id);
                final String sendId=cursor.getString(0);

                LayoutInflater layoutInflater=LayoutInflater.from(c);
                View mView =layoutInflater.inflate(R.layout.detilsimpanan,null);
                AlertDialog.Builder alertDialogBuilderUserInput=new AlertDialog.Builder(c);
                alertDialogBuilderUserInput.setView(mView);

                final TextView nama=(TextView) mView.findViewById(R.id.txtdsimpanan1);
                nama.setText(cursor.getString(1));
                final TextView tanggal=(TextView) mView.findViewById(R.id.txtdsimpanan2);
                tanggal.setText(cursor.getString(2));
                final TextView jumlah=(TextView) mView.findViewById(R.id.txtdsimpanan3);
                jumlah.setText(cursor.getString(3));
                final TextView keterangan=(TextView) mView.findViewById(R.id.txtdsimpanan4);
                keterangan.setText(cursor.getString(4));
                alertDialogBuilderUserInput.setCancelable(false).setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper.deleteSimpan(Long.parseLong(sendId));
                        loadsimpanan();
                    }
                }).setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog=alertDialogBuilderUserInput.create();
                alertDialog.setTitle("Detil Simpanan");
                alertDialog.show();
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
    public void loadsimpanan(){
        Cursor cursor = null;
        try {
            cursor = dbHelper.readAllSimpan();
        } catch (Exception e) {
            Toast.makeText(this,"Data Salah",Toast.LENGTH_LONG).show();
        }
        String[] from = new String[]{"tanggal", "nama","nilai"};
        int[] to = new int[]{R.id.txtsimpan3, R.id.txtsimpan4,R.id.txtsimpan5};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(Simpanan.this, R.layout.listsimpanan, cursor, from, to);
        adapter.notifyDataSetChanged();
        list = (ListView) findViewById(R.id.listsimpanan);
        list.setAdapter(adapter);
    }
}
