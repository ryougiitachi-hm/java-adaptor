package per.itachi.java.adaptor.zookeeper.infra.svc.zk;

import java.util.concurrent.CountDownLatch;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

@Slf4j
public class DefaultWatcher implements Watcher {

    private final CountDownLatch countDownLatch;

    public DefaultWatcher(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void process(WatchedEvent event) {
        switch (event.getState()) {
            case SaslAuthenticated -> {
                log.info("The zookeeper client has event {} on path {}. ", event.getType(), event.getPath());
            }
            case SyncConnected -> {
                if (event.getType() == Event.EventType.None) {
                    log.info("The zookeeper client connected to successfully, state={}. ", event.getState());
                    countDownLatch.countDown();
                }
                else {
                    log.info("The zookeeper client has event {} on path {}. ", event.getType(), event.getPath());
                }
            }
            default -> log.info("The zookeeper client has state {}", event.getState());
        }
    }
}
