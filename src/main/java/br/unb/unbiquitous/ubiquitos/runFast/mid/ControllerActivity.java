package br.unb.unbiquitous.ubiquitos.runFast.mid;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

public class ControllerActivity extends Activity{

	private static String TAG = "controller";
	
	private Button left,right,up,down, action1,action2, plusLeft,plusRight,plusSelect;
	
	//private Gateway gateway = null;
	
	/**
     * Called when the activity is first created.
     * @param savedInstanceState If the activity is being re-initialized after 
     * previously being shut down then this Bundle contains the data it most 
     * recently supplied in onSaveInstanceState(Bundle). <b>Note: Otherwise it is null.</b>
     */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
        setContentView(R.layout.controller);
        
        initUI();
        initListeners();
    }
	
	private void initUI() {
		left = (Button) findViewById(R.id.controller_left);
		right = (Button) findViewById(R.id.controller_right);
		up = (Button) findViewById(R.id.controller_up);
		down = (Button) findViewById(R.id.controller_down);

		action1 = (Button) findViewById(R.id.controller_action1);
		action2 = (Button) findViewById(R.id.controller_action2);
		
		plusLeft = (Button) findViewById(R.id.controller_plusLeft);
		plusRight = (Button) findViewById(R.id.controller_plusRight);
		plusSelect = (Button) findViewById(R.id.controller_plusSelect);
	}
	
	private void initListeners() {
		ControllerListener listener = new ControllerListener(MidManager.getGateway());
		left.setOnTouchListener(listener);
		right.setOnTouchListener(listener);
		up.setOnTouchListener(listener);
		down.setOnTouchListener(listener);
		
		action1.setOnTouchListener(listener);
		action2.setOnTouchListener(listener);
		
		plusLeft.setOnTouchListener(listener);
		plusRight.setOnTouchListener(listener);
		plusSelect.setOnTouchListener(listener);
		
	}
}
