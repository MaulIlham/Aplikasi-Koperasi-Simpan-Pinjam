package com.example.schiffers.simpanpinjam;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DetilPinjam extends AppCompatActivity {

    TextView dnama,dtotal,dbunga,dangsuran,dterbayar,dtanggal,dstatus;
    Button bayar;
    int a,b,dnil,djmlh;
    Long id;
    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detil_pinjam);
        dnama=(TextView)findViewById(R.id.txtpinjam4);
        dtotal=(TextView)findViewById(R.id.txtpinjam5);
        dbunga=(TextView)findViewById(R.id.txtpinjam6);
        dangsuran=(TextView)findViewById(R.id.txtpinjam7);
        dterbayar=(TextView)findViewById(R.id.txtpinjam8);
        dtanggal=(TextView)findViewById(R.id.txtpinjam9);
        dstatus=(TextView)findViewById(R.id.txtpinjam10);
        bayar=(Button)findViewById(R.id.btnpbayar);

        dbHelper=new DBHelper(this);
        Intent intent = getIntent();
        a=Integer.parseInt(intent.getStringExtra("sendangsuran"));
        b=Integer.parseInt(intent.getStringExtra("sendtotal"));
        dnil=Integer.parseInt(intent.getStringExtra("sendnilai"));
        djmlh=Integer.parseInt(intent.getStringExtra("sendjmlangsuran"));

        id=Long.parseLong(intent.getStringExtra("sendid"));
        dnama.setText(intent.getStringExtra("sendnama"));
        dtotal.setText(intent.getStringExtra("sendtotal"));
        dbunga.setText(intent.getStringExtra("sendbunga"));
        dangsuran.setText(Integer.toString(b/a));
        dterbayar.setText(intent.getStringExtra("sendterbayar"));
        dtanggal.setText(intent.getStringExtra("sendtanggal"));
        if(djmlh==a){
            bayar.setEnabled(false);
            dstatus.setText("Lunas");
            dbHelper.bayar(id,dnama.getText().toString(),dnil,Integer.parseInt(dbunga.getText().toString()),
                    a,djmlh,b,Integer.parseInt(dterbayar.getText().toString()),dtanggal.getText().toString(),"Lunas");

        }else {
            dstatus.setText(intent.getStringExtra("sendstatus"));
        }

        bayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.bayar(id,dnama.getText().toString(),dnil,Integer.parseInt(dbunga.getText().toString()),
                        a,djmlh+1,b,Integer.parseInt(dterbayar.getText().toString())+Integer.parseInt(dangsuran.getText().toString()),
                        dtanggal.getText().toString(),dstatus.getText().toString());
                        Intent i=new Intent(getApplicationContext(),Peminjaman.class);
                startActivity(i);
            }
        });
    }
}
