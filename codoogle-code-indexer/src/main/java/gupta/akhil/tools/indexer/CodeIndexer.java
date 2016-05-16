package gupta.akhil.tools.indexer;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.queryparser.classic.ParseException;

import gupta.akhil.tools.common.config.CVSConfig;
import gupta.akhil.tools.common.config.CodeIndexGroup;
import gupta.akhil.tools.common.config.ModuleSet;
import gupta.akhil.tools.common.config.SourceControlConfig;
import gupta.akhil.tools.common.config.SystemConfig;
import gupta.akhil.tools.common.utils.CVSClient;
import gupta.akhil.tools.common.utils.CVSClientFactory;
import gupta.akhil.tools.common.utils.FileUtils;
import gupta.akhil.tools.indexer.code.AsciiFileIndexer;
import gupta.akhil.tools.indexer.code.AsciiFileSearcher;
import gupta.akhil.tools.indexer.code.model.IndexInfo;

public class CodeIndexer {
	private static final Log logger = LogFactory.getLog(CodeIndexer.class);
	
	private static final CodeIndexer INSTANCE = new CodeIndexer();
	
	private CodeIndexer(){
	}
	
	public static CodeIndexer getInstance(){
		return INSTANCE;
	}
	
	public void refreshIndex() throws IOException, ParseException {
		System.setProperty("javacvs.multiple_commands_warning", "false");
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
						try{
							System.out.println("Refreshing module - " + module);
							IndexInfo indexInfo = getIndexInfo(indexGroup, module);
							if(indexInfo == null){
								System.out.println("Not indexed before, going for a full checkout.");
								cvsClient.checkoutModule(module, moduleSet.getScmBranch());
								System.out.println("Indexing module - " + module);
								AsciiFileIndexer.getInstance(indexGroup).indexFilesInFolder(sourceCodeLocation + '/' + module, codeIndexGroup.getIgnoredFiles());
								FileUtils.delete(new File(sourceCodeLocation + '/' + module));
							}else{
								System.out.println("Indexed before, going for a delta refresh.");
								String refreshStratDate = (new SimpleDateFormat("yyyy-MM-dd")).format(indexInfo.getLastIndexedOn());
								List<String> filesModified = cvsClient.getFilesModified(module, moduleSet.getScmBranch(), refreshStratDate);
								if(!filesModified.isEmpty()){
									cvsClient.checkoutFiles(filesModified, moduleSet.getScmBranch());
									System.out.println("Indexing module - " + module);
									AsciiFileIndexer.getInstance(indexGroup).indexFilesInFolder(sourceCodeLocation + '/' + module, codeIndexGroup.getIgnoredFiles());
									FileUtils.delete(new File(sourceCodeLocation + '/' + module));
								}
							}
							indexInfo = new IndexInfo(indexGroup, module, new Date());
							AsciiFileIndexer.getInstance(indexGroup).updateIndexInfo(indexInfo);
							AsciiFileIndexer.getInstance(indexGroup).close();
							AsciiFileSearcher.getInstance(indexGroup).reOpen();
						}catch(Exception e){
							logger.error("Error while indexing module - " + module, e);
						}
					}
				}
			}
		}
	}

	private IndexInfo getIndexInfo(String indexGroup, String module) {
		IndexInfo indexInfo = null;
		try{
			indexInfo = AsciiFileSearcher.getInstance(indexGroup).getIndexInfo(indexGroup, module);
		}catch(Exception e){
			e.printStackTrace();
		}
		return indexInfo;
	}
	
	public static void main(String[] args) throws IOException, ParseException {
		CodeIndexer.getInstance().getIndexInfo("branch_rel_16_10", "comm.lang");
	}
}
