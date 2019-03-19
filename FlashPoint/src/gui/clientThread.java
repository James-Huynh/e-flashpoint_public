package gui;

import client.ClientManager;

public class clientThread implements Runnable{

	Launcher myLauncher;
	ClientManager myClientManager;
	
	clientThread(Launcher mylauncher, ClientManager myManager){
		this.myLauncher = mylauncher;
		this.myClientManager = myManager;
		
	}
	
	@Override
	public void run() {
		
		
		while(true) {
//			System.out.println("this is working");
			if(myClientManager.listenForResponses() == 1) {
				System.out.println("I heard a resposne");
				myLauncher.refreshBoard();
			} 
//			else if (myClientManager.listenForResponses()==2){
//				myLauncher.refreshLobby();
//			}
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
		Thread t1 = new Thread(this);
		t1.start();
	}

}
