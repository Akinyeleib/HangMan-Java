package com.example.hangman;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class HelperClass {

    public static void main(String[] args) {

    }

    protected static void writeToFile(int data, String fileName, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new
                    OutputStreamWriter(context.openFileOutput(fileName + ".txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(String.valueOf(data));
            outputStreamWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected static void writeToFile(String data, String fileName, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new
                    OutputStreamWriter(context.openFileOutput(fileName + ".txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected static String readFromFile(String fileName, Context context) {
        String ret = "";
        try (InputStream inputStream = context.openFileInput(fileName + ".txt")) {
            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";

                if ((receiveString = bufferedReader.readLine()) != null) {
                    ret = receiveString;
                }
                inputStream.close();
            }

        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.getMessage());
        }
        return ret;

    }

}
