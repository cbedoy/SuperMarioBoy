
package com.loaders;


import com.abstracts.Loader;

import java.applet.*;
import java.net.*;
import java.io.*;
import java.util.*;
/**
 * --------------------------------------------------------
 * Created by Carlos Bedoy on 5/11/14.
 * SuperBedoyBoy
 * Mobile Developer
 * Aguascalientes Mexico
 * Email:       carlos.bedoy@gmail.com
 * Facebook:    https://www.facebook.com/carlos.bedoy
 * ---------CODE && MUSIC ----------------------------------
 */
public class SoundsLoader extends Loader {

	public ArrayList<String> playing;

	public SoundsLoader() {
		this(".", "");
	}  // end of ImagesLoader();

	public SoundsLoader(String path) {
		this(path, "");
	}

	public SoundsLoader(String path, String loader) {
		super(path, loader);
		loaded = new HashMap<String, Object>();
		playing = new ArrayList<String>();
	}

	public boolean load(File f, String name, boolean rewrite) {
		if (name == null) {
			name = f.getName();
		}
		if (!rewrite && loaded.containsKey(name)) {
			return false;
		}
		try {
			URL url = f.toURI().toURL();
			AudioClip a = Applet.newAudioClip(url);
			loaded.put(name, a);
		} catch (Exception e) {
			System.err.println("Error loanding sound "
				+name+" from "+f.getPath());
			e.printStackTrace();
			return false;
		}
		System.out.println("Loaded "+name+" from "+f.getName());
		return true;
	}  // end of load(File, String, boolean);


	public void play(String name, boolean loop) {
		if (loop) {
			((AudioClip)loaded.get(name)).loop();
			playing.add(name);
			return;
		}
		((AudioClip)loaded.get(name)).play();
	}

	public AudioClip getAudio(String name) {
		return (AudioClip)loaded.get(name);
	}

	public AudioClip getAudio(String name, boolean load, boolean rewrite) {
		Object o = super.getObject(name, load, rewrite);
		if (o == null) {
			return null;
		}
		return (AudioClip)o;
	}

}
