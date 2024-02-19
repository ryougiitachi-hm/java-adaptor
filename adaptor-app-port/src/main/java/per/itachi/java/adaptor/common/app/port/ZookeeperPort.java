package per.itachi.java.adaptor.common.app.port;

public interface ZookeeperPort {

    /**
     * not sure whether this method is needful.
     * */
    void connect();

    /**
     * not sure whether this method is needful.
     * */
    void disconnect();

    void list(String path);

    /**
     * Zookeeper stores data as byte stream.
     * */
    void createNode(String path, String value);

    void deleteNode(String path);

    void getNodeValue(String path);

    void setNodeValue(String path, String value);

    void getNodeAcls(String path);

    void destroy();
}
