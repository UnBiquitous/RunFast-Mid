package br.unb.unbiquitous.ubiquitos.runFast.mid;

import java.util.HashMap;
import java.util.Map;

import org.unbiquitous.uos.core.adaptabitilyEngine.Gateway;
import org.unbiquitous.uos.core.adaptabitilyEngine.ServiceCallException;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class ControllerListener implements OnTouchListener{

	private static final int INVALID_INPUTCODE = -1;
	
	private Gateway gateway;
	private Activity activity;
	private String character;
	private View[] views;
	private int[] inputCodes;
	
	public ControllerListener(Gateway gateway, Activity activity, String character) {
		this.gateway = gateway;
		this.activity = activity;
		this.character = character;
		//initRegularController();
		if(character.equals("pilot"))
			initPilotController();
		if(character.equals("copilot"))
			initCopilotController();
		
		inputCodes = new int[2];
		inputCodes[0] = inputCodes[1] = -1;
	}
	
	/*
	private void initRegularController(){
		views = new View[9];
		views[0] = activity.findViewById(R.id.controller_up);
		views[1] = activity.findViewById(R.id.controller_down);
		views[2] = activity.findViewById(R.id.controller_left);
		views[3] = activity.findViewById(R.id.controller_right);
		views[4] = activity.findViewById(R.id.controller_action1);
		views[5] = activity.findViewById(R.id.controller_action2);
		views[6] = activity.findViewById(R.id.controller_plusLeft);
		views[7] = activity.findViewById(R.id.controller_plusRight);
		views[8] = activity.findViewById(R.id.controller_plusSelect);
	}
	*/
	
	private void initPilotController(){
		views = new View[9];
		views[0] = activity.findViewById(R.id.controller_up);
		views[1] = activity.findViewById(R.id.controller_down);
		views[2] = activity.findViewById(R.id.controller_left);
		views[3] = activity.findViewById(R.id.controller_right);
		views[4] = activity.findViewById(R.id.controller_action1);
		views[5] = activity.findViewById(R.id.controller_action2);
		views[6] = activity.findViewById(R.id.controller_plusLeft);
		views[7] = activity.findViewById(R.id.controller_plusRight);
		views[8] = activity.findViewById(R.id.controller_plusSelect);
	}
	
	private void initCopilotController(){
		views = new View[6];
		views[0] = activity.findViewById(R.id.controller_up);
		views[1] = activity.findViewById(R.id.controller_down);
		views[2] = activity.findViewById(R.id.controller_left);
		views[3] = activity.findViewById(R.id.controller_right);
		views[4] = activity.findViewById(R.id.controller_action1);
		views[5] = activity.findViewById(R.id.controller_action2);
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		//Estrange changes
		if(event.getAction() != MotionEvent.ACTION_MOVE)
			System.out.println("action = "+event.getAction()+" left "+
		(event.getX((event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT)+v.getLeft())+
		" info "+((event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT));
		
		//Gets action and pointer index
		int action = event.getAction()&MotionEvent.ACTION_MASK;
		int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
		
		//Filters if is one valid input code
		if(pointerIndex > (inputCodes.length-1))
			return false;
		
		//Differentiates ACTION_DOWN from ACTION_UP 
		int inputCode = INVALID_INPUTCODE;
		if((action==MotionEvent.ACTION_DOWN)||(action==MotionEvent.ACTION_POINTER_DOWN)){
			inputCode = getDownAction(v, event, action, pointerIndex);
			if(inputCode != INVALID_INPUTCODE)
				callServiceEvent(true, inputCode);//Calls the driver service
			
			System.out.println("apertou");
			
		}else if((action==MotionEvent.ACTION_UP)||(action==MotionEvent.ACTION_POINTER_UP)){
			inputCode = getUpAction(v, event, action, pointerIndex);
			if(inputCode != INVALID_INPUTCODE)
				callServiceEvent(false, inputCode);//Calls the driver service
			
			System.out.println("soltou");
		}
		
		//returns if it concluded the procedure
		if(inputCode != INVALID_INPUTCODE)
			return false;
		return true;
		
	}
	
	/**
	 * Searches the view inputCode correspondent to the x and y of the touch event
	 * and returns it.
	 * @param v
	 * @param event
	 * @param action
	 * @param pointerIndex
	 * @return the view inputCode or INVALID_INPUTCODE if it was not found
	 */
	private int getDownAction(View v, MotionEvent event, int action, int pointerIndex){
		//Initiates the inputCode
		int inputCode = INVALID_INPUTCODE;
		
		//Initiates the event x and y 
		float x = event.getX(pointerIndex)+v.getLeft();
		float y = event.getY(pointerIndex)+v.getTop();
		
		//Searches based in the x and y the correspondent view
		boolean found = false;
		for(int i=0; (i<views.length)&&(!found); ++i){
			if((x>views[i].getLeft()) && (x<(views[i].getLeft()+views[i].getWidth()))&&
					(y>views[i].getTop()) && (y<(views[i].getTop()+views[i].getHeight()))){
				//System.out.println(" entrou if - i = "+i);
				inputCode = getCharacterInputCode(i);//gets the Input code
				
				inputCodes[pointerIndex] = inputCode;//saves this pointer inputCode
				
				found = true;//found the correspondent view
			}
		}
		
		//returns the view correspondent inputCode or INVALID_INPUTCODE if it was not found
		return inputCode;
	}
	
	/**
	 * Receives the view vector position and returns this inpuCode.
	 * @param pos
	 * @return the correspondent inputCode
	 */
	private int getCharacterInputCode(int pos){
		int inputCode = INVALID_INPUTCODE;
		
		//The pilot has more buttons then copilot, also they have different
		//inputCodes, not being one direct proportion to the vector position
		if(character.equals("pilot")){
			if(pos<6)
				inputCode = pos+1;
			else if(pos==6)
				inputCode = 0x11;
			else if(pos==7)
				inputCode = 0x12;
			else if(pos==8)
				inputCode = 0x10;
		}else{
			if(pos<6)
				inputCode = pos+1;
		}
		
		return inputCode;
	}
	
	/**
	 * Returns the pointer correspondent inputCode for the release.
	 * @param v
	 * @param event
	 * @param action
	 * @param pointerIndex
	 * @return the inputCode or INVALID_INPUTCODE if it was not found
	 */
	private int getUpAction(View v, MotionEvent event, int action, int pointerIndex){
		int inputCode = INVALID_INPUTCODE;
		
		if(pointerIndex==1){
			inputCode = inputCodes[1];
			inputCodes[1] = INVALID_INPUTCODE;
		}else if(pointerIndex==0){
			if(inputCodes[0] != INVALID_INPUTCODE){
				inputCode = inputCodes[0];
				inputCodes[0] = INVALID_INPUTCODE;
			}else{
				inputCode = inputCodes[1];
				inputCodes[1] = INVALID_INPUTCODE;
			}
		}
		
		return inputCode;
	}
	
	/**
	 * Sends the input event to the driver, as the middleware can inform the observers.
	 * @param performed : true if it is one input performed and false if it is one input
	 * released.
	 * @param inputCode : contents the inputCode that will be transmitted to the service
	 */
	private void callServiceEvent(boolean performed, int inputCode){
		//prepares the map to pass the inputCode parameter
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("inputCode", ""+inputCode);
		
		//Calls the middleware driver.
		try {
			if(performed)
				gateway.callService(null, "inputPerformed",
						"br.unb.unbiquitous.ubiquitos.runFast.mid.RFInputDriver",
						null, null, map);
			else
				gateway.callService(null, "inputReleased",
						"br.unb.unbiquitous.ubiquitos.runFast.mid.RFInputDriver",
						null, null, map);
		} catch (ServiceCallException e) {
			e.printStackTrace();
		}
	}
	
	/*
	private int getViewInputCode(int viewId){
		int inputCode = INVALID_INPUTCODE;
		
		switch(viewId) {
		case R.id.controller_up:
			System.out.println("UP!!! OKAY");
			inputCode = 0x01;//"UP");
			break;
		case R.id.controller_down:
			System.out.println("DOWN!!! OKAY");
			inputCode=0x02;//"DOWN");
			break;
		case R.id.controller_left:
			System.out.println("LEFT!!! OKAY");
			inputCode = 0x03;//"LEFT");
			break;
		case R.id.controller_right:
			System.out.println("RIGHT!!! OKAY");
			inputCode = 0x04;//"RIGHT");
			break;
		case R.id.controller_action1:
			System.out.println("ACTION1!!! OKAY");
			inputCode = 0x05;//"ACTION1");
			break;
		case R.id.controller_action2:
			System.out.println("ACTION2!!! OKAY");
			inputCode = 0x06;//"ACTION2");
			break;
		case R.id.controller_plusLeft:
			System.out.println("plusLEFT!!! OKAY");
			inputCode = 0x11;//"plusLEFT");
			break;
		case R.id.controller_plusRight:
			System.out.println("plusRIGHT!!! OKAY");
			inputCode = 0x12;//"plusRIGHT");
			break;
		case R.id.controller_plusSelect:
			System.out.println("plusSELECT!!! OKAY");
			inputCode = 0x10;//"plusSELECT");
			break;
		default:
			System.out.println("DEFAULT!!!!!!!!!!!");
			break;
		}
		
		return inputCode;
	}
	*/
	
}	
