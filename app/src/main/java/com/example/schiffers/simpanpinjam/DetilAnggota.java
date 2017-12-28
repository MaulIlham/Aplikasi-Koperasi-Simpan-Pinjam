package com.example.schiffers.simpanpinjam;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;

public class DetilAnggota extends AppCompatActivity {

    private static final String TAG="DetilAnggota";
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDataSetListener;
    EditText dnama,dnomor,demail,dalamat;
    Button update,delete;
    ImageView imageView;
    TextView tkode,tkel;
    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detil_anggota);

        dbHelper=new DBHelper(this);
        Intent intent = getIntent();
        tkel=(TextView)findViewById(R.id.txtanggota10);
        imageView=(ImageView)findViewById(R.id.imgtangga2);
        dnama=(EditText)findViewById(R.id.edtanggota12);
        dnomor=(EditText)findViewById(R.id.edtanggota7);
        demail=(EditText)findViewById(R.id.edtanggota6);
        dalamat=(EditText)findViewById(R.id.edtanggota8);
        tkode=(TextView) findViewById(R.id.txtanggota7);
        update=(Button)findViewById(R.id.btnupdateanggota);
        delete=(Button)findViewById(R.id.btndeleteanggota);

        tkode.setText(intent.getStringExtra("sendid"));
        dnama.setText(intent.getStringExtra("sendnama"));
        dnomor.setText(intent.getStringExtra("sendtelepon"));
        demail.setText(intent.getStringExtra("sendemail"));
        dalamat.setText(intent.getStringExtra("sendalamat"));
        tkel.setText(intent.getStringExtra("sendkelamin"));

        mDisplayDate=(TextView)findViewById(R.id.txtanggota8);
        mDisplayDate.setText(intent.getStringExtra("sendtanggal"));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal=Calendar.getInstance();
                int year=cal.get(Calendar.YEAR);
                int month=cal.get(Calendar.MONTH);
                int day=cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog=new DatePickerDialog(DetilAnggota.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,mDataSetListener,
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
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.updateAnggota(Long.parseLong(tkode.getText().toString()),dnama.getText().toString(),tkel.getText().toString(),mDisplayDate.getText().toString(),dnomor.getText().toString(),demail.getText().toString(),dalamat.getText().toString());
                Intent intent1=new Intent(getApplicationContext(),Anggota.class);
                startActivity(intent1);
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.deleteAnggota(Long.parseLong(tkode.getText().toString()));
                Intent intent1=new Intent(getApplicationContext(),Anggota.class);
                startActivity(intent1);
            }
        });
        tkel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu=new PopupMenu(DetilAnggota.this,tkel);
                popupMenu.getMenuInflater().inflate(R.menu.popup,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        tkel.setText(item.getTitle());
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.anggota, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sms) {
            Intent send=new Intent(Intent.ACTION_SENDTO, Uri.fromParts("smsto",dnomor.getText().toString(),null));
            send.putExtra("sms_body","text message");
            startActivity(send);
        }else if (id == R.id.action_telepon) {
            Uri number=Uri.parse("tel:"+dnomor.getText().toString());
            Intent intent=new Intent(Intent.ACTION_DIAL,number);
            startActivity(intent);
        }else if (id == R.id.action_email) {
            Intent intent=new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"+demail.getText().toString()));
            intent.putExtra("subject","feedback");
            intent.putExtra("body","");
            startActivity(intent);
        }else if (id == R.id.action_map) {
            Intent c = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.co.id/maps/place/"+dalamat.getText().toString()));
            startActivity(c);
        }

        return super.onOptionsItemSelected(item);
    }

}
