package com.example.varun.listpractise;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.drm.DrmStore;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.firebase.ui.FirebaseRecyclerAdapter;

import java.lang.reflect.Array;

import static com.example.varun.listpractise.R.string.navigation_drawer_close;

// Main java file
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    /**
     *  consists of courses that are displayed in our main activity
     */

    ActionBarDrawerToggle toggle;
    public final static String EXTRA_MESSAGE = "com.example.varun.listpractise.MESSAGE";
    ListView l;
    String str="Android";
    String[] courses = {"Android","Ethical-Hacking","Mechanics","Web-Development"};
    Firebase fath,child;
    RecyclerView mRecyclerView;
    Query queryref ;
   FirebaseRecyclerAdapter<TopicEntry, TopicEntryViewHolder> mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Firebase.setAndroidContext(this);
        //  List view display by using newactivity layout and with textview2 i.d. position on array string courses
        // setting action Listeners on this domains and to start the new Activity
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle=new ActionBarDrawerToggle(this,drawer,R.drawable.downreal,R.string.navigation_drawer_open, navigation_drawer_close);
        toggle.syncState();

        drawer.addDrawerListener(toggle);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);

        fath= new Firebase("https://boiling-fire-2749.firebaseio.com");


        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView1);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        child=fath.child("Allentries");
        queryref=child.orderByChild("uid");
        mAdapter = new FirebaseRecyclerAdapter<TopicEntry, TopicEntryViewHolder>
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
//
//        l = (ListView) findViewById(R.id.list);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.newactivity, R.id.textView2, courses);
//        l.setAdapter(adapter);
//        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
//                TextView tv = (TextView) v.findViewById(R.id.textView2); // typecast from the list view to text view
//                str = tv.getText().toString();
//                newActivity();
//            }
//        });




    }

    // function for firebaserecycler adapter
    public static class TopicEntryViewHolder
            extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mText, pText, tText, nText;
        FloatingActionButton button,mButton;

        //Button button,mButton;
        public TopicEntryViewHolder(View v) {
            super(v);
            mText = (TextView) v.findViewById(R.id.textView1);
            pText = (TextView) v.findViewById(R.id.textView2);
            tText = (TextView) v.findViewById(R.id.textView3);
            nText = (TextView) v.findViewById(R.id.textView4);
            button = (FloatingActionButton) v.findViewById(R.id.sms1_btn);
            button.setOnClickListener(this);

            mButton=(FloatingActionButton) v.findViewById(R.id.m_btn);
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
                intent1.setData(Uri.parse("tel:" + nText.getText()));
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
        intent.putExtra("from","MainActivity");
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
                Toast.makeText(this,"pressed",Toast.LENGTH_LONG).show();
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




    // function for moving for user input file
    public void userinput(){
        Intent intent = new Intent(this,UserInputFile.class);
        intent.putExtra(EXTRA_MESSAGE,str);
        startActivity(intent);
    }

}












