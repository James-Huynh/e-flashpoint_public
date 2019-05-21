package gui;

import client.ClientManager;

public class clientThread implements Runnable{

	Launcher myLauncher;
	ClientManager myClientManager;
	boolean inLobby;
	Thread t1;
	boolean toggle;
	boolean exit;
	
	clientThread(Launcher mylauncher, ClientManager myManager, Boolean inLobby){
		this.myLauncher = mylauncher;
		this.myClientManager = myManager;
		this.inLobby = inLobby;
		this.toggle = true;
		this.exit = true;
		
		
	}
	
	@Override
	public void run() {
		
		
		while(exit) {
			
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
					
				} else if(myClientManager.getUsersGameState().getIsDodging() && toggle) {
					System.out.println("hello we are riding");
					myLauncher.showDodgeRequest();
					toggle = false;
				}else if(myClientManager.listenForResponses() == 1) {
					if(!myClientManager.getDodgeRefreshFlag() && !myClientManager.getUsersGameState().getInRideMode()) {
						myLauncher.refreshDodgePanel();
						myClientManager.setDodgeRefreshFlag(true);
					} else if(!myClientManager.getDodgeRefreshFlag() && myClientManager.getUsersGameState().getInRideMode()) {
						myLauncher.showRideRequest();
						myClientManager.setDodgeRefreshFlag(true);
					}
//					else if(myClientManager.getDodgeRefreshFlag() == true) { //Not entirely sure about this part and how it fits in with below
//						myLauncher.refreshBoard();							// Pls have a look 
//						myClientManager.setDodgeRefreshFlag(false); //reset it to false for next time + actions etc
//					} 
					else {
						System.out.println("hello we are not riding");
						myLauncher.refreshBoard();
						toggle = true;
					}
//					System.out.println("hello we are not riding");
//					myLauncher.refreshBoard();
//					toggle = true;
				} else {
					myLauncher.backToMainMenu();
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
	
	public void stop() {
		this.exit = false;
	}

	

}
