package org.mateuszsikorski.masscorrespondencebuilder.filesystemspy;

import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SpyDirectoriesService implements Runnable{

	private WatchService watchServiceA;
	private WatchService watchServiceB;
	private WatchKey watchKeyA;
	private WatchKey watchKeyB;
	
	private boolean spyRunning = true;
	
	private Queue queue;
	
	@Autowired
	public SpyDirectoriesService(WatchService watchServiceA, WatchService watchServiceB, WatchKey watchKeyA,
			WatchKey watchKeyB, Queue queue) {
		this.watchServiceA = watchServiceA;
		this.watchServiceB = watchServiceB;
		this.watchKeyA = watchKeyA;
		this.watchKeyB = watchKeyB;
		this.queue = queue;
	}

	@Override
	public void run() {
		
		while(spyRunning) {
			
			while((watchKeyA = watchServiceA.poll()) != null) {
				proccesKeyA();
				watchKeyA.reset();
			}
			
			while((watchKeyB = watchServiceB.poll()) != null) {
				proccesKeyB();
				watchKeyB.reset();
			}
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}

	private void proccesKeyB() {
		
		for(WatchEvent<?> event : watchKeyB.pollEvents()) {
			if(event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
				Path eventPath = (Path) event.context();
				queue.addToQueueLocB(eventPath);
			}
		}
	}

	private void proccesKeyA() {

		for(WatchEvent<?> event : watchKeyA.pollEvents()) {
			if(event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
				Path eventPath = (Path) event.context();
				if(eventPath.toString().contains("ERROR"))
					continue;
				queue.addToQueueLocA(eventPath);
			}
		}
	}

	public void stopSpy() {
		this.spyRunning = false;
	}
	
	@Override
	public String toString() {
		return "SpyDirectoriesService [watchServiceA=" + watchServiceA + ", watchServiceB=" + watchServiceB
				+ ", watchKeyA=" + watchKeyA + ", watchKeyB=" + watchKeyB + "]";
	}
	
	
}
