package com.example.varun.listpractise;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by varun on 3/26/2016.
 */
public class UserInputFile extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener{
    // nam for name ; phn for phone no. ; des for question to be asked ; top for topic to discussed
    public final static String EXTRA_MESSAGE = "com.example.varun.listpractise.MESSAGE";
    EditText nam,phn,des;
  //  Button sub,can;
    Spinner spin;
    String na="",ph="",de="",to,message;
    TextView vi,not,notnam,notph;
    // for firebase updating
    Firebase mRootRef,childRef;
    FloatingActionButton sub,can;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Firebase.setAndroidContext(this);       // setting the context for firebase
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userinputfile);
        nam = (EditText) findViewById(R.id.name);
        phn = (EditText) findViewById(R.id.number);
        des = (EditText) findViewById(R.id.description);
     //   sub = (Button) findViewById(R.id.submit);
      //  can = (Button) findViewById(R.id.cancel);
        sub = (FloatingActionButton) findViewById(R.id.submit);
        can = (FloatingActionButton) findViewById(R.id.cancel);

        spin= (Spinner) findViewById(R.id.spinner);
        not= (TextView) findViewById(R.id.notify);
        notnam= (TextView) findViewById(R.id.notifyname);
        notph= (TextView) findViewById(R.id.notifyphone);
        sub.setOnClickListener(this);
        can.setOnClickListener(this);
        // intent used
        Intent intent = getIntent();
        message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        // defining the adapter for viewing the contents in a spinner from data defined in resources (string.xml)
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,R.array.topics,R.layout.spinnerlayout);
        spin.setAdapter(adapter);
        spin.setOnItemSelectedListener(this);
        //setting onclick listeners on textviews
        nam.setOnClickListener(this);
        phn.setOnClickListener(this);
        des.setOnClickListener(this);

        // actions performed on fire base
        mRootRef=new Firebase("https://boiling-fire-2749.firebaseio.com");
    }

    /**
     * na ,ph ,de, to for name , phone , IdeaDescription , topic indicators
     * onClicking the submit button and can cancel
     */
    @Override
    public void onClick(View v) {
        // onclick for edittext listeners
        if(v.getId()==R.id.name){
            if(nam.getText().toString().equals("")){
                notnam.setText("anonymity can be maintained");
                na="ANONYMOUS";
            }
            else{
                notnam.setText("");
                na=nam.getText().toString();
            }
        }

        if(v.getId()==R.id.number){
            if(phn.getText().toString().length()!=10){
                notph.setText("the entered number is invalid");
            }
            else{
                notph.setText("");
                ph=phn.getText().toString();
            }
        }
        if(v.getId()==R.id.description){
            if(des.getText().toString().equals("")){
                not.setText("the description has to mentioned");
            }
            else{
                not.setText("");
                de=des.getText().toString();
            }
        }

        // onclick for submit button
        if (v.getId()==R.id.submit){

            Date date,da=null,di=null;
            date= new Date();
            SimpleDateFormat ft = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss ");
            // updating the fire-base
            childRef=mRootRef.child(to);// refering the child-node

            if(!de.equals("")&&ph.length()==10) {
                // finding uid for the entry
                String va;
                va=ft.format(date).toString();

                try {
                    da = ft.parse("2999/12/31 1:00:00 ");
                    di = ft.parse(va);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long diff = da.getTime()-di.getTime();

                TopicEntry Topicupdate = new TopicEntry(ft.format(date).toString(), na, ph, de,diff+"");
                childRef.push().setValue(Topicupdate);
                mRootRef.child("Allentries").push().setValue(Topicupdate);
                // re-initialisation of entire entries to null
                nam.setText("");
                phn.setText("");
                des.setText("");
                Toast.makeText(this,"onsubmit with info. " + na + "--\n" + ph + "--\n" + to + "--\n" + de, Toast.LENGTH_LONG).show();
                goback();

            }
            else{
                Toast.makeText(this,"Make sure the description is not empty and phone number is a valid one and the topic has to be selected ",Toast.LENGTH_LONG).show();
            }

        }
        else if (v.getId()==R.id.cancel){
            Toast.makeText(this,"CANCEL button is pressed",Toast.LENGTH_SHORT).show();
            goback();
        }
    }
    // methods for spinner
    // to retrieve the topic domain selected by user
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        vi = (TextView) view;
        to=vi.getText().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    // a method to retreat back to MainDisplayDomain class
    // on clicking of cancel and submit button
    private void goback(){
        Intent intentgb = new Intent(this,MainDisplayDomain.class);
        intentgb.putExtra(EXTRA_MESSAGE,message);
        startActivity(intentgb);
    }

}
