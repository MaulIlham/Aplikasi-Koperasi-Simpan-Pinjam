package com.example.schiffers.simpanpinjam;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper{
    static final private String Db_NAME="Koperasi";
    static final private String ID="_id";
    static final private String NAMA="nama";

    static final private int Db_VER=8;

    ////deklarasi nama tabel
    static final private String TB_ANGGOTA="Anggota";//tabel anggota
    static final private String CREATE_TB_ANGGOTA="create table "+TB_ANGGOTA+"(_id integer primary key autoincrement,nama text,jns_kel text,tanggal text,telepon text,email text,alamat text);";
    static final private String TB_PINJAMAN="Pinjaman";//tabel pinjaman
    static final private String CREATE_TB_PINJAMAN="create table "+TB_PINJAMAN+"(_id integer primary key autoincrement,nama text,nilai integer,bunga integer,angsuran integer,jml_angsuran integer,total integer,terbayar integer,tanggal text,status text);";//tabel pinjaman
    static final private String TB_SIMPAN="Simpanan";//tabel simpanan
    static final private String CREATE_TB_SIMPAN="create table "+TB_SIMPAN+"(_id integer primary key autoincrement,nama text,nilai integer,tanggal text,keterangan text);";

    Context mycontext;
    SQLiteDatabase myDb;
    public DBHelper(Context context) {
        super(context, Db_NAME, null, Db_VER);
        mycontext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TB_ANGGOTA);
        db.execSQL(CREATE_TB_PINJAMAN);
        db.execSQL(CREATE_TB_SIMPAN);
        Log.i("Database","Table Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+TB_ANGGOTA);
        db.execSQL("drop table if exists "+TB_PINJAMAN);
        db.execSQL("drop table if exists "+TB_SIMPAN);
        onCreate(db);
    }

    //////Anggota
    public void insertDataAnggota(String s0,String s1,String s2,String s3,String s4,String s5){
        myDb=getWritableDatabase();
        myDb.execSQL("insert into "+TB_ANGGOTA+" (nama,jns_kel,tanggal,telepon,email,alamat) values('"+s0+"','"+s1+"','"+s2+"','"+s3+"','"+s4+"','"+s5+"');");
        Toast.makeText(mycontext,"Data Tersimpan",Toast.LENGTH_LONG).show();
    }
    public Cursor readAllAnggota() {
        myDb=getWritableDatabase();
        String[] columns = new String[]{ID,NAMA,"jns_kel","tanggal","telepon","email","alamat"};
        Cursor c = myDb.query(TB_ANGGOTA, columns, null, null, null, null, ID + " asc");
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }
    public List<String> getAllUsers(){
        List<String> userlist=new ArrayList<>();
        SQLiteDatabase db =this.getReadableDatabase();
        Cursor cursor=db.rawQuery("Select nama from "+TB_ANGGOTA,null);
        if (cursor.moveToFirst()){
            do {
                userlist.add(cursor.getString(0));
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return userlist;
    }
    public Cursor selectedAnggota(long id) {
        myDb=getWritableDatabase();
        String[] columns = new String[]{"_id","nama","jns_kel","tanggal","telepon","email","alamat"};
        Cursor c = myDb.query(TB_ANGGOTA, columns, ID + "=" + id, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }
    public void deleteAnggota(long id) {
        myDb=getWritableDatabase();
        myDb.delete(TB_ANGGOTA, ID + "=" + id, null);
        close();
    }
    public void updateAnggota(long id, String s0,String s1,String s2,String s3,String s4,String s5) {
        myDb=getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nama", s0);
        values.put("jns_kel", s1);
        values.put("tanggal", s2);
        values.put("telepon", s3);
        values.put("email", s4);
        values.put("alamat", s5);
        myDb.update(TB_ANGGOTA, values, ID + "=" + id, null);
        close();
    }

    ///////Simpanan
    public void insertDataSimpan(String s0,int s1,String s2,String s3){
        myDb=getWritableDatabase();
        myDb.execSQL("insert into "+TB_SIMPAN+" (nama,nilai,tanggal,keterangan) values('"+s0+"','"+s1+"','"+s2+"','"+s3+"');");
        Toast.makeText(mycontext,"Data Saved",Toast.LENGTH_LONG).show();
    }
    public Cursor readAllSimpan() {
        myDb=getWritableDatabase();
        String[] columns = new String[]{"_id","nama", "nilai", "tanggal", "keterangan"};
        Cursor c = myDb.query(TB_SIMPAN, columns, null, null, null, null, ID + " desc");
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }
    public Cursor selectedSimpan(long id) {
        myDb=getWritableDatabase();
        String[] columns = new String[]{"_id","nama", "nilai", "tanggal", "keterangan"};
        Cursor c = myDb.query(TB_SIMPAN, columns, ID + "=" + id, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }
    public void deleteSimpan(long id) {
        myDb=getWritableDatabase();
        myDb.delete(TB_SIMPAN, ID + "=" + id, null);
        myDb.close();
    }

    /////Pinjaman
    public void insertDataPinjaman(String s0,int s1,int s2,int s3,int s4,int s5,int s6, String s7,String s8){
        myDb=getWritableDatabase();
        myDb.execSQL("insert into "+TB_PINJAMAN+" (nama,nilai,bunga,angsuran,jml_angsuran,total,terbayar,tanggal,status) values('"+s0+"','"+s1+"','"+s2+"','"+s3+"','"+s4+"','"+s5+"','"+s6+"','"+s7+"','"+s8+"');");
        Toast.makeText(mycontext,"Data Tersimpan",Toast.LENGTH_LONG).show();
    }
    public Cursor readAllPinjaman() {
        myDb=getWritableDatabase();
        String[] columns = new String[]{"_id","nama", "nilai","bunga","angsuran","jml_angsuran","total","terbayar","tanggal","status"};
        Cursor c = myDb.query(TB_PINJAMAN, columns, null, null, null, null, ID + " desc");
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }
    public Cursor selectedPinjam(long id) {
        myDb=getWritableDatabase();
        String[] columns = new String[]{"_id","nama", "nilai","bunga","angsuran","jml_angsuran","total","terbayar","tanggal","status"};
        Cursor c = myDb.query(TB_PINJAMAN, columns, ID + "=" + id, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }
    public void bayar(long id,String s0,int s1,int s2,int s3,int s4,int s5,int s6, String s7,String s8) {
        myDb=getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nama", s0);
        values.put("nilai", s1);
        values.put("bunga", s2);
        values.put("angsuran", s3);
        values.put("jml_angsuran", s4);
        values.put("total", s5);
        values.put("terbayar", s6);
        values.put("tanggal", s7);
        values.put("status", s8);
        myDb.update(TB_PINJAMAN, values, ID + "=" + id, null);
        close();
    }


}