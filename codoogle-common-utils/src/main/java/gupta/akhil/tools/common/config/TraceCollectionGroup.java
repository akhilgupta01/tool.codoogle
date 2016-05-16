package gupta.akhil.tools.common.config;

import java.util.ArrayList;
import java.util.List;

public class TraceCollectionGroup {
	private String id;
	private String indexGroup;
	private long interval;
	private long duration;
	
	private List<DomainConfig> domainConfigurations = new ArrayList<DomainConfig>();
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIndexGroup() {
		return indexGroup;
	}

	public void setIndexGroup(String indexGroup) {
		this.indexGroup = indexGroup;
	}

	public long getInterval() {
		return interval;
	}
	
	public void setInterval(long interval) {
		this.interval = interval;
	}
	
	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public void setDomainConfigurations(List<DomainConfig> domainConfigurations) {
		this.domainConfigurations = domainConfigurations;
	}
	
	public List<DomainConfig> getDomainConfigurations() {
		return domainConfigurations;
	}

	@Override
	public String toString() {
		return "TraceCollectionGroup [id=" + id + ", indexGroup=" + indexGroup + ", interval=" + interval
				+ ", duration=" + duration + ", domainConfigurations=" + domainConfigurations + "]";
	}
	
	
}
