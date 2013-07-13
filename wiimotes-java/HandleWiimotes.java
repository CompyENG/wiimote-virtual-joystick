/*
 * Created by TrueJournals -- http://truejournals.com
 * Based on "AdvancedDiscovery" motej example.
 */
//package motej.demos.discovery;

import java.util.ArrayList;
import java.util.List;
import java.awt.Robot;
import java.awt.AWTException;
import java.awt.event.KeyEvent;

import motej.Mote;
import motej.MoteFinder;
import motej.MoteFinderListener;
import motej.event.*;
import motejx.extensions.classic.ClassicController;

public class HandleWiimotes {

	//private static List<Mote> motes = new ArrayList<Mote>();
	public static Mote motes[] = new Mote[4];
	private static MoteFinder finder = MoteFinder.getMoteFinder();
	public static UInput u;
	public static boolean stopping = false;
	
	static public void addMote(Mote mote) {
	    boolean added = false;
	    for(int i=0;i<motes.length;i++) {
	        if(motes[i] == null) {
	            System.out.println("Mote #"+(i+1));
	            mote.setPlayerLeds(intToBoolArray(i+1));
	            motes[i] = mote;
	            mote.addCoreButtonListener(new myCoreButtonListener(i+1));
	            mote.addMoteDisconnectedListener(new myMoteDisconnectedListener(i+1));
	            mote.addExtensionListener(new myExtensionListener(i+1));
	            added = true;
	            break;
            }
        }
        if(!added) {
            System.out.println("Too many wiimotes!");
            if(mote.getBluetoothAddressCache().contains(mote.getBluetoothAddress())) {
                System.out.println("Too many wiimotes -- found in address cache");
                mote.getBluetoothAddressCache().remove(mote.getBluetoothAddress());
                mote.disconnect();
            }
        }
    }
    
    static public boolean[] intToBoolArray(int num) {
        if(num == 1) return new boolean[] { true, false, false, false };
        if(num == 2) return new boolean[] { false, true, false, false };
        if(num == 3) return new boolean[] { false, false, true, false };
        if(num == 4) return new boolean[] { false, false, false, true };
        if(num == 5) return new boolean[] { true, true, false, false };
        if(num == 6) return new boolean[] { true, false, true, false };
        if(num == 7) return new boolean[] { true, false, false, true };
        if(num == 8) return new boolean[] { true, true, true, false };
        // That's good for now...
        return new boolean[] { true, true, true, true }; // Indicates error!
    }
	
	static public void main(String[] args) throws InterruptedException {        
        u = new UInput();
        
		MoteFinderListener listener = new MoteFinderListener() {
		
			public void moteFound(Mote mote) {
				System.out.println("Found mote: " + mote.getBluetoothAddress());
				
				// Abstract this out to somewhere else... it makes the code easier to manage
				addMote(mote);
			}
			
			public void inquiryComplete() {
			    if(!stopping) {
			        try {
			            System.out.println("Starting discovery again...");
			            Thread.sleep(300);
			            finder.startDiscovery();
		            } catch(InterruptedException e) {
		                System.out.println("Something went very wrong...");
	                }
                }
            }
		
		};
		
		//MoteFinder finder = MoteFinder.getMoteFinder();
		finder.addMoteFinderListener(listener);
		
		System.out.println("Starting discovery..");
		finder.startDiscovery();
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
		    public void run() {
		        HandleWiimotes.stopping = true;
		        finder.stopDiscovery();
		        u.kill();
		        for(int i=0;i<4;i++) {
		            if(motes[i] != null) motes[i].disconnect();
	            }
            }
        });
		
		System.out.println("Putting thread to sleep..");
		boolean cont = true;
		while(cont) {
		    try {
		        Thread.sleep(1000);
	        } catch (InterruptedException e) {
	            cont = false;
            }
        }
		
		System.out.println("Stopping discovery..");
		finder.stopDiscovery();
		u.kill();
		
		for (int i=0;i<4;i++) {
		    if(motes[i] != null) motes[i].disconnect();
		}
	}

}

class myExtensionListener implements ExtensionListener {
    private int wiimote;
    public myExtensionListener(int w) { wiimote = w; }
    public void extensionConnected(ExtensionEvent evt) {
        if(evt.getExtension() instanceof ClassicController) {
            System.out.println("Detected classic controller on wiimote "+wiimote);
        }
    }
    public void extensionDisconnected(ExtensionEvent evt) {
        if(evt.getExtension() instanceof ClassicController) {
            System.out.println("Detected disconnect on "+wiimote);
        }
    }
}

class myCoreButtonListener implements CoreButtonListener {
    private int wiimote;
    
    private boolean btnUp, btnLeft, btnDown, btnRight, btnA, btnB, btnMinus, btnHome, btnPlus, btnOne, btnTwo;
    
    public myCoreButtonListener() { }
    public myCoreButtonListener(int n) { wiimote = n; }
    
    public void setNum(int n) { wiimote = n;    }
    public int  getNum()      { return wiimote; }
    
    public void buttonPressed(CoreButtonEvent evt) {
        System.out.println("Wiimote "+wiimote+" -- "+evt.getButton());
        
        // Just so typing is easier...
        UInput u    = HandleWiimotes.u;
        
        // Theoretically, one event per button... so else if should be fine
        if(evt.isButtonAPressed() && !btnA) {
            btnA = true;
            u.sendButtonDown(wiimote, 0);
        } else if(!evt.isButtonAPressed() && btnA) {
            btnA = false;
            u.sendButtonUp(wiimote, 0);
        } else 
        
        if(evt.isButtonBPressed() && !btnB) {
            btnB = true;
            u.sendButtonDown(wiimote, 1);
        } else if(!evt.isButtonBPressed() && btnB) {
            btnB = false;
            u.sendButtonUp(wiimote, 1);
        } else 
        
        if(evt.isButtonHomePressed() && !btnHome) {
            btnHome = true;
            u.sendButtonDown(wiimote, 3);
        } else if(!evt.isButtonHomePressed() && btnHome) {
            btnHome = false;
            u.sendButtonUp(wiimote, 3);
        } else
        
        if(evt.isButtonMinusPressed() && !btnMinus) {
            btnMinus = true;
            u.sendButtonDown(wiimote, 2);
        } else if(!evt.isButtonMinusPressed() && btnMinus) {
            btnMinus = false;
            u.sendButtonUp(wiimote, 2);
        } else
        
        if(evt.isButtonOnePressed() && ! btnOne) {
            btnOne = true;
            u.sendButtonDown(wiimote, 5);
        } else if(!evt.isButtonOnePressed() && btnOne) {
            btnOne = false;
            u.sendButtonUp(wiimote, 5);
        } else
        
        if(evt.isButtonPlusPressed() && !btnPlus) {
            btnPlus = true;
            u.sendButtonDown(wiimote, 4);
        } else if(!evt.isButtonPlusPressed() && btnPlus) {
            btnPlus = false;
            u.sendButtonUp(wiimote, 4);
        } else
        
        if(evt.isButtonTwoPressed() && !btnTwo) {
            btnTwo = true;
            u.sendButtonDown(wiimote, 6);
        } else if(!evt.isButtonTwoPressed() && btnTwo) {
            btnTwo = false;
            u.sendButtonUp(wiimote, 6);
        } else
        
        if(evt.isDPadDownPressed() && !btnDown) {
            btnDown = true;
            u.sendButtonDown(wiimote, 7);
        } else if(!evt.isDPadDownPressed() && btnDown) {
            btnDown = false;
            u.sendButtonUp(wiimote, 7);
        } else
        
        if(evt.isDPadLeftPressed() && !btnLeft) {
            btnLeft = true;
            u.sendButtonDown(wiimote, 8);
        } else if(!evt.isDPadLeftPressed() && btnLeft) {
            btnLeft = false;
            u.sendButtonUp(wiimote, 8);
        } else
        
        if(evt.isDPadRightPressed() && !btnRight) {
            btnRight = true;
            u.sendButtonDown(wiimote, 9);
        } else if(!evt.isDPadRightPressed() && btnRight) {
            btnRight = false;
            u.sendButtonUp(wiimote, 9);
        } else
        
        if(evt.isDPadUpPressed() && !btnUp) {
            btnUp = true;
            u.sendButtonDown(wiimote, 10);
        } else if(!evt.isDPadUpPressed() && btnUp) {
            btnUp = false;
            u.sendButtonUp(wiimote, 10);
        }
    }
}

class myMoteDisconnectedListener implements MoteDisconnectedListener<Mote> {
    private int wiimote;
    public myMoteDisconnectedListener() { }
    public myMoteDisconnectedListener(int n) { wiimote = n; }
    
    public void setNum(int n) { wiimote = n;    }
    public int  getNum()      { return wiimote; }
    
    public void moteDisconnected(MoteDisconnectedEvent<Mote> evt) {
        System.out.println("Disconnecting wiimote "+wiimote);
        //HandleWiimotes.motes[wiimote-1].disconnect();
        HandleWiimotes.motes[wiimote-1] = null;
    }
}

class btnTranslator {
    public static int getDown(int wiimote) {
        if(wiimote == 1) { return KeyEvent.VK_DOWN; } else
        if(wiimote == 2) { return KeyEvent.VK_S;    } else { return KeyEvent.VK_DOWN; }
    }
    
    public static int getUp(int wiimote) {
        if(wiimote == 1) { return KeyEvent.VK_UP; } else
        if(wiimote == 2) { return KeyEvent.VK_W;  } else { return KeyEvent.VK_UP; }
    }
    
    public static int getLeft(int wiimote) {
        if(wiimote == 1) { return KeyEvent.VK_LEFT; } else
        if(wiimote == 2) { return KeyEvent.VK_A;    } else { return KeyEvent.VK_LEFT; }
    }
    
    public static int getRight(int wiimote) {
        if(wiimote == 1) { return KeyEvent.VK_RIGHT; } else
        if(wiimote == 2) { return KeyEvent.VK_D;     } else { return KeyEvent.VK_RIGHT; }
    }
    
    public static int getA(int wiimote) {
        if(wiimote == 1) { return KeyEvent.VK_E; } else
        if(wiimote == 2) { return KeyEvent.VK_Q; } else { return KeyEvent.VK_E; }
    }
    
    public static int getB(int wiimote) {
        if(wiimote == 1) { return KeyEvent.VK_T; } else
        if(wiimote == 2) { return KeyEvent.VK_C; } else { return KeyEvent.VK_T; }
    }
    
    public static int getMinus(int wiimote) {
        if(wiimote == 1) { return KeyEvent.VK_R; } else
        if(wiimote == 2) { return KeyEvent.VK_E; } else { return KeyEvent.VK_R; }
    }
    
    public static int getHome(int wiimote) {
        if(wiimote == 1) { return KeyEvent.VK_F; } else
        if(wiimote == 2) { return KeyEvent.VK_Z; } else { return KeyEvent.VK_F; }
    }
    
    public static int getPlus(int wiimote) {
        if(wiimote == 1) { return KeyEvent.VK_V; } else
        if(wiimote == 2) { return KeyEvent.VK_X; } else { return KeyEvent.VK_V; }
    }
    
    public static int getOne(int wiimote) {
        if(wiimote == 1) { return KeyEvent.VK_G;     } else
        if(wiimote == 2) { return KeyEvent.VK_COMMA; } else { return KeyEvent.VK_G; }
    }
    
    public static int getTwo(int wiimote) {
        if(wiimote == 1) { return KeyEvent.VK_B;      } else
        if(wiimote == 2) { return KeyEvent.VK_PERIOD; } else { return KeyEvent.VK_B; }
    }
}
