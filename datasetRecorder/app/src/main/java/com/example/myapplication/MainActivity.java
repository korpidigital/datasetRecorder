package com.example.myapplication;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import java.io.File;
import java.io.IOException;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;




public class MainActivity extends AppCompatActivity {
    private Button startbtn, playbtn, stopplay, deletebtn, zipbtn;
    private ToggleButton toggleRecord;
    private Button apuabtn, viestibtn, aikabtn, kellobtn, saabtn, musiikkibtn, kyllabtn, eibtn, soitabtn;
    public MediaRecorder mRecorder;
    private MediaPlayer mPlayer;
    private static final String LOG_TAG = "AudioRecording";
    public static String mFileName = null;
    public static final int REQUEST_AUDIO_PERMISSION_CODE = 1;
    public String subFile ="";
    public String[] buttonIds = {"btnRecordApua","btnRecordViesti","btnRecordSaa","btnRecordAika","btnRecordKello","btnRecordKylla",
            "btnRecordEi","btnRecordSoita","btnRecordMusiikki"};
    public String[] commands = {"apua", "viesti", "saa", "aika", "kello", "kylla", "ei", "soita", "musiikki"};
    public TextView lastWord;
    public List<String> recordList =new ArrayList<String>();
    public List<String> subFileList =new ArrayList<String>();
    public List<String> filesListInDir = new ArrayList<String>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //get buttons
        zipbtn = (Button) findViewById(R.id.btnZip);
        deletebtn = (Button) findViewById(R.id.btnDelete);
        playbtn = (Button) findViewById(R.id.btnPlay);
        stopplay = (Button) findViewById(R.id.btnStopPlay);
        toggleRecord = (ToggleButton) findViewById(R.id.toggleRecord);
        apuabtn = (Button) findViewById(R.id.btnRecordApua);
        aikabtn = (Button) findViewById(R.id.btnRecordAika);
        kellobtn = (Button) findViewById(R.id.btnRecordKello);
        viestibtn = (Button) findViewById(R.id.btnRecordViesti);
        saabtn = (Button) findViewById(R.id.btnRecordSaa);
        kyllabtn = (Button) findViewById(R.id.btnRecordKylla);
        eibtn = (Button) findViewById(R.id.btnRecordEi);
        musiikkibtn = (Button) findViewById(R.id.btnRecordMusiikki);
        soitabtn = (Button) findViewById(R.id.btnRecordSoita);
        //get text
        lastWord = (TextView)findViewById(R.id.txtLastWord);
        //no text on toggle btn
        toggleRecord.setText(null);
        toggleRecord.setTextOn(null);
        toggleRecord.setTextOff(null);

        //buttons disabled
        playbtn.setEnabled(false);
        stopplay.setEnabled(false);
        toggleRecord.setEnabled(false);

        // Register the onClick listener with the implementation above
        apuabtn.setOnClickListener(myListener);
        aikabtn.setOnClickListener(myListener);
        kellobtn.setOnClickListener(myListener);
        viestibtn.setOnClickListener(myListener);
        saabtn.setOnClickListener(myListener);
        soitabtn.setOnClickListener(myListener);
        kyllabtn.setOnClickListener(myListener);
        eibtn.setOnClickListener(myListener);
        musiikkibtn.setOnClickListener(myListener);
        //
        playbtn.setOnClickListener(myListener);
        stopplay.setOnClickListener(myListener);
        deletebtn.setOnClickListener(myListener);
        zipbtn.setOnClickListener(myListener);


        //create folders for each word
        createFolders();
    }


    //toggle record - stop record
    public void onToggleClicked(View view) {
        // Is the toggle on?
        boolean on = ((ToggleButton) view).isChecked();

            if (on) {
                record(startbtn, subFile);
                playbtn.setEnabled(false);
                stopplay.setEnabled(false);
            } else {

                playbtn.setEnabled(true);
                stopplay.setEnabled(true);
                mRecorder.stop();
                mRecorder.release();
                mRecorder = null;
                playbtn.setEnabled(true);
                stopplay.setEnabled(true);
                lastWord.setText(subFileList.get(subFileList.size()-1));

                //toast
                Toast toast = Toast.makeText(getApplicationContext(), "Stopped Recording", Toast.LENGTH_SHORT);
                TextView vi = (TextView) toast.getView().findViewById(android.R.id.message);
                vi.setTextColor(Color.RED);
                toast.show();
            }
        }



    //button clicked
      private  View.OnClickListener myListener = new View.OnClickListener() {
          @Override
            public void onClick(View v) {
              if (CheckPermissions()) {
                  if (startbtn != null) {
                      startbtn.setEnabled(true);
                      startbtn.setBackgroundColor(Color.rgb(230, 230, 230));
                  }

                  switch (v.getId() /*to get clicked view id**/) {
                      case R.id.btnRecordApua:
                          startbtn = (Button) findViewById(R.id.btnRecordApua);
                          subFile = "apua";

                          break;
                      case R.id.btnRecordAika:
                          startbtn = (Button) findViewById(R.id.btnRecordAika);
                          subFile = "aika";

                          break;
                      case R.id.btnRecordKello:
                          startbtn = (Button) findViewById(R.id.btnRecordKello);
                          subFile = "kello";

                          break;
                      case R.id.btnRecordViesti:
                          startbtn = (Button) findViewById(R.id.btnRecordViesti);
                          subFile = "viesti";

                          break;
                      case R.id.btnRecordKylla:
                          startbtn = (Button) findViewById(R.id.btnRecordKylla);
                          subFile = "kylla";

                          break;
                      case R.id.btnRecordEi:
                          startbtn = (Button) findViewById(R.id.btnRecordEi);
                          subFile = "ei";

                          break;
                      case R.id.btnRecordMusiikki:
                          startbtn = (Button) findViewById(R.id.btnRecordMusiikki);
                          subFile = "musiikki";

                          break;
                      case R.id.btnRecordSoita:
                          startbtn = (Button) findViewById(R.id.btnRecordSoita);
                          subFile = "soita";

                          break;
                      case R.id.btnRecordSaa:
                          startbtn = (Button) findViewById(R.id.btnRecordSaa);
                          subFile = "saa";

                          break;
                      case R.id.btnPlay:
                          mPlayer = new MediaPlayer();
                          try {
                              mPlayer.setDataSource(recordList.get(recordList.size()-1));
                              mPlayer.prepare();
                              mPlayer.start();
                              Toast.makeText(getApplicationContext(), "Recording Started Playing", Toast.LENGTH_SHORT).show();
                          } catch (IOException e) {
                              Log.e(LOG_TAG, "prepare() failed");
                          }

                          break;
                      case R.id.btnStopPlay:
                          if(mPlayer != null){
                              mPlayer.release();
                              mPlayer = null;
                              Toast.makeText(getApplicationContext(), "Playing Audio Stopped", Toast.LENGTH_SHORT).show();
                          }



                          break;
                      case R.id.btnDelete:
                          int size = recordList.size()-1;
                          if (recordList.isEmpty() == false) {
                              File file = new File(recordList.get(size));
                              file.delete();
                              recordList.remove(size);
                              subFileList.remove(size);
                          }

                          break;
                      case R.id.btnZip:
                          String dateTime = getDateTime();
                          //Make zip file
                          String audioFolder = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Audio";
                          String rootFolder = Environment.getExternalStorageDirectory().getAbsolutePath()+"/ZippedAudio/Audio-"+dateTime+".zip";
                          zipFolder zip = new zipFolder();
                          zip.zipFileAtPath(audioFolder, rootFolder);

                          //Delete original Audio folder after it is zipped
                          File audioDir = new File(Environment.getExternalStorageDirectory(), "/Audio");
                          deleteRecursive(audioDir);
                          //Create empty folders
                          createFolders();

                          Toast.makeText(getApplicationContext(), "zip file created (PATH:/zippedAudio)", Toast.LENGTH_LONG).show();


                          break;
                      default:
                          break;
                  }
                  //RUN THESE AFTER ANY BUTTON IS PRESSED
                  //last word text view
                  if (recordList.isEmpty() == false) {
                      lastWord.setText(subFileList.get(recordList.size() -1));
                  }
                  else{
                      lastWord.setText("empty");
                      playbtn.setEnabled(false);
                      stopplay.setEnabled(false);
                  }
                  //record enabled when word is selected
                  toggleRecord.setEnabled(true);
                  //selected word is green
                  if (startbtn != null){
                      startbtn.setEnabled(false);
                      startbtn.setBackgroundColor(Color.rgb(112, 255, 77));
                  }

              }
              else
              {
                  RequestPermissions();
              }
          }
    };

    public void record(Button startbtn, String subFile) {
        String dateTime = getDateTime();
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Audio/"+subFile+"/"+ dateTime + ".wav";
        //add to list
        recordList.add(mFileName);
        subFileList.add(subFile);

            startbtn.setEnabled(false);
            playbtn.setEnabled(false);
            stopplay.setEnabled(false);
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setOutputFile(mFileName);
            try {
                mRecorder.prepare();
            } catch (IOException e) {
                Log.e(LOG_TAG, "prepare() failed");
            }
            mRecorder.start();
            //make custom toast
            Toast toast = Toast.makeText(getApplicationContext(), "Recording - "+subFile, Toast.LENGTH_SHORT);
            TextView vi = (TextView) toast.getView().findViewById(android.R.id.message);
            vi.setTextColor(Color.RED);
            toast.show();

    }


//PERMISSION TO USE MIC AND STORAGE
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_AUDIO_PERMISSION_CODE:
                if (grantResults.length> 0) {
                    boolean permissionToRecord = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean permissionToStore = grantResults[1] ==  PackageManager.PERMISSION_GRANTED;
                    if (permissionToRecord && permissionToStore) {
                        Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(),"Permission Denied",Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }
    public boolean CheckPermissions() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }
    private void RequestPermissions() {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{RECORD_AUDIO, WRITE_EXTERNAL_STORAGE}, REQUEST_AUDIO_PERMISSION_CODE);
    }

    //delete folder recursively
    void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);

        fileOrDirectory.delete();
    }
    void createFolders(){
        //create main audio folder
        File mFolder = new File(Environment.getExternalStorageDirectory(), "Audio");
        if (!mFolder.exists()) {
            mFolder.mkdirs();
            mFolder.setExecutable(true);
            mFolder.setReadable(true);
            mFolder.setWritable(true);
        }
        //create zip file folder
        File zipFolder = new File(Environment.getExternalStorageDirectory(), "ZippedAudio");
        if (!zipFolder.exists()) {
            zipFolder.mkdirs();
            zipFolder.setExecutable(true);
            zipFolder.setReadable(true);
            zipFolder.setWritable(true);
        }
        //create folder for each command
        for (String comm : commands) {
            File mFolderSub = new File(Environment.getExternalStorageDirectory() + "/Audio", comm);
            if (!mFolderSub.exists()) {
                mFolderSub.mkdirs();
                mFolderSub.setExecutable(true);
                mFolderSub.setReadable(true);
                mFolderSub.setWritable(true);
            }
        }
    }
    public String getDateTime(){
        String dateTime = new SimpleDateFormat("dd-MM-yyyy hh-mm-ss aa", Locale.getDefault()).format(new Date());
        return dateTime;
    }
}

