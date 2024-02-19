package per.itachi.java.adaptor.zookeeper.infra.svc.zk;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import javax.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import per.itachi.java.adaptor.common.app.exception.AppException;
import per.itachi.java.adaptor.common.app.port.ZookeeperPort;

/**
 * TODO: how to reconnect to zookeeper if expired due to session timeout.
 * */
@Slf4j
@Component
public class RawZookeeperAdaptor implements ZookeeperPort {

    /**
     * Just for logging.
     * */
    private final ZookeeperProperties zookeeperProperties;

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
            this.zookeeperProperties = zookeeperProperties;
            this.zkClient = zkClient;
        } catch (IOException | InterruptedException e) {
            log.error("Error occurred when initializing zk client to {}", zookeeperProperties.getConnectionString(), e);
            throw new AppException(String.format("Error occurred when initializing zk client to %s",
                    zookeeperProperties.getConnectionString()));
        }

    }

    @Override
    public void connect() {
        // not sure needful or not
    }

    @Override
    public void disconnect() {
        // not sure needful or not
    }

    @Override
    public void list(String path) {
        try {
            List<String> subnodes = zkClient.getChildren(path, zookeeperProperties.isEnableWatcherForAction());
//            log.info("There are the following nodes under path {} :\n{}", path, String.join("\n", subnodes));
            log.info("There are the following nodes under path {}, {} :", path, subnodes);
            for (String pathSegment : subnodes) {
                // "//path" would be invalid zookeeper path.
                // java.lang.IllegalArgumentException: Invalid path string "//cluster" caused by empty node name specified @1
                String strPath = path.length() == 1 ?
                        path + pathSegment : String.join("/", path, pathSegment);
                getNodeValue(strPath);
            }
        }
        catch (KeeperException | InterruptedException e) {
            log.info("Zookeeper client list nodes on path {}. ", path, e);
        }
    }

    @Override
    public void createNode(String path, String value) {
        try {
            // CreateMode is needed to change as selectable.
            String result = zkClient.create(path, value.getBytes(),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            log.info("Zookeeper client created node on path {} with value {}, result={}. ", path, value, result);
        }
        catch (KeeperException | InterruptedException e) {
            log.error("Error occurred when creating data on path {} with {}, ", path, value, e);
        }
    }

    @Override
    public void deleteNode(String path) {
        Stat stat = new Stat();
        try {
            // directly throw exception if no specific node.
            byte[] bytesData = zkClient.getData(path, zookeeperProperties.isEnableWatcherForAction(), stat);
            String strData = bytesData == null ? "" : new String(bytesData);
            log.info("Deleting the node on path {}, stat.version={}, stat.aversion={}, stat.cversion={}, data={}",
                    path, stat.getVersion(), stat.getAversion(), stat.getCversion(), strData);
            zkClient.delete(path, stat.getVersion());
            log.info("Deleted the node on path {}", path);
        }
        catch (KeeperException | InterruptedException e) {
            log.error("Error occurred when trying to delete node on path {} ", path, e);
        }
    }

    @Override
    public void getNodeValue(String path) {
        Stat stat = new Stat();
        try {
            // directly throw exception if no specific node.
            byte[] bytesData = zkClient.getData(path, zookeeperProperties.isEnableWatcherForAction(), stat);
            String strData = bytesData == null ? "" : new String(bytesData);
            log.info("Zookeeper node on path {}, stat.version={}, stat.aversion={}, stat.cversion={}, " +
                            "stat.ctime={}, stat.mtime={}, stat.czxid={}, stat.mzxid={}, stat.pzxid={}, data={}",
                    path, stat.getVersion(), stat.getAversion(), stat.getCversion(),
                    stat.getCtime(), stat.getMtime(), stat.getCzxid(), stat.getMzxid(), stat.getPzxid(),
                    strData);
        }
        catch (KeeperException | InterruptedException e) {
            log.error("Error occurred when trying to query node on path {} ", path, e);
        }
    }

    @Override
    public void setNodeValue(String path, String value) {
        Stat stat = new Stat();
        try {
            // directly throw exception if no specific node.
            byte[] bytesData = zkClient.getData(path, zookeeperProperties.isEnableWatcherForAction(), stat);
            String strData = bytesData == null ? "" : new String(bytesData);
            log.info("Modifying the node on path {}, stat.version={}, stat.aversion={}, stat.cversion={}, data={}",
                    path, stat.getVersion(), stat.getAversion(), stat.getCversion(), strData);
            zkClient.setData(path, value.getBytes(), stat.getVersion());
            log.info("Modified the node on path {}, value={}", path, value);
        }
        catch (KeeperException | InterruptedException e) {
            log.error("Error occurred when trying to modify node on path {} ", path, e);
        }
    }

    @Override
    public void getNodeAcls(String path) {
    }

    @PreDestroy
    @Override
    public void destroy() {
        try {
            zkClient.close();
        }
        catch (InterruptedException e) {
            log.error("Error occurred when closing zk client connection to {}. ",
                    zookeeperProperties.getConnectionString(), e);
            throw new AppException(String.format("Error occurred when closing zk client connection to %s",
                    zookeeperProperties.getConnectionString()));
        }
    }

}
