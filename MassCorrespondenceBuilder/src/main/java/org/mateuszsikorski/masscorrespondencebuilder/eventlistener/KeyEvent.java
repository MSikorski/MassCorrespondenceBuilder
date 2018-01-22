package org.mateuszsikorski.masscorrespondencebuilder.eventlistener;

import java.awt.event.KeyListener;


public class KeyEvent implements KeyListener {
	
	private boolean ending = false;

	@Override
	public void keyTyped(java.awt.event.KeyEvent e) {
	}

	@Override
	public void keyPressed(java.awt.event.KeyEvent e) {
		char c = e.getKeyChar();
		if(c == 'p')
			ending = true;
	}

	@Override
	public void keyReleased(java.awt.event.KeyEvent e) {
	}
	
	public boolean isEnding() {
		return ending;
	}

}
