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


public class MainActivity extends AppCompatActivity implements FileListViewAdapter.ItemClickListener {

   private AppBarConfiguration appBarConfiguration;
   private ActivityMainBinding binding;

   File directory;
   File[] files;

   FileListViewAdapter files_adapter;
   //ArrayList<String> fileNames;// = new ArrayList<>();

   ArrayList <String> aList;
   ArrayAdapter <String> adapter;

   EditText edText;
   Button button;
   ListView listView;
   RecyclerView filesView;
   private static final String TAG = "CHOOH";

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      binding = ActivityMainBinding.inflate(getLayoutInflater());
      setContentView(binding.getRoot());

      edText = findViewById (R.id.editTest);
      button = findViewById (R.id.btTest);
      listView = findViewById (R.id.lvFiles);
      //filesView = findViewById (R.id.rvFiles);

      aList = new ArrayList<>();

      String[] planets = new String[] { "Mercury", "Venus", "Earth", "Mars",
              "Jupiter", "Saturn", "Uranus", "Neptune"};
      ArrayList<String> planetList = new ArrayList<String>();
      planetList.addAll( Arrays.asList(planets) );

      adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, aList);

      adapter.add( "Ceres" );
      adapter.add( "Pluto" );
      adapter.add( "Haumea" );
      adapter.add( "Makemake" );
      adapter.add( "Eris" );

      /*

      File[] externalStorageVolumes =
        ContextCompat.getExternalFilesDirs(getApplicationContext(), null);
File primaryExternalStorage = externalStorageVolumes[0];

       */

//      adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_list_item_1, aList);


      listView.setAdapter(adapter);
      //filesView.setAdapter(adapter);

      Log.i(TAG, "--------------------------");
/*
      button.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {

            String s = edText.getText().toString();
            aList.add(s);
            adapter.notifyDataSetChanged();

            Log.i(TAG, "BUTTON CLICK " + s);

         }

         }
      );
*/
/*
      listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
         public void onItemClick(AdapterView<?> parent, View view,
                                 int position, long id) {
            Log.i(TAG, "itemClick: position = " + position + ", id = "+ id);
         }
      });
*/

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
      fileNames.add("Horse");
      fileNames.add("Cow");
      fileNames.add("Camel");
      fileNames.add("Sheep");
      fileNames.add("Goat");
      fileNames.add("001");
      fileNames.add("980983t");
      fileNames.add("G-093-t");
      fileNames.add("Gioueouoweut");
      fileNames.add("4456");
      fileNames.add("0980980");
*/

      ArrayList<String> fileNames = new ArrayList<>();

      directory = Environment.getExternalStorageDirectory();
//      File[] files = directory.listFiles();
      files = directory.listFiles();

      fileNames.add("..");

      if (files != null) {
       //  Log.i("Files", "Size: " + files.length);


         for (int i = 0; i < files.length; i++) {
            //Log.i("Files", "FileName:" + files[i].getName());
            //if (files[i].isDirectory())
            //  Log.d("test", " IS DIR");
            fileNames.add(files[i].getName());

         }
         }


         // set up the RecyclerView
      RecyclerView recyclerView = findViewById(R.id.rvFiles);

      LinearLayoutManager linearLayoutManager =new LinearLayoutManager(this);

      recyclerView.setLayoutManager(linearLayoutManager);
      files_adapter = new FileListViewAdapter(this, fileNames);
      files_adapter.setClickListener(this);
      recyclerView.setAdapter(files_adapter);

      RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
      DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
              linearLayoutManager.getOrientation());
      recyclerView.addItemDecoration(dividerItemDecoration);
      
   }

   @Override
   public void onItemClick(View view, int position) {
      //  Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
      //Log.i(TAG, "You clicked " + files_adapter.getItem(position) + " on row number " + position);
      if (files_adapter.getItem(position) == "..")
         {
          Log.i(TAG, "You clicked UP DIR");
         }
      else {
            Log.i(TAG, "You clicked " + files[position - 1]);
            //check if dir
            String fname = files[position - 1].getAbsolutePath();
            File f = new File(fname);
            Log.i(TAG, fname);
            if (f.isDirectory())
               {
               }
            else
               {
               //if not, play file

               }
          }
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