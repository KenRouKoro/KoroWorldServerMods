package cn.korostudio.koroworld.multiserver;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.watch.WatchMonitor;
import cn.hutool.core.io.watch.Watcher;
import cn.hutool.core.io.watch.watchers.DelayWatcher;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.setting.Setting;
import cn.korostudio.koroworldserver.mod.ModTemplate;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@EnableAutoConfiguration
public class MultiServer extends ModTemplate {
    @Getter
    protected static Setting setting = new Setting(FileUtil.touch(System.getProperty("user.dir") + "/koroworld/config/MultiServer.setting"), CharsetUtil.CHARSET_UTF_8, true);
    @Getter
    protected static ConcurrentHashMap<String, String> Servers = new ConcurrentHashMap<>();

    public static void loadServers() {
        try {
            getServers().clear();
            FileReader fileReader = new FileReader(FileUtil.touch(System.getProperty("user.dir") + "/koroworld/data/servers.json"));
            JSONObject jsonObject = JSONUtil.parseObj(fileReader.readString());
            for (String once : jsonObject.keySet()) {
                String name;
                String address;
                name = once;
                address = jsonObject.getStr(once);
                if (name == null || address == null) {
                    continue;
                }
                getServers().put(name, address);
            }
        } catch (Exception e) {
            log.info("服务器信息为空！");
        }
    }

    @Override
    public String getName() {
        return "KoroWorldServer-MultiServer";
    }

    @Override
    public void Init() {
        log.info("KoroWorld MultiServer Model is Loaded!");
        setting.autoLoad(true);
        loadServers();

        WatchMonitor monitor = WatchMonitor.createAll(System.getProperty("user.dir") + "/koroworld/data/servers.json", new DelayWatcher(new Watcher() {
            @Override
            public void onCreate(WatchEvent<?> event, Path currentPath) {
            }

            @Override
            public void onModify(WatchEvent<?> event, Path currentPath) {
                loadServers();
            }

            @Override
            public void onDelete(WatchEvent<?> event, Path currentPath) {
            }

            @Override
            public void onOverflow(WatchEvent<?> event, Path currentPath) {
            }
        }, 500));
        monitor.start();

    }

    @Override
    public void Unload() {

    }
}
