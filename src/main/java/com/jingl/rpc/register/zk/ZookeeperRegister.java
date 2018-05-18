package com.jingl.rpc.register.zk;

import com.alibaba.fastjson.JSON;
import com.jingl.rpc.cluster.Cluster;
import com.jingl.rpc.common.Constants;
import com.jingl.rpc.common.entity.Directory;
import com.jingl.rpc.common.entity.URL;
import com.jingl.rpc.common.exceptions.SerializeException;
import com.jingl.rpc.common.extension.ExtensionLoader;
import com.jingl.rpc.container.Container;
import com.jingl.rpc.register.Register;
import com.jingl.rpc.serializer.RPCSerializer;
import com.jingl.rpc.utils.PropertyUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.shaded.com.google.common.util.concurrent.ExecutionError;
import org.apache.zookeeper.CreateMode;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Ben on 2018/4/27.
 */
public class ZookeeperRegister implements Register {

    private final String zkHost = PropertyUtils.getProperty(Constants.PROPERTY_REGISTER_URL);
    private final int zkPort = Integer.valueOf(PropertyUtils.getProperty(Constants.PROPERTY_REGISTER_PORT));

    private final String ROOT = new StringBuilder("/").append(PropertyUtils.getProperty(Constants.PROPERTY_REGISTER_ROOT)).toString();
    private final RPCSerializer serializer = (RPCSerializer) ExtensionLoader.getExtensionLoader(RPCSerializer.class, "fastjson").getActiveInstance();

    private volatile CuratorFramework client = null;
    private volatile TreeCache cache = null;

    private final CountDownLatch cacheReadyLatch = new CountDownLatch(1);


    @Override
    public void connect() {
        if (client == null) {
            synchronized (this) {
                if (client == null) {
                    try {
                        String connectionStr = new StringBuilder(zkHost).append(":").append(zkPort).toString();
                        client = CuratorFrameworkFactory.newClient(connectionStr,1000, 1000, new ExponentialBackoffRetry(1000, 3)); //// TODO: 2018/4/27 重试机制，待研究
                        client.start();
                        register();
                        watch();
                        cacheReadyLatch.await();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }

    }

    @Override
    public void set(String path, String value) {

    }


    private void register() throws UnknownHostException {
        List<URL> services = Container.getExportService();
        services.parallelStream().forEach(x -> {
            try {
                String node = new StringBuilder(x.getIp()).append(":").append(x.getPort()).toString();
                byte[] value = serializer.serialize(x);
                String path = new StringBuilder(ROOT).append("/").append(x.getInterfaceName()).append("/").append("providers/").append(node).toString();
                client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path, value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void watch() throws Exception {
        if (cache == null) {
            synchronized (this) {
                if (cache == null) {
                    cache = new TreeCache(client, ROOT);
                    cache.start();
                    cache.getListenable().addListener(new ZKListener());
                }
            }
        }
    }

    public void fetch() throws Exception {
        Map<String, ChildData> services = cache.getCurrentChildren(ROOT);
        Cluster cluster = (Cluster) ExtensionLoader.getExtensionLoader(Cluster.class).getActiveInstance();
        services.values().parallelStream().forEach(childData -> {
            try {
                String path = childData.getPath();
                String[] nodes = path.split("/");
                String name = nodes[nodes.length - 1];
                Class clazz = Class.forName(name);

                Map<String, ChildData> urls = cache.getCurrentChildren(new StringBuilder(path).append("/").append("providers").toString());
                Directory dir = cluster.getDirectory(clazz);

                urls.values().parallelStream().forEach(x -> {
                    try {
                        URL url = serializer.deserialize(x.getData(), URL.class);
                        dir.add(url);
                    } catch (Exception e) {
                    }

                });
            } catch (Exception e) {
            }
        });
    }


    class ZKListener implements TreeCacheListener {
        private volatile boolean cacheReady = false;

        @Override
        public void childEvent(CuratorFramework curatorFramework, TreeCacheEvent treeCacheEvent) throws Exception {
            if (treeCacheEvent.getType() == TreeCacheEvent.Type.INITIALIZED) {
                fetch();
                cacheReadyLatch.countDown();
                cacheReady = true;
                return;
            }

            if (cacheReady == true)
                fetch();
        }
    }

}
