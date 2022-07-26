package com.semiletov.chooh;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.semiletov.chooh.databinding.FragmentFirstBinding;

import java.io.File;
import java.util.ArrayList;

public class FirstFragment extends Fragment {

   private FragmentFirstBinding binding;

   @Override
   public View onCreateView(
           LayoutInflater inflater, ViewGroup container,
           Bundle savedInstanceState)  {

      binding = FragmentFirstBinding.inflate(inflater, container, false);
/*      ArrayList<String> listItems=new ArrayList<String>();

      //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
      ArrayAdapter<String> adapter;


      adapter=new ArrayAdapter<String>(this,
              android.R.layout.lvList,
              listItems);
      setListAdapter(adapter);

      File directory;
      directory = Environment.getExternalStorageDirectory();
      File[] files = directory.listFiles();
      if (files != null) {
         Log.i("Files", "Size: " + files.length);

         R.id.lvFiles.

*/
/*
         for (int i = 0; i < files.length; i++) {
            Log.i("Files", "FileName:" + files[i].getName());
            if (files[i].isDirectory())
               Log.d("test", " IS DIR");



         }*/


      return binding.getRoot();

   }

/*
   public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);

      binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            NavHostFragment.findNavController(FirstFragment.this)
                    .navigate(R.id.action_FirstFragment_to_SecondFragment);
         }
      });
   }
*/
   @Override
   public void onDestroyView() {
      super.onDestroyView();
      binding = null;
   }

}