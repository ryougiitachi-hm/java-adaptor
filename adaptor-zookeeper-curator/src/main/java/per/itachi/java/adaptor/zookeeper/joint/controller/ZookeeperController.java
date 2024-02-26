package per.itachi.java.adaptor.zookeeper.joint.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import per.itachi.java.adaptor.common.app.port.ZookeeperPort;

@RestController
@RequestMapping("/v1/zookeeper/curator")
public class ZookeeperController {

    @Autowired
    private ZookeeperPort zookeeperPort;

    @GetMapping("nodes")
    public void listNodes(@RequestParam List<String> pathSegments) {
        zookeeperPort.list(joinPathSegments(pathSegments));
    }

    private String joinPathSegments(List<String> pathSegments) {
        if (CollectionUtils.isEmpty(pathSegments)) {
            return "/";
        }
        return "/" + String.join("/", pathSegments);
    }
}
