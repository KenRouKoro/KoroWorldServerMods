package cn.korostudio.koroworld.multiserver.data;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
@Entity
@Data
public class PlayerServerData {
    @Id
    String UUID;
    String lastServer;
    String nextServer;
}
