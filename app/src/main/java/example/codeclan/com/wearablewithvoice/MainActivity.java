package example.codeclan.com.wearablewithvoice;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    int NOTIFICATION_ID = 001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //clear all previous notifications
        //generate new ids
        NotificationManagerCompat.from(this).cancelAll();

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        //method to handle voice notifications
        voiceNotifications(pendingIntent);
    }

    public void voiceNotifications(PendingIntent pendingIntent){
        //Key for teh string that is deleivered
        //in the 'actions' intent.

        final String EXTRA_VOICE_REPLY = "extra_voice_reply";
        final String voiceOptions = "Choose one of these options: ";

        String [] voiceChoices = getResources().getStringArray(R.array.voice_choices);

        final RemoteInput remoteInput = new RemoteInput.Builder(EXTRA_VOICE_REPLY).
                setLabel(voiceOptions).
                setChoices(voiceChoices).build();
        //calls voice notifications
        handleVoiceNotifications(remoteInput, pendingIntent);
        //get the users spoken word and display it.
        CharSequence replyText = getMessageText(getIntent(), EXTRA_VOICE_REPLY);
                if(replyText != null){
                    Log.d("VoiceNotifications",  "you replied" + replyText);
                }

    }
    // Method for responding to Voice Notification messages
    public void handleVoiceNotifications(RemoteInput remoteInput, PendingIntent pendingIntent){
        String message = "Please respond to this message.";

        //set up notification action class again.
        NotificationCompat.Action action = new NotificationCompat.Action.Builder(R.mipmap.ic_launcher,
                getString(R.string.notification_title), pendingIntent).addRemoteInput(remoteInput).build();
        Notification notification = new NotificationCompat.Builder(MainActivity.this).
                setContentText(message).
                setContentTitle(getText(R.string.notification_title)).
                setSmallIcon(R.mipmap.ic_launcher).
                extend(new NotificationCompat.WearableExtender()).addAction(action).build();

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(MainActivity.this);
                notificationManagerCompat.notify(NOTIFICATION_ID, notification);

    }

    // Method that accepts an intent and returns the voice
    // response, which is referenced by the EXTRA_VOICE_REPLY
    // key.
    public CharSequence getMessageText(Intent intent, String EXTRA_VOICE_REPLY){

        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if(remoteInput != null){
            return remoteInput.getCharSequence(EXTRA_VOICE_REPLY);
        }
        return null;
    }

}
