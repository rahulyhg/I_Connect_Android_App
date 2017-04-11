package com.example.varun.listpractise;

/**
 * Created by varun on 3/26/2016.
 */
public class TopicEntry {
    private String topic;
    private String phone;
    private String date;
    private String name;
    private String uid;

    public TopicEntry(){

    }

    public TopicEntry(String date,String name,String phone,String topic,String uid){
        this.name=name;
        this.phone=phone;
        this.date=date;
        this.topic=topic;
        this.uid=uid;
    }

    public String getdate(){
        return date;
    }
    public String getname() {
        return name;
    }
    public String getPhone() {
        return phone;
    }
    public String getTopic() {
        return topic;
    }
    public String getUid() {return uid;
    }


}
