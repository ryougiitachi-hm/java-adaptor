package per.itachi.java.adaptor.zookeeper.infra.svc.zk;

import org.apache.curator.RetryPolicy;
import org.apache.curator.retry.BoundedExponentialBackoffRetry;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryForever;
import org.springframework.stereotype.Component;

@Component
public class DefaultRetryPolicyFactory implements RetryPolicyFactory {

    @Override
    public RetryPolicy createRetryPolicy(RetryPolicyTypeEnum retryPolicyType, ZookeeperProperties zookeeperProperties) {
        return switch (retryPolicyType) {
            case EXPONENTIAL_BACKOFF_RETRY ->
//                    new ExponentialBackoffRetry(zookeeperProperties.getRetryExponentialBaseSleepMs(),
//                            zookeeperProperties.getRetryExponentialMaxRetries());
                    new ExponentialBackoffRetry(zookeeperProperties.getRetryExponentialBaseSleepMs(),
                            zookeeperProperties.getRetryExponentialMaxRetries(),
                            zookeeperProperties.getRetryExponentialMaxSleepMs());
            case BOUNDED_EXPONENTIAL_BACKOFF_RETRY ->
                    new BoundedExponentialBackoffRetry(zookeeperProperties.getRetryExponentialBaseSleepMs(),
                            zookeeperProperties.getRetryExponentialMaxRetries(),
                            zookeeperProperties.getRetryExponentialMaxSleepMs());
            default -> new RetryForever(zookeeperProperties.getRetryForeverIntervalMs());
        };
    }
}
