package gupta.akhil.tools.wls.deploy.runtime;

import javax.management.ObjectName;

import gupta.akhil.tools.common.config.DomainConfig;

public class TransactionRuntime extends MBeanProxy{
	public TransactionRuntime(ObjectName objectName, DomainConfig domainConfig){
		super(objectName, domainConfig);
	}
	
	public long getTotalCommittedCount(){
		return (Long)getAttribute("TransactionsCommittedTotalCount");
	}

	public long getTotalTimedOutCount(){
		return (Long)getAttribute("TransactionsTimedOutTotalCount");
	}

	public long getTotalRolledBackCount(){
		return (Long)getAttribute("TransactionsRolledBackTotalCount");
	}
}
