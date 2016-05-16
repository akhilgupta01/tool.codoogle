package gupta.akhil.tools.wls.tda;

import junit.framework.Assert;

import org.junit.Test;

import gupta.akhil.tools.wls.tda.MergedCallGraph;

public class MergedCallGraphTest {
	@Test
	public void testParseGraph() throws Exception{
		String testGraph = 
			"com.fullgc.customer.pctmgt.PctvWebServiceWrapper.getCustomerLinkInfoByIdentifier(PctvWebServiceWrapper.java:33)\n" +
			" com.fullgc.customer.pctmgt.PctvWebServiceProvider.getCustomerLinkInfoByIdentifier(PctvWebServiceProvider.java:102)\n" +
			"  com.fullgc.customer.pctmgt.PctvServiceHelper.getCustomerLinkInfoByIdentifier(PctvServiceHelper.java:197)\n" +
			"   com.fullgc.afe.product.ProductServiceProvider_1mpodda_ProductServiceImpl_1036_WLStub.getCustomerLinkInfoByIdentifier(Unknown Source)\n" +
			"    com.fullgc.afe.product.ProductServiceProvider_mpodda_ProductServiceImpl.getCustomerLinkInfoByIdentifier(Unknown Source)\n" +
			"     com.fullgc.afe.product.ProductServiceProvider.getCustomerLinkInfoByIdentifier(ProductServiceProvider.java:2600)\n" +
			"      com.fullgc.afe.product.PackageWrapperFacade_oixonx_EOImpl.getCustomerLinkInfoByIdentifier(Unknown Source)\n" + 
			"   com.fullgc.afe.product.ProductServiceProvider_2mpodda_ProductServiceImpl_1036_WLStub.getCustomerLinkInfoByIdentifier(Unknown Source)\n" +
			"    com.fullgc.afe.product.ProductServiceProvider_mpodda_ProductServiceImpl.getCustomerLinkInfoByIdentifier(Unknown Source)\n" +
			"     com.fullgc.afe.product.ProductServiceProvider.getCustomerLinkInfoByIdentifier(ProductServiceProvider.java:2600)\n" +
			"      com.fullgc.afe.product.PackageWrapperFacade_oixonx_EOImpl.getCustomerLinkInfoByIdentifier(Unknown Source)";
		MergedCallGraph parsedGraph = MergedCallGraph.parse(testGraph);
		Assert.assertEquals(testGraph, parsedGraph.getCallGraph());
	}
}
