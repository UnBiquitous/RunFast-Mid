package br.unb.unbiquitous.ubiquitos.runFast.mid;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.unbiquitous.uos.core.adaptabitilyEngine.Gateway;
import org.unbiquitous.uos.core.adaptabitilyEngine.ServiceCallException;
import org.unbiquitous.uos.core.messageEngine.messages.ServiceResponse;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * Activity which shows the car options to the pilot when the race is already running.
 *
 */
public class SelectCarActivity extends Activity {

	private static String TAG = "SelectCarActivity";

	private Gateway gateway = null;
	
	private Button[] btsCarsOptions;

	private SCAListener listener = null;
	
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
        setContentView(R.layout.selectcar_activity);
        
        MidManager.setActivity(this);
        gateway = MidManager.getGateway();
        createUI();
    }
	
	private void createUI(){
		btsCarsOptions = new Button[10];
		btsCarsOptions[0] = (Button)findViewById(R.id.select_btBlueCar);
		btsCarsOptions[1] = (Button)findViewById(R.id.select_btRedCar);
		btsCarsOptions[2] = (Button)findViewById(R.id.select_btBlueRedCar);
		btsCarsOptions[3] = (Button)findViewById(R.id.select_btBlueWhiteCar);
		btsCarsOptions[4] = (Button)findViewById(R.id.select_btGreenWhiteCar);
		btsCarsOptions[5] = (Button)findViewById(R.id.select_btRedYellowCar);
		btsCarsOptions[6] = (Button)findViewById(R.id.select_btWhiteCar);
		btsCarsOptions[7] = (Button)findViewById(R.id.select_btF1BlueCar);
		btsCarsOptions[8] = (Button)findViewById(R.id.select_btF1RedCar);
		btsCarsOptions[9] = (Button)findViewById(R.id.select_btRandomCar);
		
		listener = new SCAListener();
		for(int i=0; i<btsCarsOptions.length;++i){
			btsCarsOptions[i].setOnClickListener(listener);
		}
	}
	
	
	
	/**
	 * Make a request to the game to enter with the given parameters.
	 * @param teamNumber
	 * @param character
	 */
	private void requestToJoinGame(int carType){
		ServiceResponse response = null;
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("carType", carType);
			//Verify if the carType is available
			if(gateway.callService(MidManager.getGameDevice(), "isCarTypeAvailable",
					MidManager.RFDEVICES_DRIVER,null,null,map).
					getResponseString("isCarTypeAvailable").equals("true")) {
		
				//Makes the entering request
				response = gateway.callService(MidManager.getGameDevice(), "getTeamsInfos",
						MidManager.RFDEVICES_DRIVER,null,null,null);
				int size = Integer.decode((String)response.getResponseData("numberOfTeams"));
			
				map = new HashMap<String, Object>();
				map.put("deviceName", gateway.getCurrentDevice().getName());
				map.put("teamNumber", size);
				map.put("character", "pilot");
				map.put("carType", carType);
				response = gateway.callService(MidManager.getGameDevice(), "requestPlayerJoin",
						MidManager.RFDEVICES_DRIVER, null, null, map);
			}else{
				createUnvailableDialog().show();
			}
			
		} catch (ServiceCallException e) {
			e.printStackTrace();
		}
		
		if(response!=null){
			if(response.getResponseString("hadJoined").equals("true")){
				Intent intent = new Intent(getApplicationContext(), ControllerActivity.class);
				intent.putExtra("character", "pilot");
				startActivity(intent);
				finish();
			}else{
				//recreate
				Intent intent = new Intent(getApplicationContext(), SelectActivity.class);
				startActivity(intent);
				finish();
			}
		}
	}
	
	private AlertDialog createUnvailableDialog(){
		// 1. Instantiate an AlertDialog.Builder with its constructor
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		// 2. Chain together various setter methods to set the dialog characteristics
		builder.setMessage(R.string.unvailableDialog_message)
			.setTitle(R.string.unvailableDialog_title)
			.setPositiveButton(R.string.unvailableDialog_ok, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					// User sad ok: do nothing
				}
			});
				
		// 3. Get the AlertDialog from create()
		AlertDialog dialog = builder.create();
		
		return dialog;
	}
	
	/**
	 * Listener that implements OnClickListener and sends the "new team" message,
	 * based in the chosen team.
	 * @author rafaelsimao
	 */
	public class SCAListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			Random random = new Random();
			
			for(int i=0; i<btsCarsOptions.length;++i){
				if(btsCarsOptions[i]==v){
					if(i==9)						
						requestToJoinGame(Math.abs(random.nextInt()%9)+1);
					else
						requestToJoinGame(i+1);
				}
			}
		}
		
	}
	
}
