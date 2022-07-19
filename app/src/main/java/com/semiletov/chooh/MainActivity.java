package com.semiletov.chooh;

import static androidx.core.content.PackageManagerCompat.LOG_TAG;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
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
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.security.acl.Permission;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity
{

   private AppBarConfiguration appBarConfiguration;
   private ActivityMainBinding binding;

   //File directory;

   File current_dir;


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


   private void parseAllAudio() {
      try {
         String TAG = "Audio";
         Cursor cur = getContentResolver().query(
                 MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                 null);

         if (cur == null) {
            // Query failed...
            Log.e(TAG, "Failed to retrieve music: cursor is null :-(");

         }
         else if (!cur.moveToFirst()) {
            // Nothing to query. There is no music on the device. How boring.
            Log.e(TAG, "Failed to move cursor to first row (no query results).");

         }else {
            Log.i(TAG, "Listing...");
            // retrieve the indices of the columns where the ID, title, etc. of the song are

            // add each song to mItems
            do {
               int artistColumn = cur.getColumnIndex(MediaStore.Audio.Media.ARTIST);
               int titleColumn = cur.getColumnIndex(MediaStore.Audio.Media.TITLE);
               int albumColumn = cur.getColumnIndex(MediaStore.Audio.Media.ALBUM);
               int durationColumn = cur.getColumnIndex(MediaStore.Audio.Media.DURATION);
               int idColumn = cur.getColumnIndex(MediaStore.Audio.Media._ID);
               int filePathIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
               Log.i(TAG, "Title column index: " + String.valueOf(titleColumn));
               Log.i(TAG, "ID column index: " + String.valueOf(titleColumn));

               Log.i("Final ", "ID: " + cur.getString(idColumn) + " Title: " + cur.getString(titleColumn) + "Path: " + cur.getString(filePathIndex));
              /* MediaFileInfo audio = new MediaFileInfo();
               audio.setFileName(cur.getString(titleColumn));
               audio.setFilePath(cur.getString(filePathIndex));
               audio.setFileType(type);
               mediaList.add(audio);*/

            } while (cur.moveToNext());
         }

      } catch (Exception e) {
         e.printStackTrace();
      }
   }






   public List<AudioModel> getAllAudioFromDevice(final Context context) {
      final List<AudioModel> tempAudioList = new ArrayList<>();

      Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
      String[] projection = {MediaStore.Audio.AudioColumns.DATA, MediaStore.Audio.AudioColumns.TITLE, MediaStore.Audio.AudioColumns.ALBUM, MediaStore.Audio.ArtistColumns.ARTIST,};
      Cursor c = context.getContentResolver().query(uri, projection, MediaStore.Audio.Media.DATA + " like ? ", new String[]{"%utm%"}, null);

      if (c != null) {
         while (c.moveToNext()) {
            AudioModel audioModel = new AudioModel();
            String path = c.getString(0);
            String name = c.getString(1);
            String album = c.getString(2);
            String artist = c.getString(3);

            audioModel.setaName(name);
            audioModel.setaAlbum(album);
            audioModel.setaArtist(artist);
            audioModel.setaPath(path);

            Log.e("Name :" + name, " Album :" + album);
            Log.e("Path :" + path, " Artist :" + artist);

            tempAudioList.add(audioModel);
         }
         c.close();
      }

      return tempAudioList;
   }


   void fill_list_with_filenames (String path)
   {
      Log.i(TAG, "fill_list_with_filenames : " + path);

      current_dir = new File(path);

      File dir = new File (path);
      files = dir.listFiles();

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


    edText = findViewById (R.id.editTest);
    button = findViewById (R.id.btTest);
    listView = findViewById (R.id.lvFiles);

    aList = new ArrayList<>();

/*
      File[] externalStorageVolumes =
              ContextCompat.getExternalFilesDirs(getApplicationContext(), null);
      File primaryExternalStorage = externalStorageVolumes[0];
*/
    current_dir = Environment.getExternalStorageDirectory();
    //  current_dir = new File (Environment.getExternalStorageDirectory().toString()+"/Music");

    //  current_dir = new File (Environment.DIRECTORY_MUSIC);

    //  current_dir = Environment.getStorageDirectory();

    adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, aList);
    fill_list_with_filenames (current_dir.getAbsolutePath());
    listView.setAdapter(adapter);

/*
      String[] proj = { MediaStore.Audio.Media._ID,MediaStore.Audio.Media.DISPLAY_NAME };
      Cursor audioCursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, proj, null, null, null);

      ArrayList<String> audioList = new ArrayList<>();

      if(audioCursor != null){
         if(audioCursor.moveToFirst()){
            do{
               int audioIndex = audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
                Log.i(TAG, audioCursor.getString(audioIndex));
               audioList.add(audioCursor.getString(audioIndex));
            }while(audioCursor.moveToNext());
         }
      }
      audioCursor.close();
*/
//      adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, aList);

     // adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1,  audioList);
      //listView.setAdapter(adapter);


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
                if (current_dir.getParent() == null)
                   return;

                current_dir = new File(current_dir.getParent());
                fill_list_with_filenames (current_dir.getAbsolutePath());

               }
            else {
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


      //getAllAudioFromDevice(this);

      binding.fab.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
              //      .setAction("Action", null).show();

            Log.i(TAG, "OOOOOOOOOOOOOOOOOOOOOOOOOOOO");

            String[] projection = new String[] {
                    MediaStore.Audio.AudioColumns.ALBUM,
                    MediaStore.Audio.AudioColumns.TITLE };

            Uri contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

            Cursor cursor = getContentResolver().query (contentUri,
                                                        projection, null, null, null);

            Log.i(TAG, "cursor.getCount(): " + cursor.getCount());

            // Get the index of the columns we need.
            int albumIdx = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM);
            int titleIdx = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.TITLE);
            // Create an array to store the result set.

            Log.i(TAG, "cursor.getCount(): " + cursor.getCount());

            String[] result = new String[cursor.getCount()];
            /*
             * Iterate over the Cursor, extracting each album name and song
             * title.
             */
            while (cursor.moveToNext()) {
               // Extract the song title.
               String title = cursor.getString(titleIdx);
               // Extract the album name.
               String album = cursor.getString(albumIdx);
               result[cursor.getPosition()] = title + " (" + album + ")";
            }
            // Close the Cursor.
            cursor.close();

            for(String t : result)
            {
               Log.v(TAG,t);
            }
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