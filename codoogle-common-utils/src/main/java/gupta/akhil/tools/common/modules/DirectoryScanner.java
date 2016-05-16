package gupta.akhil.tools.common.modules;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DirectoryScanner implements ModulesLister{
	private String scanDirectory;
	
	public DirectoryScanner(String scanDirectory) {
		this.scanDirectory = scanDirectory;
	}
	
	@Override
	public List<Module> getModules() {
		File file = new File(this.scanDirectory);
		List<Module> modules = new ArrayList<Module>();
		for(String moduleName: file.list()){
			Module module = new Module(moduleName);
			modules.add(module);
		}
		return modules;
	}
}
