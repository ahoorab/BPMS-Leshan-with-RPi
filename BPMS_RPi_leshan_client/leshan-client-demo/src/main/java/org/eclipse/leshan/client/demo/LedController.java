package org.eclipse.leshan.client.demo;
import com.pi4j.io.gpio.*;
import org.eclipse.leshan.core.response.ExecuteResponse;

public class LedController {
	
	public LedController()
	{
	
	}
	
	public static ExecuteResponse switchOnLed() {
		/** create gpio controller */
		final GpioController gpio = GpioFactory.getInstance();

		final GpioPinDigitalOutput ledPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00 ,"LED_4", PinState.HIGH);
		gpio.shutdown();
		gpio.unprovisionPin(ledPin);

		return ExecuteResponse.success();

	}
		
	public static ExecuteResponse switchOffLed() {
		/** create gpio controller */
		final GpioController gpio = GpioFactory.getInstance();

		final GpioPinDigitalOutput ledPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00 ,"LED_4", PinState.LOW);
		gpio.shutdown();
		gpio.unprovisionPin(ledPin);
		return ExecuteResponse.success();

	}

	public static boolean getLedStatus(){
        	final GpioController gpio = GpioFactory.getInstance();

        	final boolean ledPinStatus = (boolean) gpio.isHigh((GpioPinDigital) RaspiPin.GPIO_00);
	    	return ledPinStatus;
	     	//return false;
    }

}




/*mvn install:install-file -Dfile=/opt/pi4j/lib/pi4j-core.jar -DgroupId=com.pi4j -DartifactId=pi4j-core -Dversion=1.2-SNAPSHOT -Dpackaging=jar -DgeneratePom=true
*/
