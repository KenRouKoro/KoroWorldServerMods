package cn.korostudio.koroworld.multiserver.router;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.korostudio.koroworld.multiserver.MultiServer;
import cn.korostudio.koroworld.multiserver.data.PlayerServerData;
import cn.korostudio.koroworld.multiserver.data.sql.PlayerServerDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController()
@RequestMapping(value = "/server")
public class ServerRouter {

    static protected PlayerServerDataRepository playerServerDataRepository;
    @Autowired
    public void setPlayerServerDataRepository(PlayerServerDataRepository playerServerDataRepository) {
        ServerRouter.playerServerDataRepository = playerServerDataRepository;
    }
    @GetMapping("/list")
    public String getServerList(@RequestParam Map<String, Object> params){
        JSONObject back = new JSONObject();
        back.putAll(MultiServer.getServers());
        return back.toString();
    }
    @PostMapping("/setNext")
    public String setClientNext(@RequestParam Map<String, Object> params){
        String UUID = (String) params.get("UUID");
        String server = (String) params.get("server");

        PlayerServerData playerServerData = playerServerDataRepository.findByUUID(UUID);

        if(playerServerData==null){
            return "{\"status\":false}";
        }

        playerServerData.setNextServer(server);

        playerServerDataRepository.save(playerServerData);

        return "{\"status\":true}";
    }
    @PostMapping("/setLast")
    public String setClientLast(@RequestParam Map<String, Object> params){
        String UUID = (String) params.get("UUID");
        String server = (String) params.get("server");

        PlayerServerData playerServerData = playerServerDataRepository.findByUUID(UUID);

        if(playerServerData==null){
            playerServerData = new PlayerServerData();
            playerServerData.setUUID(UUID);
            playerServerData.setNextServer(server);
        }

        playerServerData.setLastServer(server);

        playerServerDataRepository.save(playerServerData);

        return "{\"status\":true}";
    }

}
