package com.example.eventapp.messenger;

public class Dialog {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    Dialog(Message message){
        this.message = message;
    }
    Dialog(){

    }
}
