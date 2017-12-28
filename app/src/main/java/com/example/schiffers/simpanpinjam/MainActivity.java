package com.example.schiffers.simpanpinjam;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String KEYPREF="Key Preference";
    public static final String KEYUSER="Nama";
    SharedPreferences sharedPreferences;
    TextView nama,alamat,nomor;
    final Context c=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        nama=(TextView)findViewById(R.id.txthm2);
        alamat=(TextView)findViewById(R.id.txthm4);
        nomor=(TextView)findViewById(R.id.txthm3);
        load();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
        } else if (id == R.id.nav_anggota) {
            Intent intent=new Intent(getApplicationContext(),Anggota.class);
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
                    load();
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
    public void load(){
        sharedPreferences=getSharedPreferences(KEYPREF, Context.MODE_PRIVATE);
        if(sharedPreferences.contains(KEYUSER)){
            nama.setText(sharedPreferences.getString(KEYUSER,""));
            alamat.setText(sharedPreferences.getString("alamat",""));
            nomor.setText(sharedPreferences.getString("nomor",""));
        }
    }
}
