package per.itachi.java.adaptor.common.app.port;

public interface ZookeeperPort {

    // not sure whether this method is needful.
    void connect();

    // not sure whether this method is needful.
    void disconnect();

    void list(String path);

    void createNode(String path);

    void deleteNode(String path);

    void getNodeValue(String path);

    void setNodeValue(String path, String value);
}
