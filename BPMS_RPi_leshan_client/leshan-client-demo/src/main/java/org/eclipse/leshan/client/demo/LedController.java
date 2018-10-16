package org.eclipse.leshan.client.demo;
import com.pi4j.io.gpio.*;
import org.eclipse.leshan.core.response.ExecuteResponse;
import com.pi4j.wiringpi.Gpio;

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

        Gpio.pinMode( 17,Gpio.INPUT);

        if (Gpio.digitalRead(17) == 0){
            System.out.println("LED is  not switched off");
            return false;
        }else{
            System.out.println("LED is switched on");
            return true;
        }
    }


}




/*mvn install:install-file -Dfile=/opt/pi4j/lib/pi4j-core.jar -DgroupId=com.pi4j -DartifactId=pi4j-core -Dversion=1.2-SNAPSHOT -Dpackaging=jar -DgeneratePom=true
*/
