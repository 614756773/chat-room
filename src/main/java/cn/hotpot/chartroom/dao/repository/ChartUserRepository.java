package cn.hotpot.chartroom.dao.repository;

import cn.hotpot.chartroom.dao.entity.ChartUser;
import org.springframework.data.repository.CrudRepository;

/**
 * @author qinzhu
 * @since 2019/12/20
 */
public interface ChartUserRepository extends CrudRepository<ChartUser, Integer>{
    ChartUser findByUserId(String userId);
}
