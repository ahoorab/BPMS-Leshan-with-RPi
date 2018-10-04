package org.eclipse.leshan.client.demo;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.leshan.client.resource.BaseInstanceEnabler;
import org.eclipse.leshan.core.node.LwM2mResource;
import org.eclipse.leshan.core.response.ExecuteResponse;
import org.eclipse.leshan.core.response.ReadResponse;
import org.eclipse.leshan.core.response.WriteResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import apt.lab.rpi.tempsensor.TempSensor;

public class TempSensorDevice extends BaseInstanceEnabler {

    private static final Logger LOG = LoggerFactory.getLogger(TempSensorDevice.class);
    private static final int SENSOR_VALUE = 5700;
    private static final int MAX_MEASURED_VALUE = 5602;
    private static final int MIN_MEASURED_VALUE = 5601;
    private static final Random RANDOM = new Random();
    TempSensor sensor;
    //float sensor;
    public TempSensorDevice() {
        // notify new date each 5 second
        //sensor = (float) Math.random();
    	sensor = new TempSensor();
        Timer timer = new Timer("Device-Current Time");
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                fireResourcesChange(5700);
            }
        }, 5000, 5000);
    }

    @Override
    public ReadResponse read(int resourceid) {
        LOG.info("Read on Device Resource " + resourceid);
        switch (resourceid) {
        case SENSOR_VALUE:
            return ReadResponse.success(resourceid, getTemperature());
        case MIN_MEASURED_VALUE:
            return ReadResponse.success(resourceid, getMaxTemperature());
        case MAX_MEASURED_VALUE:
            return ReadResponse.success(resourceid, getMinTemperature());
        default:
            return super.read(resourceid);
        }
    }

    /*@Override
    public ExecuteResponse execute(int resourceid, String params) {
        LOG.info("Execute on Device resource " + resourceid);
        switch (resourceid) {
        case 2:
            return LedController.switchOnLed();
        case 3:
            return LedController.switchOffLed();
        default:
            return execute(resourceid, params);
        }

    }

    @Override
    public WriteResponse write(int resourceid, LwM2mResource value) {
        LOG.info("Write on Device Resource " + resourceid + " value " + value);
        switch (resourceid) {
        case 5850:
            //return WriteResponse.notFound();
            String myVal = (String) value.getValue();
            if (myVal=="1"){
                LedController.switchOnLed();
            }
            else if (myVal=="0"){
                LedController.switchOffLed();
            }
            fireResourcesChange(resourceid);
            return WriteResponse.success();
       
        default:
            return super.write(resourceid, value);
        }
    }*/

 
    
    private float getTemperature() {
    	try {
  			System.out.println("TEMP   :   " + this.sensor.getCelcius());
  			//sensor.close();
  			return this.sensor.getCelcius();

            /*System.out.println("TEMP   :   " + sensor);
  		    return sensor;*/
		}
		catch(Throwable throwable) {
            throw new RuntimeException(throwable.getMessage(), throwable.getCause());
        }
    }

    private float getMaxTemperature() {
        try {
            System.out.println("MAX TEMP   :   " + sensor);
            return 30f;
        }
        catch(Throwable throwable) {
            throw new RuntimeException(throwable.getMessage(), throwable.getCause());
        }
    }

    private float getMinTemperature() {
        try {
            System.out.println("MIN TEMP   :   " + sensor);
            return 20f;
        }
        catch(Throwable throwable) {
            throw new RuntimeException(throwable.getMessage(), throwable.getCause());
        }
    }

}