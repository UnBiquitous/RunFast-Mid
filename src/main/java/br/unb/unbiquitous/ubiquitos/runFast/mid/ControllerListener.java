package br.unb.unbiquitous.ubiquitos.runFast.mid;

import java.util.HashMap;
import java.util.Map;

import org.unbiquitous.uos.core.adaptabitilyEngine.Gateway;
import org.unbiquitous.uos.core.adaptabitilyEngine.ServiceCallException;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class ControllerListener implements OnTouchListener{

	private Gateway gateway;
	
	public ControllerListener(Gateway gateway) {
		this.gateway = gateway;
	}
	
	/*
	@Override
	public void onClick(View v) {
		Map<String, String> map = new HashMap<String, String>();
		
		switch(v.getId()){
		case R.id.controller_up:
			System.out.println("UP!!! OKAY");
			map.put("inputCode", ""+1);//"UP");
			break;
		case R.id.controller_down:
			System.out.println("DOWN!!! OKAY");
			map.put("inputCode", ""+2);//"DOWN");
			break;
		case R.id.controller_left:
			System.out.println("LEFT!!! OKAY");
			map.put("inputCode", ""+3);//"LEFT");
			break;
		case R.id.controller_right:
			System.out.println("RIGHT!!! OKAY");
			map.put("inputCode", ""+4);//"RIGHT");
			break;
		case R.id.controller_action1:
			System.out.println("ACTION1!!! OKAY");
			map.put("inputCode", ""+5);//"ACTION1");
			break;
		case R.id.controller_action2:
			System.out.println("ACTION2!!! OKAY");
			map.put("inputCode", ""+6);//"ACTION2");
			break;
		case R.id.controller_plusLeft:
			System.out.println("plusLEFT!!! OKAY");
			map.put("inputCode", ""+8);//"plusLEFT");
			break;
		case R.id.controller_plusRight:
			System.out.println("plusRIGHT!!! OKAY");
			map.put("inputCode", ""+9);//"plusRIGHT");
			break;
		case R.id.controller_plusSelect:
			System.out.println("plusSELECT!!! OKAY");
			map.put("inputCode", ""+7);//"plusSELECT");
			break;
		default:
			return;
		}
		
		//Calls the middleware driver.
		try {
			gateway.callService(null, "inputPerformed",
					"br.unb.unbiquitous.ubiquitos.runFast.mid.RFInputDriver",
					null, null, map);
		} catch (ServiceCallException e) {
			e.printStackTrace();
		}
	}
	*/

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		//Estrange changes
		if((event.getAction() != MotionEvent.ACTION_DOWN)&&
				(event.getAction() != MotionEvent.ACTION_UP))
			return false;
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		switch(v.getId()){
		case R.id.controller_up:
			System.out.println("UP!!! OKAY");
			map.put("inputCode", ""+0x01);//"UP");
			break;
		case R.id.controller_down:
			System.out.println("DOWN!!! OKAY");
			map.put("inputCode", ""+0x02);//"DOWN");
			break;
		case R.id.controller_left:
			System.out.println("LEFT!!! OKAY");
			map.put("inputCode", ""+0x03);//"LEFT");
			break;
		case R.id.controller_right:
			System.out.println("RIGHT!!! OKAY");
			map.put("inputCode", ""+0x04);//"RIGHT");
			break;
		case R.id.controller_action1:
			System.out.println("ACTION1!!! OKAY");
			map.put("inputCode", ""+0x05);//"ACTION1");
			break;
		case R.id.controller_action2:
			System.out.println("ACTION2!!! OKAY");
			map.put("inputCode", ""+0x06);//"ACTION2");
			break;
		case R.id.controller_plusLeft:
			System.out.println("plusLEFT!!! OKAY");
			map.put("inputCode", ""+0x11);//"plusLEFT");
			break;
		case R.id.controller_plusRight:
			System.out.println("plusRIGHT!!! OKAY");
			map.put("inputCode", ""+0x12);//"plusRIGHT");
			break;
		case R.id.controller_plusSelect:
			System.out.println("plusSELECT!!! OKAY");
			map.put("inputCode", ""+0x10);//"plusSELECT");
			break;
		default:
			return false;
		}
		
		//Calls the middleware driver.
		try {
			if(event.getAction() == MotionEvent.ACTION_DOWN)
				gateway.callService(null, "inputPerformed",
						"br.unb.unbiquitous.ubiquitos.runFast.mid.RFInputDriver",
						null, null, map);
			else if(event.getAction() == MotionEvent.ACTION_UP)
				gateway.callService(null, "inputReleased",
						"br.unb.unbiquitous.ubiquitos.runFast.mid.RFInputDriver",
						null, null, map);
		} catch (ServiceCallException e) {
			e.printStackTrace();
		}
		
		return true;
	}

}
