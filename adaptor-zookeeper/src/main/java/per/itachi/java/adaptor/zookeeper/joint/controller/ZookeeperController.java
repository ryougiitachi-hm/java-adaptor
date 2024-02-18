package per.itachi.java.adaptor.zookeeper.joint.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void list(@RequestParam List<String> path) {
        zookeeperPort.list("path");
    }

    @PostMapping("/nodes")
    public void createNode(@RequestParam List<String> path, @RequestParam String value) {
        zookeeperPort.createNode("path");
    }

    @DeleteMapping("/nodes")
    public void deleteNode(@RequestParam List<String> path) {
        zookeeperPort.deleteNode("path");
    }

    @PutMapping("/nodes")
    public void updateNode(@RequestParam List<String> path, @RequestParam String value) {
        zookeeperPort.setNodeValue("path", value);
    }
}
