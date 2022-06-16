package cn.korostudio.koroworld.multiserver.data.sql;

import cn.korostudio.koroworld.multiserver.data.PlayerServerData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerServerDataRepository extends JpaRepository<PlayerServerData, String> {
    PlayerServerData findByUUID(String UUID);
}
