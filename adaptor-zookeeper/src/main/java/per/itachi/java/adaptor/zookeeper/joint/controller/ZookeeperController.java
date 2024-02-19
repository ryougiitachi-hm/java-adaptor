package per.itachi.java.adaptor.zookeeper.joint.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import per.itachi.java.adaptor.common.app.port.ZookeeperPort;

@RestController
@RequestMapping("/v1/zookeeper")
public class ZookeeperController {

    @Autowired
    private ZookeeperPort zookeeperPort;

    @GetMapping("/nodes")
    public void listNodes(@RequestParam List<String> path) {
        zookeeperPort.list(joinPathSegments(path));
    }

    @PostMapping("/nodes")
    public void createNode(@RequestParam List<String> path, @RequestParam String value) {
        zookeeperPort.createNode(joinPathSegments(path), value);
    }

    @DeleteMapping("/nodes")
    public void deleteNode(@RequestParam List<String> path) {
        zookeeperPort.deleteNode(joinPathSegments(path));
    }

    @PutMapping("/nodes")
    public void updateNode(@RequestParam List<String> path, @RequestParam String value) {
        zookeeperPort.setNodeValue(joinPathSegments(path), value);
    }

    @GetMapping("/nodes/acls")
    public void listNodeAcls(@RequestParam List<String> path) {
        zookeeperPort.getNodeValue(joinPathSegments(path));
    }

    private String joinPathSegments(List<String> path) {
        if (CollectionUtils.isEmpty(path)) {
            return "/";
        }
        return "/" + String.join("/", path);
    }
}