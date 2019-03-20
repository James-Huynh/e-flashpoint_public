package gui;

import client.ClientManager;

public class clientThread implements Runnable{

	Launcher myLauncher;
	ClientManager myClientManager;
	boolean inLobby;
	Thread t1;
	
	clientThread(Launcher mylauncher, ClientManager myManager, Boolean inLobby){
		this.myLauncher = mylauncher;
		this.myClientManager = myManager;
		this.inLobby = inLobby;
		
		
	}
	
	@Override
	public void run() {
		
		
		while(true) {
			
			if(this.inLobby) {
				int flag = myClientManager.listenForResponses();
				if(flag == 1) {
					myLauncher.refreshLobby();
				} else if(flag == 2) {
					myLauncher.startGame();
					this.inLobby = false;
				}
			}else {
				if(myClientManager.listenForResponses() == 1) {
					System.out.println("I heard a resposne");
					myLauncher.refreshBoard();
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
