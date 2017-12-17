import org.teleal.cling.UpnpService;
import org.teleal.cling.UpnpServiceImpl;
import org.teleal.cling.model.meta.LocalDevice;
import org.teleal.cling.model.meta.DeviceIdentity;
import org.teleal.cling.model.meta.DeviceDetails;
import org.teleal.cling.model.meta.DeviceService;
import org.teleal.cling.model.meta.LocalService;
import org.teleal.cling.model.types.DeviceType;
import org.teleal.cling.model.meta.ModelDetails;
import org.teleal.cling.model.meta.ManufacturerDetails;
import org.teleal.cling.model.ValidationException;
import org.teleal.cling.binding.LocalServiceBindingException;
import org.teleal.cling.binding.annotations.AnnotationLocalServiceBinder;
import org.teleal.cling.model.DefaultServiceManager;
import org.teleal.cling.model.types.UDN; // UUID
import org.teleal.cling.model.meta.Icon;
import java.io.IOException;

/**
 * Cling opens all accessible and usable NetworkInterfaces and uses them
 */

public class LaunchALPApp {
  public static void main(String[] args) {
    try {
      org.teleal.common.logging.LoggingUtil.loadDefaultConfiguration();
    } catch(java.io.IOException ex) {
      System.out.println("Failed to load logging file");
    }
   // if(args.length > 0 && args[0].equals("client")) {
    //  new TestClient().run();
    //} else if(args.length > 0 && args[0].equals("server")) {
      runServer();
    //} else {
      System.out.println("Usage: java LaunchALPApp");
      System.out.println("Running modes: \"server\" ");
     // System.exit(1);
    //}
  }
  
  // This method is almost copied exactly from the example documentation
  public static void runServer() {
    try {
      final UpnpService upnpService = new UpnpServiceImpl();
      Runtime.getRuntime().addShutdownHook(new Thread() {
        public void run() {
          upnpService.shutdown();
        }
      });
      // Add the bound local device to the registry
      upnpService.getRegistry().addDevice(createDevice());
    } catch (Exception ex) {
      System.err.println("Exception occured: " + ex);
      ex.printStackTrace(System.err);
      System.exit(1);
    }
  }
  
  //This method is almost copied exactly from the example documentation
  public static LocalDevice createDevice() throws
                ValidationException, LocalServiceBindingException, IOException {
    
    DeviceIdentity identity = new DeviceIdentity(
      UDN.uniqueSystemIdentifier("Test UPnP LaunchALPApp")
    );
    DeviceType type = new DeviceType("Server", "UPnPApp", 1);
    
    DeviceDetails details = new DeviceDetails(
      "UPnP LaunchALPApp ",
      new ManufacturerDetails("Server"),
      new ModelDetails("LaunchALPApp", "TV Application "+
                                      "using Cling library",
                                      "v1")
    );
    Icon icon = null;
    
    DeviceService<LocalService> deviceService =
      new AnnotationLocalServiceBinder().read(Server.class);
    
    deviceService.getService().setManager(
      new DefaultServiceManager(deviceService.getService(), Server.class)
    );
    
    return new LocalDevice(
            identity,
            type,
            details,
            null, //new Icon[]{icon}, // Or 'null' for no icons
            new DeviceService[]{deviceService}
    );
  }
}
