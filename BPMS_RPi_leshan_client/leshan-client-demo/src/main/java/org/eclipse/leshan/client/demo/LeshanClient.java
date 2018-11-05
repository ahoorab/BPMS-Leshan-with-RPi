package org.eclipse.leshan.client.demo;

import static org.eclipse.leshan.LwM2mId.*;
import static org.eclipse.leshan.client.object.Security.*;

import java.io.File;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.eclipse.californium.core.network.config.NetworkConfig;
import org.eclipse.leshan.LwM2m;
import org.eclipse.leshan.client.californium.LeshanClientBuilder;
import org.eclipse.leshan.client.object.Server;
import org.eclipse.leshan.client.resource.LwM2mObjectEnabler;
import org.eclipse.leshan.client.resource.ObjectsInitializer;
import org.eclipse.leshan.core.model.LwM2mModel;
import org.eclipse.leshan.core.model.ObjectLoader;
import org.eclipse.leshan.core.model.ObjectModel;
import org.eclipse.leshan.core.request.BindingMode;
import org.eclipse.leshan.util.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LeshanClient {

    private static final Logger LOG = LoggerFactory.getLogger(LeshanClient.class);
    private final static String[] modelPaths = new String[] { "BPMS_objects.json" };
    private static final int OBJECT_ID_TEMPERATURE_SENSOR = 3303;
    private static final int OBJECT_ID_ACTUATOR = 3306;
    private final static String DEFAULT_ENDPOINT = "LeshanClient";
    private final static String USAGE = "java -jar leshan-client-demo.jar [OPTION]\n\n";

    public static void main(final String[] args) {

        // Define options for command line tools
        Options options = new Options();

        options.addOption("h", "help", false, "Display help information.");
        options.addOption("n", true, String.format(
                "Set the endpoint name of the Client.\nDefault: the local hostname or '%s' if any.", DEFAULT_ENDPOINT));
        options.addOption("b", false, "If present use bootstrap.");
        options.addOption("lh", true, "Set the local CoAP address of the Client.\n  Default: any local address.");
        options.addOption("lp", true,
                "Set the local CoAP port of the Client.\n  Default: A valid port value is between 0 and 65535.");
        options.addOption("u", true, String.format("Set the LWM2M or Bootstrap server URL.\nDefault: localhost:%d.",
                LwM2m.DEFAULT_COAP_PORT));
        options.addOption("i", true, "Set the LWM2M or Bootstrap server PSK identity in ascii.");
        options.addOption("cpubk", true,
                "The path to your client public key file.\n The public Key should be in SubjectPublicKeyInfo format (DER encoding).");
        options.addOption("cprik", true,
                "The path to your client private key file.\nThe private key should be in PKCS#8 format (DER encoding).");
        options.addOption("spubk", true,
                "The path to your server public key file.\n The public Key should be in SubjectPublicKeyInfo format (DER encoding).");
	 options.addOption("ip", true, "The server ip address");


        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(90);
        formatter.setOptionComparator(null);

        // Parse arguments
        CommandLine cl;
        try {
            cl = new DefaultParser().parse(options, args);
        } catch (ParseException e) {
            System.err.println("Parsing failed.  Reason: " + e.getMessage());
            formatter.printHelp(USAGE, options);
            return;
        }

        // Print help
        if (cl.hasOption("help")) {
            formatter.printHelp(USAGE, options);
            return;
        }

        // Abort if unexpected options
        if (cl.getArgs().length > 0) {
            System.err.println("Unexpected option or arguments : " + cl.getArgList());
            formatter.printHelp(USAGE, options);
            return;
        }

        // Abort if PSK config is not complete
        if ((cl.hasOption("i") && !cl.hasOption("p")) || !cl.hasOption("i") && cl.hasOption("p")) {
            System.err
                    .println("You should precise identity (-i) and Pre-Shared-Key (-p) if you want to connect in PSK");
            formatter.printHelp(USAGE, options);
            return;
        }

        // Abort if all RPK config is not complete
        if (cl.hasOption("cpubk") || cl.hasOption("cprik") || cl.hasOption("spubk")) {
            if (!cl.hasOption("cpubk") || !cl.hasOption("cprik") || !cl.hasOption("spubk")) {
                System.err.println("cpubk, cprik and spubk should be used together to connect using RPK");
                formatter.printHelp(USAGE, options);
                return;
            }
        }

        // Get endpoint name
        String endpoint;
        if (cl.hasOption("n")) {
            endpoint = cl.getOptionValue("n");
        } else {
            try {
                endpoint = InetAddress.getLocalHost().getHostName();
            } catch (UnknownHostException e) {
                endpoint = DEFAULT_ENDPOINT;
            }
        }

        // Get server URI
        String serverURI;
        if (cl.hasOption("u")) {
            if (cl.hasOption("i") || cl.hasOption("cpubk"))
                serverURI = "coaps://" + cl.getOptionValue("u");
            else
                serverURI = "coap://" + cl.getOptionValue("u");
        } else {
            if (cl.hasOption("i") || cl.hasOption("cpubk"))
                serverURI = "coaps://localhost:" + LwM2m.DEFAULT_COAP_SECURE_PORT;
            else
                serverURI = "coap://localhost:" + LwM2m.DEFAULT_COAP_PORT;
        }

	  if (cl.hasOption("ip")) {
	      String serverIP = cl.getOptionValue("ip").toString();
		System.out.println(" \n \n \n \n \n \n \n"+ serverIP + " \n \n \n \n \n \n");
		 serverURI ="coap://" +serverIP+":"+ LwM2m.DEFAULT_COAP_PORT;

	  }

        // get PSK info
        byte[] pskIdentity = null;
        byte[] pskKey = null;
        if (cl.hasOption("i")) {
            pskIdentity = cl.getOptionValue("i").getBytes();
            pskKey = Hex.decodeHex(cl.getOptionValue("p").toCharArray());
        }

        // get local address
        String localAddress = null;
        int localPort = 0;
        if (cl.hasOption("lh")) {
            localAddress = cl.getOptionValue("lh");
        }
        if (cl.hasOption("lp")) {
            localPort = Integer.parseInt(cl.getOptionValue("lp"));
        }


        createAndStartClient(endpoint, localAddress, localPort, cl.hasOption("b"), serverURI, pskIdentity, pskKey);
    }

    public static void createAndStartClient(String endpoint, String localAddress, int localPort, boolean needBootstrap,
                                            String serverURI, byte[] pskIdentity, byte[] pskKey) {


        // Initialize model
        InputStream defaultSpec = LeshanClient.class.getResourceAsStream("/models/oma-objects-spec.json");
        InputStream bpmSpec = LeshanClient.class.getResourceAsStream("/models/BPMS_objects.json");
        List<ObjectModel> models = ObjectLoader.loadJsonStream(defaultSpec);
        models.addAll(ObjectLoader.loadJsonStream(bpmSpec));


        // Initialize object list
        ObjectsInitializer initializer = new ObjectsInitializer(new LwM2mModel(models));
        if (needBootstrap) {
            if (pskIdentity != null) {
                initializer.setInstancesForObject(SECURITY, pskBootstrap(serverURI, pskIdentity, pskKey));
                initializer.setClassForObject(SERVER, Server.class);
            } else {
                initializer.setInstancesForObject(SECURITY, noSecBootstap(serverURI));
                initializer.setClassForObject(SERVER, Server.class);
            }
        } else {
            if (pskIdentity != null) {
                initializer.setInstancesForObject(SECURITY, psk(serverURI, 123, pskIdentity, pskKey));
                initializer.setInstancesForObject(SERVER, new Server(123, 30, BindingMode.U, false));
            } else {
                initializer.setInstancesForObject(SECURITY, noSec(serverURI, 123));
                initializer.setInstancesForObject(SERVER, new Server(123, 30, BindingMode.U, false));
            }
        }

        initializer.setClassForObject(OBJECT_ID_TEMPERATURE_SENSOR, TemperatureSensor.class);
        initializer.setInstancesForObject(OBJECT_ID_TEMPERATURE_SENSOR, new TemperatureSensor());
        initializer.setClassForObject(OBJECT_ID_ACTUATOR, LedActuator.class);
        initializer.setInstancesForObject(OBJECT_ID_ACTUATOR, new LedActuator());
        List<LwM2mObjectEnabler> enablers = initializer.create(SECURITY, SERVER,
                OBJECT_ID_TEMPERATURE_SENSOR, OBJECT_ID_ACTUATOR);

        // Create CoAP Config
        NetworkConfig coapConfig;
        File configFile = new File(NetworkConfig.DEFAULT_FILE_NAME);
        if (configFile.isFile()) {
            coapConfig = new NetworkConfig();
            coapConfig.load(configFile);
        } else {
            coapConfig = LeshanClientBuilder.createDefaultNetworkConfig();
            coapConfig.store(configFile);
        }

        // Create client
        LeshanClientBuilder builder = new LeshanClientBuilder(endpoint);
        builder.setLocalAddress(localAddress, localPort);
        builder.setObjects(enablers);
        builder.setCoapConfig(coapConfig);
        final org.eclipse.leshan.client.californium.LeshanClient client = builder.build();

        // Start the client
        client.start();

        // De-register on shutdown and stop client.
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                client.destroy(true); // send de-registration request before destroy
            }
        });

    }
}