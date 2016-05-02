package com.example.android.smartstreet;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class Interact extends AppCompatActivity {
    private static final String QUOTES_PATH = "https://smartstreetadminapp2.firebaseio.com/";
    ArrayList<String> listItems = new ArrayList<String>();
    ArrayAdapter<String> sensoradapter;
    ListView list;
    //to add to the git

    ArrayList<String> listsensor = new ArrayList<String>();
    ArrayAdapter<String> sensorlistadapter;
    ListView sensorList;
    //to add to the git

    interactDialog interactDialog;
    private Firebase rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interact);

        Firebase.setAndroidContext(this);
        rootRef = new Firebase(QUOTES_PATH);

        list = (ListView) findViewById(R.id.tree_list);

        listsensor.add("LED");
        listsensor.add("Music");


        Firebase ref = rootRef.child("Trees");
        ref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Map<String, String>> sensorList = new
                        ArrayList<Map<String, String>>();


                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Map<String, String> map = child.getValue(Map.class);
                    String name = "Hello";
                    name = map.get("TreeName").toString();
                    Log.v("name is:", name);
                    listItems.add(name);
                    if (name != null) {
                        sensorList.add(map);

                    } else
                        Log.v("name", "name");
                }
                Log.v("Size:", Integer.toString(sensorList.size()));
                Log.v("Size of list:", Integer.toString(listItems.size()));

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
            /*listItems.add("Tree 1");
        listItems.add("Tree 2");
        listItems.add("Tree 3");
        listItems.add("Tree 4");
*/
        sensoradapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems);
        list.setAdapter(sensoradapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                interactDialog = new interactDialog(Interact.this);
                interactDialog.show();
            }
        });
    }


    class interactDialog extends Dialog {

        public interactDialog(Context context) {
            super(context);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            //setting the layout
            setContentView(R.layout.interactoptions);
            super.onCreate(savedInstanceState);
            Window registerWindow = getWindow();
            registerWindow.setGravity(Gravity.CENTER);

            sensorList = (ListView) findViewById(R.id.sensor_list);



            sensorlistadapter = new ArrayAdapter(Interact.this, android.R.layout.simple_list_item_1, listsensor);
            sensorList.setAdapter(sensorlistadapter);

            sensorList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                   if(position == 0) {
                       Intent ledInteract = new Intent(Interact.this, LedInteract.class);
                       startActivity(ledInteract);
                   }
                    if(position == 1) {
                        Intent musicInteract = new Intent(Interact.this, MusicInteract.class);
                        startActivity(musicInteract);
                    }

                }
            });

        }

        @Override
        protected void onStop() {
            super.onStop();
        }
    }

}
