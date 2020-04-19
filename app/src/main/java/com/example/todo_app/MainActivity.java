    package com.example.todo_app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Person;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import io.paperdb.Paper;

    public class MainActivity extends AppCompatActivity {
        SwipeMenuListView listview_goals;
        SwipeMenuListView list;
        SharedPref sharedPref;
       static Databasehelper mDatabaseHelper;
       ForNewDb forNewDb;
        ArrayAdapter adapter;
        private Switch mywitch;
        ArrayList<String> goals = new ArrayList<>();
        ArrayList<String> goalsdone = new ArrayList<>();
        final Context context = this;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            sharedPref=new SharedPref(this);
            if(sharedPref.loadNightState())
            {
                setTheme(R.style.darktheme);
                todohelper.theme=1;
            }
            else {
                todohelper.theme=0;
                setTheme(R.style.AppTheme);
            }
            setContentView(R.layout.activity_main);
            mDatabaseHelper = new Databasehelper(this);
            forNewDb=new ForNewDb(this);
            mywitch=findViewById(R.id.switchh);
            if(sharedPref.loadNightState()){
                mywitch.setChecked(true);
            }
            mywitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        sharedPref.setNigthModeState(true);
                        recreate();
                    }
                    else{
                        sharedPref.setNigthModeState(false);
                        recreate();
                    }
                }
            });
            listview_goals = findViewById(R.id.list_view);
            list=findViewById(R.id.goaldone);
            populateListView();

            SwipeMenuCreator creator = new SwipeMenuCreator() {
                @Override
                public void create(SwipeMenu menu) {
                    SwipeMenuItem editItem = new SwipeMenuItem(
                            getApplicationContext());
                    editItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                            0xCE)));
                    editItem.setWidth(170);
                    editItem.setIcon(R.drawable.ic_edit);
                    menu.addMenuItem(editItem);
                    SwipeMenuItem deleteItem = new SwipeMenuItem(
                            getApplicationContext());
                    deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                            0x3F, 0x25)));
                    deleteItem.setWidth(170);
                    deleteItem.setIcon(R.drawable.ic_delete);
                    menu.addMenuItem(deleteItem);
                }
            };
            listview_goals.setMenuCreator(creator) ;
            SwipeMenuCreator creator1 = new SwipeMenuCreator() {
                @Override
                public void create(SwipeMenu menu) {
                    SwipeMenuItem deleteItem = new SwipeMenuItem(
                            getApplicationContext());
                    deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                            0x3F, 0x25)));
                    deleteItem.setWidth(170);
                    deleteItem.setIcon(R.drawable.ic_delete);
                    menu.addMenuItem(deleteItem);
                }
            };
            list.setMenuCreator(creator1);
            listview_goals.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ForNewDb.DB_name=goals.get(position);
                    Intent i = new Intent(MainActivity.this, todohelper.class);
                    startActivity(i);
                }
            });
            FloatingActionButton fab = findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LayoutInflater li = LayoutInflater.from(context);
                    View vview = li.inflate(R.layout.add_goal, null);
                    AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);
                    mDialogBuilder.setView(vview);
                    final EditText userInput = (EditText) vview.findViewById(R.id.input_text);
                    userInput.setTextColor(Color.WHITE);
                    mDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            String name_db=userInput.getText().toString();
                                            if(name_db.equals("")||name_db.contains(" ")||name_db.contains("1")||name_db.contains("2")||name_db.contains("3")||name_db.contains("4")||name_db.contains("5")||name_db.contains("6")||name_db.contains("7")||name_db.contains("8")||name_db.contains("9")||name_db.contains("0"))
                                            {
                                                toastMessage("Name of goal cannot contain numbers and symbols");
                                                dialog.cancel();

                                            }
                                            else{ mDatabaseHelper.Add(name_db);
                                            populateListView();
                                            toastMessage("Goal added");}
                                        }
                                    })
                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                    AlertDialog alertDialog = mDialogBuilder.create();
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(getColor(R.color.grey)));
                    alertDialog.show();
                    alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setTextColor(getColor(R.color.white));
                    alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setTextColor(getColor(R.color.white));
                }
            });
            listview_goals.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                    switch (index) {
                        case 0:
                            LayoutInflater li = LayoutInflater.from(context);
                            View vview = li.inflate(R.layout.add_goal, null);
                            AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);
                            mDialogBuilder.setView(vview);
                            final EditText userInput = (EditText) vview.findViewById(R.id.input_text);
                            userInput.setTextColor(Color.WHITE);
                            mDialogBuilder
                                    .setCancelable(false)
                                    .setPositiveButton("OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog,int id) {
                                                    final char dm = (char) 34;
                                                    String name= dm+goals.get(position)+dm;
                                                    mDatabaseHelper.delete(name);
                                                    String name_db=userInput.getText().toString();
                                                    if(name_db.equals("")||name_db.contains(" ")||name_db.contains("1")||name_db.contains("2")||name_db.contains("3")||name_db.contains("4")||name_db.contains("5")||name_db.contains("6")||name_db.contains("7")||name_db.contains("8")||name_db.contains("9")||name_db.contains("0"))
                                                    {
                                                        toastMessage("Name of goal cannot contain numbers and symbols");
                                                        dialog.cancel();

                                                    }
                                                    else{ mDatabaseHelper.Add(name_db);
                                                        populateListView();
                                                        toastMessage("Goad added");}
                                                    populateListView();
                                                }
                                            })
                                    .setNegativeButton("Cancel",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                }
                                            });
                            AlertDialog alertDialog = mDialogBuilder.create();
                            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(getColor(R.color.colorPrimary)));
                            alertDialog.show();
                            alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setTextColor(getColor(R.color.white));
                            alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setTextColor(getColor(R.color.white));
                            break;
                        case 1:
                                    final char dm = (char) 34;
                                   String name= dm+goals.get(position)+dm;
                                    mDatabaseHelper.delete(name);
                                    populateListView();
                                    break;
                    }
                    return false;
                }
            });
list.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
    @Override
    public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
        switch (index) {
            case 0:
                final char dm = (char) 34;
                String name = dm + goalsdone.get(position) + dm;
                mDatabaseHelper.delete(name);
                populateListView();
                break;
        }
        return false;
    }
});
        }
        public void populateListView() {
            Cursor data = mDatabaseHelper.getData();
            goals.clear();
            goalsdone.clear();
                while (data.moveToNext()) {
                if(!data.getString(2).contains("1")){
                    goals.add(data.getString(1));
                }
                   else goalsdone.add(data.getString(1));

            }
                adapter = new ArrayAdapter<>(this, R.layout.multiple_choice, goals);
                listview_goals.setAdapter(adapter);
                adapter = new ArrayAdapter<>(this, R.layout.done_list, goalsdone);
                 list.setAdapter(adapter);

        }
        private void toastMessage(String s){
            Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
        }
        public static void rep(String name) {
            char dm = (char) 34;
            mDatabaseHelper.replace(dm + name + dm, "1");
        }

            public void restartApp(){
            //Intent intent=new Intent(getApplicationContext(),MainActivity.class);
           // startActivity(intent);
                MainActivity.this.recreate();
            }
}


