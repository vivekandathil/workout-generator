package ca.vivek.cprogress;

public class Exercise 
{
	private String type;
	private int weightPounds;
	private int maxRepsTime;
	private String dateUpdated;
	private String[] setsReps;
	private String[] progressions;
	private String notes;
	private String[] muscles;
	public boolean timed = false;
	
	public Exercise(String type, int weightPounds, int maxRepsTime, String dateUpdated, String[] setsReps, String[] progressions, String notes, boolean timed, String[] muscles)
	{
		this.type = type;
		this.weightPounds = weightPounds;
		this.maxRepsTime = maxRepsTime;
		this.dateUpdated = dateUpdated;
		this.setsReps = setsReps;
		this.progressions = progressions;
		this.notes = notes;
		this.timed = timed;
		this.muscles = muscles;
	}
	
	public void setToTimed()
	{
		this.timed = true;
	}
	
	public String getType()
	{
		return this.type;
	}
	public int getWeightAdded()
	{
		return this.weightPounds;
	}
	public int getMax()
	{
		return this.maxRepsTime;
	}
	public String getDateUpdated()
	{
		return this.dateUpdated;
	}
	public String[] getSetsReps()
	{
		return this.setsReps;
	}
	public String[] getProgressions()
	{
		return this.progressions;
	}
	public String getNotes()
	{
		return this.notes;
	}
	public String[] getMuscles()
	{
		return this.muscles;
	}
	public String getCode()
	{
		return this.type.substring(1,3);
	}
	@Override
	public String toString()
	{
		return this.type + ", " + this.weightPounds + "lbs, Max: " + this.maxRepsTime + ", "+  this.progressions[0] + ", " + this.progressions[1] + ", " + this.notes + ", ";
	}
}
