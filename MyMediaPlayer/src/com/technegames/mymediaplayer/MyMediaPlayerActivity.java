package com.technegames.mymediaplayer;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MyMediaPlayerActivity extends Activity {
	WakeLock wakeLock;
	private static final String[] EXTENSIONS = { ".mp3", ".mid", ".wav", ".ogg", ".mp4" }; //Playable Extensions
	List<String> trackNames; //Playable Track Titles
	List<String> trackArtworks; //Track artwork names
	AssetManager assets; //Assets (Compiled with APK)
	File path; //directory where music is loaded from on SD Card
	File path2; //directory where album artwork is loaded from on SD Card
	Music track; //currently loaded track
	ImageView bg; //Track artwork
	Button btnPlay; //The play button will need to change from 'play' to 'pause', so we need an instance of it
	Random random; //used for shuffle
	boolean shuffle; //is shuffle mode on?
	boolean isTuning; //is user currently jammin out, if so automatically start playing the next track
	int currentTrack; //index of current track selected
	int type; //0 for loading from assets, 1 for loading from SD card
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "Lexiconda");
        setContentView(R.layout.main);
        
        initialize(0);
    }
    
    @Override
    public void onResume(){
    	super.onResume();
    	wakeLock.acquire();
    }
	
    @Override
	public void onPause(){
		super.onPause();
		wakeLock.release();
		if(track != null){
			if(track.isPlaying()){
				track.pause();
				isTuning = false;
				btnPlay.setBackgroundResource(R.drawable.play);
			}
			if(isFinishing()){
				track.dispose();
				finish();
			}
		} else{
			if(isFinishing()){
				finish();
			}
		}
	}
    
    private void initialize(int type){
    	bg = (ImageView) findViewById(R.id.bg);
        btnPlay = (Button) findViewById(R.id.btnPlay);
        btnPlay.setBackgroundResource(R.drawable.play);
    	trackNames = new ArrayList<String>();
    	trackArtworks = new ArrayList<String>();
    	assets = getAssets();
    	currentTrack = 0;
    	shuffle = false;
    	isTuning = false;
    	random = new Random();
    	this.type = type;
    	
    	addTracks(getTracks());
    	loadTrack();
    }
    
    //Generate a String Array that represents all of the files found
    private String[] getTracks(){
    	if(type == 0){
    		try {
    			String[] temp = getAssets().list("");
    			return temp;
    		} catch (IOException e) {
    			e.printStackTrace();
    			Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
    		}
    	} else if(type == 1){
    		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) 
        			|| Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED_READ_ONLY)){
        		path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
        		path2 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    			String[] temp = path.list();
    			return temp;
    		} else{
    			Toast.makeText(getBaseContext(), "SD Card is either mounted elsewhere or is unusable", Toast.LENGTH_LONG).show();
    		}
    	}
    	return null;
    }
    
    //Adds the playable files to the trackNames List
    private void addTracks(String[] temp){
    	if(temp != null){
			for(int i = 0; i < temp.length; i++){
				//Only accept files that have one of the extensions in the EXTENSIONS array
				if(trackChecker(temp[i])){
					trackNames.add(temp[i]);
					trackArtworks.add(temp[i].substring(0, temp[i].length()-4));
				}
			}
			Toast.makeText(getBaseContext(), "Loaded " + Integer.toString(trackNames.size()) + " Tracks", Toast.LENGTH_SHORT).show();
		}
    }
    
    //Checks to make sure that the track to be loaded has a correct extenson
    private boolean trackChecker(String trackToTest){
    	for(int j = 0; j < EXTENSIONS.length; j++){
			if(trackToTest.contains(EXTENSIONS[j])){
				return true;
			}
		}
    	return false;
    }
    
    //Loads the track by calling loadMusic
    private void loadTrack(){
    	if(track != null){
    		track.dispose();
    	}
    	if(trackNames.size() > 0){
    		track = loadMusic(type);
    		setImage("drawable/" + trackArtworks.get(currentTrack));
    	}
    }
    
	//loads a Music instance using either a built in asset or an external resource
    private Music loadMusic(int type){
    	switch(type){
    	case 0:
    		try{
    			AssetFileDescriptor assetDescriptor = assets.openFd(trackNames.get(currentTrack));
    			return new Music(assetDescriptor);
    		} catch(IOException e){
    			e.printStackTrace();
    			Toast.makeText(getBaseContext(), "Error Loading " + trackNames.get(currentTrack), Toast.LENGTH_LONG).show();
    		}
    		return null;
    	case 1:
    		try{
    			FileInputStream fis = new FileInputStream(new File(path, trackNames.get(currentTrack)));
    			FileDescriptor fileDescriptor = fis.getFD();
    			return new Music(fileDescriptor);
    		} catch(IOException e){
    			e.printStackTrace();
    			Toast.makeText(getBaseContext(), "Error Loading " + trackNames.get(currentTrack), Toast.LENGTH_LONG).show();
    		}
    		return null;
    	default:
    		return null;
    	}
    }
    
    //Sets the background image to match the track currently playing or a default image
	private void setImage(String name) {
		if(type == 0){
			int imageResource = getResources().getIdentifier(name, null, getPackageName());
		    if(imageResource != 0){
		    	Drawable image = getResources().getDrawable(imageResource);
		    	bg.setImageDrawable(image);
		    } else{
		    	int defaultImageResource = getResources().getIdentifier("drawable/defaultbg", null, getPackageName());
			    if(defaultImageResource != 0){
			    	Drawable image = getResources().getDrawable(defaultImageResource);
			    	bg.setImageDrawable(image);
			    }
		    }
		} else if(type == 1){
			if(new File(path2.getAbsolutePath(), trackArtworks.get(currentTrack) + ".jpg").exists()){
				bg.setImageDrawable(Drawable.createFromPath(path2.getAbsolutePath() + "/" + trackArtworks.get(currentTrack) + ".jpg"));
			} else{
		    	int defaultImageResource = getResources().getIdentifier("drawable/defaultbg", null, getPackageName());
			    if(defaultImageResource != 0){
			    	Drawable image = getResources().getDrawable(defaultImageResource);
			    	bg.setImageDrawable(image);
			    }
		    }
		}
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
		super.onCreateOptionsMenu(menu);
		createMenu(menu);
		return true;
	}
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		case 0:
			//Set Looping
			synchronized(this){
				if(track.isLooping()){
					track.setLooping(false);
					Toast.makeText(getBaseContext(), "Playing Tracks Sequentially", Toast.LENGTH_SHORT).show();
				} else{
					track.setLooping(true);
					Toast.makeText(getBaseContext(), "Looping " + trackNames.get(currentTrack), Toast.LENGTH_SHORT).show();
				}
			}
			return true;
		case 1:
			//Set Shuffle
			synchronized(this){
				if(shuffle){
					setShuffle(false);
				} else{
					setShuffle(true);
				}
			}
			return true;
		case 2:
			//Stop Music
			synchronized(this){
				track.switchTracks();
				btnPlay.setBackgroundResource(R.drawable.play);
			}
			return true;
		case 3:
			//Change Source from Assets to SD Card and vice versa
			synchronized(this){
				type++;
				if(type > 1){
					type = 0;
				}
			}
			if(type == 0){
				Toast.makeText(getBaseContext(), "Loading Tracks from Assets ", Toast.LENGTH_SHORT).show();
			} else if(type == 1){
				Toast.makeText(getBaseContext(), "Loading Tracks from SD Card", Toast.LENGTH_SHORT).show();
			}
			initialize(type);
			return true;
		default:
			return false;
		}
	}
    
    private void createMenu(Menu menu){
		MenuItem miLooping = menu.add(0, 0, 0, "Looping");{
			miLooping.setIcon(R.drawable.looping);
		}
		MenuItem miShuffle = menu.add(0, 1, 1, "Shuffle");{
			miShuffle.setIcon(R.drawable.shuffle);
		}
		MenuItem miStop = menu.add(0, 2, 2, "Stop");{
			miStop.setIcon(R.drawable.stop);
		}
		MenuItem miSource = menu.add(0, 3, 3, "Source");{
			miSource.setIcon(R.drawable.source);
		}
	}
    
    public void click(View view){
		int id = view.getId();
		switch(id){
		case R.id.btnPlay:
			synchronized(this){
				if(isTuning){
					isTuning = false;
					btnPlay.setBackgroundResource(R.drawable.play);
					track.pause();
				} else{
					isTuning = true;
					btnPlay.setBackgroundResource(R.drawable.pause);
					playTrack();
				}
			}
			return;
		case R.id.btnPrevious:
			setTrack(0);
			loadTrack();
			playTrack();
			return;
		case R.id.btnNext:
			setTrack(1);
			loadTrack();
			playTrack();
			return;
		default:
			return;
		}
	}
    
    private void setTrack(int direction){
    	if(direction == 0){
    		currentTrack--;
			if(currentTrack < 0){
				currentTrack = trackNames.size()-1;
			}
    	} else if(direction == 1){
    		currentTrack++;
			if(currentTrack > trackNames.size()-1){
				currentTrack = 0;
			}
    	}
    	if(shuffle){
			int temp = random.nextInt(trackNames.size());
			while(true){
				if(temp != currentTrack){
					currentTrack = temp;
					break;
				}
				temp++;
				if(temp > trackNames.size()-1){
					temp = 0;
				}
			}
		}
    }
    
    //Plays the Track
    private void playTrack(){
    	if(isTuning && track != null){
			track.play();
			Toast.makeText(getBaseContext(), "Playing " + trackNames.get(currentTrack).substring(0, trackNames.get(currentTrack).length()-4), Toast.LENGTH_SHORT).show();
		}
    }
    
    //Simply sets shuffle to isShuffle and then displays a message for confirmation
    private void setShuffle(boolean isShuffle) {
    	shuffle = isShuffle;
    	if(shuffle){
    		Toast.makeText(getBaseContext(), "Shuffle On", Toast.LENGTH_SHORT).show();
    	} else{
    		Toast.makeText(getBaseContext(), "Shuffle Off", Toast.LENGTH_SHORT).show();
    	}
	}
    
}