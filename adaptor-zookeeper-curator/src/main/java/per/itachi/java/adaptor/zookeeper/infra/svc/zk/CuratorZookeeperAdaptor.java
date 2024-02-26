package per.itachi.java.adaptor.zookeeper.infra.svc.zk;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import per.itachi.java.adaptor.common.app.port.ZookeeperPort;

/**
 * Blocking distributed lock can be implemented via InterProcessMutex.
 * */
@Slf4j
@Component
public class CuratorZookeeperAdaptor implements ZookeeperPort {

    private final ZookeeperProperties zookeeperProperties;

    private final CuratorFramework client;

    @Autowired
    public CuratorZookeeperAdaptor(ZookeeperProperties zookeeperProperties,
                                   RetryPolicyFactory retryPolicyFactory) {
        RetryPolicy retryPolicy = retryPolicyFactory
                .createRetryPolicy(zookeeperProperties.getRetryPolicy(), zookeeperProperties);
        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
                .connectString(zookeeperProperties.getConnectionString())
                .connectionTimeoutMs(zookeeperProperties.getConnectionTimeoutMs())
                .sessionTimeoutMs(zookeeperProperties.getSessionTimeoutMs())
                .retryPolicy(retryPolicy)
//                .zkClientConfig()
//                .zookeeperFactory()
        ;
        if (StringUtils.hasText(zookeeperProperties.getBasePath())) {
            // what if pre-defined path on connection string?
            builder.namespace(zookeeperProperties.getBasePath());
        }
        CuratorFramework client = builder.build();
        client.start();
        this.zookeeperProperties = zookeeperProperties;
        this.client = client;
    }

    @Override
    public void connect() {
        // empty
    }

    @Override
    public void disconnect() {
        // empty
    }

    @Override
    public void list(String path) {
        try {
            Stat stat = client.checkExists().forPath(path);
            if (stat == null) {
                log.info("There is no path {} on zk server {}. ",
                        path, zookeeperProperties.getConnectionString());
                return;
            }
            List<String> pathSegments = client.getChildren().forPath(path);
            log.info("There is the following nodes under path {}: {}", path, pathSegments);
        }
        catch (Exception e) {
            log.error("Error occurred when listing the path {}, server={}",
                    path, zookeeperProperties.getConnectionString(), e);
        }
    }

    @Override
    public void createNode(String path, String value) {
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

    @Override
    public void getNodeAcls(String path) {
    }

    @Override
    public void destroy() {
        if (client == null) {
            log.error("The curator zookeeper client even didn't initialize, server={} ",
                    zookeeperProperties.getConnectionString());
            return;
        }
        try {
            client.close();
        }
        catch (Exception e) {
            log.error("Error occurred when trying to close curator zookeeper client, server={}",
                    zookeeperProperties.getConnectionString(), e);
        }
    }
}
