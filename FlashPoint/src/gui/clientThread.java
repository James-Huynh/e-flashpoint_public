package gui;

public class clientThread implements Runnable{

	Launcher launcher;
	
	clientThread(Launcher mylauncher){
		this.launcher = mylauncher;
	}
	
	@Override
	public void run() {
		
		
		while(true) {
			System.out.println("this is working");
//			launcher.refreshBoard();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
