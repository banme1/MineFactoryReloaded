package powercrystals.minefactoryreloaded.circuits.analog;

import powercrystals.minefactoryreloaded.circuits.base.StatelessCircuit;

public class Max3 extends StatelessCircuit
{
	@Override
	public byte getInputCount()
	{
		return 3;
	}
	
	@Override
	public byte getOutputCount()
	{
		return 1;
	}
	
	@Override
	public int[] recalculateOutputValues(long worldTime, int[] inputValues)
	{
		return new int[] { Math.max(Math.max(inputValues[0], inputValues[1]), inputValues[2]) };
	}
	
	@Override
	public String getUnlocalizedName()
	{
		return "circuit.mfr.max.3";
	}
	
	@Override
	public String getInputPinLabel(int pin)
	{
		return "I" + pin;
	}
	
	@Override
	public String getOutputPinLabel(int pin)
	{
		return "O";
	}
}
