package com.example.android.demoapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import java.sql.DriverManager;
import java.sql.SQLException;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;


/**
 * Created by nrutemby on 10/04/2018.
 */

public class MainActivityBis extends AppCompatActivity {

    static final String url = "jdbc:mysql://192.168.4.1:3306/capteur_multi_pollution";
    static final String user = "appAndroid";
    static final String pass = "appAndroid";
    public static List<objClass> objList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_bis);
    }


    It is possible to do it but not recommended. I have done it before as I was in the same boat as you so I will share some code.

    I used this jdbc jar specifically: https://www.dropbox.com/s/wr06rtjqv0q1vgs/mysql-connector-java-3.0.17-ga-bin.jar?dl=0

    now for the code:

            package com.example.test.databaseapp;
    import java.sql.DriverManager;
    import java.sql.SQLException;

    import android.app.Activity;
    import android.content.Context;
    import android.os.AsyncTask;

    public class MainActivity extends Activity {
        static final String url = "jdbc:mysql://x.x.x.x:xxxx/DBNAME";
        static final String user = "client";
        static final String pass = "password";
        public static List<objClass> objList;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            new Download(MainActivity.this, internalUrl).execute(); //async task for getting data from db
        }
    }
    Now for my async task:

    public class Download extends AsyncTask<Void, Void, String> {
        ProgressDialog mProgressDialog;
        Context context;
        private String url;

        public Download(Context context, String url) {
            this.context = context;
            this.url = url;
        }

        protected void onPreExecute() {
            mProgressDialog = ProgressDialog.show(context, "",
                    "Please wait, getting database...");
        }

        protected String doInBackground(Void... params) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                java.sql.Connection con = DriverManager.getConnection(url, user, pass);
                java.sql.Statement st = con.createStatement();
                java.sql.ResultSet rs = st.executeQuery("select * from table");
                list = new ArrayList<objClass>();

                while (rs.next()) {
                    String field= rs.getString("field");
                    MainActivity.playerList.add(new objectClass(field));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return "Complete";
        }

        protected void onPostExecute(String result) {
            if (result.equals("Complete")) {
                mProgressDialog.dismiss();
            }
        }
    }
}
