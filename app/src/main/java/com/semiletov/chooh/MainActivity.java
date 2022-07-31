package com.semiletov.chooh;

import static androidx.core.content.PackageManagerCompat.LOG_TAG;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.security.acl.Permission;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity
{

   private AppBarConfiguration appBarConfiguration;
   private ActivityMainBinding binding;

   File current_dir;

   File[] files;
   File[] file_list;

   ArrayList<String> fnames;

   FileListViewAdapter files_adapter;
   ArrayList<String> fileNames;


   ArrayList <String> aList;
   ArrayAdapter <String> adapter;

   String current_file;
   int current_list_position;

   MediaPlayer player;

   EditText edText;
   Button button;
   ListView listView;

   private static final String TAG = "CHOOH";


   public void list_set_pos (int pos)
   {

      listView.setSelection (pos);
      listView.scrollListBy (pos);

   }

   public void directory_up()
   {
      if (current_dir.getParent() == null)
         return;
      current_dir = new File (current_dir.getParent());
      fill_list_with_filenames (current_dir.getAbsolutePath());

      //set selected item
   }


   public void track_play (String fname)
   {
      Log.d(TAG, "track_play: " + fname);

      current_file = fname;


      list_set_pos (current_list_position);

      if (player.isPlaying())
          player.stop();

      player.reset();

      try {
         player.setDataSource(fname);
         player.prepare();
         player.start();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public void track_prev()
   {
      if (current_list_position == 0)
         return;

      current_list_position--;

      File f = files[current_list_position];
      track_play (f.getAbsolutePath());

      list_set_pos (current_list_position);

   }

   public void track_next()
   {

      if (current_list_position == files.length)
         return;

      current_list_position++;

      File f = files[current_list_position];
      track_play (f.getAbsolutePath());


      list_set_pos (current_list_position);

   }

  public void track_play_pause ()
  {
     if (player.isPlaying())
         player.pause();
     else
         player.start();

  }



   public void fill_list_with_filenames (String path)
   {
      Log.i(TAG, "fill_list_with_filenames : " + path);

      current_dir = new File(path);

      File dir = new File (path);
     // files = dir.listFiles();

      files = dir.listFiles(new FileFilter() {
                               @Override
                               public boolean accept(File file)
                               {

                                  if (file.isDirectory())
                                     return true;

                                  if (file.getPath().endsWith(".mp3") ||
                                     file.getPath().endsWith(".flac") ||
                                     file.getPath().endsWith(".ogg") ||
                                     file.getPath().endsWith(".wav"))
                                     return true;

                                  return false;
                               }
                            }
      );

      if (files != null && files.length > 1) {
         Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File object1, File object2) {
               return object1.getName().toLowerCase(Locale.getDefault()).compareTo(object2.getName().toLowerCase(Locale.getDefault()));
            }
         });
      }

      adapter.clear();
      adapter.add("..");

      if (files != null)
         {
          //  Log.i("Files", "Size: " + files.length);
          for (int i = 0; i < files.length; i++)
             {
              adapter.add(files[i].getName());
              Log.i("Files", files[i].getName());

             }

         }


   }


   public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

   public void showDialog(final String msg, final Context context,
                          final String permission) {
      AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
      alertBuilder.setCancelable(true);
      alertBuilder.setTitle("Permission necessary");
      alertBuilder.setMessage(msg + " permission is necessary");
      alertBuilder.setPositiveButton(android.R.string.yes,
              new DialogInterface.OnClickListener() {
                 public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions((Activity) context,
                            new String[] { permission },
                            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                 }
              });
      AlertDialog alert = alertBuilder.create();
      alert.show();
   }

   public boolean checkPermissionREAD_EXTERNAL_STORAGE(
           final Context context) {
      int currentAPIVersion = Build.VERSION.SDK_INT;
      if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
         if (ContextCompat.checkSelfPermission(context,
                 Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    (Activity) context,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
               showDialog("External storage", context,
                       Manifest.permission.READ_EXTERNAL_STORAGE);

            } else {
               ActivityCompat
                       .requestPermissions(
                               (Activity) context,
                               new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
                               MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            }
            return false;
         } else {
            return true;
         }

      } else {
         return true;
      }
   }


   @Override
   public void onRequestPermissionsResult(int requestCode,
                                          String[] permissions, int[] grantResults) {
      switch (requestCode) {
         case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
               // do your stuff
            } else {
               //Toast.makeText(Login.this, "GET_ACCOUNTS Denied",
                 //      Toast.LENGTH_SHORT).show();
               Log.i(TAG, "GET_ACCOUNTS Denied");
            }
            break;
         default:
            super.onRequestPermissionsResult(requestCode, permissions,
                    grantResults);
      }
   }

   @Override
   protected void onCreate (Bundle savedInstanceState)
   {
    super.onCreate(savedInstanceState);

    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

      if (checkPermissionREAD_EXTERNAL_STORAGE(this)) {
         // do your stuff..
      }

    player = new MediaPlayer();


      //button = findViewById (R.id.btTest);
    listView = findViewById (R.id.lvFiles);

    aList = new ArrayList<>();

/*
      File[] externalStorageVolumes =
              ContextCompat.getExternalFilesDirs(getApplicationContext(), null);
      File primaryExternalStorage = externalStorageVolumes[0];
*/
    current_dir = Environment.getExternalStorageDirectory();
    adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, aList);
    fill_list_with_filenames (current_dir.getAbsolutePath());
    listView.setAdapter(adapter);


   // listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);


      /*

      File[] externalStorageVolumes =
        ContextCompat.getExternalFilesDirs(getApplicationContext(), null);
File primaryExternalStorage = externalStorageVolumes[0];

       */


      listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
      {
         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.i(TAG, "itemClick: position = " + position + ", id = " + id);

            if (position == 0)
               {
                //up dir
                  directory_up();
               }
            else
                {
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
                      track_play (f.getAbsolutePath());
                      current_list_position = position - 1;
                     }

               //Log.i(TAG, files[position - 1].getAbsolutePath());
            }
         }
      });


      setSupportActionBar(binding.toolbar);

      NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
      appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
      NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);


      //getAllAudioFromDevice(this);


      ImageButton btPlayPause = (ImageButton) findViewById(R.id.btPlayPause);
      btPlayPause.setOnClickListener(new View.OnClickListener() {
         public void onClick(View v) {
            track_play_pause();
         }
      });


      ImageButton btPlayNext = (ImageButton) findViewById(R.id.btPlayNext);
      btPlayNext.setOnClickListener(new View.OnClickListener() {
         public void onClick(View v) {
            track_next();
         }
      });

      ImageButton btPlayPrev = (ImageButton) findViewById(R.id.btPlayPrev);
      btPlayPrev.setOnClickListener(new View.OnClickListener() {
         public void onClick(View v) {
            track_prev();
         }
      });

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