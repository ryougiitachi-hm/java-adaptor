package per.itachi.java.adaptor.zookeeper.infra.svc.zk;

public enum RetryPolicyTypeEnum {

    /**
     * ExponentialBackoffRetry
     * */
    EXPONENTIAL_BACKOFF_RETRY,

    /**
     * BoundedExponentialBackoffRetry
     * */
    BOUNDED_EXPONENTIAL_BACKOFF_RETRY,

    /**
     * RetryUntilElapsed
     * */
    RETRY_UNTIL_ELAPSED,

    /**
     * RetryNTimes
     * */
    RETRY_N_TIMES,

    /**
     * RetryOneTime
     * */
    RETRY_ONE_TIME,

    /**
     * RetryForever
     * */
    RETRY_FOREVER,

    /**
     * SessionFailedRetryPolicy
     * */
    SESSION_FAILED_RETRY,
    ;
}
