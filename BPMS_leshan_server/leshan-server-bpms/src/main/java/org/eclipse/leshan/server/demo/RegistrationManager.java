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
	public void registered(Registration reg, Registration registration1, Collection<Observation> collection) {
		// TODO Auto-generated method stub
		//synchronized (registrations) {
		System.out.println("new registration : " + reg.getEndpoint());
		registrations.put(reg.getEndpoint(), reg);
	}

	@Override
	public void updated(RegistrationUpdate registrationUpdate, Registration registration, Registration registration1) {
	}

	@Override
	public void unregistered(Registration reg, Collection<Observation> collection, boolean b, Registration registration1) {
		registrations.remove(reg.getEndpoint());
	}
}