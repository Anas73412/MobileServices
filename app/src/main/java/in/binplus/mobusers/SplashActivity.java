package in.binplus.mobusers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import in.binplus.mobusers.util.Session_management;

public class SplashActivity extends AppCompatActivity {

    Session_management session_management;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        session_management=new Session_management(SplashActivity.this);
        Thread background = new Thread() {
            public void run() {

                try {
                    // Thread will sleep for 5 seconds
                    sleep(2 * 1000);

                    // After 5 seconds redirect to another intent
                    //checkAppPermissions();

                    go_next();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        // start thread
        background.start();
    }

    public void go_next() {
        if(session_management.isLoggedIn()) {
            Intent startmain = new Intent(SplashActivity.this, HomeActivity.class);
            startActivity(startmain);
        }else{
            Intent startmain = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(startmain);
        }
        finish();
    }
}
