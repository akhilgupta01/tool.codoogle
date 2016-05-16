package gupta.akhil.tools.common.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.netbeans.lib.cvsclient.Client;
import org.netbeans.lib.cvsclient.command.GlobalOptions;
import org.netbeans.lib.cvsclient.command.checkout.CheckoutCommand;
import org.netbeans.lib.cvsclient.command.log.LogInformation;
import org.netbeans.lib.cvsclient.command.log.RlogCommand;
import org.netbeans.lib.cvsclient.event.FileInfoEvent;


public class CVSClient {
	private Client client;
	
	private String localPath;
	
	private GlobalOptions globalOptions;
	
	CVSClient(Client client, GlobalOptions globalOptions) {
		this.client = client;
		this.globalOptions = globalOptions;
	}
	
	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}
	
	public String getLocalPath() {
		return localPath;
	}
	
	private List<String> trimLeadingSlash(List<String> fileNames){
		List<String> trimmedStrings = new ArrayList<String>();
		for(String fileName: fileNames){
			fileName = trimLeadingSlash(fileName);
			trimmedStrings.add(fileName);
		}
		return trimmedStrings;
	}

	private String trimLeadingSlash(String fileName) {
		if (fileName.indexOf("/") == 0) {
			fileName = fileName.substring(1);
		}
		return fileName;
	}
	
	public void checkoutFile(String fileName, String revision) {
		checkoutFiles(Arrays.asList(new String[]{fileName}), revision);
	}
	
	public void checkoutModule(String moduleName, String revision) {
		try {
			CheckoutCommand command = new CheckoutCommand();
			command.setBuilder(null);
			command.setRecursive(true);
			command.setModules(new String[]{trimLeadingSlash(moduleName)});
			if (!StringUtils.isBlank(revision)) {
				command.setCheckoutByRevision(revision);
			}
			command.setPruneDirectories(true);
			client.setLocalPath(getLocalPath());
			client.executeCommand(command, globalOptions);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void checkoutModules(String[] moduleNames, String revision) {
		try {
			for(int i=0; i<moduleNames.length; i++){
				moduleNames[i] = trimLeadingSlash(moduleNames[i]);
			}
			CheckoutCommand command = new CheckoutCommand();
			command.setBuilder(null);
			command.setRecursive(true);
			command.setModules(moduleNames);
			if (!StringUtils.isBlank(revision)) {
				command.setCheckoutByRevision(revision);
			}
			command.setPruneDirectories(true);
			client.setLocalPath(getLocalPath());
			client.executeCommand(command, globalOptions);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void checkoutFiles(List<String> fileNames, String revision) {
		try {
			String[] fileNamesArray = new String[fileNames.size()];
			CheckoutCommand command = new CheckoutCommand();
			command.setBuilder(null);
			command.setRecursive(false);
			command.setModules(trimLeadingSlash(fileNames).toArray(fileNamesArray));

			if (!StringUtils.isBlank(revision)) {
				command.setCheckoutByRevision(revision);
			}

			command.setPruneDirectories(true);
			client.setLocalPath(getLocalPath());
			client.executeCommand(command, globalOptions);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<String> getFilesModified(String moduleName, String revision, String sinceDate) {
		final String repositoryPath = client.getRepository();
		System.out.println(repositoryPath);
		final List<String> modifiedFiles = new ArrayList<String>();
		try {
			RlogCommand info = new RlogCommand(){
				@Override
				public void fileInfoGenerated(FileInfoEvent paramFileInfoEvent) {
					LogInformation logInformation = (LogInformation)paramFileInfoEvent.getInfoContainer();
					String rcsFile = logInformation.getRepositoryFilename(); 
					modifiedFiles.add(rcsFile.substring(repositoryPath.length(), rcsFile.length() - 2));
				}
			};
			info.setModule(moduleName);
			info.setRevisionFilter(revision);
			info.setRecursive(true);
			info.setDateFilter(">" + sinceDate);
			info.setHeaderOnly(true);
			info.setNoTags(true);
			info.setSuppressHeader(true);
			
			client.setLocalPath(getLocalPath());
			globalOptions.setVeryQuiet(true);
			globalOptions.setModeratelyQuiet(true);
			System.out.println(info.getCVSCommand());
			client.executeCommand(info, globalOptions);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return modifiedFiles;
	}
}
