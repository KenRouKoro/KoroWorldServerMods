package cn.korostudio.koroworldserver.datasave.command;

import cn.hutool.core.bean.BeanUtil;
import cn.korostudio.koroworldserver.command.CommandSource;
import cn.korostudio.koroworldserver.command.Node;
import cn.korostudio.koroworldserver.data.SQLDataPack;
import cn.korostudio.koroworldserver.wsservice.WebSocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;

@Slf4j
public class DataSaveCommand implements Node {
    @Override
    public void run(CommandSource source) {
        if (source.getNode().trim().equals("update")) {
            SQLDataPack dataPack;
            String UUID = source.getSource().getStr("UUID");
            boolean http = source.getSource().getBool("http");

            dataPack = WebSocketService.sqlDataPackRepository.findByTags(source.getSource().getStr("tags"));

            if (dataPack == null) {
                dataPack = new SQLDataPack();
                dataPack.setUUID(UUID);
                dataPack.setTags(source.getSource().getStr("tags"));
            }

            dataPack.setData(source.getData());

            source.getSource().clear();

            source.getSource().putOnce("UUID", UUID);
            source.getSource().putOnce("status", true);
            source.getSource().putOnce("type", "DataSave-Update-Back");

            WebSocketService.sqlDataPackRepository.save(dataPack);

            if (!http) {
                try {
                    source.getSession().sendMessage(new TextMessage(source.getSource().toString()));
                } catch (IOException ignored) {
                }
            }
        } else if (source.getNode().trim().equals("download")) {
            String UUID = source.getSource().getStr("UUID");
            boolean http = source.getSource().getBool("http");
            SQLDataPack dataPack = null;

            dataPack = WebSocketService.sqlDataPackRepository.findByTags(source.getSource().getStr("tags"));
            boolean dataPackStatus = true;
            if (dataPack == null) {
                dataPack = new SQLDataPack();
                dataPack.setTags(source.getSource().getStr("tags"));
                dataPackStatus = false;
            }

            dataPack.setUUID(UUID);


            source.getSource().clear();
            source.getSource().putOnce("status", dataPackStatus);
            source.getSource().putAll(BeanUtil.beanToMap(dataPack));
            source.getSource().putOnce("type", "DataSave-Download-Back");

            if (!http) {
                try {
                    source.getSession().sendMessage(new TextMessage(source.getSource().toString()));
                } catch (IOException ignored) {
                }
            }
        }
    }
}
