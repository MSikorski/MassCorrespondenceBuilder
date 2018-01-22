package org.mateuszsikorski.masscorrespondencebuilder.filesystemspy;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Queue {

	private List<Path> locAPathList;
	private List<Path> locBPathList;
	
	public Queue () {
		locAPathList = new ArrayList<>();
		locBPathList = new ArrayList<>();
	}
	
	public void addToQueueLocA(Path path) {
		locAPathList.add(path);
	}
	
	public void addToQueueLocB(Path path) {
		locBPathList.add(path);
	}
	
	public Path getFirstPathFromQueueA() {
		if(locAPathList.isEmpty())
			return null;
		Path path = locAPathList.get(0);
		locAPathList.remove(0);
		return path;
	}
	
	public Path getFirstPathFromQueueB() {
		if(locBPathList.isEmpty())
			return null;
		Path path = locBPathList.get(0);
		locBPathList.remove(0);
		return path;
	}
	
	public boolean isItemInQueueA() {
		return !locAPathList.isEmpty();
	}
	
	public boolean isItemInQueueB() {
		return !locBPathList.isEmpty();
	}
}
