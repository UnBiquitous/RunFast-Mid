package br.unb.unbiquitous.ubiquitos.runFast.mid;

import java.io.File;
import java.io.IOException;
import java.util.ListResourceBundle;
import java.util.ResourceBundle;

import org.unbiquitous.driver.execution.ExecutionDriver;
import org.unbiquitous.uos.core.ClassLoaderUtils;
import org.unbiquitous.uos.core.ContextException;
import org.unbiquitous.uos.core.UOSApplicationContext;
import org.unbiquitous.uos.core.adaptabitilyEngine.Gateway;
import org.unbiquitous.uos.core.messageEngine.dataType.UpDevice;
import org.unbiquitous.uos.network.socket.connectionManager.EthernetTCPConnectionManager;

import android.app.Activity;
import android.content.Context;

public class MidManager {

	public static final String RFDEVICES_DRIVER = "br.unb.unbiquitous.ubiquitos.runFast.devicesControl.RFDevicesDriver";
	public static final String RFDEVICES_EVENT = "RFDevicesEvent";
	
	private static Gateway gateway = null;
	private static UOSApplicationContext uosApplicationContext = null;

	private static Activity activity = null;
	private static UpDevice gameDevice = null;
	
	private static MidManager instance = null;
	
	public static MidManager getInstance(Activity activity){
		MidManager.activity = activity;
		if(instance==null){
			instance = new MidManager();
		}
		return instance;
	}
	
	private MidManager(){}
	
	/**
	 * Starts the middleware.
	 */
	public void startMiddleware() {
		if(activity == null){
			return;
		}
		if(uosApplicationContext!=null){
			stopMiddleware();
		}
		
		File writableDir = activity.getApplicationContext().getDir("temp", Context.MODE_WORLD_WRITEABLE);
		File tempDir = null;
		try {
			tempDir = File.createTempFile("temp.owl", ""+System.nanoTime(),writableDir);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		final File tempDir2 = tempDir;
		
		ResourceBundle prop = new ListResourceBundle() {
			protected Object[][] getContents() {
				return new Object[][] {
					{"ubiquitos.connectionManager", EthernetTCPConnectionManager.class.getName()},
					{"ubiquitos.eth.tcp.port", "14984"},
					{"ubiquitos.eth.tcp.passivePortRange", "14985-15000"},
					{"ubiquitos.eth.udp.port", "15001"},
					{"ubiquitos.eth.udp.passivePortRange", "15002-15017"},
					{"ubiquitos.driver.deploylist", 
						RFInputDriver.class.getName()+";"+ExecutionDriver.class.getName()},
					{"ubiquitos.ontology.path",tempDir2.getPath()},
		        };
			}
		};
		
		ClassLoaderUtils.builder = new ClassLoaderUtils.DefaultClassLoaderBuilder(){
		 	 public ClassLoader getParentClassLoader() {
		 		 return activity.getClassLoader();
		 	 };
		};

		uosApplicationContext = new UOSApplicationContext();
		try {
			uosApplicationContext.init(prop);
			gateway = uosApplicationContext.getGateway();
		} catch (ContextException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Stops the middleware.
	 */
	public void stopMiddleware(){
		uosApplicationContext.tearDown();
		uosApplicationContext = null;
		gateway = null;
	}

	public static void receiveInvite(UpDevice device){
		if(activity!=null){
			if(activity instanceof MainActivity){
				((MainActivity) activity).receiveInvite(device);
			}
		}
	}
	
	
	/**
	 * @return the gateway
	 */
	public static Gateway getGateway() {
		return gateway;
	}

	/**
	 * @return the uosApplicationContext
	 */
	public static UOSApplicationContext getUosApplicationContext() {
		return uosApplicationContext;
	}

	/**
	 * @return the activity
	 */
	public static Activity getActivity() {
		return activity;
	}

	/**
	 * @param activity the activity to set
	 */
	public static void setActivity(Activity activity) {
		MidManager.activity = activity;
	}

	/**
	 * @return the gameDevice
	 */
	public static UpDevice getGameDevice() {
		return gameDevice;
	}

	/**
	 * @param gameDevice the gameDevice to set
	 */
	public static void setGameDevice(UpDevice gameDevice) {
		MidManager.gameDevice = gameDevice;
	}

}
