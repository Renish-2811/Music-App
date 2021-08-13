package com.example.iplay;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;


public class MainActivity extends AppCompatActivity {


    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
     listView = findViewById(R.id.listview);
        super.onCreate(savedInstanceState);



        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                final ArrayList<File> mySongs = fetchSongs(Environment.getExternalStorageDirectory());
                String [] items = new String[mySongs.size()] ;

                for (int i=0;i<mySongs.size();i++)
                {
                    items[i] = (i+1) + "."+ mySongs.get(i).getName().replace(".mp3","");
                }

                ArrayAdapter <String>adapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,items);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(MainActivity.this , Mysong.class);
                        String currsong = listView.getItemAtPosition(i).toString();
                        intent.putExtra("songlist",mySongs);
                        intent.putExtra("currsong",currsong);
                        intent.putExtra("position",i);
                        startActivity(intent);
                    }
                });


            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                   permissionToken.continuePermissionRequest();
            }
        })
                .check();

    }

    public ArrayList<File>  fetchSongs(File file)
    {
        ArrayList arrayList = new ArrayList();
        File [] songs = file.listFiles();
        if (songs!=null)
       for (File myFile:songs )
       {
           if (!myFile.isHidden() && myFile.isDirectory())
           {
               arrayList.addAll(fetchSongs(myFile));
           }
           else
           {
               if (myFile.getName().endsWith(".mp3") && !myFile.getName().startsWith("."))
               {
                   arrayList.add(myFile);
               }
           }
       }

        return arrayList;
    }



}