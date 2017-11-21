package test.security.kn.com.securitytest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.GpioCallback;
import com.google.android.things.pio.PeripheralManagerService;

import java.io.IOException;

/**
 * Created by knorman on 4/17/17.
 */

public class ButtonActivity extends Activity {
    private static final String TAG = "ButtonActivity";
    private static final String BUTTON_PIN_NAME= "Button";

    private Gpio mButtonGpio;

    @Override
    protected void onCreate(Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);

        PeripheralManagerService service = new PeripheralManagerService();
        try {
            mButtonGpio = service.openGpio(BUTTON_PIN_NAME);
            mButtonGpio.setDirection(Gpio.DIRECTION_IN);
            mButtonGpio.setEdgeTriggerType(Gpio.EDGE_BOTH);
            mButtonGpio.registerGpioCallback(mCallback);
        } catch (IOException e) {
            Log.e(TAG, "Error on PeripheralIO API", e);
        }
    }

    private GpioCallback mCallback = new GpioCallback() {
        @Override
        public boolean onGpioEdge(Gpio gpio) {
            Log.i(TAG, "GPIO changed");
            return true;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mButtonGpio != null) {
            mButtonGpio.unregisterGpioCallback(mCallback);
            try {
                mButtonGpio.close();
            } catch (IOException e) {
                Log.e(TAG, "Error on PeripheralIO API", e);
            }
        }
    }
}