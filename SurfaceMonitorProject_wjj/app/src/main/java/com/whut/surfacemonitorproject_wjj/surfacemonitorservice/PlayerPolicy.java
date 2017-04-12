package com.whut.surfacemonitorproject_wjj.surfacemonitorservice;

public abstract class PlayerPolicy {

	public final static int PLAY = 0;
	public final static int NO_PLAY = 1;
	public interface SurfaceEvent {
		public boolean onLowSurfaceVideo();
	}
	abstract public String invokSurfaceDump();
	abstract public void resetAll();
}
