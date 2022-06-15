package cn.korostudio.koroworld.multiserver.router;

import cn.hutool.json.JSONObject;
import cn.korostudio.koroworld.multiserver.MultiServer;
import cn.korostudio.koroworld.multiserver.data.PlayerServerData;
import cn.korostudio.koroworld.multiserver.data.sql.PlayerServerDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController()
@RequestMapping(value = "/client")
public class ClientRouter {

    static protected PlayerServerDataRepository playerServerDataRepository;
    @Autowired
    public void setPlayerServerDataRepository(PlayerServerDataRepository playerServerDataRepository) {
        ClientRouter.playerServerDataRepository = playerServerDataRepository;
    }

    @PostMapping("/process")
    public static String process(@RequestParam Map<String, Object> params){
        boolean first;
        String UUID;
        JSONObject backJSON = new JSONObject();
        String backAddress;
        String backName;
        boolean status;

        first= params.get("first").equals("true");
        UUID = (String) params.get("UUID");
        log.info("UUID:"+UUID);
        PlayerServerData playerServerData = playerServerDataRepository.findByUUID(UUID);


        if(playerServerData!=null) {
            if (first) {
                if (MultiServer.getSetting().getBool("BackLastServer", true)) {
                    backName = playerServerData.getLastServer();
                }else{
                    backName = MultiServer.getSetting().getStr("defaultServer","lobby");
                }
            }else{
                backName = playerServerData.getNextServer();
            }
        }else{
            backName = MultiServer.getSetting().getStr("defaultServer","lobby");
        }

        backAddress = MultiServer.getServers().getOrDefault(backName, null);
        status = backAddress != null;

        backJSON.putOnce("status",status);
        backJSON.putOnce("name",backName);
        backJSON.putOnce("address", backAddress);

        return backJSON.toString();
    }
    @GetMapping("/default")
    public static String defaultServer(@RequestParam Map<String, Object> params){
        String defServerName = MultiServer.getSetting().getStr("defaultServer","lobby");
        JSONObject backJSON = new JSONObject();

        backJSON.putOnce("status",MultiServer.getServers().containsKey(defServerName));

        backJSON.putOnce("name",defServerName);
        backJSON.putOnce("address", MultiServer.getServers().getOrDefault(defServerName, null));

        return backJSON.toString();
    }
}
