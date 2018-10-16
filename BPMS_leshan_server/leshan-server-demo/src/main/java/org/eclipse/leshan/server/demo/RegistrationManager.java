package org.eclipse.leshan.server.demo;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.leshan.core.observation.Observation;
import org.eclipse.leshan.core.request.ObserveRequest;
import org.eclipse.leshan.core.response.LwM2mResponse;
import org.eclipse.leshan.server.californium.impl.LeshanServer;
import org.eclipse.leshan.server.registration.Registration;
import org.eclipse.leshan.server.registration.RegistrationListener;
import org.eclipse.leshan.server.registration.RegistrationUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegistrationManager implements RegistrationListener{

	private static final Logger LOG = LoggerFactory.getLogger(RegistrationManager.class);

	  public static Map<String, Registration> registrations = new HashMap<>();
	  public static LeshanServer server;
	  RegistrationManager(LeshanServer server) {
		  this.server=server;
	    server.getRegistrationService().addListener(this);
	  }

	  public void waitDevices(String... devices) throws InterruptedException {

	    LOG.info("Waiting for devices {}", devices.toString());

	    for (String device : devices) {
	      while (true) {
	        synchronized (registrations) {
	          if (registrations.containsKey(device)) {
	            break;
	          }
	          registrations.wait();
	        }
	      }
	    }
	  }


	@Override
	public void registered(Registration reg, Registration previousReg, Collection<Observation> previousObsersations) {

	}

	@Override
	public void updated(RegistrationUpdate update, Registration updatedReg, Registration previousReg) {

	}

	@Override
	public void unregistered(Registration reg, Collection<Observation> observations, boolean expired, Registration newReg) {

	}

	@Override
	public void updated(RegistrationUpdate update, Registration updatedReg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void registered(Registration reg) {
		// TODO Auto-generated method stub
				//synchronized (registrations) {
				System.out.println("new registration : " + reg.getEndpoint());
				MonitorGui.infotext.setText(MonitorGui.infotext.getText() +"\n" +"new registration : " + reg.getEndpoint() + "\n");
				      registrations.put(reg.getEndpoint(), reg);
				     /* if(reg.getEndpoint().equals("bpms_lechan_client")) {
				    	  ObserveRequest observe1 = new ObserveRequest("/20000/0/0");
							 
							 try {
								
								LwM2mResponse response1= this.server.send(reg, observe1);
								System.out.println("device response: " + response1);
							
							} catch (InterruptedException e) {
								
								e.printStackTrace();
							}
				      }*/
				      
				    //  registrations.notifyAll();
				    //}
		
	}

	@Override
	public void unregistered(Registration reg, Collection<Observation> arg1) {
		   registrations.remove(reg.getEndpoint());
		   MonitorGui.infotext.setText(MonitorGui.infotext.getText() +"\n" +"client with endpoint : " + reg.getEndpoint() + " unregistered \n");
			
	}


}