package gupta.akhil.tools.vcs;

import gupta.akhil.tools.common.utils.CVSClient;
import gupta.akhil.tools.common.utils.CVSClientFactory;

public class Tester {
	public static void main(String[] args) {
		CVSClient client = CVSClientFactory.getClient("cvsroot", "cvspassword");
		client.setLocalPath("checkout directory");
	}
}
