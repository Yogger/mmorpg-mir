package com.mmorpg.mir.model.ai.desires;

public interface DesireIteratorFilter {
	/**
	 * This method is called each time for every desire that is in the queue.<br>
	 * <br>
	 * {@link java.util.ConcurrentModificationException} will be thrown by
	 * {@link com.aionemu.gameserver.ai.desires.DesireQueue#iterateDesires(DesireIteratorHandler, DesireIteratorFilter[])}
	 * if any of the following methods will be called from here:
	 * <ul>
	 * <li>
	 * {@link com.aionemu.gameserver.ai.desires.DesireQueue#addDesire(Desire)}</li>
	 * <li>{@link com.aionemu.gameserver.ai.desires.DesireQueue#poll()}</li>
	 * <li>
	 * {@link com.aionemu.gameserver.ai.desires.DesireQueue#removeDesire(Desire)}
	 * </li>
	 * </ul>
	 * <p/>
	 * However {@link com.aionemu.gameserver.ai.desires.DesireQueue#reset()} can
	 * be called.
	 * 
	 * @param desire
	 *            current element of iteration that is being filtered
	 * @return true if this filter accepted desire, false otherwise
	 */
	public boolean isOk(Desire desire);
}
