package gupta.akhil.tools.common.utils;

import org.netbeans.lib.cvsclient.CVSRoot;
import org.netbeans.lib.cvsclient.Client;
import org.netbeans.lib.cvsclient.admin.StandardAdminHandler;
import org.netbeans.lib.cvsclient.command.GlobalOptions;
import org.netbeans.lib.cvsclient.commandLine.BasicListener;
import org.netbeans.lib.cvsclient.connection.PServerConnection;
import org.netbeans.lib.cvsclient.connection.StandardScrambler;

public class CVSClientFactory {
	
	public static CVSClient getClient(String cvsRoot, String password){
		CVSClient wrappedClient = null;
        try {
            PServerConnection cvsConnection = new PServerConnection(CVSRoot.parse(cvsRoot));
            cvsConnection.setEncodedPassword(StandardScrambler.getInstance().scramble(password));
            cvsConnection.open();
            
            Client client = new Client(cvsConnection, new StandardAdminHandler());
            client.getEventManager().addCVSListener(new BasicListener());

            GlobalOptions globalOptions = new GlobalOptions();
            globalOptions.setCVSRoot(cvsRoot);
            globalOptions.setUseGzip(true);
            globalOptions.setVeryQuiet(true);
            wrappedClient = new CVSClient(client, globalOptions);
        } catch (Exception e) {
        	e.printStackTrace();
            throw new RuntimeException("Unable to create client..");
        }
        return wrappedClient;
    }
	
}
