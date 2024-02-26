package per.itachi.java.adaptor.zookeeper.infra.svc.zk;

import org.apache.curator.RetryPolicy;

public interface RetryPolicyFactory {

    RetryPolicy createRetryPolicy(RetryPolicyTypeEnum retryPolicyType, ZookeeperProperties zookeeperProperties);
}
