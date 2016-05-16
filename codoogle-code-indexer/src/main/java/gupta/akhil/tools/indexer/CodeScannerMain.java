package gupta.akhil.tools.indexer;

import java.io.File;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import gupta.akhil.tools.common.config.CVSConfig;
import gupta.akhil.tools.common.config.CodeIndexGroup;
import gupta.akhil.tools.common.config.ModuleSet;
import gupta.akhil.tools.common.config.SourceControlConfig;
import gupta.akhil.tools.common.config.SystemConfig;
import gupta.akhil.tools.common.utils.CVSClient;
import gupta.akhil.tools.common.utils.CVSClientFactory;
import gupta.akhil.tools.common.utils.FileUtils;
import gupta.akhil.tools.indexer.code.AsciiFileIndexer;

public class CodeScannerMain {
	private static final Log logger = LogFactory.getLog(CodeScannerMain.class);

	public static void main(String[] args) {
		List<CodeIndexGroup> codeIndexGroups = SystemConfig.getCodeIndexGroups();
		for(CodeIndexGroup codeIndexGroup: codeIndexGroups){
			String indexGroup = codeIndexGroup.getIndexGroup();
			String sourceCodeLocation = codeIndexGroup.getCheckoutLocation();
			
			for(ModuleSet moduleSet: codeIndexGroup.getModuleConfigurations()){
				SourceControlConfig sourceControlConfig = moduleSet.getSourceControlConfig();
				if(sourceControlConfig instanceof CVSConfig){
					CVSConfig cvsConfig = (CVSConfig)sourceControlConfig;
					CVSClient cvsClient = CVSClientFactory.getClient(cvsConfig.getRoot(), cvsConfig.getPassword());
					cvsClient.setLocalPath(codeIndexGroup.getCheckoutLocation());
					for(String module : moduleSet.getModuleList()){
						List<String> filesModified = cvsClient.getFilesModified(module, moduleSet.getScmBranch(), "2016-01-01");
						cvsClient = CVSClientFactory.getClient(cvsConfig.getRoot(), cvsConfig.getPassword());
						cvsClient.setLocalPath(codeIndexGroup.getCheckoutLocation());
						cvsClient.checkoutFiles(filesModified, moduleSet.getScmBranch());
						try{
							if(!filesModified.isEmpty()){
								logger.info("Indexing module - " + module);
								AsciiFileIndexer.getInstance(indexGroup).indexFilesInFolder(sourceCodeLocation + '/' + module, codeIndexGroup.getIgnoredFiles());
								FileUtils.delete(new File(sourceCodeLocation + '/' + module));
							}
						}catch(Exception e){
							logger.error("Error while indexing module - " + module, e);
						}
					}
				}
			}
		}
	}
}
