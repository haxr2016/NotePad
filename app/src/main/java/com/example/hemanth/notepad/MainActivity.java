package com.example.hemanth.notepad;

import android.content.Context;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.text.method.ScrollingMovementMethod;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private EditText data;
    private TextView dTime;
    private Notes notes;
    private boolean textChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        data = (EditText) findViewById(R.id.editText);
        dTime = (TextView) findViewById(R.id.dateTime);

    }

    @Override
    protected void onResume() {
        notes = loadfile();
        if (notes != null) {
            textChanged = false;
            data.setText(notes.getDescription());
            dTime.setText(notes.getDateTime());
        }

        data.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (notes != null) {
                    if (!notes.getDescription().equalsIgnoreCase(editable.toString())) {
                        textChanged = true;
                    }
                }
            }
        });
        super.onResume();

    }

    private Notes loadfile() {
        Log.d(TAG, "loadFile: Loading JSON File");
        notes = new Notes();
        try {
            InputStream is = getApplicationContext().openFileInput(getString(R.string.file_name));
            JsonReader reader = new JsonReader(new InputStreamReader(is, getString(R.string.encoding)));
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("description")) {
                    notes.setDescription(reader.nextString());

                } else if (name.equals("dt")) {
                    notes.setDateTime(reader.nextString());
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();
        } catch (FileNotFoundException e) {
            Toast.makeText(this, getString(R.string.no_file), Toast.LENGTH_SHORT).show();
            notes.setDateTime(dtConversion());
            dTime.setText(notes.getDateTime());


        } catch (Exception e) {
            e.printStackTrace();
        }


        return notes;
    }


    @Override
    protected void onPause() {
        notes.setDescription(data.getText().toString());
        notes.setDateTime(dtConversion());
        super.onPause();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("dataTv", data.getText().toString());
        outState.putString("dtTv", dTime.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        data.setText(savedInstanceState.getString("dataTv"));
        dTime.setText(savedInstanceState.getString("dtTv"));

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onStop() {
        if (textChanged) {
            saveNotes();
        }
        super.onStop();
    }

    private void saveNotes() {
        Log.d(TAG, "saveProduct: Saving JSON File");
        try {
            FileOutputStream fos = getApplicationContext().openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);

            JsonWriter writer = new JsonWriter(new OutputStreamWriter(fos, getString(R.string.encoding)));
            writer.setIndent("  ");
            writer.beginObject();
            writer.name("description").value(notes.getDescription());
            notes.setDateTime(dtConversion());
            writer.name("dt").value(notes.getDateTime());
            writer.endObject();
            writer.close();


            /// You do not need to do the below - it's just
            /// a way to see the JSON that is created.
            ///
            StringWriter sw = new StringWriter();
            writer = new JsonWriter(sw);
            writer.setIndent("  ");
            writer.beginObject();
            writer.name("description").value(notes.getDescription());
            writer.name("dt").value(dtConversion());
            writer.endObject();
            writer.close();
            Log.d(TAG, "saveProduct: JSON:\n" + sw.toString());
            ///
            ///

            Toast.makeText(this, getString(R.string.saved), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.getStackTrace();
        }

    }

    public String dtConversion() {
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("E MMM d, hh:mm:ss a");
        return ("Last Update: " + ft.format(dNow));
    }


}

