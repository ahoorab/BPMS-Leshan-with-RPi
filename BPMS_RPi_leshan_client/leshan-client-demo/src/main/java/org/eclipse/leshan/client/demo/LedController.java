package org.eclipse.leshan.client.demo;
import com.pi4j.io.gpio.*;
import org.eclipse.leshan.core.response.ExecuteResponse;
import com.pi4j.wiringpi.Gpio;

public class LedController {

	public static boolean ledstate = false;
		
	public LedController()
	{
	
	}
	
	public static ExecuteResponse switchOnLed() {
		/** create gpio controller */
		final GpioController gpio = GpioFactory.getInstance();
		final GpioPinDigitalOutput ledPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00 ,"LED_4", PinState.HIGH);
		gpio.shutdown();
		gpio.unprovisionPin(ledPin);
		ledstate = true;
		return ExecuteResponse.success();

	}
		
	public static ExecuteResponse switchOffLed() {
		/** create gpio controller */
		final GpioController gpio = GpioFactory.getInstance();
		final GpioPinDigitalOutput ledPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00 ,"LED_4", PinState.LOW);
		gpio.shutdown();
		gpio.unprovisionPin(ledPin);
		ledstate = false;
		return ExecuteResponse.success();

	}

	public static boolean getLedStatus(){
		return ledstate;
        
    }

}




