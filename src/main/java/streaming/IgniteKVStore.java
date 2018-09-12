package streaming;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CachePeekMode;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.processor.StateStore;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.KeyValueStore;
import utils.IgniteConfigHelper;

import java.util.List;

public class IgniteKVStore implements KeyValueStore<String, Object>{


    private boolean isOpen = false;
    private Ignite ignite;
    private IgniteCache<String, Object> igniteCache;

    @Override
    public void put(String key, Object value) {
        this.igniteCache.put(key, value);
    }

    @Override
    public Object putIfAbsent(String key, Object value) {
        return this.igniteCache.putIfAbsent(key, value);
    }

    @Override
    public void putAll(List<KeyValue<String, Object>> entries) {
    }

    @Override
    public Object delete(String key) {
        return this.igniteCache.remove(key);
    }

    @Override
    public String name() {
        return null;
    }

    @Override
    public void init(ProcessorContext context, StateStore root) {
        ignite = Ignition.start(IgniteConfigHelper.getIgniteClientConfig());
        String cacheName = (String) context.appConfigs().get("IgniteCacheName");
        igniteCache = ignite.getOrCreateCache(cacheName);
    }

    @Override
    public void flush() {

    }

    @Override
    public void close() {
        this.ignite.close();
    }

    @Override
    public boolean persistent() {
        return false;
    }

    @Override
    public boolean isOpen() {
        return this.isOpen;
    }

    @Override
    public Object get(String key) {
        return this.igniteCache.get(key);
    }

    @Override
    public KeyValueIterator<String, Object> range(String from, String to) {
        return null;
    }

    @Override
    public KeyValueIterator<String, Object> all() {
        return null;
    }

    @Override
    public long approximateNumEntries() {
        return this.igniteCache.size(CachePeekMode.ALL);
    }
}
