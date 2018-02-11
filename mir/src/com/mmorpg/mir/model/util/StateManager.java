package com.mmorpg.mir.model.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.annotation.PostConstruct;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.ClearAndMigrate;
import com.mmorpg.mir.model.util.entity.StateEntity;
import com.windforce.common.ramcache.anno.Inject;
import com.windforce.common.ramcache.service.EntityBuilder;
import com.windforce.common.ramcache.service.EntityCacheService;

@Component
public class StateManager {

	private static StateManager self;

	public static StateManager getInstance() {
		return self;
	}

	@Inject
	private EntityCacheService<String, StateEntity> stateEntityService;

	@PostConstruct
	protected final void init() {
		self = this;
		if (!ClearAndMigrate.clear) {
			startGame();
		}
	}

	private void startGame() {
		StateEntity stateEntity = stateEntityService.loadOrCreate("1", new EntityBuilder<String, StateEntity>() {
			@Override
			public StateEntity newInstance(String id) {
				return StateEntity.valueOf(id);
			}
		});
		stateEntity.setMergeState(0);
		stateEntityService.writeBack("1", stateEntity);
	}

	public void startClearAndMerge() {
		StateEntity stateEntity = stateEntityService.loadOrCreate("1", new EntityBuilder<String, StateEntity>() {
			@Override
			public StateEntity newInstance(String id) {
				return StateEntity.valueOf(id);
			}
		});
		stateEntity.setMergeState(100);
		stateEntityService.writeBack("1", stateEntity);
	}

	public void sucessClearAndMerge() {
		// StateEntity stateEntity = stateEntityService.load("1");
		// stateEntity.setMergeState(200);
		// stateEntityService.writeBack("1", stateEntity);

		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement("UPDATE `StateEntity` SET `mergeState`=200 WHERE `id`=1");
			ps.addBatch();
			ps.executeBatch();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	public static BasicDataSource dataSource;

	public void errorClearAndMerger() {
		StateEntity stateEntity = stateEntityService.load("1");
		stateEntity.setMergeState(500);
		stateEntityService.writeBack("1", stateEntity);
	}

}
