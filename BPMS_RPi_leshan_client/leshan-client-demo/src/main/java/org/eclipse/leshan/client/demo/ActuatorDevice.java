package org.eclipse.leshan.client.demo;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.eclipse.leshan.client.resource.BaseInstanceEnabler;
import org.eclipse.leshan.core.model.ObjectModel;
import org.eclipse.leshan.core.node.LwM2mResource;
import org.eclipse.leshan.core.response.ExecuteResponse;
import org.eclipse.leshan.core.response.ReadResponse;
import org.eclipse.leshan.core.response.WriteResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.eclipse.leshan.client.demo.LedController.getLedStatus;

public class ActuatorDevice extends BaseInstanceEnabler {

    private static final Logger LOG = LoggerFactory.getLogger(ActuatorDevice.class);

    private static final int ON_OFF = 5850;

    private Date timestamp;

    public ActuatorDevice() {

    }

    @Override
    public ReadResponse read(int resourceid) {
        LOG.info("Read on Device Resource " + resourceid);
        switch (resourceid) {
            case ON_OFF:
                return ReadResponse.success(resourceid, LedController.getLedStatus());
            default:
                return super.read(resourceid);
        }
    }


    @Override
    public WriteResponse write(int resourceid, LwM2mResource value) {
        LOG.info("Write on Device Resource " + resourceid + " value " + value);
        switch (resourceid) {
            case ON_OFF:
                //return WriteResponse.notFound();
                boolean myVal = (boolean) value.getValue();
                if (myVal==true){
                    LedController.switchOnLed();
                }
                else if (myVal==false){
                    LedController.switchOffLed();
                }
		else{
			System.out.println("Error on Write");
		}
                fireResourcesChange(resourceid);
                return WriteResponse.success();

            default:
                return super.write(resourceid, value);
        }
    }

}
