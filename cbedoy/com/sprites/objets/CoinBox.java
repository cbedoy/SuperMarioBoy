
// CoinBox.java -----------------------------------------------------

package com.sprites.objets;


import com.viewcontrollers.MainViewController;
import com.viewcontrollers.StageViewController;
import com.sprites.Sprite;
import com.sprites.WorldObject;

import java.applet.*;
import java.awt.*;


/**
 *
 */
public class CoinBox extends WorldObject {

	protected static int indexClass = 0;
	protected static boolean changeImg = false;
	public static int LENGHT_IMAGES = 4;
	public static void actClass() {
		if (changeImg) {
			indexClass = (indexClass+1)%LENGHT_IMAGES;
			changeImg = false;
		}
	}

	public static AudioClip[] audio;
	public static int indexAudio;
	public static boolean first = true;
	protected String imgNormal = "box*_0";
	protected boolean effect = false;
	protected boolean moving = false;
	protected float movingSpeed = 1.5F;
	protected float initY = 0;

	public CoinBox(StageViewController s) {
		super(s);
		supportsPlayer = true;
		setPreferredSize(map.tileXSize, map.tileYSize);
		setImages(imgNormal, 0, 4);
		// Rect�ngulo para las colisiones
		bounds.add(new Rectangle(-1, -1, width+1, height+1));
		if (first) {
			first = false;
			indexAudio = 0;
			audio = new AudioClip[5];
			for (int i=0; i<audio.length; i++) {
				audio[i] = stageViewController.getSoundsLoader().getAudio("coin.wav", true, true);
			}
		}
	}

	public void act() {
		if (moving) {
			move();
			speed.setY(speed.getAccurateY()-((MainViewController) stageViewController).getGravity());
			if (y >= initY) {
				speed.setY(0);
				y = initY;
				moving = false;
			}
		}
		int frameFrec = (int)(stageViewController.getFPS()/10);
		if (frameFrec==0 
			|| stageViewController.getTotalUpdates()%frameFrec == 0) {
				setImage(indexClass);
				changeImg = true;
		}
	}

	public void collision(Sprite s) {
		if (s instanceof Player && supportsPlayer) {
			Player p = (Player)s;
			// Colisiones del eje X
			if (getLeft().intersects(p.getRight())
				 && p.getSpeed().getAccurateX() > 0) {
					//System.out.println("Izquierda del brick");
					p.getSpeed().setX(0);
					p.setLeftWall((int)x);
			} else if (getRight().intersects(p.getLeft())
				 && p.getSpeed().getAccurateX() < 0) {
					//System.out.println("Derecha del brick");
					p.getSpeed().setX(0);
					p.setRightWall((int)x+width);
			}else
			// Colisiones del eje Y
			if (p.getHead().intersects(getFoot()) && p.isRising()) {
				//System.out.println("Debajo del brick");
				if (!moving) {
					moving = true;
					((MainViewController) stageViewController).getSoundsLoader().play("blockHit", false);
					speed.setY(movingSpeed);
					if (s.getSpeed().getAccurateY()>movingSpeed) {
						s.getSpeed().setY(movingSpeed);
					}
					s.setY(y+height);
				} else {
					s.getSpeed().setY(speed.getAccurateY());
				}
			} else if (p.getFoot().intersects(getHead())) {
				if (!moving) {
					//System.out.println("Arriba del brick");
					p.setFloor((int)y);
				}
			}
		}
	}

	public void setY(float yPos) {
		super.setY(yPos);
		initY = yPos;
	}

}