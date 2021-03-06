package org.eclipse.leshan.client.demo;

import java.util.Date;
import org.eclipse.leshan.client.resource.BaseInstanceEnabler;
import org.eclipse.leshan.core.node.LwM2mResource;
import org.eclipse.leshan.core.response.ReadResponse;
import org.eclipse.leshan.core.response.WriteResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LedActuator extends BaseInstanceEnabler {

    private static final Logger LOG = LoggerFactory.getLogger(LedActuator.class);

    private static final int ON_OFF = 5850;

    private Date timestamp;

    public LedActuator() {
    }

    @Override
    public ReadResponse read(int resourceid) {
        LOG.info("Read on Device Resource " + resourceid);
        switch (resourceid) {
            case ON_OFF:
                return ReadResponse.success(resourceid, LedSwitch.getLedStatus());
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
                    LedSwitch.switchOnLed();
                }
                else if (myVal==false){
                    LedSwitch.switchOffLed();
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
