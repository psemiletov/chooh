package com.semiletov.chooh;

import static androidx.core.content.PackageManagerCompat.LOG_TAG;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Environment;
import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.semiletov.chooh.databinding.ActivityMainBinding;

import android.widget.AdapterView.OnItemSelectedListener;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity
{

   private AppBarConfiguration appBarConfiguration;
   private ActivityMainBinding binding;

   File directory;
   File[] files;
   File[] file_list;

   ArrayList<String> fnames;

   FileListViewAdapter files_adapter;
   ArrayList<String> fileNames;


   ArrayList <String> aList;
   ArrayAdapter <String> adapter;


   EditText edText;
   Button button;
   ListView listView;

   private static final String TAG = "CHOOH";


   void fill_list_with_filenames (String path)
   {
      Log.i(TAG, "fill_list_with_filenames : " + path);

      File dir = new File (path);
      files = directory.listFiles();

      adapter.clear();
      adapter.add("..");

      if (files != null)
         {
          //  Log.i("Files", "Size: " + files.length);
          for (int i = 0; i < files.length; i++)
             {
              adapter.add(files[i].getName());
             }

         }

     
   }


   @Override
   protected void onCreate (Bundle savedInstanceState)
   {
    super.onCreate(savedInstanceState);

    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    edText = findViewById (R.id.editTest);
    button = findViewById (R.id.btTest);
    listView = findViewById (R.id.lvFiles);

    aList = new ArrayList<>();
    directory = Environment.getExternalStorageDirectory();

    adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, aList);

    fill_list_with_filenames (directory.getAbsolutePath());


    listView.setAdapter(adapter);


      /*

      File[] externalStorageVolumes =
        ContextCompat.getExternalFilesDirs(getApplicationContext(), null);
File primaryExternalStorage = externalStorageVolumes[0];

       */

      Log.i(TAG, "--------------------------");

      listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
      {
         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.i(TAG, "itemClick: position = " + position + ", id = " + id);

            if (position == 0)
             {
              //up dir

            } else {
                    File f = files[position - 1];
                    if (f.isDirectory())
                       {
                        //goto dir
                          Log.i(TAG, "GOTO DIR");

                          Log.i(TAG, f.getAbsolutePath());

                          fill_list_with_filenames (f.getAbsolutePath());

                       }
                    else
                       {
                        //play file
                      }

               //Log.i(TAG, files[position - 1].getAbsolutePath());
            }
         }
      });


      setSupportActionBar(binding.toolbar);

      NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
      appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
      NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);


      binding.fab.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
         }
      });



      /*
      fileNames = new ArrayList<>();

      directory = Environment.getExternalStorageDirectory();

      files = directory.listFiles();
*/

/*
      if (files != null) {
         //  Log.i("Files", "Size: " + files.length);


         for (int i = 0; i < files.length; i++) {
            //Log.i("Files", "FileName:" + files[i].getName());
            //if (files[i].isDirectory())
            //  Log.d("test", " IS DIR");
            fileNames.add(files[i].getName());

         }
      }
*/



   }



   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(R.menu.menu_main, menu);
      return true;
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      // Handle action bar item clicks here. The action bar will
      // automatically handle clicks on the Home/Up button, so long
      // as you specify a parent activity in AndroidManifest.xml.
      int id = item.getItemId();

      //noinspection SimplifiableIfStatement
      if (id == R.id.action_settings) {
         return true;
      }

      return super.onOptionsItemSelected(item);
   }

   @Override
   public boolean onSupportNavigateUp() {
      NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
      return NavigationUI.navigateUp(navController, appBarConfiguration)
              || super.onSupportNavigateUp();
   }
}