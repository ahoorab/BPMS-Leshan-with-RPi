package org.eclipse.leshan.client.demo;

import java.util.*;
import apt.lab.rpi.tempsensor.TempSensor;
import org.eclipse.leshan.client.resource.BaseInstanceEnabler;
import org.eclipse.leshan.core.response.ReadResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TemperatureSensor extends BaseInstanceEnabler {

    private static final Logger LOG = LoggerFactory.getLogger(TemperatureSensor.class);
    private static final int SENSOR_VALUE = 5700;
    private static final int MAX_MEASURED_VALUE = 5602;
    private static final int MIN_MEASURED_VALUE = 5601;
    private static final Random RANDOM = new Random();
    private static final ArrayList<Float> tempValueList = new ArrayList();
    TempSensor sensor;
    //float sensor;
    public TemperatureSensor() {
        // notify new date each 5 second
        //sensor = (float) Math.random();
    	sensor = new TempSensor();
        Timer timer = new Timer("Device-Current Time");
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                fireResourcesChange(5700);
		fireResourcesChange(5602);
		fireResourcesChange(5601);
            }
        }, 5000, 5000);
    }

    @Override
    public ReadResponse read(int resourceid) {
        LOG.info("Read on Device Resource " + resourceid);
        switch (resourceid) {
        case SENSOR_VALUE:
            return ReadResponse.success(resourceid, getTemperature());
        case MAX_MEASURED_VALUE:
            return ReadResponse.success(resourceid, getMaxTemperature());
        case MIN_MEASURED_VALUE:
            return ReadResponse.success(resourceid, getMinTemperature());
        default:
            return super.read(resourceid);
        }
    }


    private float getTemperature() {
    	try {
	        System.out.println("TEMP   :   " + this.sensor.getCelcius());
            tempValueList.add(this.sensor.getCelcius());
  	        return this.sensor.getCelcius();
	    //System.out.println("TEMP   :   " + sensor);
	    //tempValueList.add(sensor);
  	    //return sensor;
	}
	catch(Throwable throwable) {
            throw new RuntimeException(throwable.getMessage(), throwable.getCause());
        }
    }

    private float getMaxTemperature() {
        try {
            System.out.println("MAX TEMP   :   " +  Collections.max(tempValueList));
            return Collections.max(tempValueList);
        }
        catch(Throwable throwable) {
            throw new RuntimeException(throwable.getMessage(), throwable.getCause());
        }
    }

    private float getMinTemperature() {
        try {
            System.out.println("MIN TEMP   :   " +  Collections.min(tempValueList));
            return Collections.min(tempValueList);
        }
        catch(Throwable throwable) {
            throw new RuntimeException(throwable.getMessage(), throwable.getCause());
        }
    }

}
