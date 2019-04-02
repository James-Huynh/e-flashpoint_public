package gui;

import client.ClientManager;

public class clientThread implements Runnable{

	Launcher myLauncher;
	ClientManager myClientManager;
	boolean inLobby;
	Thread t1;
	boolean toggle;
	
	clientThread(Launcher mylauncher, ClientManager myManager, Boolean inLobby){
		this.myLauncher = mylauncher;
		this.myClientManager = myManager;
		this.inLobby = inLobby;
		this.toggle = true;
		
		
	}
	
	@Override
	public void run() {
		
		
		while(true) {
			
			if(this.inLobby) {
				int flag = myClientManager.listenForResponses();
				if(flag == 1) {
					myLauncher.refreshLobby();
					System.out.println("were here");
				} else if(flag == 2) {
					myLauncher.startGame();
					this.inLobby = false;
				}
			}else {
				if(myClientManager.getUsersGameState().getInRideMode() && toggle) {
					System.out.println("hello we are riding");
					myLauncher.showRideRequest();
					toggle = false;
					
				}
				else if(myClientManager.listenForResponses() == 1) {
					System.out.println("hello we are not riding");
					myLauncher.refreshBoard();
					toggle = true;
				} else {
					System.out.println("we are not meant to be here");
				}
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	public void begin() {
		// TODO Auto-generated method stub
		t1 = new Thread(this);
		t1.start();
	}

	

}
