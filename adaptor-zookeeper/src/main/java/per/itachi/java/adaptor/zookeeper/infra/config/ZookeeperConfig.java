package per.itachi.java.adaptor.zookeeper.infra.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import per.itachi.java.adaptor.zookeeper.infra.svc.zk.ZookeeperProperties;

@Configuration
public class ZookeeperConfig {

    @Bean
    @ConfigurationProperties("infra.svc.zookeeper")
    public ZookeeperProperties zookeeperProperties() {
        return new ZookeeperProperties();
    }
}
