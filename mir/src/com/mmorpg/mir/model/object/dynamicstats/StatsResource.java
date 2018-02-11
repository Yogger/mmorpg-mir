package com.mmorpg.mir.model.object.dynamicstats;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Transient;

import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.h2.util.New;
import org.mvel2.MVEL;
import org.mvel2.ParserContext;

import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

/**
 * 属性中间表
 * 
 * @author Kuang Hao
 * @since v1.0 2014-12-15
 * 
 */
@Resource
public class StatsResource {
	private static Logger logger = Logger.getLogger(StatsResource.class);
	public static void main(String[] args) {
		ParserContext parserContext = new ParserContext();
		StatsResource statsResource = new StatsResource();
		statsResource.aAttack = 100;

		Map<String, Object> context = new HashMap<String, Object>();
		context.put("res", statsResource);
		String formula = "res.getField('aLessMagicalDefense')*1.2/0.5/3/3";
		Serializable formulaExp = MVEL.compileExpression(formula, parserContext);
		System.out.println(formulaExp);
		System.out.println(MVEL.executeExpression(formulaExp, context, Double.class));
	}

	@Transient
	private Map<String, Object> fieldMap;

	private void createFieldMap() {
		try {
			Map<String, Object> temp = New.hashMap();
			for (Field field : StatsResource.class.getDeclaredFields()) {
				if (!field.isAnnotationPresent(Transient.class)) {
					temp.put(field.getName(), field.get(this));
				}
			}
			fieldMap = temp;
		} catch (Exception e) {
			logger.error("反射生成StatsResource异常", e);
			throw new RuntimeException("反射生成StatsResource异常");
		}

	}

	@JsonIgnore
	public Object getField(String field) {
		if (fieldMap == null) {
			createFieldMap();
		}
		return fieldMap.get(field);
	}

	@Id
	private int statslevel;

	private double aAttack;

	private double aMainPhysicalDefense;

	private double aLessMagicalDefense;

	private double aMainMagicalDefense;

	private double aLessPhysicalDefense;

	private double aMaxHP;

	private double aIgnoreDefense;

	private double aCritical;

	private double aIgnore;

	private double aIcreaseDamage;

	private double aGroupAtkIncrease;

	private double aGroupAtkDamage;

	private double aSingleAtkIncrease;

	private double aSingleAtkDamage;

	private double aRecover;

	private double bAttack;

	private double bMainPhysicalDefense;

	private double bLessMagicalDefense;

	private double bMainMagicalDefense;

	private double bLessPhysicalDefense;

	private double bMaxHP;

	private double bIgnoreDefense;

	private double bCritical;

	private double bIgnore;

	private double bIcreaseDamage;

	private double bGroupAtkIncrease;

	private double bGroupAtkDamage;

	private double bSingleAtkIncrease;

	private double bSingleAtkDamage;

	private double bRecover;

	private double cAttack;

	private double cMainPhysicalDefense;

	private double cLessMagicalDefense;

	private double cMainMagicalDefense;

	private double cLessPhysicalDefense;

	private double cMaxHP;

	private double cIgnoreDefense;

	private double cCritical;

	private double cIgnore;

	private double cIcreaseDamage;

	private double cGroupAtkIncrease;

	private double cGroupAtkDamage;

	private double cSingleAtkIncrease;

	private double cSingleAtkDamage;

	private double cRecover;

	private double dAttack;

	private double dMainPhysicalDefense;

	private double dLessMagicalDefense;

	private double dMainMagicalDefense;

	private double dLessPhysicalDefense;

	private double dMaxHP;

	private double dIgnoreDefense;

	private double dCritical;

	private double dIgnore;

	private double dIcreaseDamage;

	private double dGroupAtkIncrease;

	private double dGroupAtkDamage;

	private double dSingleAtkIncrease;

	private double dSingleAtkDamage;

	private double dRecover;

	private double eAttack;

	private double eMainPhysicalDefense;

	private double eLessMagicalDefense;

	private double eMainMagicalDefense;

	private double eLessPhysicalDefense;

	private double eMaxHP;

	private double eIgnoreDefense;

	private double eCritical;

	private double eIgnore;

	private double eIcreaseDamage;

	private double eGroupAtkIncrease;

	private double eGroupAtkDamage;

	private double eSingleAtkIncrease;

	private double eSingleAtkDamage;

	private double eRecover;
	
	private double beta18aAttack;
	
	private double beta18aMainPhysicalDefense;
	
	private double beta18aLessMagicalDefense;
	
	private double beta18aMainMagicalDefense;
	
	private double beta18aLessPhysicalDefense;
	
	private double beta18aMaxHP;
	
	private double beta18aIgnoreDefense;
	
	private double beta18aCritical;
	
	private double beta18aIgnore;
	
	private double beta18aIcreaseDamage;
	
	private double beta18aGroupAtkIncrease;
	
	private double beta18aGroupAtkDamage;
	
	private double beta18aSingleAtkIncrease;
	
	private double beta18aSingleAtkDamage;
	
	private double beta18aRecover;
	
	private double beta18bAttack;
	
	private double beta18bMainPhysicalDefense;
	
	private double beta18bLessMagicalDefense;
	
	private double beta18bMainMagicalDefense;
	
	private double beta18bLessPhysicalDefense;
	
	private double beta18bMaxHP;
	
	private double beta18bIgnoreDefense;
	
	private double beta18bCritical;
	
	private double beta18bIgnore;
	
	private double beta18bIcreaseDamage;
	
	private double beta18bGroupAtkIncrease;
	
	private double beta18bGroupAtkDamage;
	
	private double beta18bSingleAtkIncrease;
	
	private double beta18bSingleAtkDamage;
	
	private double beta18bRecover;
	
	private double beta18cAttack;
	
	private double beta18cMainPhysicalDefense;
	
	private double beta18cLessMagicalDefense;
	
	private double beta18cMainMagicalDefense;
	
	private double beta18cLessPhysicalDefense;
	
	private double beta18cMaxHP;
	
	private double beta18cIgnoreDefense;
	
	private double beta18cCritical;
	
	private double beta18cIgnore;
	
	private double beta18cIcreaseDamage;
	
	private double beta18cGroupAtkIncrease;
	
	private double beta18cGroupAtkDamage;
	
	private double beta18cSingleAtkIncrease;
	
	private double beta18cSingleAtkDamage;
	
	private double beta18cRecover;
	
	private double beta18dAttack;
	
	private double beta18dMainPhysicalDefense;
	
	private double beta18dLessMagicalDefense;
	
	private double beta18dMainMagicalDefense;
	
	private double beta18dLessPhysicalDefense;
	
	private double beta18dMaxHP;
	
	private double beta18dIgnoreDefense;
	
	private double beta18dCritical;
	
	private double beta18dIgnore;
	
	private double beta18dIcreaseDamage;
	
	private double beta18dGroupAtkIncrease;
	
	private double beta18dGroupAtkDamage;
	
	private double beta18dSingleAtkIncrease;
	
	private double beta18dSingleAtkDamage;
	
	private double beta18dRecover;
	
	private double beta18eAttack;
	
	private double beta18eMainPhysicalDefense;
	
	private double beta18eLessMagicalDefense;
	
	private double beta18eMainMagicalDefense;
	
	private double beta18eLessPhysicalDefense;
	
	private double beta18eMaxHP;
	
	private double beta18eIgnoreDefense;
	
	private double beta18eCritical;
	
	private double beta18eIgnore;
	
	private double beta18eIcreaseDamage;
	
	private double beta18eGroupAtkIncrease;
	
	private double beta18eGroupAtkDamage;
	
	private double beta18eSingleAtkIncrease;
	
	private double beta18eSingleAtkDamage;
	
	private double beta18eRecover;
	
	public int getStatslevel() {
		return statslevel;
	}

	public void setStatslevel(int statslevel) {
		this.statslevel = statslevel;
	}

	public double getaAttack() {
		return aAttack;
	}

	public void setaAttack(double aAttack) {
		this.aAttack = aAttack;
	}

	public double getaMainPhysicalDefense() {
		return aMainPhysicalDefense;
	}

	public void setaMainPhysicalDefense(double aMainPhysicalDefense) {
		this.aMainPhysicalDefense = aMainPhysicalDefense;
	}

	public double getaLessMagicalDefense() {
		return aLessMagicalDefense;
	}

	public void setaLessMagicalDefense(double aLessMagicalDefense) {
		this.aLessMagicalDefense = aLessMagicalDefense;
	}

	public double getaMainMagicalDefense() {
		return aMainMagicalDefense;
	}

	public void setaMainMagicalDefense(double aMainMagicalDefense) {
		this.aMainMagicalDefense = aMainMagicalDefense;
	}

	public double getaLessPhysicalDefense() {
		return aLessPhysicalDefense;
	}

	public void setaLessPhysicalDefense(double aLessPhysicalDefense) {
		this.aLessPhysicalDefense = aLessPhysicalDefense;
	}

	public double getaMaxHP() {
		return aMaxHP;
	}

	public void setaMaxHP(double aMaxHP) {
		this.aMaxHP = aMaxHP;
	}

	public double getaIgnoreDefense() {
		return aIgnoreDefense;
	}

	public void setaIgnoreDefense(double aIgnoreDefense) {
		this.aIgnoreDefense = aIgnoreDefense;
	}

	public double getaCritical() {
		return aCritical;
	}

	public void setaCritical(double aCritical) {
		this.aCritical = aCritical;
	}

	public double getaIgnore() {
		return aIgnore;
	}

	public void setaIgnore(double aIgnore) {
		this.aIgnore = aIgnore;
	}

	public double getaIcreaseDamage() {
		return aIcreaseDamage;
	}

	public void setaIcreaseDamage(double aIcreaseDamage) {
		this.aIcreaseDamage = aIcreaseDamage;
	}

	public double getaGroupAtkIncrease() {
		return aGroupAtkIncrease;
	}

	public void setaGroupAtkIncrease(double aGroupAtkIncrease) {
		this.aGroupAtkIncrease = aGroupAtkIncrease;
	}

	public double getaGroupAtkDamage() {
		return aGroupAtkDamage;
	}

	public void setaGroupAtkDamage(double aGroupAtkDamage) {
		this.aGroupAtkDamage = aGroupAtkDamage;
	}

	public double getaSingleAtkIncrease() {
		return aSingleAtkIncrease;
	}

	public void setaSingleAtkIncrease(double aSingleAtkIncrease) {
		this.aSingleAtkIncrease = aSingleAtkIncrease;
	}

	public double getaSingleAtkDamage() {
		return aSingleAtkDamage;
	}

	public void setaSingleAtkDamage(double aSingleAtkDamage) {
		this.aSingleAtkDamage = aSingleAtkDamage;
	}

	public double getaRecover() {
		return aRecover;
	}

	public void setaRecover(double aRecover) {
		this.aRecover = aRecover;
	}

	public double getbAttack() {
		return bAttack;
	}

	public void setbAttack(double bAttack) {
		this.bAttack = bAttack;
	}

	public double getbMainPhysicalDefense() {
		return bMainPhysicalDefense;
	}

	public void setbMainPhysicalDefense(double bMainPhysicalDefense) {
		this.bMainPhysicalDefense = bMainPhysicalDefense;
	}

	public double getbLessMagicalDefense() {
		return bLessMagicalDefense;
	}

	public void setbLessMagicalDefense(double bLessMagicalDefense) {
		this.bLessMagicalDefense = bLessMagicalDefense;
	}

	public double getbMainMagicalDefense() {
		return bMainMagicalDefense;
	}

	public void setbMainMagicalDefense(double bMainMagicalDefense) {
		this.bMainMagicalDefense = bMainMagicalDefense;
	}

	public double getbLessPhysicalDefense() {
		return bLessPhysicalDefense;
	}

	public void setbLessPhysicalDefense(double bLessPhysicalDefense) {
		this.bLessPhysicalDefense = bLessPhysicalDefense;
	}

	public double getbMaxHP() {
		return bMaxHP;
	}

	public void setbMaxHP(double bMaxHP) {
		this.bMaxHP = bMaxHP;
	}

	public double getbIgnoreDefense() {
		return bIgnoreDefense;
	}

	public void setbIgnoreDefense(double bIgnoreDefense) {
		this.bIgnoreDefense = bIgnoreDefense;
	}

	public double getbCritical() {
		return bCritical;
	}

	public void setbCritical(double bCritical) {
		this.bCritical = bCritical;
	}

	public double getbIgnore() {
		return bIgnore;
	}

	public void setbIgnore(double bIgnore) {
		this.bIgnore = bIgnore;
	}

	public double getbIcreaseDamage() {
		return bIcreaseDamage;
	}

	public void setbIcreaseDamage(double bIcreaseDamage) {
		this.bIcreaseDamage = bIcreaseDamage;
	}

	public double getbGroupAtkIncrease() {
		return bGroupAtkIncrease;
	}

	public void setbGroupAtkIncrease(double bGroupAtkIncrease) {
		this.bGroupAtkIncrease = bGroupAtkIncrease;
	}

	public double getbGroupAtkDamage() {
		return bGroupAtkDamage;
	}

	public void setbGroupAtkDamage(double bGroupAtkDamage) {
		this.bGroupAtkDamage = bGroupAtkDamage;
	}

	public double getbSingleAtkIncrease() {
		return bSingleAtkIncrease;
	}

	public void setbSingleAtkIncrease(double bSingleAtkIncrease) {
		this.bSingleAtkIncrease = bSingleAtkIncrease;
	}

	public double getbSingleAtkDamage() {
		return bSingleAtkDamage;
	}

	public void setbSingleAtkDamage(double bSingleAtkDamage) {
		this.bSingleAtkDamage = bSingleAtkDamage;
	}

	public double getbRecover() {
		return bRecover;
	}

	public void setbRecover(double bRecover) {
		this.bRecover = bRecover;
	}

	public double getcAttack() {
		return cAttack;
	}

	public void setcAttack(double cAttack) {
		this.cAttack = cAttack;
	}

	public double getcMainPhysicalDefense() {
		return cMainPhysicalDefense;
	}

	public void setcMainPhysicalDefense(double cMainPhysicalDefense) {
		this.cMainPhysicalDefense = cMainPhysicalDefense;
	}

	public double getcLessMagicalDefense() {
		return cLessMagicalDefense;
	}

	public void setcLessMagicalDefense(double cLessMagicalDefense) {
		this.cLessMagicalDefense = cLessMagicalDefense;
	}

	public double getcMainMagicalDefense() {
		return cMainMagicalDefense;
	}

	public void setcMainMagicalDefense(double cMainMagicalDefense) {
		this.cMainMagicalDefense = cMainMagicalDefense;
	}

	public double getcLessPhysicalDefense() {
		return cLessPhysicalDefense;
	}

	public void setcLessPhysicalDefense(double cLessPhysicalDefense) {
		this.cLessPhysicalDefense = cLessPhysicalDefense;
	}

	public double getcMaxHP() {
		return cMaxHP;
	}

	public void setcMaxHP(double cMaxHP) {
		this.cMaxHP = cMaxHP;
	}

	public double getcIgnoreDefense() {
		return cIgnoreDefense;
	}

	public void setcIgnoreDefense(double cIgnoreDefense) {
		this.cIgnoreDefense = cIgnoreDefense;
	}

	public double getcCritical() {
		return cCritical;
	}

	public void setcCritical(double cCritical) {
		this.cCritical = cCritical;
	}

	public double getcIgnore() {
		return cIgnore;
	}

	public void setcIgnore(double cIgnore) {
		this.cIgnore = cIgnore;
	}

	public double getcIcreaseDamage() {
		return cIcreaseDamage;
	}

	public void setcIcreaseDamage(double cIcreaseDamage) {
		this.cIcreaseDamage = cIcreaseDamage;
	}

	public double getcGroupAtkIncrease() {
		return cGroupAtkIncrease;
	}

	public void setcGroupAtkIncrease(double cGroupAtkIncrease) {
		this.cGroupAtkIncrease = cGroupAtkIncrease;
	}

	public double getcGroupAtkDamage() {
		return cGroupAtkDamage;
	}

	public void setcGroupAtkDamage(double cGroupAtkDamage) {
		this.cGroupAtkDamage = cGroupAtkDamage;
	}

	public double getcSingleAtkIncrease() {
		return cSingleAtkIncrease;
	}

	public void setcSingleAtkIncrease(double cSingleAtkIncrease) {
		this.cSingleAtkIncrease = cSingleAtkIncrease;
	}

	public double getcSingleAtkDamage() {
		return cSingleAtkDamage;
	}

	public void setcSingleAtkDamage(double cSingleAtkDamage) {
		this.cSingleAtkDamage = cSingleAtkDamage;
	}

	public double getcRecover() {
		return cRecover;
	}

	public void setcRecover(double cRecover) {
		this.cRecover = cRecover;
	}

	public double getdAttack() {
		return dAttack;
	}

	public void setdAttack(double dAttack) {
		this.dAttack = dAttack;
	}

	public double getdMainPhysicalDefense() {
		return dMainPhysicalDefense;
	}

	public void setdMainPhysicalDefense(double dMainPhysicalDefense) {
		this.dMainPhysicalDefense = dMainPhysicalDefense;
	}

	public double getdLessMagicalDefense() {
		return dLessMagicalDefense;
	}

	public void setdLessMagicalDefense(double dLessMagicalDefense) {
		this.dLessMagicalDefense = dLessMagicalDefense;
	}

	public double getdMainMagicalDefense() {
		return dMainMagicalDefense;
	}

	public void setdMainMagicalDefense(double dMainMagicalDefense) {
		this.dMainMagicalDefense = dMainMagicalDefense;
	}

	public double getdLessPhysicalDefense() {
		return dLessPhysicalDefense;
	}

	public void setdLessPhysicalDefense(double dLessPhysicalDefense) {
		this.dLessPhysicalDefense = dLessPhysicalDefense;
	}

	public double getdMaxHP() {
		return dMaxHP;
	}

	public void setdMaxHP(double dMaxHP) {
		this.dMaxHP = dMaxHP;
	}

	public double getdIgnoreDefense() {
		return dIgnoreDefense;
	}

	public void setdIgnoreDefense(double dIgnoreDefense) {
		this.dIgnoreDefense = dIgnoreDefense;
	}

	public double getdCritical() {
		return dCritical;
	}

	public void setdCritical(double dCritical) {
		this.dCritical = dCritical;
	}

	public double getdIgnore() {
		return dIgnore;
	}

	public void setdIgnore(double dIgnore) {
		this.dIgnore = dIgnore;
	}

	public double getdIcreaseDamage() {
		return dIcreaseDamage;
	}

	public void setdIcreaseDamage(double dIcreaseDamage) {
		this.dIcreaseDamage = dIcreaseDamage;
	}

	public double getdGroupAtkIncrease() {
		return dGroupAtkIncrease;
	}

	public void setdGroupAtkIncrease(double dGroupAtkIncrease) {
		this.dGroupAtkIncrease = dGroupAtkIncrease;
	}

	public double getdGroupAtkDamage() {
		return dGroupAtkDamage;
	}

	public void setdGroupAtkDamage(double dGroupAtkDamage) {
		this.dGroupAtkDamage = dGroupAtkDamage;
	}

	public double getdSingleAtkIncrease() {
		return dSingleAtkIncrease;
	}

	public void setdSingleAtkIncrease(double dSingleAtkIncrease) {
		this.dSingleAtkIncrease = dSingleAtkIncrease;
	}

	public double getdSingleAtkDamage() {
		return dSingleAtkDamage;
	}

	public void setdSingleAtkDamage(double dSingleAtkDamage) {
		this.dSingleAtkDamage = dSingleAtkDamage;
	}

	public double getdRecover() {
		return dRecover;
	}

	public void setdRecover(double dRecover) {
		this.dRecover = dRecover;
	}

	public double geteAttack() {
		return eAttack;
	}

	public void seteAttack(double eAttack) {
		this.eAttack = eAttack;
	}

	public double geteMainPhysicalDefense() {
		return eMainPhysicalDefense;
	}

	public void seteMainPhysicalDefense(double eMainPhysicalDefense) {
		this.eMainPhysicalDefense = eMainPhysicalDefense;
	}

	public double geteLessMagicalDefense() {
		return eLessMagicalDefense;
	}

	public void seteLessMagicalDefense(double eLessMagicalDefense) {
		this.eLessMagicalDefense = eLessMagicalDefense;
	}

	public double geteMainMagicalDefense() {
		return eMainMagicalDefense;
	}

	public void seteMainMagicalDefense(double eMainMagicalDefense) {
		this.eMainMagicalDefense = eMainMagicalDefense;
	}

	public double geteLessPhysicalDefense() {
		return eLessPhysicalDefense;
	}

	public void seteLessPhysicalDefense(double eLessPhysicalDefense) {
		this.eLessPhysicalDefense = eLessPhysicalDefense;
	}

	public double geteMaxHP() {
		return eMaxHP;
	}

	public void seteMaxHP(double eMaxHP) {
		this.eMaxHP = eMaxHP;
	}

	public double geteIgnoreDefense() {
		return eIgnoreDefense;
	}

	public void seteIgnoreDefense(double eIgnoreDefense) {
		this.eIgnoreDefense = eIgnoreDefense;
	}

	public double geteCritical() {
		return eCritical;
	}

	public void seteCritical(double eCritical) {
		this.eCritical = eCritical;
	}

	public double geteIgnore() {
		return eIgnore;
	}

	public void seteIgnore(double eIgnore) {
		this.eIgnore = eIgnore;
	}

	public double geteIcreaseDamage() {
		return eIcreaseDamage;
	}

	public void seteIcreaseDamage(double eIcreaseDamage) {
		this.eIcreaseDamage = eIcreaseDamage;
	}

	public double geteGroupAtkIncrease() {
		return eGroupAtkIncrease;
	}

	public void seteGroupAtkIncrease(double eGroupAtkIncrease) {
		this.eGroupAtkIncrease = eGroupAtkIncrease;
	}

	public double geteGroupAtkDamage() {
		return eGroupAtkDamage;
	}

	public void seteGroupAtkDamage(double eGroupAtkDamage) {
		this.eGroupAtkDamage = eGroupAtkDamage;
	}

	public double geteSingleAtkIncrease() {
		return eSingleAtkIncrease;
	}

	public void seteSingleAtkIncrease(double eSingleAtkIncrease) {
		this.eSingleAtkIncrease = eSingleAtkIncrease;
	}

	public double geteSingleAtkDamage() {
		return eSingleAtkDamage;
	}

	public void seteSingleAtkDamage(double eSingleAtkDamage) {
		this.eSingleAtkDamage = eSingleAtkDamage;
	}

	public double geteRecover() {
		return eRecover;
	}

	public void seteRecover(double eRecover) {
		this.eRecover = eRecover;
	}

	public Map<String, Object> getFieldMap() {
		return fieldMap;
	}

	public void setFieldMap(Map<String, Object> fieldMap) {
		this.fieldMap = fieldMap;
	}

	public double getBeta18aAttack() {
		return beta18aAttack;
	}

	public void setBeta18aAttack(double beta18aAttack) {
		this.beta18aAttack = beta18aAttack;
	}

	public double getBeta18aMainPhysicalDefense() {
		return beta18aMainPhysicalDefense;
	}

	public void setBeta18aMainPhysicalDefense(double beta18aMainPhysicalDefense) {
		this.beta18aMainPhysicalDefense = beta18aMainPhysicalDefense;
	}

	public double getBeta18aLessMagicalDefense() {
		return beta18aLessMagicalDefense;
	}

	public void setBeta18aLessMagicalDefense(double beta18aLessMagicalDefense) {
		this.beta18aLessMagicalDefense = beta18aLessMagicalDefense;
	}

	public double getBeta18aMainMagicalDefense() {
		return beta18aMainMagicalDefense;
	}

	public void setBeta18aMainMagicalDefense(double beta18aMainMagicalDefense) {
		this.beta18aMainMagicalDefense = beta18aMainMagicalDefense;
	}

	public double getBeta18aLessPhysicalDefense() {
		return beta18aLessPhysicalDefense;
	}

	public void setBeta18aLessPhysicalDefense(double beta18aLessPhysicalDefense) {
		this.beta18aLessPhysicalDefense = beta18aLessPhysicalDefense;
	}

	public double getBeta18aMaxHP() {
		return beta18aMaxHP;
	}

	public void setBeta18aMaxHP(double beta18aMaxHP) {
		this.beta18aMaxHP = beta18aMaxHP;
	}

	public double getBeta18aIgnoreDefense() {
		return beta18aIgnoreDefense;
	}

	public void setBeta18aIgnoreDefense(double beta18aIgnoreDefense) {
		this.beta18aIgnoreDefense = beta18aIgnoreDefense;
	}

	public double getBeta18aCritical() {
		return beta18aCritical;
	}

	public void setBeta18aCritical(double beta18aCritical) {
		this.beta18aCritical = beta18aCritical;
	}

	public double getBeta18aIgnore() {
		return beta18aIgnore;
	}

	public void setBeta18aIgnore(double beta18aIgnore) {
		this.beta18aIgnore = beta18aIgnore;
	}

	public double getBeta18aIcreaseDamage() {
		return beta18aIcreaseDamage;
	}

	public void setBeta18aIcreaseDamage(double beta18aIcreaseDamage) {
		this.beta18aIcreaseDamage = beta18aIcreaseDamage;
	}

	public double getBeta18aGroupAtkIncrease() {
		return beta18aGroupAtkIncrease;
	}

	public void setBeta18aGroupAtkIncrease(double beta18aGroupAtkIncrease) {
		this.beta18aGroupAtkIncrease = beta18aGroupAtkIncrease;
	}

	public double getBeta18aGroupAtkDamage() {
		return beta18aGroupAtkDamage;
	}

	public void setBeta18aGroupAtkDamage(double beta18aGroupAtkDamage) {
		this.beta18aGroupAtkDamage = beta18aGroupAtkDamage;
	}

	public double getBeta18aSingleAtkIncrease() {
		return beta18aSingleAtkIncrease;
	}

	public void setBeta18aSingleAtkIncrease(double beta18aSingleAtkIncrease) {
		this.beta18aSingleAtkIncrease = beta18aSingleAtkIncrease;
	}

	public double getBeta18aSingleAtkDamage() {
		return beta18aSingleAtkDamage;
	}

	public void setBeta18aSingleAtkDamage(double beta18aSingleAtkDamage) {
		this.beta18aSingleAtkDamage = beta18aSingleAtkDamage;
	}

	public double getBeta18aRecover() {
		return beta18aRecover;
	}

	public void setBeta18aRecover(double beta18aRecover) {
		this.beta18aRecover = beta18aRecover;
	}

	public double getBeta18bAttack() {
		return beta18bAttack;
	}

	public void setBeta18bAttack(double beta18bAttack) {
		this.beta18bAttack = beta18bAttack;
	}

	public double getBeta18bMainPhysicalDefense() {
		return beta18bMainPhysicalDefense;
	}

	public void setBeta18bMainPhysicalDefense(double beta18bMainPhysicalDefense) {
		this.beta18bMainPhysicalDefense = beta18bMainPhysicalDefense;
	}

	public double getBeta18bLessMagicalDefense() {
		return beta18bLessMagicalDefense;
	}

	public void setBeta18bLessMagicalDefense(double beta18bLessMagicalDefense) {
		this.beta18bLessMagicalDefense = beta18bLessMagicalDefense;
	}

	public double getBeta18bMainMagicalDefense() {
		return beta18bMainMagicalDefense;
	}

	public void setBeta18bMainMagicalDefense(double beta18bMainMagicalDefense) {
		this.beta18bMainMagicalDefense = beta18bMainMagicalDefense;
	}

	public double getBeta18bLessPhysicalDefense() {
		return beta18bLessPhysicalDefense;
	}

	public void setBeta18bLessPhysicalDefense(double beta18bLessPhysicalDefense) {
		this.beta18bLessPhysicalDefense = beta18bLessPhysicalDefense;
	}

	public double getBeta18bMaxHP() {
		return beta18bMaxHP;
	}

	public void setBeta18bMaxHP(double beta18bMaxHP) {
		this.beta18bMaxHP = beta18bMaxHP;
	}

	public double getBeta18bIgnoreDefense() {
		return beta18bIgnoreDefense;
	}

	public void setBeta18bIgnoreDefense(double beta18bIgnoreDefense) {
		this.beta18bIgnoreDefense = beta18bIgnoreDefense;
	}

	public double getBeta18bCritical() {
		return beta18bCritical;
	}

	public void setBeta18bCritical(double beta18bCritical) {
		this.beta18bCritical = beta18bCritical;
	}

	public double getBeta18bIgnore() {
		return beta18bIgnore;
	}

	public void setBeta18bIgnore(double beta18bIgnore) {
		this.beta18bIgnore = beta18bIgnore;
	}

	public double getBeta18bIcreaseDamage() {
		return beta18bIcreaseDamage;
	}

	public void setBeta18bIcreaseDamage(double beta18bIcreaseDamage) {
		this.beta18bIcreaseDamage = beta18bIcreaseDamage;
	}

	public double getBeta18bGroupAtkIncrease() {
		return beta18bGroupAtkIncrease;
	}

	public void setBeta18bGroupAtkIncrease(double beta18bGroupAtkIncrease) {
		this.beta18bGroupAtkIncrease = beta18bGroupAtkIncrease;
	}

	public double getBeta18bGroupAtkDamage() {
		return beta18bGroupAtkDamage;
	}

	public void setBeta18bGroupAtkDamage(double beta18bGroupAtkDamage) {
		this.beta18bGroupAtkDamage = beta18bGroupAtkDamage;
	}

	public double getBeta18bSingleAtkIncrease() {
		return beta18bSingleAtkIncrease;
	}

	public void setBeta18bSingleAtkIncrease(double beta18bSingleAtkIncrease) {
		this.beta18bSingleAtkIncrease = beta18bSingleAtkIncrease;
	}

	public double getBeta18bSingleAtkDamage() {
		return beta18bSingleAtkDamage;
	}

	public void setBeta18bSingleAtkDamage(double beta18bSingleAtkDamage) {
		this.beta18bSingleAtkDamage = beta18bSingleAtkDamage;
	}

	public double getBeta18bRecover() {
		return beta18bRecover;
	}

	public void setBeta18bRecover(double beta18bRecover) {
		this.beta18bRecover = beta18bRecover;
	}

	public double getBeta18cAttack() {
		return beta18cAttack;
	}

	public void setBeta18cAttack(double beta18cAttack) {
		this.beta18cAttack = beta18cAttack;
	}

	public double getBeta18cMainPhysicalDefense() {
		return beta18cMainPhysicalDefense;
	}

	public void setBeta18cMainPhysicalDefense(double beta18cMainPhysicalDefense) {
		this.beta18cMainPhysicalDefense = beta18cMainPhysicalDefense;
	}

	public double getBeta18cLessMagicalDefense() {
		return beta18cLessMagicalDefense;
	}

	public void setBeta18cLessMagicalDefense(double beta18cLessMagicalDefense) {
		this.beta18cLessMagicalDefense = beta18cLessMagicalDefense;
	}

	public double getBeta18cMainMagicalDefense() {
		return beta18cMainMagicalDefense;
	}

	public void setBeta18cMainMagicalDefense(double beta18cMainMagicalDefense) {
		this.beta18cMainMagicalDefense = beta18cMainMagicalDefense;
	}

	public double getBeta18cLessPhysicalDefense() {
		return beta18cLessPhysicalDefense;
	}

	public void setBeta18cLessPhysicalDefense(double beta18cLessPhysicalDefense) {
		this.beta18cLessPhysicalDefense = beta18cLessPhysicalDefense;
	}

	public double getBeta18cMaxHP() {
		return beta18cMaxHP;
	}

	public void setBeta18cMaxHP(double beta18cMaxHP) {
		this.beta18cMaxHP = beta18cMaxHP;
	}

	public double getBeta18cIgnoreDefense() {
		return beta18cIgnoreDefense;
	}

	public void setBeta18cIgnoreDefense(double beta18cIgnoreDefense) {
		this.beta18cIgnoreDefense = beta18cIgnoreDefense;
	}

	public double getBeta18cCritical() {
		return beta18cCritical;
	}

	public void setBeta18cCritical(double beta18cCritical) {
		this.beta18cCritical = beta18cCritical;
	}

	public double getBeta18cIgnore() {
		return beta18cIgnore;
	}

	public void setBeta18cIgnore(double beta18cIgnore) {
		this.beta18cIgnore = beta18cIgnore;
	}

	public double getBeta18cIcreaseDamage() {
		return beta18cIcreaseDamage;
	}

	public void setBeta18cIcreaseDamage(double beta18cIcreaseDamage) {
		this.beta18cIcreaseDamage = beta18cIcreaseDamage;
	}

	public double getBeta18cGroupAtkIncrease() {
		return beta18cGroupAtkIncrease;
	}

	public void setBeta18cGroupAtkIncrease(double beta18cGroupAtkIncrease) {
		this.beta18cGroupAtkIncrease = beta18cGroupAtkIncrease;
	}

	public double getBeta18cGroupAtkDamage() {
		return beta18cGroupAtkDamage;
	}

	public void setBeta18cGroupAtkDamage(double beta18cGroupAtkDamage) {
		this.beta18cGroupAtkDamage = beta18cGroupAtkDamage;
	}

	public double getBeta18cSingleAtkIncrease() {
		return beta18cSingleAtkIncrease;
	}

	public void setBeta18cSingleAtkIncrease(double beta18cSingleAtkIncrease) {
		this.beta18cSingleAtkIncrease = beta18cSingleAtkIncrease;
	}

	public double getBeta18cSingleAtkDamage() {
		return beta18cSingleAtkDamage;
	}

	public void setBeta18cSingleAtkDamage(double beta18cSingleAtkDamage) {
		this.beta18cSingleAtkDamage = beta18cSingleAtkDamage;
	}

	public double getBeta18cRecover() {
		return beta18cRecover;
	}

	public void setBeta18cRecover(double beta18cRecover) {
		this.beta18cRecover = beta18cRecover;
	}

	public double getBeta18dAttack() {
		return beta18dAttack;
	}

	public void setBeta18dAttack(double beta18dAttack) {
		this.beta18dAttack = beta18dAttack;
	}

	public double getBeta18dMainPhysicalDefense() {
		return beta18dMainPhysicalDefense;
	}

	public void setBeta18dMainPhysicalDefense(double beta18dMainPhysicalDefense) {
		this.beta18dMainPhysicalDefense = beta18dMainPhysicalDefense;
	}

	public double getBeta18dLessMagicalDefense() {
		return beta18dLessMagicalDefense;
	}

	public void setBeta18dLessMagicalDefense(double beta18dLessMagicalDefense) {
		this.beta18dLessMagicalDefense = beta18dLessMagicalDefense;
	}

	public double getBeta18dMainMagicalDefense() {
		return beta18dMainMagicalDefense;
	}

	public void setBeta18dMainMagicalDefense(double beta18dMainMagicalDefense) {
		this.beta18dMainMagicalDefense = beta18dMainMagicalDefense;
	}

	public double getBeta18dLessPhysicalDefense() {
		return beta18dLessPhysicalDefense;
	}

	public void setBeta18dLessPhysicalDefense(double beta18dLessPhysicalDefense) {
		this.beta18dLessPhysicalDefense = beta18dLessPhysicalDefense;
	}

	public double getBeta18dMaxHP() {
		return beta18dMaxHP;
	}

	public void setBeta18dMaxHP(double beta18dMaxHP) {
		this.beta18dMaxHP = beta18dMaxHP;
	}

	public double getBeta18dIgnoreDefense() {
		return beta18dIgnoreDefense;
	}

	public void setBeta18dIgnoreDefense(double beta18dIgnoreDefense) {
		this.beta18dIgnoreDefense = beta18dIgnoreDefense;
	}

	public double getBeta18dCritical() {
		return beta18dCritical;
	}

	public void setBeta18dCritical(double beta18dCritical) {
		this.beta18dCritical = beta18dCritical;
	}

	public double getBeta18dIgnore() {
		return beta18dIgnore;
	}

	public void setBeta18dIgnore(double beta18dIgnore) {
		this.beta18dIgnore = beta18dIgnore;
	}

	public double getBeta18dIcreaseDamage() {
		return beta18dIcreaseDamage;
	}

	public void setBeta18dIcreaseDamage(double beta18dIcreaseDamage) {
		this.beta18dIcreaseDamage = beta18dIcreaseDamage;
	}

	public double getBeta18dGroupAtkIncrease() {
		return beta18dGroupAtkIncrease;
	}

	public void setBeta18dGroupAtkIncrease(double beta18dGroupAtkIncrease) {
		this.beta18dGroupAtkIncrease = beta18dGroupAtkIncrease;
	}

	public double getBeta18dGroupAtkDamage() {
		return beta18dGroupAtkDamage;
	}

	public void setBeta18dGroupAtkDamage(double beta18dGroupAtkDamage) {
		this.beta18dGroupAtkDamage = beta18dGroupAtkDamage;
	}

	public double getBeta18dSingleAtkIncrease() {
		return beta18dSingleAtkIncrease;
	}

	public void setBeta18dSingleAtkIncrease(double beta18dSingleAtkIncrease) {
		this.beta18dSingleAtkIncrease = beta18dSingleAtkIncrease;
	}

	public double getBeta18dSingleAtkDamage() {
		return beta18dSingleAtkDamage;
	}

	public void setBeta18dSingleAtkDamage(double beta18dSingleAtkDamage) {
		this.beta18dSingleAtkDamage = beta18dSingleAtkDamage;
	}

	public double getBeta18dRecover() {
		return beta18dRecover;
	}

	public void setBeta18dRecover(double beta18dRecover) {
		this.beta18dRecover = beta18dRecover;
	}

	public double getBeta18eAttack() {
		return beta18eAttack;
	}

	public void setBeta18eAttack(double beta18eAttack) {
		this.beta18eAttack = beta18eAttack;
	}

	public double getBeta18eMainPhysicalDefense() {
		return beta18eMainPhysicalDefense;
	}

	public void setBeta18eMainPhysicalDefense(double beta18eMainPhysicalDefense) {
		this.beta18eMainPhysicalDefense = beta18eMainPhysicalDefense;
	}

	public double getBeta18eLessMagicalDefense() {
		return beta18eLessMagicalDefense;
	}

	public void setBeta18eLessMagicalDefense(double beta18eLessMagicalDefense) {
		this.beta18eLessMagicalDefense = beta18eLessMagicalDefense;
	}

	public double getBeta18eMainMagicalDefense() {
		return beta18eMainMagicalDefense;
	}

	public void setBeta18eMainMagicalDefense(double beta18eMainMagicalDefense) {
		this.beta18eMainMagicalDefense = beta18eMainMagicalDefense;
	}

	public double getBeta18eLessPhysicalDefense() {
		return beta18eLessPhysicalDefense;
	}

	public void setBeta18eLessPhysicalDefense(double beta18eLessPhysicalDefense) {
		this.beta18eLessPhysicalDefense = beta18eLessPhysicalDefense;
	}

	public double getBeta18eMaxHP() {
		return beta18eMaxHP;
	}

	public void setBeta18eMaxHP(double beta18eMaxHP) {
		this.beta18eMaxHP = beta18eMaxHP;
	}

	public double getBeta18eIgnoreDefense() {
		return beta18eIgnoreDefense;
	}

	public void setBeta18eIgnoreDefense(double beta18eIgnoreDefense) {
		this.beta18eIgnoreDefense = beta18eIgnoreDefense;
	}

	public double getBeta18eCritical() {
		return beta18eCritical;
	}

	public void setBeta18eCritical(double beta18eCritical) {
		this.beta18eCritical = beta18eCritical;
	}

	public double getBeta18eIgnore() {
		return beta18eIgnore;
	}

	public void setBeta18eIgnore(double beta18eIgnore) {
		this.beta18eIgnore = beta18eIgnore;
	}

	public double getBeta18eIcreaseDamage() {
		return beta18eIcreaseDamage;
	}

	public void setBeta18eIcreaseDamage(double beta18eIcreaseDamage) {
		this.beta18eIcreaseDamage = beta18eIcreaseDamage;
	}

	public double getBeta18eGroupAtkIncrease() {
		return beta18eGroupAtkIncrease;
	}

	public void setBeta18eGroupAtkIncrease(double beta18eGroupAtkIncrease) {
		this.beta18eGroupAtkIncrease = beta18eGroupAtkIncrease;
	}

	public double getBeta18eGroupAtkDamage() {
		return beta18eGroupAtkDamage;
	}

	public void setBeta18eGroupAtkDamage(double beta18eGroupAtkDamage) {
		this.beta18eGroupAtkDamage = beta18eGroupAtkDamage;
	}

	public double getBeta18eSingleAtkIncrease() {
		return beta18eSingleAtkIncrease;
	}

	public void setBeta18eSingleAtkIncrease(double beta18eSingleAtkIncrease) {
		this.beta18eSingleAtkIncrease = beta18eSingleAtkIncrease;
	}

	public double getBeta18eSingleAtkDamage() {
		return beta18eSingleAtkDamage;
	}

	public void setBeta18eSingleAtkDamage(double beta18eSingleAtkDamage) {
		this.beta18eSingleAtkDamage = beta18eSingleAtkDamage;
	}

	public double getBeta18eRecover() {
		return beta18eRecover;
	}

	public void setBeta18eRecover(double beta18eRecover) {
		this.beta18eRecover = beta18eRecover;
	}

}
