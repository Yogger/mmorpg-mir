package com.mmorpg.mir.model.country.resource;

import javax.persistence.Transient;

import org.apache.commons.lang.ArrayUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

/**
 * 
 * 
 * @author Kuang Hao
 * @since v1.0 2014-9-16
 * 
 */
@Resource
public class CountryAuthorityResource {

	@Id
	private String id;
	/** 国王 */
	private String[] king;
	/** 宰相 */
	private String[] permier;
	/** 御史大夫 */
	private String[] minister;
	/** 大元帅 */
	private String[] generalissimo;
	/** 将军 */
	private String[] general;
	/** 卫队 */
	private String[] guard;
	/** 公民 */
	private String[] citizen;

	@Transient
	private CoreConditions kingConditions;
	@Transient
	private CoreConditions permierConditions;
	@Transient
	private CoreConditions ministerConditions;
	@Transient
	private CoreConditions generalissimoConditions;
	@Transient
	private CoreConditions generalConditions;
	@Transient
	private CoreConditions citizenConditions;
	@Transient
	private CoreConditions guardCondition;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String[] getKing() {
		return king;
	}

	public void setKing(String[] king) {
		this.king = king;
	}

	public String[] getPermier() {
		return permier;
	}

	public void setPermier(String[] permier) {
		this.permier = permier;
	}

	public String[] getMinister() {
		return minister;
	}

	public void setMinister(String[] minister) {
		this.minister = minister;
	}

	public String[] getGeneralissimo() {
		return generalissimo;
	}

	public void setGeneralissimo(String[] generalissimo) {
		this.generalissimo = generalissimo;
	}

	public String[] getGeneral() {
		return general;
	}

	public void setGeneral(String[] general) {
		this.general = general;
	}

	@JsonIgnore
	public CoreConditions getGuardCondition() {
		if (guardCondition == null) {
			if (ArrayUtils.isEmpty(guard)) {
				guardCondition = new CoreConditions();
			} else {
				guardCondition = CoreConditionManager.getInstance().getCoreConditions(1, guard);
			}
		}
		return guardCondition;
	}

	@JsonIgnore
	public void setGuardCondition(CoreConditions guardCondition) {
		this.guardCondition = guardCondition;
	}

	@JsonIgnore
	public CoreConditions getKingConditions() {
		if (kingConditions == null) {
			if (ArrayUtils.isEmpty(king)) {
				kingConditions = new CoreConditions();
			} else {
				kingConditions = CoreConditionManager.getInstance().getCoreConditions(1, king);
			}
		}
		return kingConditions;
	}

	@JsonIgnore
	public void setKingConditions(CoreConditions kingConditions) {
		this.kingConditions = kingConditions;
	}

	@JsonIgnore
	public CoreConditions getPermierConditions() {
		if (permierConditions == null) {
			if (ArrayUtils.isEmpty(permier)) {
				permierConditions = new CoreConditions();
			} else {
				permierConditions = CoreConditionManager.getInstance().getCoreConditions(1, permier);
			}
		}
		return permierConditions;
	}

	@JsonIgnore
	public void setPermierConditions(CoreConditions permierConditions) {
		this.permierConditions = permierConditions;
	}

	@JsonIgnore
	public CoreConditions getMinisterConditions() {
		if (ministerConditions == null) {
			if (ArrayUtils.isEmpty(minister)) {
				ministerConditions = new CoreConditions();
			} else {
				ministerConditions = CoreConditionManager.getInstance().getCoreConditions(1, minister);
			}
		}
		return ministerConditions;
	}

	@JsonIgnore
	public void setMinisterConditions(CoreConditions ministerConditions) {
		this.ministerConditions = ministerConditions;
	}

	@JsonIgnore
	public CoreConditions getGeneralissimoConditions() {
		if (generalissimoConditions == null) {
			if (ArrayUtils.isEmpty(generalissimo)) {
				generalissimoConditions = new CoreConditions();
			} else {
				generalissimoConditions = CoreConditionManager.getInstance().getCoreConditions(1, generalissimo);
			}
		}
		return generalissimoConditions;
	}

	@JsonIgnore
	public void setGeneralissimoConditions(CoreConditions generalissimoConditions) {
		this.generalissimoConditions = generalissimoConditions;
	}

	@JsonIgnore
	public CoreConditions getGeneralConditions() {
		if (generalConditions == null) {
			if (ArrayUtils.isEmpty(general)) {
				generalConditions = new CoreConditions();
			} else {
				generalConditions = CoreConditionManager.getInstance().getCoreConditions(1, general);
			}
		}
		return generalConditions;
	}

	@JsonIgnore
	public void setGeneralConditions(CoreConditions generalConditions) {
		this.generalConditions = generalConditions;
	}

	public String[] getCitizen() {
		return citizen;
	}

	public void setCitizen(String[] citizen) {
		this.citizen = citizen;
	}

	@JsonIgnore
	public CoreConditions getCitizenConditions() {
		if (citizenConditions == null) {
			if (ArrayUtils.isEmpty(citizen)) {
				citizenConditions = new CoreConditions();
			} else {
				citizenConditions = CoreConditionManager.getInstance().getCoreConditions(1, citizen);
			}
		}
		return citizenConditions;
	}

	@JsonIgnore
	public void setCitizenConditions(CoreConditions citizenConditions) {
		this.citizenConditions = citizenConditions;
	}

	public String[] getGuard() {
		return guard;
	}

	public void setGuard(String[] guard) {
		this.guard = guard;
	}

}
