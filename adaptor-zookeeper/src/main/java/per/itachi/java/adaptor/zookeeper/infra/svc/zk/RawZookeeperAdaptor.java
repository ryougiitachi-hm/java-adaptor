package per.itachi.java.adaptor.zookeeper.infra.svc.zk;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import per.itachi.java.adaptor.common.app.exception.AppException;
import per.itachi.java.adaptor.common.app.port.ZookeeperPort;

@Slf4j
@Component
public class RawZookeeperAdaptor implements ZookeeperPort {

    private final ZooKeeper zkClient;

    @Autowired
    public RawZookeeperAdaptor(ZookeeperProperties zookeeperProperties) {
        try {
            CountDownLatch countDownLatch = new CountDownLatch(1);
            Watcher watcher = new DefaultWatcher(countDownLatch);
            ZooKeeper zkClient = new ZooKeeper(
                    zookeeperProperties.getConnectionString(),
                    zookeeperProperties.getSessionTimeoutMs(),
                    watcher);
            countDownLatch.await();
            this.zkClient = zkClient;
        } catch (IOException | InterruptedException e) {
            log.error("Error occurred when initializing zk client to {}", zookeeperProperties.getConnectionString(), e);
            throw new AppException(String.format("Error occurred when initializing zk client to %s",
                    zookeeperProperties.getConnectionString()));
        }

    }

    @Override
    public void connect() {
    }

    @Override
    public void disconnect() {
    }

    @Override
    public void list(String path) {
    }

    @Override
    public void createNode(String path) {
    }

    @Override
    public void deleteNode(String path) {
    }

    @Override
    public void getNodeValue(String path) {
    }

    @Override
    public void setNodeValue(String path, String value) {
    }
}
