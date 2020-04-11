package com.example.todo_app;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
public class todohelper extends AppCompatActivity {
    ForNewDb mDatabaseHelper;
    private ListView mListView;
    EditText edittodo;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todolayout);
         mListView = (ListView) findViewById(R.id.todolist);
        mDatabaseHelper = new ForNewDb(this);
edittodo=(EditText) findViewById(R.id.newtodo);
                final ArrayAdapter<String> adapter;
                edittodo.setOnKeyListener(  new View.OnKeyListener() {
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if (event.getAction() == KeyEvent.ACTION_DOWN)
                            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                                mDatabaseHelper.Add( edittodo.getText().toString());
                                edittodo.setText("");
                                populateListView();
                                return true;
                            }
                return false;
            }
        });
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        SparseBooleanArray chosen = ((ListView) parent).getCheckedItemPositions();
                        for (int i = 0; i < chosen.size(); i++) {
                            // если пользователь выбрал пункт списка,
                            // то выводим его в TextView.
                            if (chosen.valueAt(i)) {
                             //   selection.append(foods[chosen.keyAt(i)] + " ");
                            }
                        }
                    }
                });
        populateListView();
    }
    public void populateListView() {
        //get the data and append to a list
        Cursor data = mDatabaseHelper.getData();

        ArrayList<String> listData = new ArrayList<>();
        listData.clear();
        while(data.moveToNext()) {
            listData.add( data.getString(1) );
        }
        ListAdapter adapter = new ArrayAdapter<>(this, R.layout.multiple_choice, listData);

        mListView.setAdapter(adapter);
    }


}
