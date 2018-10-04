package org.eclipse.leshan.server.demo;

import java.util.Map;

import org.eclipse.leshan.core.node.LwM2mNode;
import org.eclipse.leshan.core.node.LwM2mObjectInstance;
import org.eclipse.leshan.core.node.LwM2mResource;
import org.eclipse.leshan.core.node.LwM2mSingleResource;
import org.eclipse.leshan.core.observation.Observation;
import org.eclipse.leshan.core.request.ExecuteRequest;
import org.eclipse.leshan.core.request.WriteRequest;
import org.eclipse.leshan.core.response.LwM2mResponse;
import org.eclipse.leshan.core.response.ObserveResponse;
import org.eclipse.leshan.server.californium.impl.LeshanServer;
import org.eclipse.leshan.server.observation.ObservationListener;
import org.eclipse.leshan.server.registration.Registration;
import org.json.JSONObject;

public class ObservationManager implements ObservationListener{
	


	public ObservationManager(LeshanServer server ) {
		  server.getObservationService().addListener(this);
	     // queue = eventQueue;
	}
	
	
	@Override
	public void newObservation(Observation observation, Registration registration) {
		// TODO Auto-generated method stub
		System.out.println("Observing resource " + observation.getPath() + " from client "
	            + registration.getEndpoint());
	}

	@Override
	public void cancelled(Observation observation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onResponse(Observation observation, Registration registration, ObserveResponse response) {
		// TODO Auto-generated method stub
		 System.out.println("New notification from client " + observation.getRegistrationId()+ ": "
	                + response.getContent() + response.getObservation().getPath().toString());
		// System.out.println( observation.getContext());
		 
		 if (registration != null) {
	            String path = response.getObservation().getPath().toString();

	            LwM2mNode node = response.getContent();
	            //If there are multiple values
	            if (node instanceof LwM2mObjectInstance) {
	                LwM2mObjectInstance instance = (LwM2mObjectInstance) node;
	                Map<Integer, LwM2mResource> resources = instance.getResources();

	                for (Map.Entry<Integer, LwM2mResource> entry : resources.entrySet()) {
	                    LwM2mSingleResource value = (LwM2mSingleResource) entry.getValue();
	                    String fullPath = path + '/' + value.getId();
	                    mapEvent(new ObservationData(registration, value, fullPath));
	                  
	                }
	            }
	            //If it's only one value
	            else if (node instanceof LwM2mSingleResource) {
	                LwM2mSingleResource value = (LwM2mSingleResource) node;
	                mapEvent(new ObservationData(registration, value, path));
	              
	                
	            }
	        }
	}

	@Override
	public void onError(Observation observation, Registration registration, Exception error) {
		// TODO Auto-generated method stub
		
	}
	
    private  void mapEvent(ObservationData data) {
        String endpointName = data.getEndpoint();
        String path = data.getPath();
       // JSONObject json1 = new JSONObject(data.getValue(String.class));
       // String data= data.get
        System.out.println("NEW TEMPERATURE  :  " +data.getValue());
        MonitorGui.infotext.setText(MonitorGui.infotext.getText() +"\n" +"NEW TEMPERATURE  :  " +data.getValue() + "\n");
        
        
        	for(Registration registration : RegistrationManager.registrations.values()) {
				if(registration.getEndpoint().equals("raspberry")) {
					if(Float.valueOf(data.getValue().toString())>0f) {
			        	//ExecuteRequest onled4 = new ExecuteRequest("3306/5850/1");
						WriteRequest onled4 = new WriteRequest(3306,0,5850,"1");
			        	 try {

								LwM2mResponse responseon= RegistrationManager.server.send(registration, onled4);
								System.out.println("device response: " + responseon);

							} catch (InterruptedException e) {

								e.printStackTrace();
							}
			        	 }
					else {
						 //ExecuteRequest offled4 = new ExecuteRequest("3306/5850/0");
						 WriteRequest offled4 = new WriteRequest(3306,0,5850,"0");
			        	 try {

								LwM2mResponse responseoff= RegistrationManager.server.send(registration, offled4);
								System.out.println("device response: " + responseoff);

							} catch (InterruptedException e) {

								e.printStackTrace();
							}
		        	 }
			        }
        }
       
		
    }
    

}
