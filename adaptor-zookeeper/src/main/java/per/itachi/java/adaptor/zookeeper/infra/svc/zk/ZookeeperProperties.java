package per.itachi.java.adaptor.zookeeper.infra.svc.zk;

import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ZookeeperProperties {

    /**
     * The connection string can be host1:port1,host2:port2/chroot-path
     * */
    private String connectionString;

    private int sessionTimeoutMs;

    private List<String> addresses = Collections.emptyList();

    private String basePath;

}
