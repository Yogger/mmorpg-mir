package com.mmorpg.mir.model.artifact.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.artifact.core.ArtifactService;
import com.mmorpg.mir.model.artifact.packet.CM_Artifact_Buff_Deprect;
import com.mmorpg.mir.model.artifact.packet.CM_Artifact_Buy_Buff;
import com.mmorpg.mir.model.artifact.packet.CM_Artifact_Uplevel;
import com.mmorpg.mir.model.artifact.packet.SM_Artifact_Uplevel;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.moduleopen.event.ModuleOpenEvent;
import com.mmorpg.mir.model.moduleopen.manager.ModuleOpenManager;
import com.mmorpg.mir.model.moduleopen.resource.ModuleOpenResource;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.SessionUtil;
import com.windforce.common.event.anno.ReceiverAnno;
import com.xiaosan.dispatcher.anno.HandlerAnno;
import com.xiaosan.socket.core.TSession;

@Component
public class ArtifactFacade {

	private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(ArtifactFacade.class);

	@Autowired
	private ArtifactService artifactService;

	/**
	 * 神兵进阶
	 * 
	 * @param session
	 * @param request
	 * @return
	 */
	@HandlerAnno
	public SM_Artifact_Uplevel uplevel(TSession session, CM_Artifact_Uplevel request) {
		SM_Artifact_Uplevel sm = new SM_Artifact_Uplevel();
		try {
			Player player = SessionUtil.getPlayerBySession(session);
			player.getArtifact().refreshBlessing(player);
			sm = artifactService.uplevel(player, request.getUseCurrency() == 1);
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			LOGGER.error("神兵进阶 | " + e.getMessage(), e);
			sm.setCode(ManagedErrorCode.SYS_ERROR);
		}
		return sm;
	}

	@HandlerAnno
	public void buyArtifactBuff(TSession session, CM_Artifact_Buy_Buff req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			artifactService.buyArtifactBuff(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			LOGGER.error("神兵buff购买 " + e.getMessage(), e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void clientArtifactBuffDeprect(TSession session, CM_Artifact_Buff_Deprect req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			artifactService.deprechArtifactBuff(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			LOGGER.error("神兵buff过期 " + e.getMessage(), e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@ReceiverAnno
	public void openArtifact(ModuleOpenEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		ModuleOpenResource resource = ModuleOpenManager.getInstance().getResource(event.getModuleResourceId());
		if (resource.getModuleKey() == ModuleKey.ARTIFACT.value()) {
			artifactService.flushAritfact(player, true, true);
		}
	}

}
