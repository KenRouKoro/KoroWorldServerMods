package cn.korostudio.koroworldserver.datasave;

import cn.korostudio.koroworldserver.command.CommandAPI;
import cn.korostudio.koroworldserver.command.CommandNode;
import cn.korostudio.koroworldserver.datasave.command.DataSaveCommand;
import cn.korostudio.koroworldserver.mod.ModTemplate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataSave extends ModTemplate {
    @Override
    public String getName() {
        return "KoroWorldServer-DataSave";
    }

    @Override
    public void Init() {
        DataSaveCommand dataSaveCommand = new DataSaveCommand();
        CommandAPI.register(CommandNode.creat("datasave", e -> {
        }).then(CommandNode.creat("update", dataSaveCommand)));
        CommandAPI.register(CommandNode.creat("datasave", e -> {
        }).then(CommandNode.creat("download", dataSaveCommand)));
        log.info("KoroWorldServer-DataSave is Loaded!");
    }

    @Override
    public void Unload() {
    }
}
