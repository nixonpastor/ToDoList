package com.example.todolist;

import android.os.Build;
import android.os.Bundle;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;


import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity{
    ArrayList<String> toDoListItems = new ArrayList<>();
    RecyclerView recyclerView;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toDoListItems.add("Wash the car");
        toDoListItems.add("Workout");
        toDoListItems.add("Pick up sister from school");
        toDoListItems.add("Do math homework");
        toDoListItems.add("Finish drawing");

        recyclerView = findViewById(R.id.recyclerViewToDoList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new RecyclerViewAdapter(toDoListItems));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                final View popUp = inflater.inflate(R.layout.popup_addtolist, null);

                int width = ConstraintLayout.LayoutParams.WRAP_CONTENT;
                int height = ConstraintLayout.LayoutParams.WRAP_CONTENT;

                final PopupWindow popUpWindow = new PopupWindow(popUp, width, height, true);

                popUpWindow.showAtLocation(view, Gravity.CENTER_HORIZONTAL, 0 , 0 );

                Button addButton = (Button) popUp.findViewById(R.id.addButtonToDoList);

                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView toDoTextView = popUp.findViewById(R.id.textViewAddItem);
                        toDoListItems.add(toDoTextView.getText().toString());
                        recyclerView.setAdapter(new RecyclerViewAdapter(toDoListItems));
                        popUpWindow.dismiss();
                    }
                });
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    String deletedItem;

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            final int position = viewHolder.getAdapterPosition();

           switch (direction){
               case ItemTouchHelper.LEFT:
                   deletedItem = toDoListItems.get(position);
                   toDoListItems.remove(position);
                   recyclerView.setAdapter(new RecyclerViewAdapter(toDoListItems));
                   Snackbar.make(recyclerView, "Deleted: " + deletedItem, BaseTransientBottomBar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                       @RequiresApi(api = Build.VERSION_CODES.O)
                       @Override
                       public void onClick(View v) {
                           toDoListItems.add(position, deletedItem);
                           recyclerView.setAdapter(new RecyclerViewAdapter(toDoListItems));
                       }
                   }).show();
                   break;
           }
        }
    };



}