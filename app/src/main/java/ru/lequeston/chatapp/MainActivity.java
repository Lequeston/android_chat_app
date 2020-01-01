package ru.lequeston.chatapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private static int SIGN_IN_CODE = 1;
    private RelativeLayout mActivityMain;
    private FirebaseListAdapter<Message> mAdapter;
    private FloatingActionButton mSendButton;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_IN_CODE){
            if (resultCode == RESULT_OK){
                Snackbar.make(mActivityMain, "Вы авторизованы", Snackbar.LENGTH_LONG).show();
                displayAllMessages();
            } else {
                Snackbar.make(mActivityMain, "Вы авторизованы", Snackbar.LENGTH_LONG).show();
                finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActivityMain = findViewById(R.id.activity_main);
        mSendButton = findViewById(R.id.button_send_message);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText textField = findViewById(R.id.message_field);
                if (textField.getText().toString() == "")
                    return;
                FirebaseDatabase
                        .getInstance()
                        .getReference()
                        .push()
                        .setValue(
                                new Message(
                                        FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                                        textField.getText().toString()
                                )
                        );
                textField.setText("");
            }
        });

        // Пользователь еще не авторизован
        if (FirebaseAuth.getInstance().getCurrentUser() == null){
            //авторизация пользователя
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(), SIGN_IN_CODE);
        } else {
            Snackbar.make(mActivityMain, "Вы авторизованы", Snackbar.LENGTH_LONG).show();
        }
        displayAllMessages();
    }

    private void displayAllMessages(){
        ListView listOfMessages = findViewById(R.id.list_of_messages);
        mAdapter = new FirebaseListAdapter<Message>(this, Message.class, R.layout.list_item, FirebaseDatabase.getInstance().getReference()) {
            @Override
            protected void populateView(View v, Message model, int position) {
                TextView mess_user, mess_time, mess_text;
                mess_user = v.findViewById(R.id.message_user);
                mess_time = v.findViewById(R.id.message_time);
                mess_text = v.findViewById(R.id.message_text);

                mess_user.setText(model.getUserName());
                mess_text.setText(model.getTextMessage());
                mess_time.setText(DateFormat.format("dd-mm-yyyy", model.getMessageTime()));
            }
        };

        listOfMessages.setAdapter(mAdapter);
    }
}
