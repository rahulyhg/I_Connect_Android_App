package com.example.varun.listpractise;

import android.app.DownloadManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;

import com.firebase.client.FirebaseException;
import com.firebase.client.Query;
import com.firebase.ui.FirebaseRecyclerAdapter;

import java.io.IOException;
import java.util.ArrayList;

import static com.example.varun.listpractise.R.string.navigation_drawer_close;

/**
 * Created by varun on 3/26/2016.
 */
public class MainDisplayDomain extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{

    public final static String EXTRA_MESSAGE = "com.example.varun.listpractise.MESSAGE";
    Firebase mChildRef, fir;
    String message;
    RecyclerView mRecyclerView;
    ImageButton imgbt;
    Query queryref ;
    String str="Android";
    ActionBarDrawerToggle toggle;
    FloatingActionButton fab;
    TextView categorydetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Firebase.setAndroidContext(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maindisplaydomain);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle=new ActionBarDrawerToggle(this,drawer,R.drawable.downreal,R.string.navigation_drawer_open, navigation_drawer_close);
        toggle.syncState();

        drawer.addDrawerListener(toggle);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);

        categorydetails = (TextView) findViewById(R.id.category);


        fir = new Firebase("https://boiling-fire-2749.firebaseio.com");
        //  settings for the recycler view
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // intent message retrieval
        Intent intent = getIntent();
        message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        categorydetails.setText(message);

        try{
            mChildRef = fir.child(message);
        }
        catch (FirebaseException e){
            e.printStackTrace();
        }
        //mChildRef = fir.child(message);
        queryref =mChildRef.orderByChild("uid");
        // setting up of firebaseRecycler Adapter
        FirebaseRecyclerAdapter<TopicEntry, TopicEntryViewHolder> mAdapter =
                new FirebaseRecyclerAdapter<TopicEntry, TopicEntryViewHolder>
                        (TopicEntry.class, R.layout.duplic, TopicEntryViewHolder.class,queryref) {
                    @Override
                    public void populateViewHolder(TopicEntryViewHolder chatMessageViewHolder, TopicEntry chatMessage, int position) {
                        chatMessageViewHolder.mText.setText(chatMessage.getdate());
                        chatMessageViewHolder.pText.setText(chatMessage.getname());
                        chatMessageViewHolder.nText.setText(chatMessage.getPhone());
                        chatMessageViewHolder.tText.setText(chatMessage.getTopic());
                    }
                };
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
//        imgbt = (ImageButton) findViewById(R.id.imageButton);
//        imgbt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                userinput();
//            }
//        });


        fab = (FloatingActionButton) findViewById(R.id.imageButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userinput();
            }
        });

    }

// function for firebaserecycler adapter
    public static class TopicEntryViewHolder
            extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mText, pText, tText, nText;

        FloatingActionButton button,mButton;

        public TopicEntryViewHolder(View v) {
            super(v);
            mText = (TextView) v.findViewById(R.id.textView1);
            pText = (TextView) v.findViewById(R.id.textView2);
            tText = (TextView) v.findViewById(R.id.textView3);
            nText = (TextView) v.findViewById(R.id.textView4);
            button = (FloatingActionButton) v.findViewById(R.id.sms1_btn);
            button.setOnClickListener(this);

        mButton= (FloatingActionButton) v.findViewById(R.id.m_btn);
        mButton.setOnClickListener(this);
        }

//onclick listeners for the buttons in static Context
        @Override
        public void onClick(View vi) {
    int id=vi.getId();
            // for calling the number
                if(id==R.id.sms1_btn) {
                    Toast.makeText(vi.getContext(), "button clicked" + nText.getText().toString(), Toast.LENGTH_LONG).show();
                    Intent intent1 = new Intent(Intent.ACTION_DIAL);
                    intent1.setData(Uri.parse("tel:" +nText.getText() ));
                    if (intent1.resolveActivity(vi.getContext().getPackageManager()) != null) {
                        vi.getContext().startActivity(intent1);
                    }

                }
            if(id==R.id.m_btn){
                // for sending the message
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setType("vnd.android-dir/mms-sms");
                //  intent.setData(Uri.parse("smsto:"));  // This ensures only SMS apps respond
                intent.setData(Uri.parse("smsto:" + nText.getText()));
                intent.putExtra("sms_body","message");
                if (intent.resolveActivity(vi.getContext().getPackageManager()) != null) {
                        Toast.makeText(vi.getContext(), "Your phone enters to check this activity", Toast.LENGTH_LONG).show();
                        vi.getContext().startActivity(intent);
                    }   else{
                        Toast.makeText(vi.getContext(), "Your phone does not support this activity", Toast.LENGTH_LONG).show();
                    }
            }
        }
    }

// function for moving for user input file
    public void userinput(){
            Intent intent = new Intent(this,UserInputFile.class);
            intent.putExtra(EXTRA_MESSAGE,message);
        Log.e("tag","entered");
            startActivity(intent);
        Log.e("tag","passed");
    }

    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        toggle.syncState();

    }

    @Override
    public void onBackPressed(){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }



    public boolean onNavigationItemSelected(MenuItem item) {
        int id=item.getItemId();

        if(id==R.id.nav_android)
        {   str="Android";
            newActivity();
        }else

        if(id==R.id.nav_ethicalhacking)
        {   str="Ethical-Hacking";
            newActivity();
        }else

        if(id==R.id.nav_webdevelopment)
        {   str="Web-Development";
            newActivity();
        }else

        if(id==R.id.nav_mechanics)
        {   str="Mechanics";
            newActivity();
        }

        return false;
    }


    private void newActivity(){
        Intent intent = new Intent(this,MainDisplayDomain.class);
        intent.putExtra(EXTRA_MESSAGE, str);
        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case android.R.id.home:
                Intent main_activity = new Intent(this,MainActivity.class);
                startActivity(main_activity);
                break;
            case R.id.action_settings:
                Toast.makeText(getApplicationContext(),
                        "Setting...",
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.andr:
                str="Android";
                newActivity();
                break;
            case R.id.web:
                str="Web-Development";
                newActivity();
                break;
            case R.id.ethic:
                str="Ethical-Hacking";
                newActivity();
            case R.id.mechi:
                str="Mechanics";
                newActivity();
                break;
        }
        return false;
    }




}
