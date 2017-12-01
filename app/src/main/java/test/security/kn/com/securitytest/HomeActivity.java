package test.security.kn.com.securitytest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.things.pio.GpioCallback;
import com.google.android.things.pio.PeripheralManagerService;
import com.google.android.things.pio.Gpio;

import java.io.IOException;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PeripheralManagerService service = new PeripheralManagerService();
        Log.d(TAG, "Available GPIO: " + service.getGpioList());
        for (String name : service.getGpioList()) {
            try {
                Gpio mButtonGpio = service.openGpio(name);
                mButtonGpio.setDirection(Gpio.DIRECTION_IN);
                mButtonGpio.setEdgeTriggerType(Gpio.EDGE_BOTH);
                mButtonGpio.registerGpioCallback(mCallback);
            } catch (IOException e) {
                Log.e(TAG, "Error on PeripheralIO API", e);
            }

        }
        //setContentView(R.layout.activity_main);
    }

    private GpioCallback mCallback = new GpioCallback() {
        @Override
        public boolean onGpioEdge(Gpio gpio) {
            String gName = gpio.toString();
            try {
                if (gpio.getValue()) {
                    Log.i(TAG, "GPIO " + gName + " changed to TRUE");
                }
                else {
                    Log.i(TAG, "GPIO " + gName + " changed to FALSE");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return true;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        PeripheralManagerService service = new PeripheralManagerService();
        Log.d(TAG, "Available GPIO: " + service.getGpioList());
        for (String name : service.getGpioList()) {
            try {
                Gpio mButtonGpio = service.openGpio(name);
                if (mButtonGpio != null) {
                    mButtonGpio.unregisterGpioCallback(mCallback);
                    try {
                        mButtonGpio.close();
                    } catch (IOException e) {
                        Log.e(TAG, "Error on PeripheralIO API", e);
                    }
                }
            } catch (IOException e) {
                Log.e(TAG, "Error on PeripheralIO API", e);
            }

        }

    }
}
